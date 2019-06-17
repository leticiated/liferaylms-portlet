package com.liferay.lms.hook.flag;

import java.text.SimpleDateFormat;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.liferay.lms.model.Course;
import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.service.CourseLocalServiceUtil;
import com.liferay.lms.service.InappropiateLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

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
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);		
		
			//Comprobamos que ese usuario no haya dado una calificacion ya sobre esa p2p
			Inappropiate inappropiate=null;
			inappropiate=InappropiateLocalServiceUtil.findByUserIdClassNameClassPK(reportedUserId, className, classPK);
			
			if(inappropiate == null){
				if (className.equals(P2pActivity.class.getName()) || className.equals("com.liferay.lms.bookmarksactivity.model.BookmarksActivity")){
					User reporterUser = UserLocalServiceUtil.fetchUserByEmailAddress(themeDisplay.getCompanyId(), reporterEmailAddress);
					if (reporterUser != null){
						inappropiate=InappropiateLocalServiceUtil.addInappropiate(reporterUser.getUserId(), themeDisplay.getScopeGroupId(), className, classPK, reason);
					}
				}else{
					inappropiate=InappropiateLocalServiceUtil.addInappropiate(reportedUserId, themeDisplay.getScopeGroupId(), className, classPK, reason);
				}
				contentTitle = contentTitle.replaceAll("\\<.*?>","");
				reason = reason.replaceAll("\\<.*?>","");
				Course course = CourseLocalServiceUtil.fetchByGroupCreatedId(themeDisplay.getScopeGroupId());
				//long[] userIds = CourseLocalServiceUtil.getTeachersAndEditorsIdsFromCourse(course);
				//List<User> listTeachers= CourseLocalServiceUtil.getTeachersFromCourse(course.getCourseId());
				String fromAddress=PrefsPropsUtil.getString(themeDisplay.getCompanyId(),PropsKeys.ADMIN_EMAIL_FROM_ADDRESS,"");
				String fromName=PrefsPropsUtil.getString(themeDisplay.getCompanyId(),PropsKeys.ADMIN_EMAIL_FROM_NAME,"");
				String userName = "";
				String userEmail = "";
				if (reportedUserId <= 0){
					userName = contentTitle.split("##")[1];
					userEmail = contentTitle.split("##")[2];
				}else{
					User userReported = UserLocalServiceUtil.fetchUser(reportedUserId);
					userName = userReported.getFullName();
					userEmail = userReported.getEmailAddress();
				}
				String reporterUserName = UserLocalServiceUtil.fetchUserByEmailAddress(themeDisplay.getCompanyId(), reporterEmailAddress).getFullName();
				SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = dFormat.format(inappropiate.getCreateDate());
			/*	for (User teacher: listTeachers){				
					sendMail(teacher.getUserId(), portletConfig, course, className, contentTitle, userName, userEmail, reporterUserName, reporterEmailAddress, reason, date, contentURL, fromName, fromAddress);
				}*/
				// EL EMAIL SE ENVIA A LOS QUE TIENEN EL ROL DE SUPERVISORS
				Role role = RoleLocalServiceUtil.fetchRole(themeDisplay.getCompanyId(), "Supervisors");
				if (role != null){
					long[] adminUsers = UserLocalServiceUtil.getRoleUserIds(role.getRoleId());
					for (long userId : adminUsers){
						sendMail(userId, portletConfig, course, className, contentTitle, userName, userEmail, reporterUserName, reporterEmailAddress, reason, date, contentURL, fromName, fromAddress);
					}
				}
			}else{
				log.debug("El usuario: " + reportedUserId + " ya ha calificado como inapropiada la actividad: " + classPK + " anteriormente. No se realiza el guardado de esta última calificación");					
			}	
	}

	private void sendMail(long userId, PortletConfig portletConfig, Course course, String className, String contentTitle, String userName, String userEmail, String reporterUserName, String reporterEmailAddress, String reason, String date, String contentURL, String fromName, String fromAddress ) throws Exception{
		User teacher = UserLocalServiceUtil.fetchUser(userId);
		String subject = LanguageUtil.format(portletConfig, teacher.getLocale(), "flag.mail.subject", course.getTitle(LocaleUtil.getDefault())); 
		String type = LanguageUtil.format(teacher.getLocale(), "p2ptask-correction", "");
		if (className.equals(P2pActivity.class.getName()) || className.equals("com.liferay.lms.bookmarksactivity.model.BookmarksActivity")){
			type = LanguageUtil.format(teacher.getLocale(), "coursestats.modulestats.activity", "");
		}
		String tipo = type.substring(0, 1).toLowerCase()+type.substring(1, type.length());
		String messageArgs[]= {teacher.getFirstName(), tipo, StringEscapeUtils.unescapeHtml(contentTitle),userName, userEmail, reporterUserName , reporterEmailAddress, StringEscapeUtils.unescapeHtml(reason), date, contentURL, fromName};
		String message = LanguageUtil.format(portletConfig,teacher.getLocale(), "flag.mail.body", messageArgs, false);

		InternetAddress from = new InternetAddress(fromAddress, fromName);
		InternetAddress to = new InternetAddress(teacher.getEmailAddress(), teacher.getFullName());
		
		MailMessage mailMessage = new MailMessage(from, to, subject, message, true);
		
		MailServiceUtil.sendEmail(mailMessage);	
	}
	
	public String render(
			StrutsPortletAction originalStrutsPortletAction, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return originalStrutsPortletAction.render(
	            null, portletConfig, renderRequest, renderResponse);
	}
}
