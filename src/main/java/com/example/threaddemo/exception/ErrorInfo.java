package com.example.threaddemo.exception;

import lombok.Data;

@Data
public class ErrorInfo {

    private Integer row;

    private Integer column;

    private String errorMsg;
}
