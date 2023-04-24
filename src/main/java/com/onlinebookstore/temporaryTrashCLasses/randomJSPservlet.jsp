<%@ page contentType="text/html;charset=UTF-8"
         language="java"
         import="java.util.Date"
         import="java.lang.Math" %>
<html>
<body>
<%
    double num = Math.random();
    if (num > 0.95) {
%>
<h2>Ты счастливчик, user!</h2><p>(<%= num %>)</p>
<%
} else {
%>
<h2> Сегодня не твой день, user!</h2><p>(<%= num %>)</p>
<%
    }
%>
</body>
</html>