package com.liferay.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.model.P2pActivityCorrections;
import com.liferay.lms.service.InappropiateLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Team;
import com.liferay.portal.model.User;
import com.liferay.portal.service.TeamLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class P2pTaskActivitySearchUtil {
	private static final Log log = LogFactoryUtil.getLog(P2pTaskActivitySearchUtil.class);
	
	
	public static List<User> getUserByFilter(String criteria, LinkedHashMap<java.lang.String, java.lang.Object> params, long actId, long userId, int start, int end, int inapropValue, int inapropReviewValue,  int state  ){
		
		List<User> registros = new ArrayList<User>();
		List<User> registrosFinal = new ArrayList<User>();
		boolean existsP2p = false;
		boolean correctionCompleted = true;	
		int startAux = start;
		int endAux = end;
		
		if (state == 1){	//incompleta		
			existsP2p=true;
			correctionCompleted=false;
		}
		else if(state == 2){ //realizada
			existsP2p=true;
			correctionCompleted=true;
		}
		else if(state == 3){ //no realizada
			existsP2p=false;
			correctionCompleted=false;
		}
		
		Long groupId = new Long((String)""+params.get("usersGroups"));	
		
		if(log.isDebugEnabled()){
			log.debug("getUserByFilter: inapropValue: " + inapropValue + ", inapropReviewValue: " + inapropReviewValue + ", state: " + state + ", actId: " + actId + ", userId: " + userId + ", groupId: " + groupId);		
			log.debug("getUserByFilter: existsP2p: " + existsP2p + ", correctionCompleted: " + correctionCompleted);
		}
		
		try{			
			
			User user= UserLocalServiceUtil.getUser(userId);	
			List<Team> userTeams=TeamLocalServiceUtil.getUserTeams(user.getUserId(), groupId);
			if(state != 0){
				start = -1;
				end = -1;
			}			
			switch(inapropValue){
			case 0:					
				if(userTeams!=null && !userTeams.isEmpty() ){
					registros = InappropiateLocalServiceUtil.getUsersWithWithoutInappropiateUserTeams(inapropReviewValue, actId, groupId, existsP2p, correctionCompleted,  userId, start, end);	
				}else{
					registros = InappropiateLocalServiceUtil.getUsersWithWithoutInappropiate(inapropReviewValue, actId, groupId, existsP2p, correctionCompleted, userId, start, end);
				}								
				//A partir de estos sacamos los que cumplen la otra condicion
				if(log.isDebugEnabled()){
					log.debug("Registros with without "+registros.size());
				}
				
				break;				
			case 1:
				registros = InappropiateLocalServiceUtil.getUsersWithInappropiate(inapropReviewValue, groupId, P2pActivity.class.getName(), existsP2p, correctionCompleted, actId, start, end);
				//A partir de estos sacamos los que cumplen la otra condicion
				if(log.isDebugEnabled()){
					log.debug("Registros with "+registros.size());
				}
				break;
			case 2:
				registros = InappropiateLocalServiceUtil.getUsersWithOutInappropiate(inapropReviewValue, groupId, P2pActivity.class.getName(), existsP2p, correctionCompleted, actId, start, end);				
				if(log.isDebugEnabled()){
					log.debug("Registros without "+registros.size());
				}
				break;
			}	
			//Solo filtamos por el Estado cuando el filtro del Estado es distinto de 0 (hemos seleccionado algun estado)
			if(state != 0){
				registrosFinal = InappropiateLocalServiceUtil.selectUsersByStatusCorrection(registros, actId, existsP2p, correctionCompleted, startAux, endAux);	 
			}else{
				return registros;
			}			
				 
		}catch(Exception e){
			log.error("Error al recuperar las tareas filtradas: "+e.getMessage());
		}		
		return registrosFinal;

	}
}
