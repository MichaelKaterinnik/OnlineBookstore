package com.onlinebookstore.temporaryTrashCLasses;

import jakarta.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/backColorText")
public class BackColorTextServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Получаем параметр “text” и “color” из запроса
        String text = request.getParameter("text");
        String color = request.getParameter("color");

        // Печатаем HTML в качестве ответа для браузера
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head> <title> ColorTextServlet </title> </head>");
            out.println("<body>");
            out.println("<h1 style=color:" + color + ">" + text + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
