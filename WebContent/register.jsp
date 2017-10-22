<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
<c:if test="${ requestScope.error == null }">
			<h1>Welcome, please register!</h1>
</c:if>
<c:if test="${ requestScope.error != null }">
			<h1 style="color: red">Sorry, registration unsuccessfull. Reason: ${requestScope.error }</h1>
</c:if>
<form action="register" method="post" enctype="multipart/form-data">
	Username<input type="text" name="username"><br>
	Password<input type="password" name="password"><br>
	Confirm password<input type="password" name="confpassword"><br>
	First Name<input type="text" name="firstname"><br>
	Last Name<input type="text" name="lastname"><br>
	Email<input type="email" name="email"><br>
	Description <input type="text" name="description"><br>
	Avatar<input type="file" name="avatar"><br>
	<input type="submit" value="Register">
</form>
If you already have an account, please login <a href="login.jsp">here</a>.
</body>
</html>