package fr.cg44.plugin.seo.policyfilter;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jalios.jcms.Category;
import com.jalios.jcms.Channel;
import com.jalios.jcms.Content;
import com.jalios.jcms.Data;
import com.jalios.jcms.DescriptiveURLs;
import com.jalios.jcms.HttpUtil;
import com.jalios.jcms.Member;
import com.jalios.jcms.Publication;
import com.jalios.jcms.context.JcmsContext;
import com.jalios.jcms.context.JcmsJspContext;
import com.jalios.jcms.plugin.Plugin;
import com.jalios.jcms.policy.BasicPortalPolicyFilter;
import com.jalios.jcms.portlet.DisplayContext;
import com.jalios.jcms.portlet.PortalManager;
import com.jalios.jcms.portlet.PortalRedirect;
import com.jalios.util.ServletUtil;
import com.jalios.util.Util;

import fr.cg44.plugin.seo.SEOExtensionUtils;
import fr.cg44.plugin.seo.SEOUtils;
import generated.PortletPortal;
import generated.WelcomeSection;

public class SEOPortalPolicyFilter extends BasicPortalPolicyFilter {
  
  private static final Logger logger = Logger.getLogger(SEOPortalPolicyFilter.class);
  private static Channel channel;
  private static boolean textPartEnabled;
  private static boolean languagePartEnabled;
  private static String publicationFormat;
  private static String portalFormat;
  private static String categoryFormat;
  private static String memberFormat;
  private static String defaultFormat;
  private static String roots;
  private static String prefix;
  private static String fullPortal;
  
  // Nom du paramètre permettant de forcer une catégorie
  public static final String CATEGORY_PARAMETER = "category";
  
  @Override
  public boolean init(Plugin plugin){
    channel = Channel.getChannel();
    textPartEnabled = Util.toBoolean(channel.getProperty("descriptive-urls.text.enabled"), true); 
    languagePartEnabled = (Util.toBoolean(channel.getProperty("descriptive-urls.language.enabled"), true));
    publicationFormat = channel.getProperty("descriptive-urls.text.pub-format");
    categoryFormat = channel.getProperty("descriptive-urls.text.cat-format");
    portalFormat = channel.getProperty("descriptive-urls.text.portal-format");
    memberFormat = channel.getProperty("descriptive-urls.text.mbr-format");
    defaultFormat = channel.getProperty("descriptive-urls.text.dft-format");
    roots = channel.getProperty("descriptive-urls.text.root-cats");
    prefix = channel.getProperty("descriptive-urls.prefix");
    fullPortal = channel.getProperty("plugin.seo.portal.fullDisplay.id");
    return true;
  }


  
  /* Rewrite the path url : add the locale if language part enable, put the id at the end 
   * @see com.jalios.jcms.policy.BasicPortalPolicyFilter#getDescriptiveURLPath(com.jalios.jcms.Data, java.util.Locale)
   */
  @Override
  public String getDescriptiveURLPath(Data data, Locale locale) {
    String urlPath = "";
    String error404Id = channel.getProperty("com.jalios.jcmsplugin.seo.category.id.error.404","");
    String error403Id = channel.getProperty("com.jalios.jcmsplugin.seo.category.id.error.403","");
    // Ajoute la partie textuelle (nom publication et/ou chemin des catégories)
    if (textPartEnabled) {
      String urlText = DescriptiveURLs.getDescriptiveURLText(data, locale);
      if ((urlText != null) && Util.notEmpty(data.getId())) {
        urlPath = urlPath.concat(urlText + "/");
      }
    }
    // Ajoute la locale si la proprieté descriptive-urls.language.enabled est à true et que l'on est pas sur l'url d'une des pages d'erreur
    if (languagePartEnabled && !error403Id.equals(data.getId()) && !error404Id.equals(data.getId())) {
      urlPath = urlPath.concat((locale != null) ? locale.getLanguage() : channel.getLanguage());
      urlPath = urlPath.concat("/");
    } 
    // Place l'id à la fin du chemin
    if(Util.notEmpty(data.getId())) {
      urlPath = urlPath.concat(data.getId());
    }
    return urlPath;
  }
  
  @Override
  public String getDescriptiveURLText(String descriptiveUrlText, Data data, Locale locale){
	  if (descriptiveUrlText != null) {
		  return descriptiveUrlText;
	  }
	  String language = locale.getLanguage();
	  String name = data.getDataName(language);
	  String msgFmtPattern;
	  Object msgFmtArguments[];

	  if (!(data instanceof Publication))	{
		  return super.getDescriptiveURLText(descriptiveUrlText, data, locale);
	  }


	  Publication pub = (Publication) data;

	  String thematique = Util.notEmpty(SEOUtils.getURLCategory(pub)) ? SEOUtils.getURLCategory(pub).getName() : "";

	  msgFmtPattern = publicationFormat;
	  msgFmtArguments = (new Object[] { language, name, null, null, null, null, thematique});   

	  MessageFormat msgFmt = new MessageFormat(msgFmtPattern, locale);
	  descriptiveUrlText = msgFmt.format(((msgFmtArguments)));
	  
	  if(descriptiveUrlText.startsWith("/")){
		  descriptiveUrlText = descriptiveUrlText.replaceFirst("/", ""); 
	  }

	  return DescriptiveURLs.cleanDescriptiveURLText(descriptiveUrlText, locale);
  }  
 
  /* Get the id from the descriptiveURL. Here the id is to be find at the end of the path after the '/'
   * @see com.jalios.jcms.policy.BasicPortalPolicyFilter#getDescriptiveURLId(java.lang.String, java.lang.String)
   */
  @Override
  public String getDescriptiveURLId(String descriptiveURL, String id) {
    if (Util.notEmpty(id)) {
      return id;
    }
    int lastDotPos = descriptiveURL.lastIndexOf("/");
    if (lastDotPos == -1) {
      return null;
    }
    return descriptiveURL.substring(lastDotPos+1);
  }
  
  
  /* (non-Javadoc)
   * @see com.jalios.jcms.policy.BasicPortalPolicyFilter#initHeaders(java.util.Map, java.lang.String, com.jalios.jcms.context.JcmsJspContext)
   */
  @Override
  public void setupHeaders(Map paramMap, String paramString, JcmsJspContext paramJcmsJspContext) {
	  logger.debug("START SEO - Init Headers");

	  // Ajout des méta-données
	  if (Util.notEmpty(paramJcmsJspContext.getRequest())){
		  String titlePage = SEOExtensionUtils.getTitlePage(channel, "");
		  if(Util.notEmpty(titlePage)){
			  logger.debug("Title Page : "+titlePage);
			  paramJcmsJspContext.setPageTitle(titlePage);
		  }

		  Publication metaPub = (Publication)paramJcmsJspContext.getRequest().getAttribute(PortalManager.PORTAL_PUBLICATION);
		  if (Util.notEmpty(metaPub)) {
			  // Si le contenu est de type Publication
			  String metarobots = SEOExtensionUtils.getPublicationMetaRobot(metaPub);
			  paramJcmsJspContext.addHttpNameHeader("robots", metarobots);
		  }
		  // Si aucune publication n'est affichée, on utilise la catégorie pour renseigner le tag description
		  else {
			  Category currentCategory = (Category) paramJcmsJspContext.getRequest().getAttribute(PortalManager.PORTAL_CURRENTCATEGORY);
			  String metarobots = SEOExtensionUtils.getCategoryMetaRobot(currentCategory);
			  paramJcmsJspContext.addHttpNameHeader("robots", metarobots);
		  }
	  }
	  logger.debug("END SEO - Init Headers");
  }  
  /**
   * Méthode permettant de forcer une catégorie dans un portail.
   * Permet de ne pas quitter la rubrique courante quand un contenu est multi catégorisé par exemple.
   */
  @Override
  public void checkDisplayContext(DisplayContext context) {
    if (context == null) {
      return;
    }

    String categoryString = HttpUtil.getUntrustedStringParameter(channel.getCurrentServletRequest(), CATEGORY_PARAMETER, null);

    if (Util.isEmpty(categoryString)) {
      return;
    }

    Category currentCategory = channel.getCategory(categoryString);

    if (Util.notEmpty(currentCategory)) {
      context.setCurrentCategory(currentCategory);
      return;
    }

    logger.warn(" The category with id : " + categoryString + " has been forced but this category not exists. Referer : "
        + ServletUtil.getUrl(channel.getCurrentJcmsContext().getRequest()));

  }
  
  /** 
   * Test : pour une catégorie donnée, renvoie vers l'accueil de rubrique correspondant, s'il existe.
   * A développer en fonction des besoins. Ceci est une version allégée de l'ancien site.
   * Voir s'il faut reprendre l'ancien developpement.
   **/
  @Override
  public void filterDisplayContext(PortalManager.DisplayContextParameters dcp) {

	  String id = dcp.id;
	  Channel channel = Channel.getChannel();
	  Member loggedMember = dcp.loggedMember;
	  HttpServletResponse response = channel.getCurrentServletResponse();
	  HttpServletRequest request = channel.getCurrentServletRequest();
	  JcmsContext jcmsContext = channel.getCurrentJcmsContext();
	  Locale userLocale= channel.getCurrentUserLocale();
	  String browserUrl = ServletUtil.getUrl(request);
	  String redirectUrl = "";
	  Data data = channel.getData(id);
	  String resourcePath = ServletUtil.getResourcePath(request);
	  HashMap<String,String> canonicalUrls = new HashMap();

	  // Liste noire des contenus à ne pas rediriger vers le portail "full"
	  String[] backlistTab = channel.getStringArrayProperty("plugin.seo.fullPage.content.blacklist", new String[]{});
	  List<String> backlistList = Arrays.asList(backlistTab);

	  if (data !=null && jcmsContext.isInFrontOffice()) {
		  if (data instanceof Category) {
			  Category cat = (Category)data; 
			  Content firstPublication = null;
			  Publication firstPortal = null;

			  Set<Content> contents = cat.getContentSet();

			  // 1) On check si on a une (ou des) redirections
			  TreeSet<PortalRedirect> portalRedirectSet = new TreeSet<PortalRedirect>();
			  portalRedirectSet.addAll(cat.getPublicationSet(PortalRedirect.class));
			  if(Util.notEmpty(portalRedirectSet)) {
				  PortalRedirect portlet = portalRedirectSet.first();
				  redirectUrl = portlet.getRedirectURL(loggedMember);
			  }
			  /*
			  // 2) On check si on a des portails JSP Collection sur la catégorie
			  TreeSet<PortalJspCollection> portletJspCollections = new TreeSet<PortalJspCollection>();
			  portletJspCollections.addAll(cat.getPublicationSet(PortalJspCollection.class));
			  if(Util.isEmpty(redirectUrl) && Util.notEmpty(portletJspCollections)) {
				  PortalJspCollection portlet = portletJspCollections.first();
				  redirectUrl = portlet.getDisplayUrl(userLocale);
			  }
			  */
			  // 3) On check si on a des portails sur la catégorie
			  TreeSet<PortletPortal> portletPortalSet = new TreeSet<PortletPortal>();
			  portletPortalSet.addAll(cat.getPublicationSet(PortletPortal.class));
			  if(Util.isEmpty(redirectUrl) && Util.notEmpty(portletPortalSet)) {
				  PortletPortal portlet = portletPortalSet.first();
				  redirectUrl = portlet.getDisplayUrl(userLocale);
			  }
			  
			  // 4) On check si on a des publications de type "WelcomeSection" sur la catégorie
			  TreeSet<WelcomeSection> welcomeSectionSet = new TreeSet<WelcomeSection>();
			  welcomeSectionSet.addAll(cat.getPublicationSet(WelcomeSection.class));
			  if(Util.isEmpty(redirectUrl) && Util.notEmpty(welcomeSectionSet)) {
				  WelcomeSection pub = welcomeSectionSet.first();
				  firstPublication = pub;
			  }
			  
			  // 5) On check les autres publications
			  if(Util.isEmpty(redirectUrl)) {
				  for(Content itContent : contents) {
					  
		    		if(Util.notEmpty(backlistList) && backlistList.contains(itContent.getClass().getSimpleName())) {
		    			continue;	
		    		}
		    		else {
		    			firstPublication = itContent;
	    				break;
		    		}
					  

				  }
			  }
			  if(Util.notEmpty(firstPortal)) {
				  redirectUrl = firstPortal.getDisplayUrl(userLocale);
			  }
			  if(Util.notEmpty(firstPublication)) {
				  redirectUrl = firstPublication.getDisplayUrl(userLocale);

				  /* On force un portal full pour l'instant sinon les anciens portails prennent
				   * le dessus et buggent !
				   */
				  redirectUrl+="?portal="+fullPortal;
				  redirectUrl+="&category=" + cat.getId();

			  }

			  if (request.getMethod() != "POST") {
				  response.setStatus(301);
				  response.setHeader("Location", ServletUtil.getContextPath(request)+"/"+redirectUrl);
			  }
		  }




			  /*
		    if (!ToolsUtil.inArray(categoryException, currentCategory.getId())) {
		      if (dcp.overrided == null && autorizedForFullDisplayPortal(dcp.id)) {
		        String portalId = channel.getProperty("plugin.tools.fullDisplayPortal");
		        PortalInterface portalStandard = (PortalInterface) channel.getPublication(portalId);
		        if (portalStandard != null) {
		          dcp.overrided = portalStandard;
		          LOGGER.info("applique le portail '" + portalStandard + "' (" + portalId + ")");
		        }
		      }
		    }
			   */

		  }
  }
}
