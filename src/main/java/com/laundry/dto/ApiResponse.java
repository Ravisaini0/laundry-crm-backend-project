package com.laundry.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private String message;
    private T data;

    public ApiResponse(String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
