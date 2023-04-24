package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.EXAMPLE_UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/**
 * Клас-сервлет для реалізації логіки роботи сервера при отриманні запиту клієнта на авторизацію на сервері.
 * З отриманого реквесту виокремлюємо значення "логін-пароль", далі за допомогою екземпляру EXAMPLE_UserDAO
 * отримуємо доступ до таблиці з користувачами, де звіряємо, чи є такий користувач у БД.
 * За результатом авторизації або перенаправляємо користувача у ApiSectionController, або видаємо сторінку
 * з помилкою щодо неправильного логіна/пароля.
 */
@WebServlet(value = "/login")
public class EXAMPLE_LoginController extends HttpServlet {
    private EXAMPLE_UserDAO userDAO;

    public void init() {
        userDAO = new EXAMPLE_UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<String> user = userDAO.findByUsername(username, password);


        if (user.isPresent()) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            response.sendRedirect(request.getContextPath() + "/api-sections");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
        }
    }
}
