<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="CreateAlbum" method="post" enctype="multipart/form-data">
			Category<input type="text" name="category"><br>
			Image<input type="file" name="picture"><br>
			<input type="submit" value="Upload post"><br>
	</form>
	
</body>
</html>