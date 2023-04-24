package com.onlinebookstore.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Виконує обробку помилки 404 (Not Found). У випадку, якщо запит користувача на неіснуючу сторінку
 * (або ресурс), сервер буде відправляти відповідь зі статусом 404, відповідає за рендеринг сторінки з
 * повідомленням про помилку..
 */
@WebServlet(name = "CustomError404HandlerServlet", urlPatterns = {"/CustomError404HandlerServlet"})
public class CustomError404HandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.getRequestDispatcher("/WEB-INF/error/404.jsp")
                .forward(request, response);
    }
}
