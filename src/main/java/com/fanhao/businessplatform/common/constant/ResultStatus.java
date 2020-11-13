package com.fanhao.businessplatform.common.constant;

/**
 * 通用返回结果状态码，用于前端状态判断
 * @author fanhao
 */
public enum ResultStatus {
    SUCCESS("200", "操作成功","ok"),
    FAILED("201", "操作失败","fail"),
    NOT_FOUND("404", "资源不存在","no_found"),
    NO_PERMISSION("403", "权限不足","no_permission"),
    NOT_LOGIN("403", "未登录","no_login")
    ;

    private String resultCode;
    private String attachMessage;
    private String status;

    ResultStatus(String resultCode, String attachMessage, String status) {
        this.resultCode = resultCode;
        this.attachMessage = attachMessage;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
