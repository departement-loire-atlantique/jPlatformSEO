<%@ include file='/jcore/doInitPage.jspf' %><%
%><%@ include file='/jcore/portal/doPortletParams.jspf' %>

<%
if (jcmsContext.isInFrontOffice() || "true".equals(request.getAttribute("pageErreur"))) { %>
	<%-- TODO : tester si l'utilisateur a donné son consentement --%>
	
	
	<%-- Alimentation du dataLayer pour Google Tag Manager --%>
	<%
	String pageType = "";
	String breadcrumb = "";
	
	Publication pub = jcmsContext.getPublication();

	if(Util.notEmpty(pub)){
		pageType = pub.getClass().getSimpleName();
	}
	if(Util.notEmpty(currentCategory)){
		breadcrumb = currentCategory.getAncestorString(" > ", true);
	}
	%>
	
	<script>
	window.dataLayer = window.dataLayer || [];
	var data = {
			chemin_fer:'<%= breadcrumb %>',
			type_page:'<%= pageType %>'
    	        };
    window.dataLayer.push(data);
	</script>
	
	<!-- Google Tag Manager -->
	<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
	new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
	j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
	'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
	})(window,document,'script','dataLayer','<%=channel.getProperty("plugin.seo.gtm.id")%>');</script>
	<!-- End Google Tag Manager -->
<%}%>

