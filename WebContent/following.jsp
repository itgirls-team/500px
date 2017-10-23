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

<c:if test="${!sessionScope.noFollowed}">
<table id="following-table" border="1">
			<c:forEach var="entry" items="${sessionScope.isFollowed}">
				<tr>
					<td><img id="avatar" src="avatar"></td>
					<td> ${entry.key.userName}</td>	
					<c:set var="followername" scope="session" value="${entry.key.userName}"/>			
						<c:if test="${entry.value}">
								<td>
										<form action="unfollow" method="post">
										<input type="hidden" name="followedUserName" value="${followername}" />
											<input type="submit" value="Unfollow"/> 
										</form>
						</c:if>
						<c:if test="${not entry.value}">
								<td>
										<form  action="follow" method="post">
										<input type="hidden" name="followedUserName" value="${followername}" />
											<input type="submit" value="Follow"/> 
										</form>
								</td>
						</c:if>
				</tr>
			</c:forEach>		
	</table>
</c:if>
<c:if test="${sessionScope.noFollowed}">
		<p>You're not following anyone!</p>
</c:if>
</body>
</html>