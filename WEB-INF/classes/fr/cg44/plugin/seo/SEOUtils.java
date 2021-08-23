package fr.cg44.plugin.seo;

import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.jalios.jcms.Category;
import com.jalios.jcms.Channel;
import com.jalios.jcms.Content;
import com.jalios.jcms.JcmsUtil;
import com.jalios.jcms.Publication;
import com.jalios.util.HtmlUtil;
import com.jalios.util.Util;

/**
 * @author 890730
 *
 */
public class SEOUtils {
  
  private static final Logger logger = Logger.getLogger(SEOUtils.class);
  private static final Channel channel = Channel.getChannel();
  

  public static String clean(String htmlString,int truncateLength){
    String noHtml = htmlString.replaceAll("<[^>]*>", "");
    noHtml = HtmlUtil.truncate(noHtml,truncateLength, "");
    return noHtml;
  }

	/** DEP44 : récupère la catégorie à afficher dans l'URL
	 * Récupère la première catégorie trouvée, en remontant parmi les ancetres de la publication
	 * et en parcourant les racines des URLs descriptives.
	 * @param la publication
	 * @return la catégorie à afficher dans l'URL
	 */

	public static Category getURLCategory(Publication paramPublication) {
		List<Category> rootCatList;
		Category catThematique = null;
		
		if(Util.isEmpty(paramPublication)) {
		  return null;
		}
		
		/* Récupération des racines de catégories des URLs descriptives */
		rootCatList = JcmsUtil.stringToDataList(channel.getProperty("descriptive-urls.text.root-cats"), ",", Category.class);
	    if (rootCatList.isEmpty()) {
	      rootCatList.add(channel.getRootCategory());
	    }
	    
	    /* Récupération des thématiques à partir des racines (catégories enfants des racines) */
	    TreeSet<Category> thematiquesCategorySet = new TreeSet<Category>();
	    for (Category itRootCategory : rootCatList) {
	    	thematiquesCategorySet.addAll(itRootCategory.getChildrenSet());
		}
	    
		/* Récupération des catégories de la publication */
		TreeSet<Category> pubCategorySet = paramPublication.getCategorySet();
		if (pubCategorySet == null) {
			return null;
		}
		
		if (!channel.isAvailable()) {
			pubCategorySet = new TreeSet<Category>(pubCategorySet);
		}
		
		
		for (Category itPubCategory : pubCategorySet) {
			for (Category itRootCategory : thematiquesCategorySet) {
				if ((itPubCategory == itRootCategory) || 
						(itPubCategory.hasAncestor(itRootCategory)))
				{
					catThematique = itRootCategory;
					break;
				}
			}
			if (catThematique != null) {
				break;
			}
		}
		
		// Si une catégorie prioritaire est définie pour un Content (en store)		
		if(!paramPublication.isDBData() && paramPublication instanceof Content) {		  
		  Category currentCatPrioritaire = channel.getCategory(paramPublication.getExtraData("extra." + paramPublication.getClass().getSimpleName()  + ".jcmsplugin.seo.principal.cat")); 
		  if(currentCatPrioritaire != null) {     
		    for (Category itRootCategory : thematiquesCategorySet) { 
		      if ((currentCatPrioritaire == itRootCategory) ||  
		          (currentCatPrioritaire.hasAncestor(itRootCategory))) 
		      { 
		        catThematique = itRootCategory;
		        break; 
		      } 
		    }      
		  } 
		}
		
		return catThematique;
	}
	
	
	/*
	 * Regarde si une publication possède telle catégorie comme ancètre
	 * */
	public static boolean hasAncestor(Publication pub, Category ancestorCat) {
		for (Category itPubCategory : pub.getCategorySet()) {
			if(itPubCategory.hasAncestor(ancestorCat) || itPubCategory.equals(ancestorCat)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasAncestor(Category cat, Category ancestorCat) {
		if(cat.hasAncestor(ancestorCat) || cat.equals(ancestorCat)) {
			return true;
		}
		return false;
	}
	
	

}
