package com.jhe.question_bank.common.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.jhe.question_bank.common.exception.CsrfException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CsrfValidator {
    
    public void validate(HttpServletRequest request) {
        Cookie csrfCookie = WebUtils.getCookie(request, "csrfToken");
        if (csrfCookie == null) {
            throw new CsrfException();
        }

        String csrfHeader = request.getHeader("X-CSRF-TOKEN");
        if (csrfHeader == null) {
            throw new CsrfException();
        }

        boolean isCsrfTokenValid = csrfHeader.equals(csrfCookie.getValue());
        if (!isCsrfTokenValid) {
            throw new CsrfException();
        }
    }
}
