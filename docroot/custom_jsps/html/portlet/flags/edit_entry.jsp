<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/html/portlet/init.jsp" %>
<%@page import="com.liferay.lms.model.P2pActivity"%>
<%@page import="com.liferay.lms.model.P2pActivityCorrections"%>

<%
String className = ParamUtil.getString(request, "className");
long classPK = ParamUtil.getLong(request, "classPK");
String contentTitle = ParamUtil.getString(request, "contentTitle");
String contentURL = ParamUtil.getString(request, "contentURL");
long reportedUserId = ParamUtil.getLong(request, "reportedUserId");
//long p2pActivityId= ParamUtil.getLong(request, "p2pActivityId");
String reporterEmailAddress = themeDisplay.getUser().getEmailAddress();
%>

<style type="text/css">
	.portlet-flags .aui-form fieldset {
		border: none;
		padding: 0;
		width: 100%;
	}

	.portlet-flags .aui-form .aui-fieldset label {
		font-weight: bold;
	}
</style>

<div class="portlet-flags" id="<portlet:namespace />flagsPopup">
	<aui:form method="post" name="flagsForm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "flag();" %>'>
		<p><%= LanguageUtil.format(pageContext, "you-are-about-to-report","", true) %></p>
		<aui:fieldset>			
			<span  id="<portlet:namespace />otherReasonContainer">
				<aui:input type="textarea" rows="5" cols="30" label="reason-for-the-report" name="otherReason" />
			</span>
			

			<c:if test="<%= themeDisplay.isSignedIn() %>">
				<aui:input type="hidden" label="email-address" name="reporterEmailAddress" value="<%=reporterEmailAddress %>"/>
			</c:if>
		</aui:fieldset>

		<aui:button-row>
			<aui:button name="flagsSubmit" type="submit" />
		</aui:button-row>
	</aui:form>
</div>

<div class="aui-helper-hidden" id="<portlet:namespace />confirmation">
	<p><strong><liferay-ui:message key="thank-you-for-your-report" /></strong></p>

	<p><%= LanguageUtil.format(pageContext, "although-we-cannot-disclose-our-final-decision,-we-do-review-every-report-and-appreciate-your-effort-to-make-sure-x-is-a-safe-environment-for-everyone", company.getName()) %></p>
</div>

<div class="aui-helper-hidden" id="<portlet:namespace />error">
	<p><strong><liferay-ui:message key="an-error-occurred-while-sending-the-report.-please-try-again-in-a-few-minutes" /></strong></p>
</div>

<aui:script use="aui-dialog">
	function <portlet:namespace />flag() {
		var reasonNode = A.one('#<portlet:namespace />otherReason');
		var reason = reasonNode.val();
		if (reason == 'other' || reason == '') {		
			alert(Liferay.Language.get('bookmarks.activity.empty-desc'));
			return false;
		}

		var reporterEmailAddressNode = A.one('#<portlet:namespace />reporterEmailAddress');
		var reporterEmailAddress = (reporterEmailAddressNode && reporterEmailAddressNode.val()) || '';

		var flagsPopupNode = A.one('#<portlet:namespace />flagsPopup');
		var errorMessageNode = A.one('#<portlet:namespace />error');
		var confirmationMessageNode = A.one('#<portlet:namespace />confirmation');

		var errorMessage = (errorMessageNode && errorMessageNode.html()) || '';
		var confirmationMessage = (confirmationMessageNode && confirmationMessageNode.html()) || '';

		var setDialogContent = function(message) {
			var dialog = A.DialogManager.findByChild(flagsPopupNode);

			dialog.setStdModContent('body', message);
		};

		A.io.request(
			'<liferay-portlet:actionURL portletName="<%= PortletKeys.FLAGS %>" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>"><portlet:param name="struts_action" value="/flags/edit_entry" /></liferay-portlet:actionURL>',
			{
				data: {
					className: '<%= HtmlUtil.escape(className) %>',
					classPK: '<%= classPK %>',
					contentTitle: '<%= HtmlUtil.escape(contentTitle) %>',
					contentURL: '<%= HtmlUtil.escape(contentURL) %>',
					reason: reason,
					reportedUserId: '<%= reportedUserId %>',
					reporterEmailAddress: reporterEmailAddress					
				},
				on: {
					failure: function() {
						setDialogContent(errorMessage);
					},
					success: function() {
						setDialogContent(confirmationMessage);
						var flag = A.one('#p2pflag-container<%=classPK%>');   												
 						flag.hide();
  						
					}
				}
			}
		);
	}

	Liferay.Util.toggleSelectBox('<portlet:namespace />reason', 'other', '<portlet:namespace />otherReasonContainer');

	A.one('#<portlet:namespace />flagsSubmit').on(
		'click',
		function(event) {
			<portlet:namespace />flag();

			event.halt();
		}
	);
</aui:script>