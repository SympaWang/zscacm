package com.example.zscacm.handler;

import com.example.zscacm.enums.EmBusinessError;
import com.example.zscacm.error.TransactionException;
import com.example.zscacm.utils.ResponseResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception e) {
        System.out.println(123);
        logger.error(e.getMessage(), e);

        Map<String, Object> responseData = new HashMap<>();

        if (e instanceof TransactionException) {
            TransactionException businessException = (TransactionException)e;
            responseData.put("errCode", businessException.getErrorCode());
            responseData.put("errMsg", businessException.getErrorMsg());
        }
        else{
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }
        return new ResponseResult((Integer) responseData.get("errCode"), "fail", responseData);
    }

}
