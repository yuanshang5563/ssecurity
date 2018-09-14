<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

<html>
<head>
    <title>admin</title>
</head>
<body>
<h1>该页面只有权限为ROLE_ADMIN的用户才可以访问</h1>
    <a href="${pageContext.request.contextPath}/j_spring_security_logout">退出登陆</a>
</body>
</html>
