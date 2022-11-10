<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="games">

    <h2>Game Information</h2>

    <h1>SERÍA EL LOBBY?</h1>

    <spring:url value="/welcome"  var="goToLobby">
    </spring:url>
    <a href="${fn:escapeXml(goToLobby)}" class="btn btn-default">Go to Lobby</a>

</petclinic:layout>
