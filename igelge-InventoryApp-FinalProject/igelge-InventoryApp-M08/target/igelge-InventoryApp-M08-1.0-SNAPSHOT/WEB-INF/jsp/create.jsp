<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<h2>Add / Edit Item</h2>

<form method="post" action="${pageContext.request.contextPath}/inventory" enctype="multipart/form-data">
    <input type="hidden" name="action" value="create"/>
    Name: <input type="text" name="name" required/><br/>
    Manufacturer: <input type="text" name="manufacturer"/><br/>
    Price: <input type="number" step="0.01" name="price" required/><br/>
    Inventory: <input type="number" name="inventory" required/><br/>
    Type:
    <select name="type">
        <option value="FOOD_DRINK">Food & Drink</option>
        <option value="APPAREL">Apparel</option>
        <option value="ACCESSORY">Accessory</option>
        <option value="BOOK">Book</option>
        <option value="SCHOOL_MATERIAL">School Material</option>
    </select><br/>
    Image: <input type="file" name="imageFile"/><br/>
    <button type="submit">Save</button>
</form>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
