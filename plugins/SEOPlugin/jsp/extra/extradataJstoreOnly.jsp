<%@ include file='/jcore/doInitPage.jspf' %>
<%
  String itShortKey   = Util.getString(request.getAttribute("itShortKey"),"");
  String itValue      = Util.getString(request.getAttribute("itValue"),"");
  String itLabel      = Util.getString(request.getAttribute("itLabel"),"");
  String itDesc       = Util.getString(request.getAttribute("itDesc"),"");
  String itIcon        = Util.getString(request.getAttribute("itIcon"),"");
  
  Publication itPub = channel.getPublication(request.getParameter("id"));
  if (itPub.isInDatabase()) return;
%>

 

<jalios:field name="extraValues" label='<%= itLabel %>' value='<%= itValue %>' description='<%= itDesc %>'>
      <input type="hidden" name="extraKeys" value="<%= itShortKey %>" />
      <jalios:control settings='<%= new TextFieldSettings().maxLength(60) %>' />
</jalios:field>