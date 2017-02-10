package com.huotu.tourist.controller.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 *
 * @author slt
 */
@ControllerAdvice
public class Advice {
    private static final Log log = LogFactory.getLog(Advice.class);


    /**
     * 权限不足异常处理
     *
     * @param request
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedException(HttpServletRequest request, Exception ex, Model model) {
        String path = request.getRequestURI();
        model.addAttribute("path", path);
        model.addAttribute("code", 403);
        model.addAttribute("message", ex.getMessage());
        return "redirect:/error";
    }

    /**
     * 所有未知异常统一处理
     *
     * @param request
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public String noHandlerFoundException(HttpServletRequest request, Exception ex, Model model) {
//        log.info(ex.getMessage());
        log.error("web request error", ex);
        String path = request.getRequestURI();
        model.addAttribute("path", path);
        model.addAttribute("code", 500);
        model.addAttribute("message", ex.getMessage());
        return "redirect:/error";
    }


}
