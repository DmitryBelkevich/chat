<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- tags --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- css --%>
<spring:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" var="bootstrapCss"/>
<spring:url value="/resources/css/main.css" var="mainCss"/>

<%-- js --%>
<spring:url value="https://code.jquery.com/jquery-3.3.1.slim.min.js" var="jqueryJs"/>
<spring:url value="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" var="popperJs"/>
<spring:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" var="bootstrapJs"/>

<spring:url value="/resources/js/socket.js" var="socketJs"/>
<spring:url value="/resources/js/main.js" var="mainJs"/>

<html lang="en">
<head>
    <title>Title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%-- css --%>
    <link rel="stylesheet" href="${bootstrapCss}" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
    <link href="${mainCss}" rel="stylesheet"/>

    <%-- js --%>
    <script src="${jqueryJs}" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="${popperJs}" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="${bootstrapJs}" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
    <script src="${socketJs}"></script>
    <script src="${mainJs}"></script>
</head>
<body>

<div class="container">

    <jsp:include page="../templates/chat-frame.jsp"/>

</div>

</body>
</html>