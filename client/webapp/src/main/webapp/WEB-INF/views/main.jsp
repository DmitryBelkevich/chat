<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/resources/css/main.css" var="mainCss" />

<html>
<head>
    <title>Title</title>
    <link href="${mainCss}" rel="stylesheet" />
</head>
<body>

<div id="content"></div>
<form class="form" role="form">
    <div id="">
        <input id="inputField" type="text" class="form-control" placeholder="type message" />
        <button id="sendButton" type="button" class="btn btn-default">send</button>
    </div>
</form>

<script src="/resources/js/main.js"></script>

</body>
</html>