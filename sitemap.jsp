<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="fr.cg44.plugin.socle.SocleUtils"%>
<%--

  @Summary: Generates a sitemap, by following guidelines Sitemaps XML format http://www.sitemaps.org/
  @Copyright: Jalios SA - Sitemap Plugin
  
--%><%
  // inform doInitPage to set the proper content type
  request.setAttribute("ContentType", "text/xml; charset=UTF-8"); 
  
%><%@ include file="/jcore/doInitPage.jsp" %><%!

 public void generateUrl(JspWriter out, Locale userLocale, Data data, float priority) throws IOException {
   out.println("\t<url>");
   
   out.print("\t\t<loc>");
   out.print(channel.getUrl());
   out.print(data.getDisplayUrl(userLocale));
   // Force le portail full display de l'espace enseignant pour les fiche ressource
   if("FicheRessource".equals(data.getClass().getSimpleName())) {
       out.print("?portal=" + channel.getProperty("jcmsplugin.espaceenseignants.full.portal"));
   }
   out.println("</loc>");
   
   out.print("\t\t<lastmod>");
   out.print(DateUtil.formatW3cDate(data.getMdate()));
   out.println("</lastmod>");
   
   if (priority >= 0) {
     out.print("\t\t<priority>");
     out.print(priority);
     out.println("</priority>");
   }
   
   out.println("\t</url>");
 }
 
%>
<urlset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"
        xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
<%
 // CATEGORIES
  
 DataSelector authorizedCategoriesSelector = Category.getAuthorizedSelector(loggedMember);
 Category homeCat = PortalManager.getHomeCategory();
 TreeSet navCatSet = new TreeSet(Category.getOrderComparator(userLang));
 Category homeParentCat = homeCat.getParent(); 
 navCatSet.addAll(homeParentCat.getChildrenSet());
 navCatSet = JcmsUtil.select(navCatSet, authorizedCategoriesSelector, null);
 

 
 // CONTENTS
 
 // Liste blanche des contenus à prendre en compte
 String[] whitelistTab = channel.getStringArrayProperty("jcmsplugin.seo.sitemap.content.whitelist", new String[]{});
 List<String> whitelistList = Arrays.asList(whitelistTab);
 
 
 Set contentSet = channel.getPublicationSet(Content.class, loggedMember);
 for (Iterator it = contentSet.iterator(); it.hasNext(); ) {
   Content content = (Content) it.next();

   // Skip media documents
   if (content instanceof FileDocument) {
     FileDocument doc = (FileDocument) content;
     if (doc.isImage() || doc.isAudio() ||
         doc.isVideo() || doc.isFlash()) {
       continue;
     }
   }
     
   
   // Passe les contenus sur la liste noire et les contenus importés
   if((Util.notEmpty(whitelistList) && !whitelistList.contains(content.getClass().getSimpleName()))
   		|| content.isImported() || SocleUtils.isNonRepertoriee(content) ) {
       continue;
   }
   
   generateUrl(out, userLocale, content, -1);
 }
 
%>
</urlset>
