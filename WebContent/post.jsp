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

			<img src="${sessionScope.post.path}" width="50px" height="auto"/>
			Description :  ${sessionScope.post.description}
			Likes : ${sessionScope.post.countsOfLikes}
			Dislikes : ${sessionScope.post.countsOfDislikes}
			Date : ${sessionScope.post.dateOfUpload}
			
			Who like post : 
			<c:forEach items="${sessionScope.post.usersWhoLike}" var="user">
					<h1>${user.userName}</h1>
			</c:forEach>
			
			<br> Tags :
			<c:forEach items="${sessionScope.post.usersWhoDislike}" var="user">
					${user.userName}
			</c:forEach>
			
			<br> Comments :
			<c:forEach items="${sessionScope.post.commentsOfPost}" var="comment">
				<!-- !!!!Username : ${comment.user.username} -->
				Description : ${comment.description}
				Date : ${comment.dateAndTimeOfUpload}
				Likes : ${comment.numberOfLikes}
				Dislikes : ${comment.numberOfDislikes}
			</c:forEach>
	
</body>
</html>