package com.liferay.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.service.InappropiateLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;

public class P2pTaskActivitySearchUtil {
	private static final Log log = LogFactoryUtil.getLog(P2pTaskActivitySearchUtil.class);
	
	public static List<User> getUserByFilter(LinkedHashMap<java.lang.String, java.lang.Object> params, long actId, int start, int end, int inapropValue ){
		List<User> registros = new ArrayList<User>();
		
		try{
			Long groupId = new Long((String)""+params.get("usersGroups"));
						
			switch(inapropValue){
			case 1:
				registros = InappropiateLocalServiceUtil.getUsersWithInappropiate(groupId, P2pActivity.class.getName(),actId, start, end);
				log.debug("Registros with "+registros.size());
				break;
			case 2:
				registros = InappropiateLocalServiceUtil.getUsersWithOutInappropiate(groupId, P2pActivity.class.getName(),actId, start, end);
				log.debug("Registros without "+registros.size());
				break;
			}
			 
 
		}catch(Exception e){
			log.debug("Error al recuperar las tareas filtradas: "+e.getMessage());
		}
		
		return registros;
	}
	
	public static int countUserByFilter(LinkedHashMap<java.lang.String, java.lang.Object> params, long actId, int start, int end, int inapropValue) throws SystemException{
		int count = 0;
		List<User> registros = new ArrayList<User>();
		try{
			Long groupId = new Long((String)""+params.get("usersGroups"));
			switch(inapropValue){
			case 1:
				registros = InappropiateLocalServiceUtil.getUsersWithInappropiate(groupId, P2pActivity.class.getName(), actId, start, end);
				log.debug("Registros count with "+registros.size());
				break;
			case 2:
				registros = InappropiateLocalServiceUtil.getUsersWithOutInappropiate(groupId, P2pActivity.class.getName(), actId, start, end);
				log.debug("Registros count wihtout "+registros.size());
				break;
			}

		}catch(Exception e){
			log.debug("Error al recuperar las tareas filtradas: "+e.getMessage());
		}
		count = registros.size();
		return count;
	}
}
