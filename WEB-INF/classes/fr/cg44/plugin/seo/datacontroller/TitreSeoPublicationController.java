package fr.cg44.plugin.seo.datacontroller;

import com.jalios.jcms.BasicDataController;
import com.jalios.jcms.Channel;
import com.jalios.jcms.ControllerStatus;
import com.jalios.jcms.Data;
import com.jalios.jcms.Publication;
import com.jalios.jcms.db.DBData;
import com.jalios.jcms.plugin.PluginComponent;
import com.jalios.util.Util;

public class TitreSeoPublicationController extends BasicDataController implements PluginComponent {
    private static final Channel channel = Channel.getChannel();
    String titreSeoProp = ".Content.jcmsplugin.seo.titreseo.";
    String descriptionSeoProp = ".Content.jcmsplugin.seo.descriptionseo.";
    String prefixExtra = "extra";
    String prefixExtraDb = "extradb";
    String suffixFr = "fr";
    String suffixEn = "en";
    private static final int TAILLE_MAX_TITRE = channel.getIntegerProperty("jcmsplugin.seo.titreseo.tailleMax", 70);
    private static final int TAILLE_MAX_DESCRIPTION = channel.getIntegerProperty("jcmsplugin.seo.descriptionseo.tailleMax",170);

   public ControllerStatus checkIntegrity(Data data) {
     Publication pub = (Publication)data ;
     
     if (pub.isInDatabase() || pub instanceof DBData) {
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixFr)) && pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixFr).length() > TAILLE_MAX_TITRE) {
             return new ControllerStatus("Le champ Titre Seo FR doit contenir moins de " + TAILLE_MAX_TITRE + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixEn)) && pub.getExtraDBData(prefixExtraDb + titreSeoProp + suffixEn).length() > TAILLE_MAX_TITRE) {
             return new ControllerStatus("Le champ Titre Seo EN doit contenir moins de " + TAILLE_MAX_TITRE + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + descriptionSeoProp + suffixFr)) && pub.getExtraDBData(prefixExtraDb + descriptionSeoProp + suffixFr).length() > TAILLE_MAX_DESCRIPTION) {
           return new ControllerStatus("Le champ Description Seo FR doit contenir moins de " + TAILLE_MAX_DESCRIPTION + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraDBData(prefixExtraDb + descriptionSeoProp + suffixEn)) && pub.getExtraDBData(prefixExtraDb + descriptionSeoProp + suffixEn).length() > TAILLE_MAX_DESCRIPTION) {
           return new ControllerStatus("Le champ Description Seo EN doit contenir moins de " + TAILLE_MAX_DESCRIPTION + " caractères.");
         }         
     } else {
         if (Util.notEmpty(pub.getExtraData(prefixExtra + titreSeoProp + suffixFr)) && pub.getExtraData(prefixExtra + titreSeoProp + suffixFr).length() > TAILLE_MAX_TITRE) {
             return new ControllerStatus("Le champ Titre Seo FR doit contenir moins de " + TAILLE_MAX_TITRE + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraData(prefixExtra + titreSeoProp + suffixEn)) && pub.getExtraData(prefixExtra + titreSeoProp + suffixEn).length() > TAILLE_MAX_TITRE) {
             return new ControllerStatus("Le champ Titre Seo EN doit contenir moins de " + TAILLE_MAX_TITRE + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraData(prefixExtra + descriptionSeoProp + suffixFr)) && pub.getExtraData(prefixExtra + descriptionSeoProp + suffixFr).length() > TAILLE_MAX_DESCRIPTION) {
           return new ControllerStatus("Le champ Description Seo FR doit contenir moins de " + TAILLE_MAX_DESCRIPTION + " caractères.");
         }
         if (Util.notEmpty(pub.getExtraData(prefixExtra + descriptionSeoProp + suffixEn)) && pub.getExtraData(prefixExtra + descriptionSeoProp + suffixEn).length() > TAILLE_MAX_DESCRIPTION) {
           return new ControllerStatus("Le champ Description Seo EN doit contenir moins de " + TAILLE_MAX_DESCRIPTION + " caractères.");
         }
     }
     
     
     return ControllerStatus.OK ;
   }
} 