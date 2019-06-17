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

package com.liferay.lms.service.persistence;

import com.liferay.lms.NoSuchLearningActivityResultException;
import com.liferay.lms.model.LearningActivityResult;
import com.liferay.lms.model.impl.LearningActivityResultImpl;
import com.liferay.lms.model.impl.LearningActivityResultModelImpl;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The persistence implementation for the learning activity result service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author TLS
 * @see LearningActivityResultPersistence
 * @see LearningActivityResultUtil
 * @generated
 */
public class LearningActivityResultPersistenceImpl extends BasePersistenceImpl<LearningActivityResult>
	implements LearningActivityResultPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link LearningActivityResultUtil} to access the learning activity result persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = LearningActivityResultImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] { String.class.getName() },
			LearningActivityResultModelImpl.UUID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_FETCH_BY_ACT_USER = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByact_user",
			new String[] { Long.class.getName(), Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACT_USER = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByact_user",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_FETCH_BY_USERIDACTID = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByUserIdActId",
			new String[] { Long.class.getName(), Long.class.getName() },
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERIDACTID = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserIdActId",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_AP = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByap",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AP = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByap",
			new String[] { Long.class.getName(), Boolean.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.PASSED_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_AP = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByap",
			new String[] { Long.class.getName(), Boolean.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_APD = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByapd",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Date.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_APD = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByapd",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Date.class.getName()
			},
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.PASSED_COLUMN_BITMASK |
			LearningActivityResultModelImpl.ENDDATE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_APD = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByapd",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Date.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_AC = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByac",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AC = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByac",
			new String[] { Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_AC = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByac",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USER = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByuser",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USER = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByuser",
			new String[] { Long.class.getName() },
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USER = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByuser",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdNotMultipleUserId",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdNotMultipleUserId",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdNotMultipleUserIdStarted",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdNotMultipleUserIdStarted",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByActIdStarted",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByActIdStarted",
			new String[] { Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDSTARTED = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByActIdStarted",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdPassedNotMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdPassedNotMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdNotMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdNotMultipleUserIdFinished",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByActIdFinished",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByActIdFinished",
			new String[] { Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDFINISHED = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByActIdFinished",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdPassedNotMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdPassedNotMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdPassedMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByActIdPassedMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			},
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.PASSED_COLUMN_BITMASK |
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActIdPassedMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdPassedMultipleUserId",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdPassedMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByActIdPassedMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			},
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.PASSED_COLUMN_BITMASK |
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActIdPassedMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdPassedMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Long.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdPassedFinished",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByActIdPassedFinished",
			new String[] { Long.class.getName(), Boolean.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.PASSED_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDPASSEDFINISHED = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActIdPassedFinished",
			new String[] { Long.class.getName(), Boolean.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdMultipleUserIdStarted",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByActIdMultipleUserIdStarted",
			new String[] { Long.class.getName(), Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActIdMultipleUserIdStarted",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdMultipleUserIdStarted",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByActIdMultipleUserIdFinished",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByActIdMultipleUserIdFinished",
			new String[] { Long.class.getName(), Long.class.getName() },
			LearningActivityResultModelImpl.ACTID_COLUMN_BITMASK |
			LearningActivityResultModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByActIdMultipleUserIdFinished",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED =
		new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"countByActIdMultipleUserIdFinished",
			new String[] { Long.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the learning activity result in the entity cache if it is enabled.
	 *
	 * @param learningActivityResult the learning activity result
	 */
	public void cacheResult(LearningActivityResult learningActivityResult) {
		EntityCacheUtil.putResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			learningActivityResult.getPrimaryKey(), learningActivityResult);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ACT_USER,
			new Object[] {
				Long.valueOf(learningActivityResult.getActId()),
				Long.valueOf(learningActivityResult.getUserId())
			}, learningActivityResult);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERIDACTID,
			new Object[] {
				Long.valueOf(learningActivityResult.getUserId()),
				Long.valueOf(learningActivityResult.getActId())
			}, learningActivityResult);

		learningActivityResult.resetOriginalValues();
	}

	/**
	 * Caches the learning activity results in the entity cache if it is enabled.
	 *
	 * @param learningActivityResults the learning activity results
	 */
	public void cacheResult(
		List<LearningActivityResult> learningActivityResults) {
		for (LearningActivityResult learningActivityResult : learningActivityResults) {
			if (EntityCacheUtil.getResult(
						LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
						LearningActivityResultImpl.class,
						learningActivityResult.getPrimaryKey()) == null) {
				cacheResult(learningActivityResult);
			}
			else {
				learningActivityResult.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all learning activity results.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(LearningActivityResultImpl.class.getName());
		}

		EntityCacheUtil.clearCache(LearningActivityResultImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the learning activity result.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(LearningActivityResult learningActivityResult) {
		EntityCacheUtil.removeResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			learningActivityResult.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(learningActivityResult);
	}

	@Override
	public void clearCache(List<LearningActivityResult> learningActivityResults) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (LearningActivityResult learningActivityResult : learningActivityResults) {
			EntityCacheUtil.removeResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
				LearningActivityResultImpl.class,
				learningActivityResult.getPrimaryKey());

			clearUniqueFindersCache(learningActivityResult);
		}
	}

	protected void clearUniqueFindersCache(
		LearningActivityResult learningActivityResult) {
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_ACT_USER,
			new Object[] {
				Long.valueOf(learningActivityResult.getActId()),
				Long.valueOf(learningActivityResult.getUserId())
			});

		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERIDACTID,
			new Object[] {
				Long.valueOf(learningActivityResult.getUserId()),
				Long.valueOf(learningActivityResult.getActId())
			});
	}

	/**
	 * Creates a new learning activity result with the primary key. Does not add the learning activity result to the database.
	 *
	 * @param larId the primary key for the new learning activity result
	 * @return the new learning activity result
	 */
	public LearningActivityResult create(long larId) {
		LearningActivityResult learningActivityResult = new LearningActivityResultImpl();

		learningActivityResult.setNew(true);
		learningActivityResult.setPrimaryKey(larId);

		String uuid = PortalUUIDUtil.generate();

		learningActivityResult.setUuid(uuid);

		return learningActivityResult;
	}

	/**
	 * Removes the learning activity result with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param larId the primary key of the learning activity result
	 * @return the learning activity result that was removed
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult remove(long larId)
		throws NoSuchLearningActivityResultException, SystemException {
		return remove(Long.valueOf(larId));
	}

	/**
	 * Removes the learning activity result with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the learning activity result
	 * @return the learning activity result that was removed
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public LearningActivityResult remove(Serializable primaryKey)
		throws NoSuchLearningActivityResultException, SystemException {
		Session session = null;

		try {
			session = openSession();

			LearningActivityResult learningActivityResult = (LearningActivityResult)session.get(LearningActivityResultImpl.class,
					primaryKey);

			if (learningActivityResult == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchLearningActivityResultException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(learningActivityResult);
		}
		catch (NoSuchLearningActivityResultException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected LearningActivityResult removeImpl(
		LearningActivityResult learningActivityResult)
		throws SystemException {
		learningActivityResult = toUnwrappedModel(learningActivityResult);

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.delete(session, learningActivityResult);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(learningActivityResult);

		return learningActivityResult;
	}

	@Override
	public LearningActivityResult updateImpl(
		com.liferay.lms.model.LearningActivityResult learningActivityResult,
		boolean merge) throws SystemException {
		learningActivityResult = toUnwrappedModel(learningActivityResult);

		boolean isNew = learningActivityResult.isNew();

		LearningActivityResultModelImpl learningActivityResultModelImpl = (LearningActivityResultModelImpl)learningActivityResult;

		if (Validator.isNull(learningActivityResult.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			learningActivityResult.setUuid(uuid);
		}

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, learningActivityResult, merge);

			learningActivityResult.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !LearningActivityResultModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						learningActivityResultModelImpl.getOriginalUuid()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);

				args = new Object[] { learningActivityResultModelImpl.getUuid() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AP.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getOriginalPassed())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_AP, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AP,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getPassed())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_AP, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AP,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_APD.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getOriginalPassed()),
						
						learningActivityResultModelImpl.getOriginalEndDate()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_APD, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_APD,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getPassed()),
						
						learningActivityResultModelImpl.getEndDate()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_APD, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_APD,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AC.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_AC, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AC,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_AC, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AC,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USER.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USER, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USER,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USER, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USER,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDSTARTED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDSTARTED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDSTARTED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDSTARTED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDSTARTED,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDFINISHED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDFINISHED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDFINISHED,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getOriginalPassed()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getPassed()),
						Long.valueOf(learningActivityResultModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getOriginalPassed()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getPassed()),
						Long.valueOf(learningActivityResultModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getOriginalPassed())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Boolean.valueOf(learningActivityResultModelImpl.getPassed())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Long.valueOf(learningActivityResultModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED,
					args);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED,
					args);

				args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getActId()),
						Long.valueOf(learningActivityResultModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED,
					args);
			}
		}

		EntityCacheUtil.putResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
			LearningActivityResultImpl.class,
			learningActivityResult.getPrimaryKey(), learningActivityResult);

		if (isNew) {
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ACT_USER,
				new Object[] {
					Long.valueOf(learningActivityResult.getActId()),
					Long.valueOf(learningActivityResult.getUserId())
				}, learningActivityResult);

			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERIDACTID,
				new Object[] {
					Long.valueOf(learningActivityResult.getUserId()),
					Long.valueOf(learningActivityResult.getActId())
				}, learningActivityResult);
		}
		else {
			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_ACT_USER.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_ACT_USER, args);

				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_ACT_USER, args);

				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ACT_USER,
					new Object[] {
						Long.valueOf(learningActivityResult.getActId()),
						Long.valueOf(learningActivityResult.getUserId())
					}, learningActivityResult);
			}

			if ((learningActivityResultModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERIDACTID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(learningActivityResultModelImpl.getOriginalUserId()),
						Long.valueOf(learningActivityResultModelImpl.getOriginalActId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERIDACTID,
					args);

				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERIDACTID,
					args);

				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERIDACTID,
					new Object[] {
						Long.valueOf(learningActivityResult.getUserId()),
						Long.valueOf(learningActivityResult.getActId())
					}, learningActivityResult);
			}
		}

		return learningActivityResult;
	}

	protected LearningActivityResult toUnwrappedModel(
		LearningActivityResult learningActivityResult) {
		if (learningActivityResult instanceof LearningActivityResultImpl) {
			return learningActivityResult;
		}

		LearningActivityResultImpl learningActivityResultImpl = new LearningActivityResultImpl();

		learningActivityResultImpl.setNew(learningActivityResult.isNew());
		learningActivityResultImpl.setPrimaryKey(learningActivityResult.getPrimaryKey());

		learningActivityResultImpl.setUuid(learningActivityResult.getUuid());
		learningActivityResultImpl.setLarId(learningActivityResult.getLarId());
		learningActivityResultImpl.setActId(learningActivityResult.getActId());
		learningActivityResultImpl.setUserId(learningActivityResult.getUserId());
		learningActivityResultImpl.setResult(learningActivityResult.getResult());
		learningActivityResultImpl.setStartDate(learningActivityResult.getStartDate());
		learningActivityResultImpl.setEndDate(learningActivityResult.getEndDate());
		learningActivityResultImpl.setLatId(learningActivityResult.getLatId());
		learningActivityResultImpl.setComments(learningActivityResult.getComments());
		learningActivityResultImpl.setPassed(learningActivityResult.isPassed());

		return learningActivityResultImpl;
	}

	/**
	 * Returns the learning activity result with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the learning activity result
	 * @return the learning activity result
	 * @throws com.liferay.portal.NoSuchModelException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public LearningActivityResult findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the learning activity result with the primary key or throws a {@link com.liferay.lms.NoSuchLearningActivityResultException} if it could not be found.
	 *
	 * @param larId the primary key of the learning activity result
	 * @return the learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByPrimaryKey(long larId)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByPrimaryKey(larId);

		if (learningActivityResult == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + larId);
			}

			throw new NoSuchLearningActivityResultException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				larId);
		}

		return learningActivityResult;
	}

	/**
	 * Returns the learning activity result with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the learning activity result
	 * @return the learning activity result, or <code>null</code> if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public LearningActivityResult fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the learning activity result with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param larId the primary key of the learning activity result
	 * @return the learning activity result, or <code>null</code> if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByPrimaryKey(long larId)
		throws SystemException {
		LearningActivityResult learningActivityResult = (LearningActivityResult)EntityCacheUtil.getResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
				LearningActivityResultImpl.class, larId);

		if (learningActivityResult == _nullLearningActivityResult) {
			return null;
		}

		if (learningActivityResult == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				learningActivityResult = (LearningActivityResult)session.get(LearningActivityResultImpl.class,
						Long.valueOf(larId));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (learningActivityResult != null) {
					cacheResult(learningActivityResult);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(LearningActivityResultModelImpl.ENTITY_CACHE_ENABLED,
						LearningActivityResultImpl.class, larId,
						_nullLearningActivityResult);
				}

				closeSession(session);
			}
		}

		return learningActivityResult;
	}

	/**
	 * Returns all the learning activity results where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByUuid(String uuid)
		throws SystemException {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByUuid(String uuid, int start,
		int end) throws SystemException {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByUuid(String uuid, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid, start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if (!Validator.equals(uuid, learningActivityResult.getUuid())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else {
				if (uuid.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_UUID_UUID_3);
				}
				else {
					query.append(_FINDER_COLUMN_UUID_UUID_2);
				}
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (uuid != null) {
					qPos.add(uuid);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByUuid_First(String uuid,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByUuid_First(uuid,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByUuid_First(String uuid,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByUuid(uuid, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByUuid_Last(String uuid,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByUuid_Last(uuid,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByUuid_Last(String uuid,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUuid(uuid);

		List<LearningActivityResult> list = findByUuid(uuid, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where uuid = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByUuid_PrevAndNext(long larId,
		String uuid, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByUuid_PrevAndNext(session, learningActivityResult,
					uuid, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByUuid_PrevAndNext(session, learningActivityResult,
					uuid, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByUuid_PrevAndNext(Session session,
		LearningActivityResult learningActivityResult, String uuid,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		if (uuid == null) {
			query.append(_FINDER_COLUMN_UUID_UUID_1);
		}
		else {
			if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (uuid != null) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the learning activity result where actId = &#63; and userId = &#63; or throws a {@link com.liferay.lms.NoSuchLearningActivityResultException} if it could not be found.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByact_user(long actId, long userId)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByact_user(actId,
				userId);

		if (learningActivityResult == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("actId=");
			msg.append(actId);

			msg.append(", userId=");
			msg.append(userId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchLearningActivityResultException(msg.toString());
		}

		return learningActivityResult;
	}

	/**
	 * Returns the learning activity result where actId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByact_user(long actId, long userId)
		throws SystemException {
		return fetchByact_user(actId, userId, true);
	}

	/**
	 * Returns the learning activity result where actId = &#63; and userId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByact_user(long actId, long userId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_ACT_USER,
					finderArgs, this);
		}

		if (result instanceof LearningActivityResult) {
			LearningActivityResult learningActivityResult = (LearningActivityResult)result;

			if ((actId != learningActivityResult.getActId()) ||
					(userId != learningActivityResult.getUserId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACT_USER_ACTID_2);

			query.append(_FINDER_COLUMN_ACT_USER_USERID_2);

			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				List<LearningActivityResult> list = q.list();

				result = list;

				LearningActivityResult learningActivityResult = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ACT_USER,
						finderArgs, list);
				}
				else {
					learningActivityResult = list.get(0);

					cacheResult(learningActivityResult);

					if ((learningActivityResult.getActId() != actId) ||
							(learningActivityResult.getUserId() != userId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_ACT_USER,
							finderArgs, learningActivityResult);
					}
				}

				return learningActivityResult;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_ACT_USER,
						finderArgs);
				}

				closeSession(session);
			}
		}
		else {
			if (result instanceof List<?>) {
				return null;
			}
			else {
				return (LearningActivityResult)result;
			}
		}
	}

	/**
	 * Returns the learning activity result where userId = &#63; and actId = &#63; or throws a {@link com.liferay.lms.NoSuchLearningActivityResultException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param actId the act ID
	 * @return the matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByUserIdActId(long userId, long actId)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByUserIdActId(userId,
				actId);

		if (learningActivityResult == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append(", actId=");
			msg.append(actId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchLearningActivityResultException(msg.toString());
		}

		return learningActivityResult;
	}

	/**
	 * Returns the learning activity result where userId = &#63; and actId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param actId the act ID
	 * @return the matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByUserIdActId(long userId, long actId)
		throws SystemException {
		return fetchByUserIdActId(userId, actId, true);
	}

	/**
	 * Returns the learning activity result where userId = &#63; and actId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param actId the act ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByUserIdActId(long userId, long actId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { userId, actId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERIDACTID,
					finderArgs, this);
		}

		if (result instanceof LearningActivityResult) {
			LearningActivityResult learningActivityResult = (LearningActivityResult)result;

			if ((userId != learningActivityResult.getUserId()) ||
					(actId != learningActivityResult.getActId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_USERIDACTID_USERID_2);

			query.append(_FINDER_COLUMN_USERIDACTID_ACTID_2);

			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(actId);

				List<LearningActivityResult> list = q.list();

				result = list;

				LearningActivityResult learningActivityResult = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERIDACTID,
						finderArgs, list);
				}
				else {
					learningActivityResult = list.get(0);

					cacheResult(learningActivityResult);

					if ((learningActivityResult.getUserId() != userId) ||
							(learningActivityResult.getActId() != actId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERIDACTID,
							finderArgs, learningActivityResult);
					}
				}

				return learningActivityResult;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERIDACTID,
						finderArgs);
				}

				closeSession(session);
			}
		}
		else {
			if (result instanceof List<?>) {
				return null;
			}
			else {
				return (LearningActivityResult)result;
			}
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByap(long actId, boolean passed)
		throws SystemException {
		return findByap(actId, passed, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByap(long actId, boolean passed,
		int start, int end) throws SystemException {
		return findByap(actId, passed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByap(long actId, boolean passed,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AP;
			finderArgs = new Object[] { actId, passed };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_AP;
			finderArgs = new Object[] {
					actId, passed,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_AP_ACTID_2);

			query.append(_FINDER_COLUMN_AP_PASSED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByap_First(long actId, boolean passed,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByap_First(actId,
				passed, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByap_First(long actId, boolean passed,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByap(actId, passed, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByap_Last(long actId, boolean passed,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByap_Last(actId,
				passed, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByap_Last(long actId, boolean passed,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByap(actId, passed);

		List<LearningActivityResult> list = findByap(actId, passed, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByap_PrevAndNext(long larId,
		long actId, boolean passed, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByap_PrevAndNext(session, learningActivityResult,
					actId, passed, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByap_PrevAndNext(session, learningActivityResult,
					actId, passed, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByap_PrevAndNext(Session session,
		LearningActivityResult learningActivityResult, long actId,
		boolean passed, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_AP_ACTID_2);

		query.append(_FINDER_COLUMN_AP_PASSED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByapd(long actId, boolean passed,
		Date endDate) throws SystemException {
		return findByapd(actId, passed, endDate, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByapd(long actId, boolean passed,
		Date endDate, int start, int end) throws SystemException {
		return findByapd(actId, passed, endDate, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByapd(long actId, boolean passed,
		Date endDate, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_APD;
			finderArgs = new Object[] { actId, passed, endDate };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_APD;
			finderArgs = new Object[] {
					actId, passed, endDate,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						!Validator.equals(endDate,
							learningActivityResult.getEndDate())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_APD_ACTID_2);

			query.append(_FINDER_COLUMN_APD_PASSED_2);

			if (endDate == null) {
				query.append(_FINDER_COLUMN_APD_ENDDATE_1);
			}
			else {
				query.append(_FINDER_COLUMN_APD_ENDDATE_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (endDate != null) {
					qPos.add(CalendarUtil.getTimestamp(endDate));
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByapd_First(long actId, boolean passed,
		Date endDate, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByapd_First(actId,
				passed, endDate, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", endDate=");
		msg.append(endDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByapd_First(long actId, boolean passed,
		Date endDate, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByapd(actId, passed, endDate,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByapd_Last(long actId, boolean passed,
		Date endDate, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByapd_Last(actId,
				passed, endDate, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", endDate=");
		msg.append(endDate);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByapd_Last(long actId, boolean passed,
		Date endDate, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByapd(actId, passed, endDate);

		List<LearningActivityResult> list = findByapd(actId, passed, endDate,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByapd_PrevAndNext(long larId,
		long actId, boolean passed, Date endDate,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByapd_PrevAndNext(session, learningActivityResult,
					actId, passed, endDate, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByapd_PrevAndNext(session, learningActivityResult,
					actId, passed, endDate, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByapd_PrevAndNext(Session session,
		LearningActivityResult learningActivityResult, long actId,
		boolean passed, Date endDate, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_APD_ACTID_2);

		query.append(_FINDER_COLUMN_APD_PASSED_2);

		if (endDate == null) {
			query.append(_FINDER_COLUMN_APD_ENDDATE_1);
		}
		else {
			query.append(_FINDER_COLUMN_APD_ENDDATE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		if (endDate != null) {
			qPos.add(CalendarUtil.getTimestamp(endDate));
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByac(long actId)
		throws SystemException {
		return findByac(actId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByac(long actId, int start, int end)
		throws SystemException {
		return findByac(actId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByac(long actId, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_AC;
			finderArgs = new Object[] { actId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_AC;
			finderArgs = new Object[] { actId, start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_AC_ACTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByac_First(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByac_First(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByac_First(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByac(actId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByac_Last(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByac_Last(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByac_Last(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByac(actId);

		List<LearningActivityResult> list = findByac(actId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByac_PrevAndNext(long larId,
		long actId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByac_PrevAndNext(session, learningActivityResult,
					actId, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByac_PrevAndNext(session, learningActivityResult,
					actId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByac_PrevAndNext(Session session,
		LearningActivityResult learningActivityResult, long actId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_AC_ACTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByuser(long userId)
		throws SystemException {
		return findByuser(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByuser(long userId, int start,
		int end) throws SystemException {
		return findByuser(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByuser(long userId, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USER;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USER;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_USER_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByuser_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByuser_First(userId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByuser_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByuser(userId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByuser_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByuser_Last(userId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByuser_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByuser(userId);

		List<LearningActivityResult> list = findByuser(userId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where userId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByuser_PrevAndNext(long larId,
		long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByuser_PrevAndNext(session, learningActivityResult,
					userId, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByuser_PrevAndNext(session, learningActivityResult,
					userId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByuser_PrevAndNext(Session session,
		LearningActivityResult learningActivityResult, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_USER_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long userId) throws SystemException {
		return findByActIdNotMultipleUserId(actId, userId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long userId, int start, int end) throws SystemException {
		return findByActIdNotMultipleUserId(actId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERID;
		finderArgs = new Object[] { actId, userId, start, end, orderByComparator };

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserId_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserId_First(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserId_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdNotMultipleUserId(actId,
				userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserId_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserId_Last(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserId_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdNotMultipleUserId(actId, userId);

		List<LearningActivityResult> list = findByActIdNotMultipleUserId(actId,
				userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdNotMultipleUserId_PrevAndNext(
		long larId, long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdNotMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdNotMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdNotMultipleUserId_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, long userId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long[] userIds) throws SystemException {
		return findByActIdNotMultipleUserId(actId, userIds, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdNotMultipleUserId(actId, userIds, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserId(
		long actId, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERID;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long userId) throws SystemException {
		return findByActIdNotMultipleUserIdStarted(actId, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long userId, int start, int end) throws SystemException {
		return findByActIdNotMultipleUserIdStarted(actId, userId, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDSTARTED;
		finderArgs = new Object[] { actId, userId, start, end, orderByComparator };

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserIdStarted_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserIdStarted_First(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserIdStarted_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdNotMultipleUserIdStarted(actId,
				userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserIdStarted_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserIdStarted_Last(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserIdStarted_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdNotMultipleUserIdStarted(actId, userId);

		List<LearningActivityResult> list = findByActIdNotMultipleUserIdStarted(actId,
				userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdNotMultipleUserIdStarted_PrevAndNext(
		long larId, long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdNotMultipleUserIdStarted_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdNotMultipleUserIdStarted_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdNotMultipleUserIdStarted_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, long userId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long[] userIds) throws SystemException {
		return findByActIdNotMultipleUserIdStarted(actId, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdNotMultipleUserIdStarted(actId, userIds, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdStarted(
		long actId, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDSTARTED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdStarted(long actId)
		throws SystemException {
		return findByActIdStarted(actId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdStarted(long actId,
		int start, int end) throws SystemException {
		return findByActIdStarted(actId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdStarted(long actId,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDSTARTED;
			finderArgs = new Object[] { actId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDSTARTED;
			finderArgs = new Object[] { actId, start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDSTARTED_ACTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdStarted_First(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdStarted_First(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdStarted_First(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdStarted(actId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdStarted_Last(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdStarted_Last(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdStarted_Last(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdStarted(actId);

		List<LearningActivityResult> list = findByActIdStarted(actId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdStarted_PrevAndNext(long larId,
		long actId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdStarted_PrevAndNext(session,
					learningActivityResult, actId, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdStarted_PrevAndNext(session,
					learningActivityResult, actId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdStarted_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDSTARTED_ACTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long userId) throws SystemException {
		return findByActIdPassedNotMultipleUserId(actId, passed, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long userId, int start, int end)
		throws SystemException {
		return findByActIdPassedNotMultipleUserId(actId, passed, userId, start,
			end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERID;
		finderArgs = new Object[] {
				actId, passed, userId,
				
				start, end, orderByComparator
			};

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedNotMultipleUserId_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedNotMultipleUserId_First(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedNotMultipleUserId_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdPassedNotMultipleUserId(actId,
				passed, userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedNotMultipleUserId_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedNotMultipleUserId_Last(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedNotMultipleUserId_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdPassedNotMultipleUserId(actId, passed, userId);

		List<LearningActivityResult> list = findByActIdPassedNotMultipleUserId(actId,
				passed, userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdPassedNotMultipleUserId_PrevAndNext(
		long larId, long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdPassedNotMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdPassedNotMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdPassedNotMultipleUserId_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long[] userIds) throws SystemException {
		return findByActIdPassedNotMultipleUserId(actId, passed, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdPassedNotMultipleUserId(actId, passed, userIds,
			start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserId(
		long actId, boolean passed, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERID;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, passed, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, passed, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long userId) throws SystemException {
		return findByActIdNotMultipleUserIdFinished(actId, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long userId, int start, int end) throws SystemException {
		return findByActIdNotMultipleUserIdFinished(actId, userId, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDFINISHED;
		finderArgs = new Object[] { actId, userId, start, end, orderByComparator };

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserIdFinished_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserIdFinished_First(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserIdFinished_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdNotMultipleUserIdFinished(actId,
				userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdNotMultipleUserIdFinished_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdNotMultipleUserIdFinished_Last(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdNotMultipleUserIdFinished_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdNotMultipleUserIdFinished(actId, userId);

		List<LearningActivityResult> list = findByActIdNotMultipleUserIdFinished(actId,
				userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdNotMultipleUserIdFinished_PrevAndNext(
		long larId, long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdNotMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdNotMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdNotMultipleUserIdFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, long userId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long[] userIds) throws SystemException {
		return findByActIdNotMultipleUserIdFinished(actId, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdNotMultipleUserIdFinished(actId, userIds, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdNotMultipleUserIdFinished(
		long actId, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDNOTMULTIPLEUSERIDFINISHED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdFinished(long actId)
		throws SystemException {
		return findByActIdFinished(actId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdFinished(long actId,
		int start, int end) throws SystemException {
		return findByActIdFinished(actId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdFinished(long actId,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDFINISHED;
			finderArgs = new Object[] { actId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDFINISHED;
			finderArgs = new Object[] { actId, start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDFINISHED_ACTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdFinished_First(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdFinished_First(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdFinished_First(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdFinished(actId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdFinished_Last(long actId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdFinished_Last(actId,
				orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdFinished_Last(long actId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdFinished(actId);

		List<LearningActivityResult> list = findByActIdFinished(actId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdFinished_PrevAndNext(
		long larId, long actId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdFinished_PrevAndNext(session,
					learningActivityResult, actId, orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdFinished_PrevAndNext(session,
					learningActivityResult, actId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDFINISHED_ACTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long userId) throws SystemException {
		return findByActIdPassedNotMultipleUserIdFinished(actId, passed,
			userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long userId, int start, int end)
		throws SystemException {
		return findByActIdPassedNotMultipleUserIdFinished(actId, passed,
			userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED;
		finderArgs = new Object[] {
				actId, passed, userId,
				
				start, end, orderByComparator
			};

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedNotMultipleUserIdFinished_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedNotMultipleUserIdFinished_First(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedNotMultipleUserIdFinished_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdPassedNotMultipleUserIdFinished(actId,
				passed, userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedNotMultipleUserIdFinished_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedNotMultipleUserIdFinished_Last(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedNotMultipleUserIdFinished_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdPassedNotMultipleUserIdFinished(actId, passed,
				userId);

		List<LearningActivityResult> list = findByActIdPassedNotMultipleUserIdFinished(actId,
				passed, userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdPassedNotMultipleUserIdFinished_PrevAndNext(
		long larId, long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdPassedNotMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdPassedNotMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdPassedNotMultipleUserIdFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds) throws SystemException {
		return findByActIdPassedNotMultipleUserIdFinished(actId, passed,
			userIds, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdPassedNotMultipleUserIdFinished(actId, passed,
			userIds, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedNotMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, passed, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, passed, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long userId) throws SystemException {
		return findByActIdPassedMultipleUserId(actId, passed, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long userId, int start, int end)
		throws SystemException {
		return findByActIdPassedMultipleUserId(actId, passed, userId, start,
			end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID;
			finderArgs = new Object[] { actId, passed, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID;
			finderArgs = new Object[] {
					actId, passed, userId,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedMultipleUserId_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedMultipleUserId_First(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedMultipleUserId_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdPassedMultipleUserId(actId,
				passed, userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedMultipleUserId_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedMultipleUserId_Last(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedMultipleUserId_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdPassedMultipleUserId(actId, passed, userId);

		List<LearningActivityResult> list = findByActIdPassedMultipleUserId(actId,
				passed, userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdPassedMultipleUserId_PrevAndNext(
		long larId, long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdPassedMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdPassedMultipleUserId_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdPassedMultipleUserId_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long[] userIds) throws SystemException {
		return findByActIdPassedMultipleUserId(actId, passed, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdPassedMultipleUserId(actId, passed, userIds, start,
			end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserId(
		long actId, boolean passed, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERID;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, passed, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, passed, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long userId) throws SystemException {
		return findByActIdPassedMultipleUserIdFinished(actId, passed, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long userId, int start, int end)
		throws SystemException {
		return findByActIdPassedMultipleUserIdFinished(actId, passed, userId,
			start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED;
			finderArgs = new Object[] { actId, passed, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED;
			finderArgs = new Object[] {
					actId, passed, userId,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedMultipleUserIdFinished_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedMultipleUserIdFinished_First(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedMultipleUserIdFinished_First(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<LearningActivityResult> list = findByActIdPassedMultipleUserIdFinished(actId,
				passed, userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedMultipleUserIdFinished_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedMultipleUserIdFinished_Last(actId,
				passed, userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedMultipleUserIdFinished_Last(
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByActIdPassedMultipleUserIdFinished(actId, passed,
				userId);

		List<LearningActivityResult> list = findByActIdPassedMultipleUserIdFinished(actId,
				passed, userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdPassedMultipleUserIdFinished_PrevAndNext(
		long larId, long actId, boolean passed, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdPassedMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, true);

			array[1] = learningActivityResult;

			array[2] = getByActIdPassedMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdPassedMultipleUserIdFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, boolean passed, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds) throws SystemException {
		return findByActIdPassedMultipleUserIdFinished(actId, passed, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdPassedMultipleUserIdFinished(actId, passed, userIds,
			start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedMultipleUserIdFinished(
		long actId, boolean passed, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, passed, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, passed, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedFinished(long actId,
		boolean passed) throws SystemException {
		return findByActIdPassedFinished(actId, passed, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedFinished(long actId,
		boolean passed, int start, int end) throws SystemException {
		return findByActIdPassedFinished(actId, passed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdPassedFinished(long actId,
		boolean passed, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED;
			finderArgs = new Object[] { actId, passed };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDPASSEDFINISHED;
			finderArgs = new Object[] {
					actId, passed,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(passed != learningActivityResult.getPassed())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_PASSED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedFinished_First(long actId,
		boolean passed, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedFinished_First(actId,
				passed, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedFinished_First(long actId,
		boolean passed, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdPassedFinished(actId,
				passed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdPassedFinished_Last(long actId,
		boolean passed, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdPassedFinished_Last(actId,
				passed, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", passed=");
		msg.append(passed);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdPassedFinished_Last(long actId,
		boolean passed, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdPassedFinished(actId, passed);

		List<LearningActivityResult> list = findByActIdPassedFinished(actId,
				passed, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and passed = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param passed the passed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdPassedFinished_PrevAndNext(
		long larId, long actId, boolean passed,
		OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdPassedFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdPassedFinished_PrevAndNext(session,
					learningActivityResult, actId, passed, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdPassedFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, boolean passed, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_PASSED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(passed);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long userId) throws SystemException {
		return findByActIdMultipleUserIdStarted(actId, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long userId, int start, int end) throws SystemException {
		return findByActIdMultipleUserIdStarted(actId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED;
			finderArgs = new Object[] { actId, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED;
			finderArgs = new Object[] {
					actId, userId,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdMultipleUserIdStarted_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdMultipleUserIdStarted_First(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdMultipleUserIdStarted_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdMultipleUserIdStarted(actId,
				userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdMultipleUserIdStarted_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdMultipleUserIdStarted_Last(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdMultipleUserIdStarted_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdMultipleUserIdStarted(actId, userId);

		List<LearningActivityResult> list = findByActIdMultipleUserIdStarted(actId,
				userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdMultipleUserIdStarted_PrevAndNext(
		long larId, long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdMultipleUserIdStarted_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdMultipleUserIdStarted_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdMultipleUserIdStarted_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, long userId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long[] userIds) throws SystemException {
		return findByActIdMultipleUserIdStarted(actId, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdMultipleUserIdStarted(actId, userIds, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdStarted(
		long actId, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDSTARTED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long userId) throws SystemException {
		return findByActIdMultipleUserIdFinished(actId, userId,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long userId, int start, int end) throws SystemException {
		return findByActIdMultipleUserIdFinished(actId, userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED;
			finderArgs = new Object[] { actId, userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED;
			finderArgs = new Object[] {
					actId, userId,
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						(userId != learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdMultipleUserIdFinished_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdMultipleUserIdFinished_First(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the first learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdMultipleUserIdFinished_First(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		List<LearningActivityResult> list = findByActIdMultipleUserIdFinished(actId,
				userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult findByActIdMultipleUserIdFinished_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = fetchByActIdMultipleUserIdFinished_Last(actId,
				userId, orderByComparator);

		if (learningActivityResult != null) {
			return learningActivityResult;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("actId=");
		msg.append(actId);

		msg.append(", userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchLearningActivityResultException(msg.toString());
	}

	/**
	 * Returns the last learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching learning activity result, or <code>null</code> if a matching learning activity result could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult fetchByActIdMultipleUserIdFinished_Last(
		long actId, long userId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByActIdMultipleUserIdFinished(actId, userId);

		List<LearningActivityResult> list = findByActIdMultipleUserIdFinished(actId,
				userId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the learning activity results before and after the current learning activity result in the ordered set where actId = &#63; and userId = &#63;.
	 *
	 * @param larId the primary key of the current learning activity result
	 * @param actId the act ID
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next learning activity result
	 * @throws com.liferay.lms.NoSuchLearningActivityResultException if a learning activity result with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult[] findByActIdMultipleUserIdFinished_PrevAndNext(
		long larId, long actId, long userId, OrderByComparator orderByComparator)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByPrimaryKey(larId);

		Session session = null;

		try {
			session = openSession();

			LearningActivityResult[] array = new LearningActivityResultImpl[3];

			array[0] = getByActIdMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					true);

			array[1] = learningActivityResult;

			array[2] = getByActIdMultipleUserIdFinished_PrevAndNext(session,
					learningActivityResult, actId, userId, orderByComparator,
					false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected LearningActivityResult getByActIdMultipleUserIdFinished_PrevAndNext(
		Session session, LearningActivityResult learningActivityResult,
		long actId, long userId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

		query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_2);

		query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(actId);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(learningActivityResult);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<LearningActivityResult> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long[] userIds) throws SystemException {
		return findByActIdMultipleUserIdFinished(actId, userIds,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long[] userIds, int start, int end)
		throws SystemException {
		return findByActIdMultipleUserIdFinished(actId, userIds, start, end,
			null);
	}

	/**
	 * Returns an ordered range of all the learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findByActIdMultipleUserIdFinished(
		long actId, long[] userIds, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_ACTIDMULTIPLEUSERIDFINISHED;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderArgs = new Object[] { actId, StringUtil.merge(userIds) };
		}
		else {
			finderArgs = new Object[] {
					actId, StringUtil.merge(userIds),
					
					start, end, orderByComparator
				};
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (LearningActivityResult learningActivityResult : list) {
				if ((actId != learningActivityResult.getActId()) ||
						!ArrayUtil.contains(userIds,
							learningActivityResult.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				list = (List<LearningActivityResult>)QueryUtil.list(q,
						getDialect(), start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns all the learning activity results.
	 *
	 * @return the learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the learning activity results.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @return the range of learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the learning activity results.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of learning activity results
	 * @param end the upper bound of the range of learning activity results (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public List<LearningActivityResult> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = new Object[] { start, end, orderByComparator };

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<LearningActivityResult> list = (List<LearningActivityResult>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_LEARNINGACTIVITYRESULT);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_LEARNINGACTIVITYRESULT.concat(LearningActivityResultModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<LearningActivityResult>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<LearningActivityResult>)QueryUtil.list(q,
							getDialect(), start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the learning activity results where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByUuid(String uuid) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByUuid(uuid)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes the learning activity result where actId = &#63; and userId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the learning activity result that was removed
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult removeByact_user(long actId, long userId)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByact_user(actId,
				userId);

		return remove(learningActivityResult);
	}

	/**
	 * Removes the learning activity result where userId = &#63; and actId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param actId the act ID
	 * @return the learning activity result that was removed
	 * @throws SystemException if a system exception occurred
	 */
	public LearningActivityResult removeByUserIdActId(long userId, long actId)
		throws NoSuchLearningActivityResultException, SystemException {
		LearningActivityResult learningActivityResult = findByUserIdActId(userId,
				actId);

		return remove(learningActivityResult);
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByap(long actId, boolean passed)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByap(actId,
				passed)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; and endDate = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByapd(long actId, boolean passed, Date endDate)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByapd(actId,
				passed, endDate)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByac(long actId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByac(actId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByuser(long userId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByuser(userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and userId &ne; &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdNotMultipleUserId(long actId, long userId)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdNotMultipleUserId(
				actId, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and userId &ne; &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdNotMultipleUserIdStarted(long actId, long userId)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdNotMultipleUserIdStarted(
				actId, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdStarted(long actId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdStarted(
				actId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdPassedNotMultipleUserId(long actId,
		boolean passed, long userId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdPassedNotMultipleUserId(
				actId, passed, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and userId &ne; &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdNotMultipleUserIdFinished(long actId, long userId)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdNotMultipleUserIdFinished(
				actId, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdFinished(long actId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdFinished(
				actId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdPassedNotMultipleUserIdFinished(long actId,
		boolean passed, long userId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdPassedNotMultipleUserIdFinished(
				actId, passed, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdPassedMultipleUserId(long actId, boolean passed,
		long userId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdPassedMultipleUserId(
				actId, passed, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; and userId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdPassedMultipleUserIdFinished(long actId,
		boolean passed, long userId) throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdPassedMultipleUserIdFinished(
				actId, passed, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and passed = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdPassedFinished(long actId, boolean passed)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdPassedFinished(
				actId, passed)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and userId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdMultipleUserIdStarted(long actId, long userId)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdMultipleUserIdStarted(
				actId, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results where actId = &#63; and userId = &#63; from the database.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByActIdMultipleUserIdFinished(long actId, long userId)
		throws SystemException {
		for (LearningActivityResult learningActivityResult : findByActIdMultipleUserIdFinished(
				actId, userId)) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Removes all the learning activity results from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (LearningActivityResult learningActivityResult : findAll()) {
			remove(learningActivityResult);
		}
	}

	/**
	 * Returns the number of learning activity results where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUuid(String uuid) throws SystemException {
		Object[] finderArgs = new Object[] { uuid };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_UUID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else {
				if (uuid.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_UUID_UUID_3);
				}
				else {
					query.append(_FINDER_COLUMN_UUID_UUID_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (uuid != null) {
					qPos.add(uuid);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByact_user(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACT_USER,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACT_USER_ACTID_2);

			query.append(_FINDER_COLUMN_ACT_USER_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACT_USER,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where userId = &#63; and actId = &#63;.
	 *
	 * @param userId the user ID
	 * @param actId the act ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUserIdActId(long userId, long actId)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, actId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERIDACTID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_USERIDACTID_USERID_2);

			query.append(_FINDER_COLUMN_USERIDACTID_ACTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(actId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERIDACTID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByap(long actId, boolean passed) throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_AP,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_AP_ACTID_2);

			query.append(_FINDER_COLUMN_AP_PASSED_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_AP, finderArgs,
					count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and endDate = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param endDate the end date
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByapd(long actId, boolean passed, Date endDate)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed, endDate };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_APD,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_APD_ACTID_2);

			query.append(_FINDER_COLUMN_APD_PASSED_2);

			if (endDate == null) {
				query.append(_FINDER_COLUMN_APD_ENDDATE_1);
			}
			else {
				query.append(_FINDER_COLUMN_APD_ENDDATE_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (endDate != null) {
					qPos.add(CalendarUtil.getTimestamp(endDate));
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_APD, finderArgs,
					count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByac(long actId) throws SystemException {
		Object[] finderArgs = new Object[] { actId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_AC,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_AC_ACTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_AC, finderArgs,
					count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByuser(long userId) throws SystemException {
		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USER,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_USER_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USER,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserId(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserId(long actId, long[] userIds)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, StringUtil.merge(userIds) };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserIdStarted(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDSTARTED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDSTARTED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserIdStarted(long actId, long[] userIds)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, StringUtil.merge(userIds) };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDSTARTED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDSTARTED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdStarted(long actId) throws SystemException {
		Object[] finderArgs = new Object[] { actId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDSTARTED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDSTARTED_ACTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDSTARTED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedNotMultipleUserId(long actId, boolean passed,
		long userId) throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedNotMultipleUserId(long actId, boolean passed,
		long[] userIds) throws SystemException {
		Object[] finderArgs = new Object[] {
				actId, passed, StringUtil.merge(userIds)
			};

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserIdFinished(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId &ne; all &#63;.
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdNotMultipleUserIdFinished(long actId, long[] userIds)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, StringUtil.merge(userIds) };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDNOTMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63;.
	 *
	 * @param actId the act ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdFinished(long actId) throws SystemException {
		Object[] finderArgs = new Object[] { actId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDFINISHED_ACTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId &ne; &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedNotMultipleUserIdFinished(long actId,
		boolean passed, long userId) throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId &ne; all &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedNotMultipleUserIdFinished(long actId,
		boolean passed, long[] userIds) throws SystemException {
		Object[] finderArgs = new Object[] {
				actId, passed, StringUtil.merge(userIds)
			};

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_AND);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedMultipleUserId(long actId, boolean passed,
		long userId) throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedMultipleUserId(long actId, boolean passed,
		long[] userIds) throws SystemException {
		Object[] finderArgs = new Object[] {
				actId, passed, StringUtil.merge(userIds)
			};

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedMultipleUserIdFinished(long actId,
		boolean passed, long userId) throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63; and userId = any &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedMultipleUserIdFinished(long actId,
		boolean passed, long[] userIds) throws SystemException {
		Object[] finderArgs = new Object[] {
				actId, passed, StringUtil.merge(userIds)
			};

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDPASSEDMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and passed = &#63;.
	 *
	 * @param actId the act ID
	 * @param passed the passed
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdPassedFinished(long actId, boolean passed)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, passed };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDPASSEDFINISHED_PASSED_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(passed);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDPASSEDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdMultipleUserIdStarted(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdMultipleUserIdStarted(long actId, long[] userIds)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, StringUtil.merge(userIds) };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDSTARTED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId = &#63;.
	 *
	 * @param actId the act ID
	 * @param userId the user ID
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdMultipleUserIdFinished(long actId, long userId)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_2);

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results where actId = &#63; and userId = any &#63;.
	 *
	 * @param actId the act ID
	 * @param userIds the user IDs
	 * @return the number of matching learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countByActIdMultipleUserIdFinished(long actId, long[] userIds)
		throws SystemException {
		Object[] finderArgs = new Object[] { actId, StringUtil.merge(userIds) };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler();

			query.append(_SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE);

			boolean conjunctionable = false;

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_5);

			conjunctionable = true;

			if ((userIds == null) || (userIds.length > 0)) {
				if (conjunctionable) {
					query.append(WHERE_AND);
				}

				query.append(StringPool.OPEN_PARENTHESIS);

				for (int i = 0; i < userIds.length; i++) {
					query.append(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_5);

					if ((i + 1) < userIds.length) {
						query.append(WHERE_OR);
					}
				}

				query.append(StringPool.CLOSE_PARENTHESIS);

				conjunctionable = true;
			}

			if (conjunctionable) {
				query.append(WHERE_AND);
			}

			query.append("learningActivityResult.endDate IS NOT null");

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(actId);

				if (userIds != null) {
					qPos.add(userIds);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_WITH_PAGINATION_COUNT_BY_ACTIDMULTIPLEUSERIDFINISHED,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of learning activity results.
	 *
	 * @return the number of learning activity results
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_LEARNINGACTIVITYRESULT);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the learning activity result persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.liferay.lms.model.LearningActivityResult")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<LearningActivityResult>> listenersList = new ArrayList<ModelListener<LearningActivityResult>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<LearningActivityResult>)InstanceFactory.newInstance(
							listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(LearningActivityResultImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = ActivityTriesDeletedPersistence.class)
	protected ActivityTriesDeletedPersistence activityTriesDeletedPersistence;
	@BeanReference(type = AsynchronousProcessAuditPersistence.class)
	protected AsynchronousProcessAuditPersistence asynchronousProcessAuditPersistence;
	@BeanReference(type = AuditEntryPersistence.class)
	protected AuditEntryPersistence auditEntryPersistence;
	@BeanReference(type = CheckP2pMailingPersistence.class)
	protected CheckP2pMailingPersistence checkP2pMailingPersistence;
	@BeanReference(type = CompetencePersistence.class)
	protected CompetencePersistence competencePersistence;
	@BeanReference(type = CoursePersistence.class)
	protected CoursePersistence coursePersistence;
	@BeanReference(type = CourseCompetencePersistence.class)
	protected CourseCompetencePersistence courseCompetencePersistence;
	@BeanReference(type = CourseResultPersistence.class)
	protected CourseResultPersistence courseResultPersistence;
	@BeanReference(type = CourseTypePersistence.class)
	protected CourseTypePersistence courseTypePersistence;
	@BeanReference(type = CourseTypeCalificationTypePersistence.class)
	protected CourseTypeCalificationTypePersistence courseTypeCalificationTypePersistence;
	@BeanReference(type = CourseTypeCourseEvalPersistence.class)
	protected CourseTypeCourseEvalPersistence courseTypeCourseEvalPersistence;
	@BeanReference(type = CourseTypeInscriptionTypePersistence.class)
	protected CourseTypeInscriptionTypePersistence courseTypeInscriptionTypePersistence;
	@BeanReference(type = CourseTypeLearningActivityPersistence.class)
	protected CourseTypeLearningActivityPersistence courseTypeLearningActivityPersistence;
	@BeanReference(type = CourseTypeTemplatePersistence.class)
	protected CourseTypeTemplatePersistence courseTypeTemplatePersistence;
	@BeanReference(type = InappropiatePersistence.class)
	protected InappropiatePersistence inappropiatePersistence;
	@BeanReference(type = LearningActivityPersistence.class)
	protected LearningActivityPersistence learningActivityPersistence;
	@BeanReference(type = LearningActivityResultPersistence.class)
	protected LearningActivityResultPersistence learningActivityResultPersistence;
	@BeanReference(type = LearningActivityTryPersistence.class)
	protected LearningActivityTryPersistence learningActivityTryPersistence;
	@BeanReference(type = LmsPrefsPersistence.class)
	protected LmsPrefsPersistence lmsPrefsPersistence;
	@BeanReference(type = ModulePersistence.class)
	protected ModulePersistence modulePersistence;
	@BeanReference(type = ModuleResultPersistence.class)
	protected ModuleResultPersistence moduleResultPersistence;
	@BeanReference(type = P2pActivityPersistence.class)
	protected P2pActivityPersistence p2pActivityPersistence;
	@BeanReference(type = P2pActivityCorrectionsPersistence.class)
	protected P2pActivityCorrectionsPersistence p2pActivityCorrectionsPersistence;
	@BeanReference(type = SchedulePersistence.class)
	protected SchedulePersistence schedulePersistence;
	@BeanReference(type = SurveyResultPersistence.class)
	protected SurveyResultPersistence surveyResultPersistence;
	@BeanReference(type = TestAnswerPersistence.class)
	protected TestAnswerPersistence testAnswerPersistence;
	@BeanReference(type = TestQuestionPersistence.class)
	protected TestQuestionPersistence testQuestionPersistence;
	@BeanReference(type = UserCertificateDownloadPersistence.class)
	protected UserCertificateDownloadPersistence userCertificateDownloadPersistence;
	@BeanReference(type = UserCompetencePersistence.class)
	protected UserCompetencePersistence userCompetencePersistence;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static final String _SQL_SELECT_LEARNINGACTIVITYRESULT = "SELECT learningActivityResult FROM LearningActivityResult learningActivityResult";
	private static final String _SQL_SELECT_LEARNINGACTIVITYRESULT_WHERE = "SELECT learningActivityResult FROM LearningActivityResult learningActivityResult WHERE ";
	private static final String _SQL_COUNT_LEARNINGACTIVITYRESULT = "SELECT COUNT(learningActivityResult) FROM LearningActivityResult learningActivityResult";
	private static final String _SQL_COUNT_LEARNINGACTIVITYRESULT_WHERE = "SELECT COUNT(learningActivityResult) FROM LearningActivityResult learningActivityResult WHERE ";
	private static final String _FINDER_COLUMN_UUID_UUID_1 = "learningActivityResult.uuid IS NULL";
	private static final String _FINDER_COLUMN_UUID_UUID_2 = "learningActivityResult.uuid = ?";
	private static final String _FINDER_COLUMN_UUID_UUID_3 = "(learningActivityResult.uuid IS NULL OR learningActivityResult.uuid = ?)";
	private static final String _FINDER_COLUMN_ACT_USER_ACTID_2 = "learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACT_USER_USERID_2 = "learningActivityResult.userId = ?";
	private static final String _FINDER_COLUMN_USERIDACTID_USERID_2 = "learningActivityResult.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERIDACTID_ACTID_2 = "learningActivityResult.actId = ?";
	private static final String _FINDER_COLUMN_AP_ACTID_2 = "learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_AP_PASSED_2 = "learningActivityResult.passed = ?";
	private static final String _FINDER_COLUMN_APD_ACTID_2 = "learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_APD_PASSED_2 = "learningActivityResult.passed = ? AND ";
	private static final String _FINDER_COLUMN_APD_ENDDATE_1 = "learningActivityResult.endDate IS NULL";
	private static final String _FINDER_COLUMN_APD_ENDDATE_2 = "learningActivityResult.endDate = ?";
	private static final String _FINDER_COLUMN_AC_ACTID_2 = "learningActivityResult.actId = ?";
	private static final String _FINDER_COLUMN_USER_USERID_2 = "learningActivityResult.userId = ?";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_2 = "learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_5 = "(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_2 = "learningActivityResult.userId != ?";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_5 = "(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERID_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_2 =
		"learningActivityResult.userId != ?";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDSTARTED_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDSTARTED_ACTID_2 = "learningActivityResult.actId = ? AND learningActivityResult.startDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_2 =
		"learningActivityResult.passed = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_PASSED_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_2 =
		"learningActivityResult.userId != ?";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERID_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_2 =
		"learningActivityResult.userId != ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDNOTMULTIPLEUSERIDFINISHED_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDFINISHED_ACTID_2 = "learningActivityResult.actId = ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_2 =
		"learningActivityResult.passed = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_PASSED_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_2 =
		"learningActivityResult.userId != ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDNOTMULTIPLEUSERIDFINISHED_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_2 =
		"learningActivityResult.passed = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_PASSED_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_2 =
		"learningActivityResult.userId = ?";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERID_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_2 =
		"learningActivityResult.passed = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_PASSED_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_2 =
		"learningActivityResult.userId = ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDPASSEDMULTIPLEUSERIDFINISHED_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDPASSEDFINISHED_ACTID_2 = "learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDPASSEDFINISHED_PASSED_2 = "learningActivityResult.passed = ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_2 =
		"learningActivityResult.userId = ?";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDSTARTED_USERID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_2 =
		"learningActivityResult.actId = ? AND ";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_ACTID_2) +
		")";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_2 =
		"learningActivityResult.userId = ? AND learningActivityResult.endDate IS NOT null";
	private static final String _FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_5 =
		"(" +
		_removeConjunction(_FINDER_COLUMN_ACTIDMULTIPLEUSERIDFINISHED_USERID_2) +
		")";

	private static String _removeConjunction(String sql) {
		int pos = sql.indexOf(" AND ");

		if (pos != -1) {
			sql = sql.substring(0, pos);
		}

		return sql;
	}

	private static final String _ORDER_BY_ENTITY_ALIAS = "learningActivityResult.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No LearningActivityResult exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No LearningActivityResult exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(LearningActivityResultPersistenceImpl.class);
	private static LearningActivityResult _nullLearningActivityResult = new LearningActivityResultImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<LearningActivityResult> toCacheModel() {
				return _nullLearningActivityResultCacheModel;
			}
		};

	private static CacheModel<LearningActivityResult> _nullLearningActivityResultCacheModel =
		new CacheModel<LearningActivityResult>() {
			public LearningActivityResult toEntityModel() {
				return _nullLearningActivityResult;
			}
		};
}