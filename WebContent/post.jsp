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
			<h5>Description :  ${sessionScope.post.description} </h5><br>
			<h5>Likes : ${sessionScope.post.countsOfLikes} </h5><br>
			<h5>Dislikes : ${sessionScope.post.countsOfDislikes} </h5><br>
			<h5>Date : ${sessionScope.post.dateOfUpload} </h5><br>
			<br>
			<h5>Tags : </h5>
			<c:forEach items="${sessionScope.post.tagsOfPost}" var="tag">
					<h6>${tag.title}</h6>
			</c:forEach>
			
			<br>
			<h5>Who like post : </h5>  <br>
			<c:forEach items="${sessionScope.post.usersWhoLike}" var="user">
					<h6>${user.userName}</h6> 
			</c:forEach>
			<br>
			<h5>Who dislike post  : </h5> <br>
			<c:forEach items="${sessionScope.post.usersWhoDislike}" var="user">
					<h6> ${user.userName} </h6> 
			</c:forEach>
			<br>
			<h5> Comments : </h5> <br>
			<c:forEach items="${sessionScope.post.commentsOfPost}" var="comment">
				<h6>Username : ${comment.user.id} </h6> <br>
				<h6>Description : ${comment.description} </h6> <br>
				<h6>Date : ${comment.dateAndTimeOfUpload} </h6> <br>
				<h6>Likes : ${comment.numberOfLikes} </h6> <br>
				<h6>Dislikes : ${comment. numberOfDislikes} </h6> <br>
			</c:forEach>
	
</body>
</html>