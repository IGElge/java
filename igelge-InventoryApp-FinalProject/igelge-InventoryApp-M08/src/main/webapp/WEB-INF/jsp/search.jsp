<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/jsp/header.jsp" />
<h2>Search</h2>
<form action="${pageContext.request.contextPath}/inventory" method="get">
    <input type="hidden" name="action" value="search"/>
    <input type="text" name="query" placeholder="Enter query" required />
    <button type="submit">Search</button>
</form>
<jsp:include page="/WEB-INF/jsp/footer.jsp" />
