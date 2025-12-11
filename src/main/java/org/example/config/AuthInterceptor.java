package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        if (path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images") ||
                path.equals("/login") || path.equals("/register") || path.equals("/error") || path.equals("/")) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(contextPath + "/login");
            return false;
        }

        if (user.getRole() == Role.USER) {
            boolean isAllowed = path.equals("/subscribers") ||
                    path.matches("/subscribers/\\d+") ||
                    path.equals("/services") ||
                    path.equals("/logout");

            if (!isAllowed) {
                response.sendError(403, "Доступ запрещен.");
                return false;
            }
        }

        return true;
    }
}