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

package com.liferay.lms.model.impl;

import com.liferay.lms.model.Inappropiate;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import java.io.Serializable;

import java.util.Date;

/**
 * The cache model class for representing Inappropiate in entity cache.
 *
 * @author TLS
 * @see Inappropiate
 * @generated
 */
public class InappropiateCacheModel implements CacheModel<Inappropiate>,
	Serializable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", inappropiateId=");
		sb.append(inappropiateId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", className=");
		sb.append(className);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", reason=");
		sb.append(reason);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append("}");

		return sb.toString();
	}

	public Inappropiate toEntityModel() {
		InappropiateImpl inappropiateImpl = new InappropiateImpl();

		if (uuid == null) {
			inappropiateImpl.setUuid(StringPool.BLANK);
		}
		else {
			inappropiateImpl.setUuid(uuid);
		}

		inappropiateImpl.setInappropiateId(inappropiateId);
		inappropiateImpl.setUserId(userId);

		if (userName == null) {
			inappropiateImpl.setUserName(StringPool.BLANK);
		}
		else {
			inappropiateImpl.setUserName(userName);
		}

		if (className == null) {
			inappropiateImpl.setClassName(StringPool.BLANK);
		}
		else {
			inappropiateImpl.setClassName(className);
		}

		inappropiateImpl.setClassPK(classPK);
		inappropiateImpl.setGroupId(groupId);
		inappropiateImpl.setCompanyId(companyId);

		if (reason == null) {
			inappropiateImpl.setReason(StringPool.BLANK);
		}
		else {
			inappropiateImpl.setReason(reason);
		}

		if (createDate == Long.MIN_VALUE) {
			inappropiateImpl.setCreateDate(null);
		}
		else {
			inappropiateImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			inappropiateImpl.setModifiedDate(null);
		}
		else {
			inappropiateImpl.setModifiedDate(new Date(modifiedDate));
		}

		inappropiateImpl.resetOriginalValues();

		return inappropiateImpl;
	}

	public String uuid;
	public long inappropiateId;
	public long userId;
	public String userName;
	public String className;
	public long classPK;
	public long groupId;
	public long companyId;
	public String reason;
	public long createDate;
	public long modifiedDate;
}