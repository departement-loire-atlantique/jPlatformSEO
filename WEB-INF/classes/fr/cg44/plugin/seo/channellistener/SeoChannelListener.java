package fr.cg44.plugin.seo.channellistener;

import java.util.List;

import com.jalios.jcms.Channel;
import com.jalios.jcms.ChannelListener;
import com.jalios.jcms.JcmsUtil;
import com.jalios.jcms.plugin.PluginComponent;
import com.jalios.util.JProperties;
import com.jalios.util.JPropertiesListener;
import com.jalios.util.LangProperties;
import com.jalios.util.Util;


/**
 * 
 * @author mformont
 *
 * Initialise la propriété pour les types de contenus ayant une catégorie de navigation principale
 *
 */
public class SeoChannelListener extends ChannelListener implements JPropertiesListener, PluginComponent {

  @Override
  public void handleFinalize() {
  }

  @Override
  public void initAfterStoreLoad() throws Exception {
    propertiesChange(null);    
  }

  @Override
  public void initBeforeStoreLoad() throws Exception {   
  }

  @Override
  public void propertiesChange(JProperties arg0) { 
    
    Channel channel = Channel.getChannel();
    LangProperties langproperties = channel.getProperties("extra.");
    
    // Retire l'extradata de navigation principale de tous les contenus
    for(String itProp : langproperties.keySet()) {
      if (itProp != null && itProp.indexOf(".jcmsplugin.seo.principal.cat") > -1){
        channel.getChannelProperties().remove(itProp);
      }
    }
    
    // Ajoute l'extradata sur les contenus indiqués (plugin.prop et fr/en.prop)
    String[] types = channel.getStringArrayProperty("jcmsplugin.seo.principal.cat.types", new String[] {});
    List<String> languages = channel.getLanguageList();
    
    if(Util.notEmpty(types)) {
      for(String itType : types) {
        // Ajoute l'extra data sur le type de contenu
        channel.setProperty("extra." + itType + ".jcmsplugin.seo.principal.cat.chooser-category", "");      
        // Ajouter la propriété sur toutes les langues du site
        for (String itLang : languages) {
          String itLabel = JcmsUtil.glp(itLang, "jcmsplugin.seo.principal.cat", new Object[0]);
          channel.setProperty(itLang + ".extra." + itType + ".jcmsplugin.seo.principal.cat", itLabel);
          
          String itLabelHelp = JcmsUtil.glp(itLang, "jcmsplugin.seo.principal.cat.aide", new Object[0]);         
          channel.setProperty(itLang + ".extra." + itType + ".jcmsplugin.seo.principal.cat.help", itLabelHelp);
        }       
      }
    }
    
    
  }

}
