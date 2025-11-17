<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.igelgeinventoryappm08.UserType" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><c:out value="${pageTitle != null ? pageTitle : 'MCC Bookstore – Login'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
</head>
<body>
<header style="display: flex; align-items: center; justify-content: space-between; padding: 10px;">
    <div style="flex: 0 0 auto;">
        <img src="${pageContext.request.contextPath}/Images/header.png" alt="Header Logo" style="height: 60px;" />
    </div>
    <div style="flex: 1 1 auto; text-align: center;">
        <h1 style="margin: 0;">MCC Bookstore Inventory</h1>
    </div>
    <div style="flex: 0 0 auto; text-align: right;">
        <a href="${pageContext.request.contextPath}/login">Login</a>
    </div>
</header>

<nav>
    <a href="${pageContext.request.contextPath}/index.jsp">Home</a> |
    <a href="${pageContext.request.contextPath}/inventory?action=list">List Items</a> |
    <a href="${pageContext.request.contextPath}/inventory?action=search">Search</a>
</nav>
<hr/>

<main style="padding: 20px;">
    <h2>Login</h2>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required />
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required />
        </div>
        <div>
            <button type="submit">Login</button>
        </div>
    </form>
    <c:if test="${not empty error}">
        <p style="color: red;"><c:out value="${error}" /></p>
    </c:if>
</main>

</body>
</html>
