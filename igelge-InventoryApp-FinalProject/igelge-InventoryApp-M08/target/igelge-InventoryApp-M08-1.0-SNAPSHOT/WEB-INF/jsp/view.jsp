<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/header.jsp" />
<c:set var="item" value="${requestScope.item}" />
<h2><c:out value="${item.name}" /></h2>

<c:choose>
    <c:when test="${not empty item.image.base64Image}">
        <img src="data:image;base64,${item.image.base64Image}" width="200"/>
    </c:when>
    <c:otherwise>
        <img src="${pageContext.request.contextPath}/Images/noimage.png" width="200"/>
    </c:otherwise>
</c:choose>

<p><strong>Manufacturer:</strong> <c:out value="${item.manufacturer}" /></p>
<p><strong>Type:</strong> <c:out value="${item.type}" /></p>
<p><strong>Price:</strong> $<c:out value="${item.price}" /></p>

<p>
    <strong>Inventory:</strong>
<form method="post" action="${pageContext.request.contextPath}/inventory" style="display:inline">
    <input type="hidden" name="action" value="updateInventory" />
    <input type="hidden" name="itemID" value="${item.id}" />
    <input type="number" name="inventory" value="${item.inventory}" min="0" />
    <button type="submit">Update</button>
</form>
</p>

<c:if test="${not empty sessionScope.user and (sessionScope.user.role.name() == 'MANAGER' or sessionScope.user.role.name() == 'ADMIN')}">
    <p>
        <a href="${pageContext.request.contextPath}/inventory?action=create&edit=true&itemID=${item.id}">Edit</a>
        |
        <a href="${pageContext.request.contextPath}/inventory?action=delete&itemID=${item.id}" onclick="return confirm('Delete this item?')">Delete</a>
    </p>
</c:if>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
