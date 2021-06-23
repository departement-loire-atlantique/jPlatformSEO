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
     if (Util.notEmpty(pub.getExtraData("extra.Content.jcmsplugin.seo.titreseo")) && pub.getExtraData("extra.Content.jcmsplugin.seo.titreseo").length() > 60) {
       return new ControllerStatus("Le résumé doit contenir au moins 300 caractères");
     }
      return ControllerStatus.OK ;
   }
} 