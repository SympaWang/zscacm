package com.example.zscacm.enums;

import com.example.zscacm.error.CommonError;

public enum EmBusinessError implements CommonError {

    RCONNECTION_ERROR(10001,"获取R服务器链接失败，请通知管理员"),

    USER_NOT_INUSE(20000,"该用户还未得到管理员许可"),
    USER_NOT_MATCH(20001,"用户名和密码不匹配"),
    USER_NOT_LOGIN(20002,"用户还未登录"),
    USER_LOGIN_TLE(20003,"用户登陆超时"),

    USER_REGESITERDUPLICATEKEY(20003,"注册使用账号重复"),
    USER_REGESITERFAIL(20004,"注册失败，未知原因"),

    FILE_NOT_EXIST(30001,"token非法"),
    MYSQL_NOT_EXIST(30002,"数据库错误！"),

    UNKNOWN_ERROR(40004,"未知错误！"),
    RSERVERERROR(50001,"选择上传的文件错误！")
    ;

    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode,String errMsg ) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    /* (non-Javadoc)
     * @see com.example.demo.error.CommonError#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    /* (non-Javadoc)
     * @see com.example.demo.error.CommonError#getErrorMsg()
     */
    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    /* (non-Javadoc)
     * @see com.example.demo.error.CommonError#setErrorMsg(java.lang.String)
     * 这个接口就可以用来改动通用错误类型中的ErrorMsg
     */
    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        this.errMsg = ErrorMsg;
        return this;
    }

}