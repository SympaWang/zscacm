package com.example.zscacm.error;

public class TransactionException extends Exception implements CommonError{

    /**
     * 序列化
     */
    private static final long serialVersionUID = -4586789845574329434L;
    private CommonError commonError;
    //直接接受EmBusinessError的传参用于构造业务异常
    public TransactionException(CommonError CommonError)
    {
        super();
        this.commonError = CommonError;
    }

    //接收自定义errMsg的方法构造业务异常
    public TransactionException(CommonError CommonError,String ErrMsg)
    {
        super();
        this.commonError = CommonError;
        this.commonError.setErrorMsg(ErrMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        this.commonError.setErrorMsg(ErrorMsg);
        return this;
    }

}