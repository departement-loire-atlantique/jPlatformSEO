<%@ page contentType="text/html; charset=UTF-8"%>
<% request.setAttribute("inFO", true); %>
<%@ include file='/jcore/doInitPage.jspf' %>

<% request.setAttribute("titrePage", "500"); %>
<% request.setAttribute("titreErreur", glp("plugin.seo.error.500.titre")); %>
<% request.setAttribute("soustitreErreur", glp("plugin.seo.error.500.soustitre")); %>

<%@include file="erreur50x.jspf" %>
