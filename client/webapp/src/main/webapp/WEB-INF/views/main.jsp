<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url value="/resources/css/main.css" var="mainCss"/>
<spring:url value="/resources/js/main.js" var="mainJs"/>

<html>
<head>
    <title>Title</title>

    <%-- css --%>
    <link href="${mainCss}" rel="stylesheet"/>

    <%-- js --%>
    <script src="${mainJs}"></script>
</head>
<body>

<jsp:include page="../templates/chat-frame.jsp"/>

</body>
</html>