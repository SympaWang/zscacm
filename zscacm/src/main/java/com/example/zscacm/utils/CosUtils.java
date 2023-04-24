package com.example.zscacm.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CosUtils {
    /**
     * COSClient类型的静态变量
     * 注意：请确保程序生命周期内COSClient实例只有一个
     */
    private static COSClient sCosClient;

    private CosUtils() {
    }

    /**
     * 初始化CosClient实例，在Springboot初始化执行一次，可以保证COSClient实例只有一个
     */
    public static void initCosClient() {
        //----------------------初始化客户端---------------------------
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = CosPropertiesConstantsUtils.Tencent_secretId;
        String secretKey = CosPropertiesConstantsUtils.Tencent_secretKey;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(CosPropertiesConstantsUtils.Tencent_region);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 为COSClient静态变量赋值
        sCosClient = cosClient;
    }

    /**
     * 获取COSClient实例
     * @return cosclient实例
     */
    public static COSClient getCosClient () {
        return sCosClient;
    }

    /**
     * 创建存储桶，如果在 COS控制台中提前创建好了存储桶，并且没有额外创建存储桶的需求，该方法是不必执行的。
     * 注意：这里自动创建了 公有读私有写 的存储桶，如果想要改变，可以将该属性设置为该方法的一个参数
     * @param bucketName 存储桶名称
     */
    public static void createBucket(String bucketName) {
        //-------------------创建存储桶-------------------------

        //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        try {
            Bucket bucketResult = sCosClient.createBucket(createBucketRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param file MultipartFile类型的文件
     * @param bucketName 存储桶名称
     * @param key 文件路径及文件名称的组合
     *            指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
     * @return URL对象，在该对象中有很多方法，其中获取 url 链接的方法是：getContent()
     * @throws IOException
     */
    public static URL uploadFile
    (MultipartFile file, String bucketName, String key) throws IOException {
        //----------------------上传对象-------------------------

        // 指定要上传的文件，这里使用 流类型，不使用文件类型
        int inputStreamLength = file.getBytes().length;
        InputStream inputStream = file.getInputStream();
        // 数据流类型需要额外定义一个参数
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(inputStreamLength);
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        // PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);

        PutObjectResult putObjectResult = sCosClient.putObject(putObjectRequest);

        System.out.println(putObjectResult.getRequestId());

        return getURL(bucketName, key);
    }

    /**
     * 重写方法，参入参数中没有bucketName，表示默认使用application.yml配置文件中的 bucketName
     * @param file MultipartFile类型的文件
     * @param key 文件路径及文件名称的组合
     *             指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
     * @return
     * @throws IOException
     */
    public static URL uploadFile(MultipartFile file, String key) throws IOException {
        return uploadFile(file, CosPropertiesConstantsUtils.Tencent_bucketName, key);
    }

    /**
     * 根据 bucketName 和 key 获取 URL 对象
     * @param bucketName 存储桶名称
     * @param key 文件路径及文件内容的组合
     * @return URL 对象
     */
    public static URL getURL(String bucketName, String key) {
        URL url = sCosClient.getObjectUrl(bucketName, key);
        return url;
    }

    /**
     * 重写方法，根据 key 获取 URL 对象
     * @param key 文件路径及文件内容的组合
     * @return URL 对象
     */
    public static URL getURL(String key) {
        return getURL(CosPropertiesConstantsUtils.Tencent_bucketName, key);
    }

    /**
     * 关闭 COSClient 实例，在 springboot 销毁时调用
     */
    public static void shutdownClient() {
        // 关闭客户端(关闭后台线程)
        sCosClient.shutdown();
    }
}