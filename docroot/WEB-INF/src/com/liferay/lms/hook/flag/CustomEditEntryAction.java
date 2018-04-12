package com.liferay.lms.hook.flag;

import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.service.InappropiateLocalServiceUtil;
import com.liferay.lms.service.LearningActivityLocalServiceUtil;
import com.liferay.lms.service.P2pActivityLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.flags.service.FlagsEntryServiceUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class CustomEditEntryAction extends BaseStrutsPortletAction {
	
	private static Log log = LogFactoryUtil.getLog(CustomEditEntryAction.class);	
			
	public void processAction(
            StrutsPortletAction originalStrutsPortletAction,
            PortletConfig portletConfig, ActionRequest actionRequest,
            ActionResponse actionResponse)
		throws Exception {

		String className = ParamUtil.getString(actionRequest, "className");
		long classPK = ParamUtil.getLong(actionRequest, "classPK"); 
		String reporterEmailAddress = ParamUtil.getString(
			actionRequest, "reporterEmailAddress");
		long reportedUserId = ParamUtil.getLong(
			actionRequest, "reportedUserId");
		String contentTitle = ParamUtil.getString(
			actionRequest, "contentTitle");
		String contentURL = ParamUtil.getString(actionRequest, "contentURL");
		String reason = ParamUtil.getString(actionRequest, "reason");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			"com.liferay.portlet.flags.model.FlagsEntry", actionRequest);
		
		//Para las actividades p2p
		if(className.equals(P2pActivity.class.getName())){
			P2pActivity p2p=P2pActivityLocalServiceUtil.fetchP2pActivity(classPK);	
		
			FlagsEntryServiceUtil.addEntry(
					className, p2p.getActId(), reporterEmailAddress, reportedUserId,
					contentTitle, contentURL, reason, serviceContext);		
		
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);		
			User user = themeDisplay.getUser();
			
			//Guardamos en lms_inappropiate el contenido inapropiado			
			String inappropiateFlag = LearningActivityLocalServiceUtil.getExtraContentValue(p2p.getActId(),"inappropiateFlag");
			
			boolean enableFlags = false;
			try {
				enableFlags = Boolean.valueOf(inappropiateFlag);
				log.debug("La calificacion de contenido inapropiado esta a: " + inappropiateFlag);
			}catch(Exception e){}
			
			if(enableFlags && p2p != null){					
				//Comprobamos que ese usuario no haya dado una calificacion ya sobre esa p2p
				Inappropiate inappropiate=null;
				inappropiate=InappropiateLocalServiceUtil.findByUserIdClassNameClassPK(user.getUserId(), className, classPK);
				
				if(inappropiate == null){
					inappropiate=InappropiateLocalServiceUtil.addInappropiate(user.getUserId(), themeDisplay.getScopeGroupId(), className, classPK, reason);

				}else{
					log.debug("El usuario: " + user.getUserId() + " ya ha calificado como inapropiada la actividad p2p: " + p2p.getP2pActivityId() + " anteriormente. No se realiza el guardado de esta última calificación");					
				}				
			}
			
		}else{			
			
			FlagsEntryServiceUtil.addEntry(
					className, classPK, reporterEmailAddress, reportedUserId,
					contentTitle, contentURL, reason, serviceContext);
		}		

		originalStrutsPortletAction.processAction(
	            originalStrutsPortletAction, portletConfig, actionRequest,
	            actionResponse);
	}

	public String render(
			StrutsPortletAction originalStrutsPortletAction, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return originalStrutsPortletAction.render(
	            null, portletConfig, renderRequest, renderResponse);
	}
}
