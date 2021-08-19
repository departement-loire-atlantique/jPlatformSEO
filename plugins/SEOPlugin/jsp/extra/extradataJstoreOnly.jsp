<%@ include file='/jcore/doInitPage.jspf' %>
<%@ page import="com.jalios.jcms.handler.EditDataHandler" %>
<%
  String itShortKey   = Util.getString(request.getAttribute("itShortKey"),"");
  String itValue      = Util.getString(request.getAttribute("itValue"),"");
  String itLabel      = Util.getString(request.getAttribute("itLabel"),"");
  String itDesc       = Util.getString(request.getAttribute("itDesc"),"");
  String itIcon        = Util.getString(request.getAttribute("itIcon"),"");
  
  EditDataHandler handler  = (EditDataHandler) request.getAttribute("formHandler");
  if (handler == null || !handler.isFieldEdition("")){
    return;
  }
  
  Class classBeingProcessed = (Class) request.getAttribute("classBeingProcessed");
  if (classBeingProcessed == null) {
    classBeingProcessed = handler.getDataClass() ;
  }  
  
  if (classBeingProcessed == null) {
    return;
  }
  
  boolean isDBDataClass = DBData.class.isAssignableFrom(classBeingProcessed);
  if (isDBDataClass) return;
%>

 

<jalios:field name="extraValues" label='<%= itLabel %>' value='<%= itValue %>' description='<%= itDesc %>'>
      <input type="hidden" name="extraKeys" value="<%= itShortKey %>" />
      <jalios:control settings='<%= new TextAreaSettings().rows(1) %>' />
</jalios:field>

