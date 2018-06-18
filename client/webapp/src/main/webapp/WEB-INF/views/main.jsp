<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- tags --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- css --%>
<spring:url value="/resources/css/main.css" var="mainCss"/>

<%-- js --%>
<spring:url value="/resources/js/socket.js" var="socketJs"/>
<spring:url value="/resources/js/main.js" var="mainJs"/>

<html>
<head>
    <title>Title</title>

    <%-- css --%>
    <link href="${mainCss}" rel="stylesheet"/>

    <%-- js --%>
    <script src="${socketJs}"></script>
    <script src="${mainJs}"></script>
</head>
<body>

<jsp:include page="../templates/chat-frame.jsp"/>

</body>
</html>