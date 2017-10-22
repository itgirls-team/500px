<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Followers</title>
</head>
<body>
<c:if test="${ sessionScope.user == null }">
			<c:redirect url="login.jsp"></c:redirect>
</c:if>
<jsp:include page="main.jsp"></jsp:include>
	<table id="followers-table" border="1">
		<c:forEach var="follower" items="${ sessionScope.followers }">
				<tr>
					<td>${follower.userName}</td>
					<td><input type="button" value="Yaa"> </td>
				</tr>	
		</c:forEach>
	</table>

</body>
</html>