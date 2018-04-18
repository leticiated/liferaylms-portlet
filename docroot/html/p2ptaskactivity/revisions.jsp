<%@page import="com.liferay.lms.service.LearningActivityLocalServiceUtil"%>
<%@page import="com.liferay.portal.model.Team"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.service.TeamLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.CustomSQLParam"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.lms.service.P2pActivityCorrectionsLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.workflow.WorkflowConstants"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.lms.service.P2pActivityLocalServiceUtil"%>
<%@page import="com.liferay.lms.model.P2pActivity"%>
<%@page import="com.liferay.portal.service.UserGroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.service.persistence.GroupUtil"%>
<%@page import="com.liferay.portal.service.GroupServiceUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.DynamicQuery"%>
<%@page import="com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="com.liferay.lms.service.InappropiateLocalServiceUtil"%>
<%@page import="com.liferay.lms.model.Inappropiate"%>
<%@include file="/init.jsp" %>

<%
	long actId=ParamUtil.getLong(request,"actId",0);
	String criteria = ParamUtil.getString(request,"criteria","");
	int inapropValue = ParamUtil.getInteger(request, "inapropValue",0);
	int state = GetterUtil.getInteger(ParamUtil.getString(request,"state","0"),0);
	if (inapropValue > 0 || state > 0){
		criteria = "";
	}
	PortletURL portletURL = renderResponse.createRenderURL();
	portletURL.setParameter("jspPage","/html/p2ptaskactivity/revisions.jsp");
	portletURL.setParameter("criteria", criteria);
	portletURL.setParameter("inapropValue", String.valueOf(inapropValue));
	portletURL.setParameter("state",""+state);
	portletURL.setParameter("delta", "10");
	
	boolean enableFlag=false;
	String enableFlagString = LearningActivityLocalServiceUtil.getExtraContentValue(actId,"inappropiateFlag");
	if(enableFlagString.equals("true")){
		enableFlag = true;
	}
%>

<div class="student_search"> 

	<portlet:renderURL var="buscarURL">
		<portlet:param name="jspPage" value="/html/p2ptaskactivity/revisions.jsp"></portlet:param>
	</portlet:renderURL>

	<aui:form name="studentsearch" action="<%=buscarURL %>" cssClass="search_lms" method="post">
		<aui:fieldset>
			<aui:column>
				<aui:input label="studentsearch.criteria" name="criteria" size="20" value="<%=criteria %>" />	
			</aui:column>
			<aui:column>
				<aui:select name="state">
				    <aui:option value="0"><liferay-ui:message key="p2ptask-all" /></aui:option>
				    <aui:option value="1"><liferay-ui:message key="p2ptask-incompletas" /></aui:option>
				    <aui:option value="2"><liferay-ui:message key="p2ptask-realizadas" /></aui:option>
				    <aui:option value="3"><liferay-ui:message key="p2ptask-no-realizadas" /></aui:option>
				</aui:select>
			</aui:column>
			<%if (enableFlag){ %>
			<aui:column>
				<aui:select label="inappropiate.label" name="inapropValue">
					<aui:option label="inappropiate.all" value="0"/>
					<aui:option label="inappropiate.yes" value="1"/>
					<aui:option label="inappropiate.no" value="2"/>
				</aui:select>
			</aui:column>
			<%}%>
			<aui:column cssClass="search_lms_button">
				<aui:button-row>
					<aui:button name="searchUsers" value="search" type="submit" />
				</aui:button-row>
			</aui:column>	
		</aui:fieldset>
	</aui:form>

	<%if (!enableFlag || (inapropValue == 0 && state == 0)){%>
		<%@include file="/html/p2ptaskactivity/sinFiltro.jsp" %>
	<%}else if (inapropValue>0 || state > 0){ %>
		<%@include file="/html/p2ptaskactivity/conFiltro.jsp" %>
	<%} %>
	
<portlet:renderURL var="back">
	<portlet:param name="jspPage" value="/html/p2ptaskactivity/view.jsp" />
	<portlet:param name="actId" value="<%=String.valueOf(actId) %>" />
</portlet:renderURL>
<%
String urlback = "self.location = '"+back+"';";
%>
<aui:button-row>
	<aui:button cssClass="floatl" value="back" type="button" onClick="<%=urlback %>" style="margin-top:10px" />
</aui:button-row>
</div>