<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<form action="UploadPost" method="post" enctype="multipart/form-data">
			Description<input type="text" name="description"><br>
			Tags<input type="text" name="tags"><br>
			Image<input type="file" name="image"><br>
			<input type="submit" value="Upload post"><br>
			
		</form>
	
	
	</body>
</html>