<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
<c:if test="${ requestScope.error == null }">
			<h1>Welcome, please login!</h1>
</c:if>
<c:if test="${ requestScope.error != null }">
			<h4 style="color: red">Sorry, Reason: ${requestScope.error }.Please try again!</h4>
</c:if>
<form action="login" method="post">
	Username<input type="text" name="username"><br>
	Password<input type="password" name="password"><br>
	<input type="submit" value="Login">
</form>
If you don't have an account, please register <a href="register.jsp">here</a>.
</body>
</html>