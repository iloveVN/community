package com.garen.community.config;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 400);
        //判断异常的类型,返回不一样的返回值
        if(ex instanceof MissingServletRequestParameterException){
            map.put("msg","缺少必需参数："+((MissingServletRequestParameterException) ex).getParameterName());
        }
        else if(ex instanceof MyException){
            map.put("msg","这是自定义异常");
        }
        else if( ex instanceof RuntimeException) {
            map.put("msg", "这是RuntimeException: " + ex.getMessage());
        }
        else if( ex instanceof BindException) {
            BindException bindException = (BindException)ex;
            BindingResult bindingResult = bindException.getBindingResult();
            StringBuilder errMsg = new StringBuilder(bindingResult.getFieldErrors().size() * 16);
            errMsg.append("Invalid request:");
            for (int i = 0 ; i < bindingResult.getFieldErrors().size() ; i++) {
                if(i > 0) {
                    errMsg.append(",");
                }
                FieldError error = bindingResult.getFieldErrors().get(i);
                errMsg.append(error.getField()+":"+error.getDefaultMessage());
            }
            map.put("errcode", 500);
            map.put("errmsg", errMsg.toString());
        }
        return map;
    }

}
