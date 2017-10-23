<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Posts</h1>
	<c:forEach items="${sessionScope.posts}" var="post">
		<a href="post?postId=${post.id}">${post.path}</a>
	</c:forEach>
	
	<form action="uploadPost.jsp" method="post">
				<input type="submit" value="Upload Post">
		</form>
	
</body>
</html>