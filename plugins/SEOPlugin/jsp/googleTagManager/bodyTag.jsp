<%@ include file='/jcore/doInitPage.jspf' %><%
%><%@ include file='/jcore/portal/doPortletParams.jspf' %>
<%
if (jcmsContext.isInFrontOffice()) { %>
	<%-- TODO : tester si l'utilisateur a donné son consentement --%>
	<!-- Google Tag Manager (noscript) -->
	<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=<%=channel.getProperty("plugin.seo.gtm.id")%>"
	height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
	<!-- End Google Tag Manager (noscript) -->
<%}%>