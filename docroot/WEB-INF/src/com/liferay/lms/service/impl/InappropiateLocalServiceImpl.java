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

package com.liferay.lms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.service.ClpSerializer;
import com.liferay.lms.service.P2pActivityCorrectionsLocalServiceUtil;
import com.liferay.lms.service.P2pActivityLocalServiceUtil;
import com.liferay.lms.service.base.InappropiateLocalServiceBaseImpl;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;

/**
 * The implementation of the inappropiate local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.lms.service.InappropiateLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author TLS
 * @see com.liferay.lms.service.base.InappropiateLocalServiceBaseImpl
 * @see com.liferay.lms.service.InappropiateLocalServiceUtil
 */
public class InappropiateLocalServiceImpl
	extends InappropiateLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.liferay.lms.service.InappropiateLocalServiceUtil} to access the inappropiate local service.
	 */
	
	private static final Log log = LogFactoryUtil.getLog(InappropiateLocalServiceImpl.class);
	//el usuario que se guarda es el que lo denuncia. El que es denunciado es el usuario al que pertenece la p2pactivity
		public Inappropiate addInappropiate(long userId, long groupId, String className, long classPk, String reason) throws PortalException, SystemException {

			User user = userPersistence.findByPrimaryKey(userId);
			
			Date now = new Date(System.currentTimeMillis());
			
			long inappropiateId = counterLocalService.increment(Inappropiate.class.getName());

		    Inappropiate inappropiate = inappropiatePersistence.create(inappropiateId);

		    inappropiate.setUserId(user.getUserId());
		    inappropiate.setUserName(user.getFullName());
		    inappropiate.setGroupId(groupId);
		    inappropiate.setCompanyId(user.getCompanyId());
		    inappropiate.setClassName(className); 
		    inappropiate.setClassPK(classPk);
		    inappropiate.setReason(reason);
		    inappropiate.setCreateDate(now);
		    inappropiate.setModifiedDate(now);

		    super.addInappropiate(inappropiate);
		    
		   /* resourceLocalService.addResources(
	                inappropiate.getCompanyId(), inappropiate.getGroupId(), inappropiate.getUserId(),
	                Inappropiate.class.getName(), inappropiate.getInappropiateId(), false,
	                true, true);*/

		    return inappropiate;
		}
		
		public Inappropiate deleteInappropiate(Inappropiate inappropiate) throws SystemException, PortalException{

			/*resourceLocalService.deleteResource(inappropiate.getCompanyId(),
						Inappropiate.class.getName(),
						ResourceConstants.SCOPE_INDIVIDUAL,
						inappropiate.getPrimaryKey());*/
		    	    
		    return inappropiatePersistence.remove(inappropiate);
		}
		
		public Inappropiate deleteInappropiate(long inappropiateId) throws PortalException, SystemException {

			    Inappropiate inappropiate = inappropiatePersistence.findByPrimaryKey(inappropiateId);

			    return deleteInappropiate(inappropiate);
		}
		
		
		public List<Inappropiate> getInappropiatesByGroupId(long groupId) throws SystemException {

			return inappropiatePersistence.findByGroupId(groupId);
		}
		
		public List<Inappropiate> getInappropiatesByGroupId(long groupId, int start, int end) throws SystemException {

			return inappropiatePersistence.findByGroupId(groupId, start, end);
		}
		
		public int getInappropiatesCountByGroupId(long groupId) throws SystemException {

			return inappropiatePersistence.countByGroupId(groupId);
		}
		
		public List<Inappropiate> getInnapropiatesByClassPk (long classPk) throws SystemException{
			return inappropiatePersistence.findByClassPK(classPk);
		}
		
		public int getInappropiatesCountByClassPk (long classPk) throws SystemException{
			return inappropiatePersistence.countByClassPK(classPk);
		}
		
		public List<Inappropiate> getInappropiatesByGroupIdClassName(long groupId, String className) throws SystemException{
			return inappropiatePersistence.findByGroupIdClassName(groupId, className);
		}
		public int getInappropiatesCountByGroupIdClassName(long groupId, String className) throws SystemException{
			return inappropiatePersistence.countByGroupIdClassName(groupId, className);
		}
		
		public List<User> getUsersWithInappropiate (int reviewSearch, long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
			//Si es no realizada no hay correspondencia en lms_inapropiate, nunca hay resultados
			if(!exists && !correctionsCompleted){
				return new ArrayList<User>();
			}
			else{
				return inappropiateFinder.findByInappropiate(reviewSearch, groupId, className, exists, correctionsCompleted, actId, start, end);
			}			
		}
		
		public List<User> getUsersWithOutInappropiate (int inapropValue, int reviewSearch,  long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
			//Si es no realizada no hay correspondencia en lms_inapropiate, y nunca puede devolver resultados con review (no deolvemos resultados cuando se selecciona "si" en la busqueda por review inap)
			if(!correctionsCompleted && !exists && reviewSearch ==1){
				return new ArrayList<User>();
			}
			else if((!correctionsCompleted && !exists && reviewSearch != 1) || (correctionsCompleted && !exists && reviewSearch !=1 )){
				//no realizadas o todas
				return inappropiateFinder.findByWorkNotDone(reviewSearch, inapropValue, actId, groupId, exists, correctionsCompleted, start, end);
			}else{
				//incompletas
				return inappropiateFinder.findByNoInappropiate(reviewSearch, groupId, className, exists, correctionsCompleted, actId, start, end);
			}
		}
		
		public List<User>  getUsersWithWithoutInappropiateUserTeams (int inapropValue, int reviewSearch, long actId, long groupId, boolean exists, boolean correctionsCompleted, long userId, int start, int end){
			
			if(!correctionsCompleted && !exists && reviewSearch ==1){
				return new ArrayList<User>();
			}
			else if( (!correctionsCompleted && !exists && reviewSearch !=1) || (reviewSearch ==2 && correctionsCompleted == true)){
				return inappropiateFinder.findByWorkNotDone(reviewSearch, inapropValue, actId, groupId, exists, correctionsCompleted, start, end);
			}
			else{
				return inappropiateFinder.findByWithWithoutInappropiateUserTeams(reviewSearch, actId, groupId,  exists, correctionsCompleted, userId, start, end);
			}
		}
		
		public List<User>  getUsersWithWithoutInappropiate(int inapropValue, int reviewSearch, long actId, long groupId, boolean exists, boolean correctionsCompleted, long userId, int start, int end){
						
			if(!correctionsCompleted && !exists && reviewSearch ==1){
				return new ArrayList<User>();
			}
			else if( (!correctionsCompleted && !exists && reviewSearch !=1) || (reviewSearch ==2 && correctionsCompleted == true)){
				return inappropiateFinder.findByWorkNotDone(reviewSearch, inapropValue, actId, groupId, exists, correctionsCompleted, start, end);
			}
			else{
				return inappropiateFinder.findByWithWithoutInappropiate(reviewSearch, actId, groupId, exists, correctionsCompleted, start, end);
			}
			
		}
		
		public Inappropiate findByUserIdClassNameClassPK(long userId, String className, long classPK) throws SystemException {
				
			List<Inappropiate> inappropiateList= inappropiatePersistence.findByUserIdClassNameClassPK(userId, className, classPK);
			if(inappropiateList != null && !inappropiateList.isEmpty()){
				return inappropiateList.get(0);
			}else{
				return null;
			}
				
		}
		
		public List<User> selectUsersByStatusCorrection(List<User>usersList, long actId, boolean existsP2p, boolean correctionCompleted, int start , int end){
			
			List<User>usersListAux=new ArrayList<User>();
			List<User>usersListFinal=new ArrayList<User>();
			usersListAux.addAll(usersList);			
			for (User user: usersList){				
				try {	
					boolean corrCompleted = P2pActivityCorrectionsLocalServiceUtil.areAllCorrectionsDoneByUserInP2PActivity(actId, user.getUserId());					
					//Si no coincide con el para metro de busqueda lo eliminamos de la lista
					if(corrCompleted != correctionCompleted){
						usersListAux.remove(user);						
					}
				} catch (SystemException e) {					
					log.error("Error en selectUsersByStatusCorrection: " + e.getMessage());
				}
			}
			if(start < 0 && end < 0){
				return usersListAux;
			}else{
				if(end > usersListAux.size()){
					end = usersListAux.size();
				}
				if(!usersListAux.isEmpty()){
					for (int i = start; i< end; i++){
						User user = usersListAux.get(i);
						usersListFinal.add(user);
					}
				}				
				return usersListFinal;
			}
			
		}
			
	}
		
		