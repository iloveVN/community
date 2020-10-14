package com.garen.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;

public class AppErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }
}
