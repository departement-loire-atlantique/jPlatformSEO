package fr.cg44.plugin.seo.servletfilter;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jalios.jcms.Channel;
import com.jalios.jcms.Data;
import com.jalios.jcms.DescriptiveURLs;
import com.jalios.jcms.FriendlyURLManager;
import com.jalios.jcms.context.JcmsContext;
import com.jalios.jcms.portlet.PortalRedirect;
import com.jalios.util.ServletUtil;
import com.jalios.util.Util;

import fr.cg44.plugin.seo.SEOUtils;
import generated.PortletPortalRedirect;

public class ErrorRedirectFilter implements Filter {
 private static final Logger logger = Logger.getLogger(SEOUtils.class);
 private static final String JSP_404_PROP = "plugin.seo.error.404";
 private static final String JSP_403_PROP = "plugin.seo.error.403";
 private static Channel channel;
 private FilterConfig config;
 
 public ErrorRedirectFilter() {
 }

 @Override
 public void init(FilterConfig paramFilterConfig) {
 	channel = Channel.getChannel();
 	config = paramFilterConfig;
}
 
 @Override
 public void destroy() {
 	channel = null;
 }

 @Override
 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
         throws IOException, ServletException {
 				
     HttpServletRequest req = (HttpServletRequest) request;
     HttpServletResponse res = (HttpServletResponse) response;
     
     String pathInfo = ServletUtil.getPathInfo(req);
     String requestURL = req.getRequestURI();
     boolean isRoot = Util.isEmpty(pathInfo) || "/".equals(pathInfo);
     String currentId = DescriptiveURLs.getId(req);
     Data currentData = Channel.getChannel().getData(currentId);
     
     // Pas de contenu correspondant à cet ID : 404
     if (Util.isEmpty(currentData) && !isRoot) {
      	res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      	res.sendRedirect(ServletUtil.getBaseUrl(req) + Channel.getChannel().getProperty(JSP_404_PROP));
     }
     // Détection de l'URL "forbidden"
     else if(Util.notEmpty(requestURL) && requestURL.indexOf("/front/forbidden.jsp") > -1) {
     	res.setStatus(HttpServletResponse.SC_FORBIDDEN);
     	res.sendRedirect(ServletUtil.getBaseUrl(req) + Channel.getChannel().getProperty(JSP_403_PROP));
      chain.doFilter(req, res);
     }
     else {
     	chain.doFilter(req, res);
     }


 }

}
