package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.EXAMPLE_ApiSectionDAO;
import com.onlinebookstore.models.EXAMPLE_ApiSection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Клас-сервлет для реалізації логіки роботи сервера при отриманні запиту клієнта на додавання
 * нового об'єкту типу EXAMPLE_ApiSection у БД.
 */
@WebServlet("/secure/add_api_section")
public class EXAMPLE_AddApiSectionController extends HttpServlet {

    private EXAMPLE_ApiSectionDAO apiSectionDAO;

    public void init() {
        apiSectionDAO = new EXAMPLE_ApiSectionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        EXAMPLE_ApiSection apiSection = new EXAMPLE_ApiSection(title, description);
        apiSectionDAO.insertApiSection(apiSection);

        response.sendRedirect(request.getContextPath() + "/api-sections");
    }
}
