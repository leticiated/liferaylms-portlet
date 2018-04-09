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

import java.util.Date;
import java.util.List;

import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.service.base.InappropiateLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;

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
	//el usuario que se guarda es el que lo denuncia. El que es denunciado es el usuario al que pertenece la p2pactivity
		public Inappropiate addInappropiate(long userId, long groupId, String className, long classPk, String reason) throws PortalException, SystemException {

			User user = userPersistence.findByPrimaryKey(userId);
			
			Date now = new Date();
			
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
		    
		    resourceLocalService.addResources(
	                inappropiate.getCompanyId(), inappropiate.getGroupId(), inappropiate.getUserId(),
	                Inappropiate.class.getName(), inappropiate.getInappropiateId(), false,
	                true, true);

		    return inappropiate;
		}
		
		public Inappropiate deleteInappropiate(Inappropiate inappropiate) throws SystemException, PortalException{

			resourceLocalService.deleteResource(inappropiate.getCompanyId(),
						Inappropiate.class.getName(),
						ResourceConstants.SCOPE_INDIVIDUAL,
						inappropiate.getPrimaryKey());
		    	    
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
		
		public List<Inappropiate> getInappropiatesCountByGroupIdClassName(long groupId, String className) throws SystemException{
			return inappropiatePersistence.findByGroupIdClassName(groupId, className);
		}
}