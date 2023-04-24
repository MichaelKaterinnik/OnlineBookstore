package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.EXAMPLE_ApiExampleDAO;
import com.onlinebookstore.models.EXAMPLE_ApiExample;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Клас-сервлет для реалізації логіки роботи сервера при отриманні запиту клієнта на виведення на екран
 * певного об'єкту типу ApiExample за наданим користувачем у запиті параметром "sectionID", який, в свою
 * чергу, є у таблиці із EXAMPLE_ApiExample. Пири уьому об'єктів із таким значенням може бути декілька,
 * тому результат віддаємо у List.
 */
@WebServlet(name = "ApiExampleController", urlPatterns = {"/api-examples"})
public class EXAMPLE_ApiExampleController extends HttpServlet {
    private final EXAMPLE_ApiExampleDAO apiExampleDAO = new EXAMPLE_ApiExampleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int sectionId = Integer.parseInt(request.getParameter("sectionId"));

        List<EXAMPLE_ApiExample> apiExamples = apiExampleDAO.getApiExamplesBySectionId(sectionId);
        request.setAttribute("apiExamples", apiExamples);
        request.getRequestDispatcher("/WEB-INF/views/api_example/index.jsp").forward(request, response);
    }
}
