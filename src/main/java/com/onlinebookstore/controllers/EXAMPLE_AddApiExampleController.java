package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.EXAMPLE_ApiExampleDAO;
import com.onlinebookstore.models.EXAMPLE_ApiExample;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Клас-сервлет для реалізації логіки роботи сервера при отриманні запиту клієнта на додавання
 * нового об'єкту типу EXAMPLE_ApiExample у БД.
 */
@WebServlet("/secure/add_api_example")
public class EXAMPLE_AddApiExampleController extends HttpServlet {
    private EXAMPLE_ApiExampleDAO apiExampleDAO;


    public void init() {
        apiExampleDAO = new EXAMPLE_ApiExampleDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String code = request.getParameter("code");

        EXAMPLE_ApiExample apiExample = new EXAMPLE_ApiExample(title, code);
        apiExampleDAO.insertApiExample(apiExample);

        response.sendRedirect(request.getContextPath() + "/api-examples");
    }
}
