package fr.cg44.plugin.seo.datacontroller;

import com.jalios.jcms.BasicDataController;
import com.jalios.jcms.ControllerStatus;
import com.jalios.jcms.Data;
import com.jalios.jcms.Publication;
import com.jalios.jcms.db.DBData;
import com.jalios.jcms.plugin.PluginComponent;
import com.jalios.util.Util;

public class TitreSeoPublicationController extends BasicDataController implements PluginComponent {
    
    String titreSeoProp = ".Content.jcmsplugin.seo.titreseo.";
    String prefixExtra = "extra";
    String prefixExtraDb = "extradb";
    String suffixFr = "fr";
    String suffixEn = "en";

   public ControllerStatus checkIntegrity(Data data) {
     Publication pub = (Publication)data ;
     
     if (pub.isInDatabase() || pub instanceof DBData) {
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixFr)) && pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixFr).length() > 60) {
             return new ControllerStatus("Le champ Titre Seo FR doit contenir moins de 60 caractères.");
         }
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixEn)) && pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixEn).length() > 60) {
             return new ControllerStatus("Le champ Titre Seo EN doit contenir moins de 60 caractères.");
         }
     } else {
         if (Util.notEmpty(pub.getExtraData(prefixExtra + titreSeoProp + suffixFr)) && pub.getExtraData(prefixExtra + titreSeoProp + suffixFr).length() > 60) {
             return new ControllerStatus("Le champ Titre Seo FR doit contenir moins de 60 caractères.");
         }
         if (Util.notEmpty(pub.getExtraData(prefixExtra + titreSeoProp + suffixEn)) && pub.getExtraData(prefixExtra + titreSeoProp + suffixEn).length() > 60) {
             return new ControllerStatus("Le champ Titre Seo EN doit contenir moins de 60 caractères.");
         }
     }
     
     
     return ControllerStatus.OK ;
   }
} 