package fr.cg44.plugin.seo.datacontroller;

import com.jalios.jcms.BasicDataController;
import com.jalios.jcms.ControllerStatus;
import com.jalios.jcms.Data;
import com.jalios.jcms.Publication;
import com.jalios.jcms.plugin.PluginComponent;
import com.jalios.util.Util;

public class TitreSeoPublicationController extends BasicDataController implements PluginComponent {

   public ControllerStatus checkIntegrity(Data data) {
     Publication pub = (Publication)data ;
     if (Util.notEmpty(pub.getExtraData("extradb.Content.jcmsplugin.seo.titreseo.fr")) && pub.getExtraData("extradb.Content.jcmsplugin.seo.titreseo.fr").length() > 60) {
         return new ControllerStatus("Le champ Titre Seo FR doit contenir moins de 60 caractères.");
     }
     if (Util.notEmpty(pub.getExtraData("extradb.Content.jcmsplugin.seo.titreseo.en")) && pub.getExtraData("extradb.Content.jcmsplugin.seo.titreseo.en").length() > 60) {
         return new ControllerStatus("Le champ Titre Seo EN doit contenir moins de 60 caractères.");
     }
     return ControllerStatus.OK ;
   }
} 