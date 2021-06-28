package fr.cg44.plugin.seo;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jalios.jcms.Category;
import com.jalios.jcms.Channel;
import com.jalios.jcms.Publication;
import com.jalios.jcms.portlet.PortalManager;
import com.jalios.util.Util;

import fr.cg44.plugin.socle.SocleUtils;

public class SEOExtensionUtils {

  private static final Logger logger = Logger.getLogger(SEOExtensionUtils.class);
  private static final Channel channel = Channel.getChannel();
  private static final Category navigationBranchCat = channel.getCategory(channel.getProperty("plugin.seo.category.navigation.id","j_5"));
  public static final String metaRobotsContentOK = "index,follow";
  public static final String metaRobotsContentNOK = "noindex,nofollow";
  
  public static final String getTitlePage(Channel channel, String defaultTitlePage){
    try{
    HttpServletRequest request = channel.getCurrentServletRequest();
    Object obj = request.getAttribute(PortalManager.PORTAL_PUBLICATION);
    if(Util.notEmpty(obj) && obj instanceof Publication){
      Publication pub = (Publication)obj;
      if (Util.notEmpty(pub.getExtraData("extra.Content.jcmsplugin.seo.titreseo." + channel.getCurrentUserLang()))) {
          return pub.getExtraData("extra.Content.jcmsplugin.seo.titreseo." + channel.getCurrentUserLang()) + " - " + channel.getName();
      }
      return getExtraDBPublicationTitle(pub,channel.getCurrentUserLang(),true);
    }
    Category cat = (Category)request.getAttribute(PortalManager.PORTAL_CURRENTCATEGORY);
    if(Util.notEmpty(cat)){
      return getExtraDBCategoryTitle(cat,channel.getCurrentUserLang(),true);
    }
    }catch(Exception ex){
      logger.error("Erreur recuperation title of page",ex);
    }
    int tailleMaxTitle = Integer.parseInt(channel.getProperty(SEOConstants.PROPERTY_SEO_MAXLENGTH_META_TITLE,"65"));
    String titleOfPage = defaultTitlePage;
    if(titleOfPage.length()>tailleMaxTitle){
      logger.info("SEOPlugin : Le titre "+titleOfPage+" est trop long, il doit être limité à "+tailleMaxTitle+" caractères");
    }
    return titleOfPage;
  }
  
  /*
  public static final String getExtraDBCategoryMetaDescription(Category cat, String lang, boolean useDefault,Channel channel) throws NoSuchFieldException {
    if(cat == null) {
      return "";
    }
    String labSpe = cat.getExtraDBData(SEOConstants.EXTRADB_CATEGORY_SEO_DESCRIPTION+"."+lang); 
    if(Util.notEmpty(labSpe)){
      return labSpe;
    }else{
      labSpe = SEOUtils.clean(cat.getDescription(lang, useDefault),Integer.parseInt(channel.getProperty(SEOConstants.PROPERTY_SEO_MAXLENGTH_META_DESCRIPTION,"50")));
      if(Util.isEmpty(labSpe)) {
        labSpe=channel.getProperty(SEOConstants.DEFAULT_PUBLICATION_SEO_DESCRIPTION+"."+lang,"");
      }
      return labSpe;
    }
  }
*/
  
  public static final String getExtraDBCategoryTitle(Category cat, String lang, boolean useDefault ) throws NoSuchFieldException {
    if(cat == null) {
      return "";
    }
    String labSpe = cat.getExtraDBData(SEOConstants.EXTRADB_CATEGORY_SEO_TITLE+"."+lang); 
    return Util.notEmpty(labSpe)? labSpe : cat.getName(lang, useDefault);
  }

  
  /*
  public static final String getExtraDBPublicationMetaDescription(Publication pub, String lang, boolean useDefault,Channel channel) throws NoSuchFieldException {
    if(pub == null) {
      return "";
    }
    String labSpe = pub.getExtraDBData(SEOConstants.EXTRADB_PUBLICATION_SEO_DESCRIPTION+"."+lang); 
    if(Util.notEmpty(labSpe)){
      return labSpe;
    }else{
      labSpe = SEOUtils.clean(pub.getAbstract(lang, useDefault),Integer.parseInt(channel.getProperty(SEOConstants.PROPERTY_SEO_MAXLENGTH_META_DESCRIPTION,"50")));
      if(Util.isEmpty(labSpe)) {
        labSpe=channel.getProperty(SEOConstants.DEFAULT_PUBLICATION_SEO_DESCRIPTION+"."+lang,"");
      }
      return labSpe;
    }
  }
  */
  
  
  public static final String getExtraDBPublicationTitle(Publication pub, String lang, boolean useDefault ) throws NoSuchFieldException {
    if(pub == null) {
      return "";
    }
    String labSpe = pub.getExtraDBData(SEOConstants.EXTRADB_PUBLICATION_SEO_TITLE+"."+lang); 
    return Util.notEmpty(labSpe)? labSpe : pub.getTitle(lang, useDefault);
  } 
   
  
  /*
  public static boolean isCategoryVisibleInURL(Category cat){
    String visibleInURL = cat.getExtraDBData(SEOConstants.EXTRADB_CATEGORY_SEO_CLASSEMENT);
    logger.debug(cat.getName()+"-"+visibleInURL);
    if(Util.notEmpty(visibleInURL) && "false".equals(visibleInURL)){
      return false;
    }
    return true;
  }
  */
  
  /*
  public static final String getExtraDBPublicationMetaKeywords(Publication pub, String lang, boolean useDefault,Channel channel) throws NoSuchFieldException {
    if(pub == null) {
      return "";
    }
    String labSpe = pub.getExtraDBData(SEOConstants.EXTRADB_PUBLICATION_SEO_KEYWORDS+"."+lang); 
    if(Util.notEmpty(labSpe)){
      return labSpe;
    }else{
      labSpe=channel.getProperty(SEOConstants.DEFAULT_PUBLICATION_SEO_KEYWORDS+"."+lang,"");
      return labSpe;
    }
  } 
  */ 
  
  /*
  public static final String getExtraDBCategoryMetaKeywords(Category cat, String lang, boolean useDefault,Channel channel) throws NoSuchFieldException {
    if(cat == null) {
      return "";
    }
    String labSpe = cat.getExtraDBData(SEOConstants.EXTRADB_CATEGORY_SEO_KEYWORDS+"."+lang); 
    if(Util.notEmpty(labSpe)){
      return labSpe;
    }else{
      labSpe=channel.getProperty(SEOConstants.DEFAULT_PUBLICATION_SEO_KEYWORDS+"."+lang,"");
      return labSpe;
    }
  } 
  */ 
  
  /*
  public static String getExtraDBCategoryNameURL(Category cat,String lang){
    String libelle = cat.getExtraDBData(SEOConstants.EXTRADB_CATEGORY_SEO_TITLE_SHORT+"."+lang);
    if(Util.notEmpty(libelle)){
      return libelle;
    }
    return cat.getName(lang);
  }
  */

  /**
   * Calcul de l'attribut "content" de la balise meta "robots" pour une publication donnée.
   * Si la publication est protégée par des droits OU qu'elle est dans une catégorie / sous catégorie protégée,
   * alors on ne doit pas l'indexer ni suivre les liens.
   * Les valeurs retournées seront soit "noindex,nofollow", soit "index,follow".
   * 
   * ex : <meta name="robots" content="noindex,nofollow" />
   * 
   * @param pub La publication 
   * @return  Une chaine de caractère avec le contenu de l'attribut "content". 
   */
  public static final String getPublicationMetaRobot(Publication pub)  {
	  
	  if(pub.canBeReadBy(null,false) && !SocleUtils.isNonRepertoriee(pub) && !pub.isImported()) {
		  return metaRobotsContentOK;  
	  }

	  return metaRobotsContentNOK;
  }

  /**
   * Calcul de l'attribut "content" de la balise meta "robots" pour une catégorie donnée.
   * Si la catégorie est protégée par des droits OU qu'elle a une catégorie parente protégée,
   * alors on ne doit pas l'indexer ni suivre les liens.
   * De même si elle est dans une branche de catégorie autre que de la navigation.
   * Les valeurs retournées seront soit "noindex,nofollow", soit "index,follow".
   * 
   * ex : <meta name="robots" content="noindex,nofollow" />
   * 
   * @param cat La catégorie
   * @return  Une chaine de caractère avec le contenu de l'attribut "content". 
   */
  public static final String getCategoryMetaRobot(Category cat) {
	  if(null!=cat && cat.canBeReadBy(null,false)) {
		  return metaRobotsContentOK;  
	  }

	  return metaRobotsContentNOK;
  }   
  
}
