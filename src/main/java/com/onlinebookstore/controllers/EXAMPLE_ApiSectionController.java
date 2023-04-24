package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.EXAMPLE_ApiSectionDAO;
import com.onlinebookstore.models.EXAMPLE_ApiSection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Клас-сервлет для реалізації логіки роботи сервера при отриманні запиту клієнта на виведення на екран
 * усіх наявних у БД об'єктів типу EXAMPLE_ApiSection. Отримавши дані з БД перенаправляє клієнта
 * на сформовану за допомогою jsp-файлу веб-сторінку.
 */
@WebServlet(name = "ApiSectionController", urlPatterns = {"/api-sections"})
public class EXAMPLE_ApiSectionController extends HttpServlet {
    private final EXAMPLE_ApiSectionDAO apiSectionDAO = new EXAMPLE_ApiSectionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<EXAMPLE_ApiSection> apiSections = apiSectionDAO.getAllApiSections();

        request.setAttribute("apiSections", apiSections);

        request.getRequestDispatcher("/WEB-INF/views/api_section/index.jsp")
                .forward(request, response);
    }
}
