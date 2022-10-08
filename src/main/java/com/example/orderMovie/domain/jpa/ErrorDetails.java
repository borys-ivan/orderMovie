package com.example.orderMovie.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorDetails {
        private HttpStatus status;
        private String message;
        private String details;
        private Date timestamp;

}
