package com.fanhao.businessplatform.common.constant;

public enum ResultStatus {
    SUCCESS("200", "操作成功"),
    FAILED("201", "操作失败"),
    NOT_FOUND("404", "资源不存在"),
    NO_PERMISSION("403", "权限不足"),
    NOT_LOGIN("403", "未登录")
    ;

    private String resultCode;
    private String attachMessage;

    private ResultStatus(final String resultCode,
                         final String attachMessage) {
        this.resultCode = resultCode;
        this.attachMessage = attachMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getAttachMessage() {
        return attachMessage;
    }

    public void setAttachMessage(String attachMessage) {
        this.attachMessage = attachMessage;
    }
}
