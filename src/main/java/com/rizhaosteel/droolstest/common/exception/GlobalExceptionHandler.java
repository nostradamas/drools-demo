package com.rizhaosteel.droolstest.common.exception;

import com.rizhaosteel.droolstest.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 * 
 * @author System
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> handleBusinessException(BusinessException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder("参数校验失败: ");
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        }
        logger.warn("参数校验异常: {}", message.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(message.toString()));
    }
    
    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder message = new StringBuilder("参数绑定失败: ");
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        }
        logger.warn("参数绑定异常: {}", message.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(message.toString()));
    }
    
    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = String.format("参数类型错误: %s 应该是 %s 类型", 
                e.getName(), e.getRequiredType().getSimpleName());
        logger.warn("参数类型异常: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(message));
    }
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Object> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ApiResponse.error("RUNTIME_ERROR", "系统运行异常: " + e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.error("SYSTEM_ERROR", "系统异常: " + e.getMessage());
    }
}
