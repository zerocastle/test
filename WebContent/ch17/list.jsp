<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

<h1>List</h1>

<c:if test="${!empty members}">
	<c:forEach var="p" items="${members}">
		id : ${p.id} / passwd: ${p.passwd} / name:  ${p.name} / reg_date : ${p.reg_date} 
		/address : ${p.address} / tel : ${p.tel} 
		<br>
	</c:forEach>
</c:if>

</body>
</html>