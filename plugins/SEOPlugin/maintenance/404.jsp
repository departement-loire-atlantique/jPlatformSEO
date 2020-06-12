<%@ page contentType="text/html; charset=UTF-8"%>
<% request.setAttribute("inFO", true); %>
<%@ include file='/jcore/doInitPage.jspf' %>

<% request.setAttribute("titrePage", "404"); %>
<% request.setAttribute("titreErreur", glp("plugin.seo.error.404.titre")); %>
<% request.setAttribute("soustitreErreur", glp("plugin.seo.error.404.soustitre")); %>

<%@include file="erreur40x.jspf" %>
