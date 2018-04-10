<liferay-ui:search-container iteratorURL="<%=portletURL%>" emptyResultsMessage="there-are-no-results" delta="5">

	   	<liferay-ui:search-container-results>
			<%
				String middleName = null;
		
				LinkedHashMap<String,Object> params = new LinkedHashMap<String,Object>();
				params.put("usersGroups", new Long(themeDisplay.getScopeGroupId()));
				
				//Si el tutor pertenece a uno o más equipos, sólo se muestran los usuarios de los equipos
				
				List<Team> userTeams = null;
				try {
					userTeams=TeamLocalServiceUtil.getUserTeams(themeDisplay.getUserId(), themeDisplay.getScopeGroupId());
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				if(userTeams != null && userTeams.size() > 0){
					String teamIds = "";
					for(int i = 0; i < userTeams.size();i++){
						teamIds += userTeams.get(i).getTeamId() + ",";
					}
					if(teamIds.length() > 0){
						teamIds = teamIds.substring(0, teamIds.length()-1);
					}
					
					params.put("userTeamIds", new CustomSQLParam("INNER JOIN users_teams ON user_.userId = users_teams.userId "
							+ "WHERE users_teams.teamId IN (" + teamIds + ")", null));
				}
				
				OrderByComparator obc = null;
				
				List<User> userListPage = UserLocalServiceUtil.search(themeDisplay.getCompanyId(), criteria, WorkflowConstants.STATUS_APPROVED, params, searchContainer.getStart(), searchContainer.getEnd(), obc);
				int userCount = UserLocalServiceUtil.searchCount(themeDisplay.getCompanyId(), criteria, WorkflowConstants.STATUS_APPROVED, params);
				System.out.println(userListPage.size()+" aaa "+userCount);		
				pageContext.setAttribute("results", userListPage);
			    pageContext.setAttribute("total", userCount);
			    pageContext.setAttribute("delta", 10);
			%>
		</liferay-ui:search-container-results>
		
		<liferay-ui:search-container-row className="com.liferay.portal.model.User" keyProperty="userId" modelVar="user">
		
		<portlet:renderURL var="verDetalle">
			<portlet:param name="jspPage" value="/html/p2ptaskactivity/detalleAct.jsp" />
			<portlet:param name="actId" value="<%=String.valueOf(actId) %>" />
			<portlet:param name="userId" value="<%=String.valueOf(user.getUserId()) %>" />
		</portlet:renderURL>
		
		<%
		String nameTit = LanguageUtil.get(pageContext, "name");
		%>
		
		<liferay-ui:search-container-column-text value="<%=user.getFullName()%>" name="<%=nameTit %>"  />
		<%
		boolean existP2p = P2pActivityLocalServiceUtil.existP2pAct(Long.valueOf(actId), Long.valueOf(user.getUserId()));
		boolean correctionCompleted = P2pActivityCorrectionsLocalServiceUtil.areAllCorrectionsDoneByUserInP2PActivity(actId, user.getUserId());
		
		String textTaks = "";
		
		String textTaksTit = LanguageUtil.get(pageContext, "state");
		
		//Si se ha entregado la tarea
		if(existP2p && !correctionCompleted ){
			textTaks = LanguageUtil.get(pageContext, "p2ptask-incompleta");
		} else if(existP2p && correctionCompleted){
			textTaks = LanguageUtil.get(pageContext, "p2ptask-superada");
		}else{
			textTaks = LanguageUtil.get(pageContext, "p2ptask-nosuperada");
		}
		%>
		<liferay-ui:search-container-column-text value="<%=textTaks %>" name="<%=textTaksTit %>" />
		<%
		P2pActivity myP2PActivity = P2pActivityLocalServiceUtil.findByActIdAndUserId(actId, Long.valueOf(user.getUserId()));

		String dateTit = LanguageUtil.get(pageContext, "date");
		
		Date dateDel;
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dFormat.setTimeZone(themeDisplay.getTimeZone());	
		String dateDelS = "";
		if (enableFlag){
			boolean isInappropiate = false; 
			List<Inappropiate> listInappropiates = null;
			if(myP2PActivity != null){
				dateDel = myP2PActivity.getDate();
				dateDelS = dFormat.format(dateDel);
				try{
					listInappropiates = InappropiateLocalServiceUtil.getInnapropiatesByClassPk(myP2PActivity.getActId());
					if (listInappropiates != null && listInappropiates.size()>0){
						isInappropiate = true;
					}
				}catch(Exception e){
					
				}
			}
			if (isInappropiate){
				%>
				
					<liferay-ui:search-container-column-text name="inappropiate.label" >
						<a href="javascript:<portlet:namespace />openPopUp(<%=actId%>);"><liferay-ui:message key="inappropiate.yes" /></a>
					</liferay-ui:search-container-column-text>
					
					<!-- Start PopUp Show Inappropiate -->
	
					<div id="<portlet:namespace />inappropiate" style="display:none">
						<h1><liferay-ui:message key="inappropiate.popup.title" /></h1>
						<br />
						<div class="tableDetail_"+<%=actId %>>
							<table>
								<thead>
									<th><liferay-ui:message key="inappropiate.popup.name" /></th>
									<th><liferay-ui:message key="inappropiate.popup.reason" /></th>
									<th><liferay-ui:message key="inappropiate.popup.date" /></th>
								</thead>
								<tbody>
									<%
									for (Inappropiate in: listInappropiates){
										%>
										<tr>
											<td><%=in.getUserName() %></td>
											<td><%=in.getReason() %></td>
											<td><%=dFormat.format(in.getCreateDate()) %></td>
										</tr>
										<%
									}
									%>
								</tbody>
							</table>
						</div>
						<div class="buttons">
							<input type="button" class="button simplemodal-close" onclick="<portlet:namespace />closeShowInappropiate()" value="<liferay-ui:message key="cancel" />" />
						</div>
					</div>
					<!-- End PopUp Show Inappropiate -->
				<%
			}else{
				%>
					<liferay-ui:search-container-column-text name="inappropiate.label" >
						<liferay-ui:message key="inappropiate.no" />
					</liferay-ui:search-container-column-text>
				<%
			}
		}	
		%>
		<liferay-ui:search-container-column-text value="<%=dateDelS %>" name="<%=dateTit %>" />
		<%
		if(existP2p){
			String urlResume = "self.location = '"+verDetalle+"';";
			String nameButton =LanguageUtil.get(pageContext, "p2ptask-see-task"); 
		%>
			<liferay-ui:search-container-column-button href="<%=urlResume %>"  name="<%=nameButton %>" />
		<%} else{%>
			<liferay-ui:search-container-column-text value="" />
		<%} %>
		</liferay-ui:search-container-row>
	 	<liferay-ui:search-iterator />
	</liferay-ui:search-container>
	
	
	<aui:script>
	
	Liferay.provide(
	        window,
	        '<portlet:namespace />openPopUp',
	        function(numero) {
				var A = AUI();

				//Start opening popUp			
				if(A.one('#<portlet:namespace />inappropiate')) {
					window.<portlet:namespace />inappropiateTitle = A.one('#<portlet:namespace />inappropiate h1').html();
					var body = '#<portlet:namespace />inappropiate .tableDetail_'+numero;
					window.<portlet:namespace />inappropiateBody = A.one(body).html();
					A.one('#<portlet:namespace />inappropiate').remove();
				}
				
				window.<portlet:namespace />inappropiate = new A.Dialog({
					id:'<portlet:namespace />showpinappropiate',
					title: window.<portlet:namespace />inappropiateTitle,
		            bodyContent: window.<portlet:namespace />inappropiateBody,
		            centered: true,
		            modal: true,
		            width: "auto",
		            height: "auto"
		        }).render();
				
				window.<portlet:namespace />inappropiate.show();
	        },
	        ['aui-dialog']
	    );
		
	Liferay.provide(
	        window,
	        '<portlet:namespace />closeShowInappropiate',
	        function() {
				var A = AUI();
				
				A.DialogManager.closeByChild('#<portlet:namespace />showpinappropiate');
	        },
	        ['aui-dialog']
	    );

</aui:script>