package com.example.zscacm.error;

public interface CommonError {
    public int getErrorCode();

    public String getErrorMsg();

    public CommonError setErrorMsg(String ErrorMsg);
}