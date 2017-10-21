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
	<c:forEach items="${sessionScope.post}" var="p">
			
			Description :  <c:out value="${p.description }"></c:out>
			Likes : <c:out value="${p.countsOfLikes }"></c:out>
			Dislikes :  <c:out value="${p.countsOfDislikes }"></c:out>
			Date :  <c:out value="${p.dateOfUpload }"></c:out>
			
			Who like post : 
			<c:forEach items="${p.usersWhoLike}" var="user">
				<c:out value="#${user.username }"></c:out>
			</c:forEach>
			
			<br> Tags :
			<c:forEach items="${p.usersWhoDislike}" var="user">
				<c:out value="#${user.username }"></c:out>
			</c:forEach>
			
			<br> Comments :
			<c:forEach items="${p.commentsOfPost}" var="comment">
				Username : <c:out value="#${comment.user.username }"></c:out>
				Description : <c:out value="#${comment.description }"></c:out>
				Date : <c:out value="#${comment.dateAndTimeOfUpload }"></c:out>
				Likes : <c:out value="#${comment.numberOfLikes }"></c:out>
				Dislikes :  <c:out value="#${comment.numberOfDislikes }"></c:out>
			</c:forEach>
			
	</c:forEach>
</body>
</html>