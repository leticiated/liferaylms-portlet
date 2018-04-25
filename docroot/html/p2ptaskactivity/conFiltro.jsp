
<%@page import="com.liferay.util.P2pTaskActivitySearchUtil"%>
<liferay-ui:search-container iteratorURL="<%=portletURL%>" emptyResultsMessage="there-are-no-results" delta="5">

	   	<liferay-ui:search-container-results>
			<%
			String middleName = null;
			LinkedHashMap<String,Object> params = new LinkedHashMap<String,Object>();
			params.put("usersGroups", new Long(themeDisplay.getScopeGroupId()));
			//List<User> userListPage = P2pTaskActivitySearchUtil.getUserByFilter(params,actId, searchContainer.getStart(), searchContainer.getEnd(), inapropValue);
			List<User> userListPage = P2pTaskActivitySearchUtil.getUserByFilter(criteria, params, actId, themeDisplay.getUserId(), searchContainer.getStart(), searchContainer.getEnd(), inapropValue, state);
			//int countUser = P2pTaskActivitySearchUtil.countUserByFilter(criteria, params, actId, themeDisplay.getUserId(), searchContainer.getStart(), searchContainer.getEnd(), inapropValue, state);	
			pageContext.setAttribute("results", userListPage);
		    pageContext.setAttribute("total", userListPage.size());
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
		boolean isInappropiate = false; 
		List<Inappropiate> listInappropiates = null;
		List<P2pActivityCorrections> listCorrections = null;
		if(myP2PActivity != null){
			dateDel = myP2PActivity.getDate();
			dateDelS = dFormat.format(dateDel);
			try{
				listInappropiates = InappropiateLocalServiceUtil.getInnapropiatesByClassPk(myP2PActivity.getP2pActivityId());
				if (listInappropiates != null && listInappropiates.size()>0){
					isInappropiate = true;
				}
			}catch(Exception e){
				
			}
			listCorrections =P2pActivityCorrectionsLocalServiceUtil.findByP2pActivityId(myP2PActivity.getP2pActivityId());
		}
		boolean isReviewReported = false;
		List<Inappropiate> listReviewsInap = new ArrayList<Inappropiate>();
		if (listCorrections != null && listCorrections.size()>0){
			for (P2pActivityCorrections paCorrection : listCorrections){
				List<Inappropiate> listReviewsTemp= InappropiateLocalServiceUtil.getInnapropiatesByClassPk(paCorrection.getP2pActivityCorrectionsId());
				if (listReviewsTemp != null && listReviewsTemp.size()>0){
					isReviewReported = true;
					listReviewsInap.addAll(listReviewsTemp);
				}
			}
		}
		%>


	<%
	String claseInappropiateTable = "inappropiateTable";
	String claseDetalleRate = "tableDetailRate_noP2p";
	String claseDetalleReview = "tableDetailReview_noP2p";
	if (isInappropiate){		
		claseDetalleRate = "tableDetailRate_" + myP2PActivity.getP2pActivityId();
	}
	if (isReviewReported){		
		claseDetalleReview = "tableDetailReview_" + myP2PActivity.getP2pActivityId();
	}
	%>
			<liferay-ui:search-container-column-text name="inappropiate.label" >	
					<table class = "<%=claseInappropiateTable%>">
						<tr>
							<th><liferay-ui:message key="inappropiate.rate.label" /></th>
							<th><liferay-ui:message key="inappropiate.review.label" /></th>
						</tr>
						<tr>
							<td>
								<%if (isInappropiate){%>		
									<a href="javascript:<portlet:namespace />openPopUp(<%=myP2PActivity.getP2pActivityId()%>);"><liferay-ui:message key="inappropiate.yes" /></a>
								<%}else{%>
									<liferay-ui:message key="inappropiate.no" />
								<%} %>
							</td>
							<td>
								<%if (isReviewReported){%>		
									<a href="javascript:<portlet:namespace />openCorrectionPopUp(<%=myP2PActivity.getP2pActivityId()%>);"><liferay-ui:message key="inappropiate.yes" /></a>
								<%}else{%>
									<liferay-ui:message key="inappropiate.no" />
								<%} %>
							</td>
						</tr>
					</table>
					
				</liferay-ui:search-container-column-text>
				
			<!-- Start PopUp Show Inappropiate rate-->
			<% if (isInappropiate){ %>
				<div id="<portlet:namespace />inappropiate" style="display:none">
					<h1><liferay-ui:message key="inappropiate.popup.title" /></h1>
					<br />
					<div class="<%=claseDetalleRate%>">
						<div style="margin:1em 0;" class="aui-searchcontainer">
							<table class="taglib-search-iterator">
								<thead>
									<tr class="portlet-section-header results-header"> 
										<th class="col-1 col-name first"><liferay-ui:message key="inappropiate.popup.name" /></th>
										<th class="col-2 col-reason "><liferay-ui:message key="inappropiate.popup.reason" /></th>
										<th class="col-3 col-date last"><liferay-ui:message key="inappropiate.popup.date" /></th>
									</tr>
								</thead>
								<tbody>
									<%
									for (Inappropiate in: listInappropiates){
										%>
										<tr class="results-row">
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
					</div>
					<div class="buttons">
						<input type="button" class="button simplemodal-close" onclick="<portlet:namespace />closeShowInappropiate()" value="<liferay-ui:message key="cancel" />" />
					</div>
				</div>
				<!-- End PopUp Show Inappropiate rate-->
			<% }
			
			if (isReviewReported){%>
			
			<!-- Start PopUp Show Inappropiate review-->
	
				<div id="<portlet:namespace />inappropiateCorrection" style="display:none">
					<h1><liferay-ui:message key="inappropiate.popup.title" /></h1>
					<br />
					<div class="<%=claseDetalleReview %>">
						<div style="margin:1em 0;" class="aui-searchcontainer">
							<table class="taglib-search-iterator">
								<thead>
									<tr class="portlet-section-header results-header"> 
									<th class="col-1 col-name first"><liferay-ui:message key="inappropiate.popup.name" /></th>
									<th class="col-2 col-reason "><liferay-ui:message key="inappropiate.popup.reason" /></th>
									<th class="col-3 col-date last"><liferay-ui:message key="inappropiate.popup.date" /></th>
									</tr>
								</thead>
								<tbody>
									<%
									for (Inappropiate in: listReviewsInap){
										%>
										<tr class="results-row">
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
					</div>
					<div class="buttons">
						<input type="button" class="button simplemodal-close" onclick="<portlet:namespace />closeShowInappropiateCorrection()" value="<liferay-ui:message key="cancel" />" />
					</div>
				</div>
				<!-- End PopUp Show Inappropiate review -->
		<% }%>
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
	        function(numero, rate) {
				var A = AUI();

				//Start opening popUp			
				if(A.one('#<portlet:namespace />inappropiate')) {
					window.<portlet:namespace />inappropiateTitle = A.one('#<portlet:namespace />inappropiate h1').html();
					
					var body = '#<portlet:namespace />inappropiate .tableDetailRate_'+numero;
					
					
					window.<portlet:namespace />inappropiateBody = A.one(body).html();
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
	    
	    Liferay.provide(
	        window,
	        '<portlet:namespace />openCorrectionPopUp',
	        function(numero) {
				var A = AUI();

				//Start opening popUp			
				if(A.one('#<portlet:namespace />inappropiateCorrection')) {
					window.<portlet:namespace />inappropiateTitle = A.one('#<portlet:namespace />inappropiateCorrection h1').html();
					var body = '#<portlet:namespace />inappropiateCorrection .tableDetailReview_'+numero;
					window.<portlet:namespace />inappropiateBody = A.one(body).html();
				}
				
				window.<portlet:namespace />inappropiateCorrection = new A.Dialog({
					id:'<portlet:namespace />showpinappropiateCorrection',
					title: window.<portlet:namespace />inappropiateTitle,
		            bodyContent: window.<portlet:namespace />inappropiateBody,
		            centered: true,
		            modal: true,
		            width: "auto",
		            height: "auto"
		        }).render();
				
				window.<portlet:namespace />inappropiateCorrection.show();
	        },
	        ['aui-dialog']
	    );
		
	Liferay.provide(
	        window,
	        '<portlet:namespace />closeShowInappropiateCorrection',
	        function() {
				var A = AUI();
				
				A.DialogManager.closeByChild('#<portlet:namespace />showpinappropiateCorrection');
	        },
	        ['aui-dialog']
	    );

</aui:script>