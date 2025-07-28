package com.rizhaosteel.droolstest.common.response;

/**
 * 统一API响应格式
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String code;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(true, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<T>(false, message, null);
        response.setCode(code);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
