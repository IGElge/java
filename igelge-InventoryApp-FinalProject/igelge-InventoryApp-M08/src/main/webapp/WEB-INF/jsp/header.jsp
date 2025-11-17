<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.igelgeinventoryappm08.UserType" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><c:out value="${pageTitle != null ? pageTitle : 'MCC Bookstore'}" /></title>
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
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                Welcome, <c:out value="${sessionScope.user.username}" /> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>

<nav>
    <a href="${pageContext.request.contextPath}/index.jsp">Home</a> |
    <a href="${pageContext.request.contextPath}/inventory?action=list">List Items</a> |
    <a href="${pageContext.request.contextPath}/inventory?action=search">Search</a>
    <c:if test="${not empty sessionScope.user and (sessionScope.user.role.name() == 'MANAGER' or sessionScope.user.role.name() == 'ADMIN')}">
        | <a href="${pageContext.request.contextPath}/inventory?action=create">Add Item</a>
    </c:if>
</nav>
<hr/>
<main>
