<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<h2>Edit Item</h2>
<form method="POST" action="${pageContext.request.contextPath}/inventory" enctype="multipart/form-data">
    <input type="hidden" name="action" value="edit" />
    <input type="hidden" name="id" value="${item.id}" />

    <label>Name: <input type="text" name="name" value="${item.name}" required></label><br>
    <label>Manufacturer: <input type="text" name="manufacturer" value="${item.manufacturer}" required></label><br>
    <label>Price: <input type="number" step="0.01" name="price" value="${item.price}" required></label><br>
    <label>Inventory: <input type="number" name="inventory" value="${item.inventory}" required></label><br>
    <label>Type:
        <select name="type">
            <c:forEach var="type" items="${T(com.example.igelgeinventoryappm08.ItemType).values()}">
                <option value="${type}" <c:if test="${type == item.type}">selected</c:if>>${type}</option>
            </c:forEach>
        </select>
    </label><br>
    <label>Image: <input type="file" name="imageFile"></label><br>
    <input type="submit" value="Update Item">
</form>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
