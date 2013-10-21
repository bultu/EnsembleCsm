<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="navbarDb.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="refresh" content="1;url=<s:url includeParams="all" />"/> 
<title>Action in Progress</title>
</head>
<body>

<div style = "position:absolute; left: 50%; top : 45%">Please Wait</div>
<img src='images/ajax-loader.gif' style = "position:absolute; left: 45%; top : 50%">
</body>
</html>