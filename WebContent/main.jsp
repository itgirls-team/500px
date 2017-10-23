<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page</title>
</head>
<body>
<c:if test="${ sessionScope.user == null }">
			<c:redirect url="login.jsp"></c:redirect>
</c:if>
<jsp:include page="header.jsp"></jsp:include>
<img id="profilePicture" src="avatar">
<div class="main">
	<form action="description.jsp">
				<input type="submit" value="Description">
	</form>
	<form action="contact.jsp">
				<input type="submit" value="Contacts">
	</form>
	<form action="albums">
			<input type="submit" value="Albums">
	</form>

	<form action="following">
				<input type="hidden" name="pageToRedirect" value="following.jsp" /> 	
				<input type="submit" value="Following">
	</form>
	
	<form action="followers">
				<input type="hidden" name="pageToRedirect" value="followers.jsp" />	
				<input type="submit" value="Followers">
	</form>
</div>
				
</body>	
</body>
</html>