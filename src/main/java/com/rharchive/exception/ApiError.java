// exception/ApiError.java
package com.rharchive.exception;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @Builder
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;
}