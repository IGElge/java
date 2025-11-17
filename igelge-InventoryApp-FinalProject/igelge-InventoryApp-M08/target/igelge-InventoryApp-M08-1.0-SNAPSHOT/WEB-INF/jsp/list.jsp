<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<h2>Inventory</h2>

<form action="${pageContext.request.contextPath}/inventory" method="get">
    <input type="hidden" name="action" value="search"/>
    <input type="text" name="query" placeholder="Search name or manufacturer"/>
    <button type="submit">Search</button>

    <select name="type" onchange="if(this.value!='') { window.location='${pageContext.request.contextPath}/inventory?action=filter&type='+this.value }">
        <option value="">Filter by type</option>
        <option value="FOOD_DRINK">Food & Drink</option>
        <option value="APPAREL">Apparel</option>
        <option value="ACCESSORY">Accessory</option>
        <option value="BOOK">Book</option>
        <option value="SCHOOL_MATERIAL">School Material</option>
    </select>
    <a href="${pageContext.request.contextPath}/inventory?action=list">Reset</a>
</form>

<c:choose>
    <c:when test="${empty items}">
        <p>No items available.</p>
    </c:when>
    <c:otherwise>
        <div style="display:flex;flex-wrap:wrap;gap:12px">
            <c:forEach var="item" items="${items}">
                <section style="border:1px solid #ccc;padding:8px;">
                    <c:choose>
                        <c:when test="${not empty item.image.base64Image}">
                            <img src="data:image;base64,${item.image.base64Image}" width="120"/>
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/Images/noimage.png" width="120"/>
                        </c:otherwise>
                    </c:choose>
                    <h3><a href="${pageContext.request.contextPath}/inventory?action=view&itemID=${item.id}">${item.name}</a></h3>
                    <p>${item.type}</p>
                    <p>$${item.price}</p>
                </section>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
