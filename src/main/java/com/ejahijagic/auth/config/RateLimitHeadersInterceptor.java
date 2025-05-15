
package com.ejahijagic.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitHeadersInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String phone = request.getParameter("phoneNumber");
        if (phone != null) {
            response.addHeader("X-RateLimit-Limit", "5");
            response.addHeader("X-RateLimit-Remaining", "0"); // dynamic value could be injected
            response.addHeader("Retry-After", "1800");
        }
    }
}
