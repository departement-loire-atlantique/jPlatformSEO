<%@ include file='/jcore/doInitPage.jspf' %><%
%><%@ include file='/jcore/portal/doPortletParams.jspf' %>

<%
if (jcmsContext.isInFrontOffice() || "true".equals(request.getAttribute("pageErreur"))) { %>
    
    <%-- Alimentation du dataLayer pour Google Tag Manager --%>
    <%
    String pageType = "";
    String breadcrumb = "";
    
    Publication pub = jcmsContext.getPublication();

    if(Util.notEmpty(pub)){
        pageType = pub.getClass().getSimpleName();
    }
    if(Util.notEmpty(currentCategory)){
        breadcrumb = HttpUtil.encodeForHTMLAttribute(currentCategory.getAncestorString(" > ", true));
    }
    %>
    
    <script
        type="opt-in"
        data-type="application/javascript"
        data-name="google-tag-manager">
        window.dataLayer = window.dataLayer || [];
        var data = {
            chemin_fer:'<%= breadcrumb %>',
            type_page:'<%= pageType %>'
                };
        window.dataLayer.push(data);
    </script>
    
    <!-- Google Tag Manager -->
    <script
        type="opt-in"
        data-type="application/javascript"
        data-name="google-tag-manager">
        (function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
        'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
        })(window,document,'script','dataLayer','<%=channel.getProperty("plugin.seo.gtm.id")%>');
    </script>
    <!-- End Google Tag Manager -->
<%}%>

