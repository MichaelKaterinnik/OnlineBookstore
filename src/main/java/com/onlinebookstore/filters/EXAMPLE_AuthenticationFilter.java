package com.onlinebookstore.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Клас, що є фільтром аутентифікації для веб-додатку. Він перевіряє, чи має поточний користувач дійсну
 * сесію (тобто, чи він успішно пройшов аутентифікацію) та перенаправляє його на сторінку логіну, якщо
 * цього не відбулося. Якщо у користувача є дійсна сесія, то фільтр дозволяє запиту продовжити свій хід і
 * обробляє його далі.
 */
public class EXAMPLE_AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
