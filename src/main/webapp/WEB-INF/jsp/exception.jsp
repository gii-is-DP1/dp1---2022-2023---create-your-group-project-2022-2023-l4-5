<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="nt4h" tagdir="/WEB-INF/tags" %>

<nt4h:layout pageName="error">

    <spring:url value="/resources/images/piter.png" var="logoImage"/>
    <img class="img-responsive" src="${logoImage}" width=30%/>

    <h2>Más vale mudo callao que proyecto fallao</h2>

    <p>${exception.message}</p>

</nt4h:layout>
