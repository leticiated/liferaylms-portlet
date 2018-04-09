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

import com.liferay.lms.NoSuchInappropiateException;
import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.model.impl.InappropiateImpl;
import com.liferay.lms.model.impl.InappropiateModelImpl;

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
import java.util.List;

/**
 * The persistence implementation for the inappropiate service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author TLS
 * @see InappropiatePersistence
 * @see InappropiateUtil
 * @generated
 */
public class InappropiatePersistenceImpl extends BasePersistenceImpl<Inappropiate>
	implements InappropiatePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link InappropiateUtil} to access the inappropiate persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = InappropiateImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] { String.class.getName() },
			InappropiateModelImpl.UUID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_FETCH_BY_UUID_G = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
			new String[] { String.class.getName(), Long.class.getName() },
			InappropiateModelImpl.UUID_COLUMN_BITMASK |
			InappropiateModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID_G = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] { String.class.getName(), Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] { Long.class.getName() },
			InappropiateModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPID = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_CLASSPK = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByClassPK",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSPK =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByClassPK",
			new String[] { Long.class.getName() },
			InappropiateModelImpl.CLASSPK_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_CLASSPK = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByClassPK",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_CLASSNAME =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByClassName",
			new String[] {
				String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSNAME =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByClassName",
			new String[] { String.class.getName() },
			InappropiateModelImpl.CLASSNAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_CLASSNAME = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByClassName",
			new String[] { String.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPIDCLASSNAME =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupIdClassName",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPIDCLASSNAME =
		new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByGroupIdClassName",
			new String[] { Long.class.getName(), String.class.getName() },
			InappropiateModelImpl.GROUPID_COLUMN_BITMASK |
			InappropiateModelImpl.CLASSNAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_GROUPIDCLASSNAME = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByGroupIdClassName",
			new String[] { Long.class.getName(), String.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, InappropiateImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the inappropiate in the entity cache if it is enabled.
	 *
	 * @param inappropiate the inappropiate
	 */
	public void cacheResult(Inappropiate inappropiate) {
		EntityCacheUtil.putResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateImpl.class, inappropiate.getPrimaryKey(), inappropiate);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
			new Object[] {
				inappropiate.getUuid(), Long.valueOf(inappropiate.getGroupId())
			}, inappropiate);

		inappropiate.resetOriginalValues();
	}

	/**
	 * Caches the inappropiates in the entity cache if it is enabled.
	 *
	 * @param inappropiates the inappropiates
	 */
	public void cacheResult(List<Inappropiate> inappropiates) {
		for (Inappropiate inappropiate : inappropiates) {
			if (EntityCacheUtil.getResult(
						InappropiateModelImpl.ENTITY_CACHE_ENABLED,
						InappropiateImpl.class, inappropiate.getPrimaryKey()) == null) {
				cacheResult(inappropiate);
			}
			else {
				inappropiate.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all inappropiates.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(InappropiateImpl.class.getName());
		}

		EntityCacheUtil.clearCache(InappropiateImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the inappropiate.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Inappropiate inappropiate) {
		EntityCacheUtil.removeResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateImpl.class, inappropiate.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(inappropiate);
	}

	@Override
	public void clearCache(List<Inappropiate> inappropiates) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Inappropiate inappropiate : inappropiates) {
			EntityCacheUtil.removeResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
				InappropiateImpl.class, inappropiate.getPrimaryKey());

			clearUniqueFindersCache(inappropiate);
		}
	}

	protected void clearUniqueFindersCache(Inappropiate inappropiate) {
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
			new Object[] {
				inappropiate.getUuid(), Long.valueOf(inappropiate.getGroupId())
			});
	}

	/**
	 * Creates a new inappropiate with the primary key. Does not add the inappropiate to the database.
	 *
	 * @param inappropiateId the primary key for the new inappropiate
	 * @return the new inappropiate
	 */
	public Inappropiate create(long inappropiateId) {
		Inappropiate inappropiate = new InappropiateImpl();

		inappropiate.setNew(true);
		inappropiate.setPrimaryKey(inappropiateId);

		String uuid = PortalUUIDUtil.generate();

		inappropiate.setUuid(uuid);

		return inappropiate;
	}

	/**
	 * Removes the inappropiate with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param inappropiateId the primary key of the inappropiate
	 * @return the inappropiate that was removed
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate remove(long inappropiateId)
		throws NoSuchInappropiateException, SystemException {
		return remove(Long.valueOf(inappropiateId));
	}

	/**
	 * Removes the inappropiate with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the inappropiate
	 * @return the inappropiate that was removed
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Inappropiate remove(Serializable primaryKey)
		throws NoSuchInappropiateException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Inappropiate inappropiate = (Inappropiate)session.get(InappropiateImpl.class,
					primaryKey);

			if (inappropiate == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchInappropiateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(inappropiate);
		}
		catch (NoSuchInappropiateException nsee) {
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
	protected Inappropiate removeImpl(Inappropiate inappropiate)
		throws SystemException {
		inappropiate = toUnwrappedModel(inappropiate);

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.delete(session, inappropiate);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(inappropiate);

		return inappropiate;
	}

	@Override
	public Inappropiate updateImpl(
		com.liferay.lms.model.Inappropiate inappropiate, boolean merge)
		throws SystemException {
		inappropiate = toUnwrappedModel(inappropiate);

		boolean isNew = inappropiate.isNew();

		InappropiateModelImpl inappropiateModelImpl = (InappropiateModelImpl)inappropiate;

		if (Validator.isNull(inappropiate.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			inappropiate.setUuid(uuid);
		}

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, inappropiate, merge);

			inappropiate.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !InappropiateModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						inappropiateModelImpl.getOriginalUuid()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);

				args = new Object[] { inappropiateModelImpl.getUuid() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);
			}

			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getOriginalGroupId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);

				args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getGroupId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID,
					args);
			}

			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSPK.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getOriginalClassPK())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_CLASSPK, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSPK,
					args);

				args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getClassPK())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_CLASSPK, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSPK,
					args);
			}

			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSNAME.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						inappropiateModelImpl.getOriginalClassName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_CLASSNAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSNAME,
					args);

				args = new Object[] { inappropiateModelImpl.getClassName() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_CLASSNAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSNAME,
					args);
			}

			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPIDCLASSNAME.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getOriginalGroupId()),
						
						inappropiateModelImpl.getOriginalClassName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPIDCLASSNAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPIDCLASSNAME,
					args);

				args = new Object[] {
						Long.valueOf(inappropiateModelImpl.getGroupId()),
						
						inappropiateModelImpl.getClassName()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_GROUPIDCLASSNAME,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPIDCLASSNAME,
					args);
			}
		}

		EntityCacheUtil.putResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
			InappropiateImpl.class, inappropiate.getPrimaryKey(), inappropiate);

		if (isNew) {
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
				new Object[] {
					inappropiate.getUuid(),
					Long.valueOf(inappropiate.getGroupId())
				}, inappropiate);
		}
		else {
			if ((inappropiateModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_UUID_G.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						inappropiateModelImpl.getOriginalUuid(),
						Long.valueOf(inappropiateModelImpl.getOriginalGroupId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_G, args);

				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G, args);

				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
					new Object[] {
						inappropiate.getUuid(),
						Long.valueOf(inappropiate.getGroupId())
					}, inappropiate);
			}
		}

		return inappropiate;
	}

	protected Inappropiate toUnwrappedModel(Inappropiate inappropiate) {
		if (inappropiate instanceof InappropiateImpl) {
			return inappropiate;
		}

		InappropiateImpl inappropiateImpl = new InappropiateImpl();

		inappropiateImpl.setNew(inappropiate.isNew());
		inappropiateImpl.setPrimaryKey(inappropiate.getPrimaryKey());

		inappropiateImpl.setUuid(inappropiate.getUuid());
		inappropiateImpl.setInappropiateId(inappropiate.getInappropiateId());
		inappropiateImpl.setUserId(inappropiate.getUserId());
		inappropiateImpl.setUserName(inappropiate.getUserName());
		inappropiateImpl.setClassName(inappropiate.getClassName());
		inappropiateImpl.setClassPK(inappropiate.getClassPK());
		inappropiateImpl.setGroupId(inappropiate.getGroupId());
		inappropiateImpl.setCompanyId(inappropiate.getCompanyId());
		inappropiateImpl.setReason(inappropiate.getReason());
		inappropiateImpl.setCreateDate(inappropiate.getCreateDate());
		inappropiateImpl.setModifiedDate(inappropiate.getModifiedDate());

		return inappropiateImpl;
	}

	/**
	 * Returns the inappropiate with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the inappropiate
	 * @return the inappropiate
	 * @throws com.liferay.portal.NoSuchModelException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Inappropiate findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the inappropiate with the primary key or throws a {@link com.liferay.lms.NoSuchInappropiateException} if it could not be found.
	 *
	 * @param inappropiateId the primary key of the inappropiate
	 * @return the inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByPrimaryKey(long inappropiateId)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByPrimaryKey(inappropiateId);

		if (inappropiate == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + inappropiateId);
			}

			throw new NoSuchInappropiateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				inappropiateId);
		}

		return inappropiate;
	}

	/**
	 * Returns the inappropiate with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the inappropiate
	 * @return the inappropiate, or <code>null</code> if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Inappropiate fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the inappropiate with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param inappropiateId the primary key of the inappropiate
	 * @return the inappropiate, or <code>null</code> if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByPrimaryKey(long inappropiateId)
		throws SystemException {
		Inappropiate inappropiate = (Inappropiate)EntityCacheUtil.getResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
				InappropiateImpl.class, inappropiateId);

		if (inappropiate == _nullInappropiate) {
			return null;
		}

		if (inappropiate == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				inappropiate = (Inappropiate)session.get(InappropiateImpl.class,
						Long.valueOf(inappropiateId));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (inappropiate != null) {
					cacheResult(inappropiate);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(InappropiateModelImpl.ENTITY_CACHE_ENABLED,
						InappropiateImpl.class, inappropiateId,
						_nullInappropiate);
				}

				closeSession(session);
			}
		}

		return inappropiate;
	}

	/**
	 * Returns all the inappropiates where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByUuid(String uuid) throws SystemException {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the inappropiates where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByUuid(String uuid, int start, int end)
		throws SystemException {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByUuid(String uuid, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
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

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Inappropiate inappropiate : list) {
				if (!Validator.equals(uuid, inappropiate.getUuid())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

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

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (uuid != null) {
					qPos.add(uuid);
				}

				list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
						start, end);
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
	 * Returns the first inappropiate in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByUuid_First(String uuid,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByUuid_First(uuid, orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the first inappropiate in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByUuid_First(String uuid,
		OrderByComparator orderByComparator) throws SystemException {
		List<Inappropiate> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last inappropiate in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByUuid_Last(String uuid,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByUuid_Last(uuid, orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the last inappropiate in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByUuid_Last(String uuid,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUuid(uuid);

		List<Inappropiate> list = findByUuid(uuid, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the inappropiates before and after the current inappropiate in the ordered set where uuid = &#63;.
	 *
	 * @param inappropiateId the primary key of the current inappropiate
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate[] findByUuid_PrevAndNext(long inappropiateId,
		String uuid, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByPrimaryKey(inappropiateId);

		Session session = null;

		try {
			session = openSession();

			Inappropiate[] array = new InappropiateImpl[3];

			array[0] = getByUuid_PrevAndNext(session, inappropiate, uuid,
					orderByComparator, true);

			array[1] = inappropiate;

			array[2] = getByUuid_PrevAndNext(session, inappropiate, uuid,
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

	protected Inappropiate getByUuid_PrevAndNext(Session session,
		Inappropiate inappropiate, String uuid,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (uuid != null) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(inappropiate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Inappropiate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the inappropiate where uuid = &#63; and groupId = &#63; or throws a {@link com.liferay.lms.NoSuchInappropiateException} if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByUUID_G(String uuid, long groupId)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByUUID_G(uuid, groupId);

		if (inappropiate == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("uuid=");
			msg.append(uuid);

			msg.append(", groupId=");
			msg.append(groupId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInappropiateException(msg.toString());
		}

		return inappropiate;
	}

	/**
	 * Returns the inappropiate where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByUUID_G(String uuid, long groupId)
		throws SystemException {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the inappropiate where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByUUID_G(String uuid, long groupId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { uuid, groupId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_UUID_G,
					finderArgs, this);
		}

		if (result instanceof Inappropiate) {
			Inappropiate inappropiate = (Inappropiate)result;

			if (!Validator.equals(uuid, inappropiate.getUuid()) ||
					(groupId != inappropiate.getGroupId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_1);
			}
			else {
				if (uuid.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_UUID_G_UUID_3);
				}
				else {
					query.append(_FINDER_COLUMN_UUID_G_UUID_2);
				}
			}

			query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (uuid != null) {
					qPos.add(uuid);
				}

				qPos.add(groupId);

				List<Inappropiate> list = q.list();

				result = list;

				Inappropiate inappropiate = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
						finderArgs, list);
				}
				else {
					inappropiate = list.get(0);

					cacheResult(inappropiate);

					if ((inappropiate.getUuid() == null) ||
							!inappropiate.getUuid().equals(uuid) ||
							(inappropiate.getGroupId() != groupId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
							finderArgs, inappropiate);
					}
				}

				return inappropiate;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
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
				return (Inappropiate)result;
			}
		}
	}

	/**
	 * Returns all the inappropiates where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupId(long groupId)
		throws SystemException {
		return findByGroupId(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the inappropiates where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupId(long groupId, int start, int end)
		throws SystemException {
		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupId(long groupId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPID;
			finderArgs = new Object[] { groupId, start, end, orderByComparator };
		}

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Inappropiate inappropiate : list) {
				if ((groupId != inappropiate.getGroupId())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
						start, end);
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
	 * Returns the first inappropiate in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByGroupId_First(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByGroupId_First(groupId,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the first inappropiate in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByGroupId_First(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Inappropiate> list = findByGroupId(groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last inappropiate in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByGroupId_Last(long groupId,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByGroupId_Last(groupId,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the last inappropiate in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByGroupId_Last(long groupId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByGroupId(groupId);

		List<Inappropiate> list = findByGroupId(groupId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the inappropiates before and after the current inappropiate in the ordered set where groupId = &#63;.
	 *
	 * @param inappropiateId the primary key of the current inappropiate
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate[] findByGroupId_PrevAndNext(long inappropiateId,
		long groupId, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByPrimaryKey(inappropiateId);

		Session session = null;

		try {
			session = openSession();

			Inappropiate[] array = new InappropiateImpl[3];

			array[0] = getByGroupId_PrevAndNext(session, inappropiate, groupId,
					orderByComparator, true);

			array[1] = inappropiate;

			array[2] = getByGroupId_PrevAndNext(session, inappropiate, groupId,
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

	protected Inappropiate getByGroupId_PrevAndNext(Session session,
		Inappropiate inappropiate, long groupId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

		query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(inappropiate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Inappropiate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the inappropiates where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @return the matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassPK(long classPK)
		throws SystemException {
		return findByClassPK(classPK, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the inappropiates where classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param classPK the class p k
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassPK(long classPK, int start, int end)
		throws SystemException {
		return findByClassPK(classPK, start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates where classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param classPK the class p k
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassPK(long classPK, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSPK;
			finderArgs = new Object[] { classPK };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_CLASSPK;
			finderArgs = new Object[] { classPK, start, end, orderByComparator };
		}

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Inappropiate inappropiate : list) {
				if ((classPK != inappropiate.getClassPK())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_CLASSPK_CLASSPK_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classPK);

				list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
						start, end);
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
	 * Returns the first inappropiate in the ordered set where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByClassPK_First(long classPK,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByClassPK_First(classPK,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the first inappropiate in the ordered set where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByClassPK_First(long classPK,
		OrderByComparator orderByComparator) throws SystemException {
		List<Inappropiate> list = findByClassPK(classPK, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last inappropiate in the ordered set where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByClassPK_Last(long classPK,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByClassPK_Last(classPK,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classPK=");
		msg.append(classPK);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the last inappropiate in the ordered set where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByClassPK_Last(long classPK,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByClassPK(classPK);

		List<Inappropiate> list = findByClassPK(classPK, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the inappropiates before and after the current inappropiate in the ordered set where classPK = &#63;.
	 *
	 * @param inappropiateId the primary key of the current inappropiate
	 * @param classPK the class p k
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate[] findByClassPK_PrevAndNext(long inappropiateId,
		long classPK, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByPrimaryKey(inappropiateId);

		Session session = null;

		try {
			session = openSession();

			Inappropiate[] array = new InappropiateImpl[3];

			array[0] = getByClassPK_PrevAndNext(session, inappropiate, classPK,
					orderByComparator, true);

			array[1] = inappropiate;

			array[2] = getByClassPK_PrevAndNext(session, inappropiate, classPK,
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

	protected Inappropiate getByClassPK_PrevAndNext(Session session,
		Inappropiate inappropiate, long classPK,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

		query.append(_FINDER_COLUMN_CLASSPK_CLASSPK_2);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(classPK);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(inappropiate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Inappropiate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the inappropiates where className = &#63;.
	 *
	 * @param className the class name
	 * @return the matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassName(String className)
		throws SystemException {
		return findByClassName(className, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the inappropiates where className = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param className the class name
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassName(String className, int start,
		int end) throws SystemException {
		return findByClassName(className, start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates where className = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param className the class name
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByClassName(String className, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_CLASSNAME;
			finderArgs = new Object[] { className };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_CLASSNAME;
			finderArgs = new Object[] { className, start, end, orderByComparator };
		}

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Inappropiate inappropiate : list) {
				if (!Validator.equals(className, inappropiate.getClassName())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

			if (className == null) {
				query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_1);
			}
			else {
				if (className.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_3);
				}
				else {
					query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_2);
				}
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (className != null) {
					qPos.add(className);
				}

				list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
						start, end);
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
	 * Returns the first inappropiate in the ordered set where className = &#63;.
	 *
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByClassName_First(String className,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByClassName_First(className,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the first inappropiate in the ordered set where className = &#63;.
	 *
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByClassName_First(String className,
		OrderByComparator orderByComparator) throws SystemException {
		List<Inappropiate> list = findByClassName(className, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last inappropiate in the ordered set where className = &#63;.
	 *
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByClassName_Last(String className,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByClassName_Last(className,
				orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the last inappropiate in the ordered set where className = &#63;.
	 *
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByClassName_Last(String className,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByClassName(className);

		List<Inappropiate> list = findByClassName(className, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the inappropiates before and after the current inappropiate in the ordered set where className = &#63;.
	 *
	 * @param inappropiateId the primary key of the current inappropiate
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate[] findByClassName_PrevAndNext(long inappropiateId,
		String className, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByPrimaryKey(inappropiateId);

		Session session = null;

		try {
			session = openSession();

			Inappropiate[] array = new InappropiateImpl[3];

			array[0] = getByClassName_PrevAndNext(session, inappropiate,
					className, orderByComparator, true);

			array[1] = inappropiate;

			array[2] = getByClassName_PrevAndNext(session, inappropiate,
					className, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Inappropiate getByClassName_PrevAndNext(Session session,
		Inappropiate inappropiate, String className,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

		if (className == null) {
			query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_1);
		}
		else {
			if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_3);
			}
			else {
				query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_2);
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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (className != null) {
			qPos.add(className);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(inappropiate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Inappropiate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the inappropiates where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @return the matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupIdClassName(long groupId,
		String className) throws SystemException {
		return findByGroupIdClassName(groupId, className, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the inappropiates where groupId = &#63; and className = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupIdClassName(long groupId,
		String className, int start, int end) throws SystemException {
		return findByGroupIdClassName(groupId, className, start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates where groupId = &#63; and className = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findByGroupIdClassName(long groupId,
		String className, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_GROUPIDCLASSNAME;
			finderArgs = new Object[] { groupId, className };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_GROUPIDCLASSNAME;
			finderArgs = new Object[] {
					groupId, className,
					
					start, end, orderByComparator
				};
		}

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Inappropiate inappropiate : list) {
				if ((groupId != inappropiate.getGroupId()) ||
						!Validator.equals(className, inappropiate.getClassName())) {
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
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_GROUPID_2);

			if (className == null) {
				query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_1);
			}
			else {
				if (className.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_3);
				}
				else {
					query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_2);
				}
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (className != null) {
					qPos.add(className);
				}

				list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
						start, end);
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
	 * Returns the first inappropiate in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByGroupIdClassName_First(long groupId,
		String className, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByGroupIdClassName_First(groupId,
				className, orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the first inappropiate in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByGroupIdClassName_First(long groupId,
		String className, OrderByComparator orderByComparator)
		throws SystemException {
		List<Inappropiate> list = findByGroupIdClassName(groupId, className, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last inappropiate in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate findByGroupIdClassName_Last(long groupId,
		String className, OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = fetchByGroupIdClassName_Last(groupId,
				className, orderByComparator);

		if (inappropiate != null) {
			return inappropiate;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("groupId=");
		msg.append(groupId);

		msg.append(", className=");
		msg.append(className);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInappropiateException(msg.toString());
	}

	/**
	 * Returns the last inappropiate in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching inappropiate, or <code>null</code> if a matching inappropiate could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate fetchByGroupIdClassName_Last(long groupId,
		String className, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByGroupIdClassName(groupId, className);

		List<Inappropiate> list = findByGroupIdClassName(groupId, className,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the inappropiates before and after the current inappropiate in the ordered set where groupId = &#63; and className = &#63;.
	 *
	 * @param inappropiateId the primary key of the current inappropiate
	 * @param groupId the group ID
	 * @param className the class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next inappropiate
	 * @throws com.liferay.lms.NoSuchInappropiateException if a inappropiate with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate[] findByGroupIdClassName_PrevAndNext(
		long inappropiateId, long groupId, String className,
		OrderByComparator orderByComparator)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByPrimaryKey(inappropiateId);

		Session session = null;

		try {
			session = openSession();

			Inappropiate[] array = new InappropiateImpl[3];

			array[0] = getByGroupIdClassName_PrevAndNext(session, inappropiate,
					groupId, className, orderByComparator, true);

			array[1] = inappropiate;

			array[2] = getByGroupIdClassName_PrevAndNext(session, inappropiate,
					groupId, className, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Inappropiate getByGroupIdClassName_PrevAndNext(Session session,
		Inappropiate inappropiate, long groupId, String className,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INAPPROPIATE_WHERE);

		query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_GROUPID_2);

		if (className == null) {
			query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_1);
		}
		else {
			if (className.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_3);
			}
			else {
				query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_2);
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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(groupId);

		if (className != null) {
			qPos.add(className);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(inappropiate);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Inappropiate> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the inappropiates.
	 *
	 * @return the inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the inappropiates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @return the range of inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the inappropiates.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of inappropiates
	 * @param end the upper bound of the range of inappropiates (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public List<Inappropiate> findAll(int start, int end,
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

		List<Inappropiate> list = (List<Inappropiate>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_INAPPROPIATE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_INAPPROPIATE;
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<Inappropiate>)QueryUtil.list(q, getDialect(),
							start, end);
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
	 * Removes all the inappropiates where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByUuid(String uuid) throws SystemException {
		for (Inappropiate inappropiate : findByUuid(uuid)) {
			remove(inappropiate);
		}
	}

	/**
	 * Removes the inappropiate where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the inappropiate that was removed
	 * @throws SystemException if a system exception occurred
	 */
	public Inappropiate removeByUUID_G(String uuid, long groupId)
		throws NoSuchInappropiateException, SystemException {
		Inappropiate inappropiate = findByUUID_G(uuid, groupId);

		return remove(inappropiate);
	}

	/**
	 * Removes all the inappropiates where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByGroupId(long groupId) throws SystemException {
		for (Inappropiate inappropiate : findByGroupId(groupId)) {
			remove(inappropiate);
		}
	}

	/**
	 * Removes all the inappropiates where classPK = &#63; from the database.
	 *
	 * @param classPK the class p k
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByClassPK(long classPK) throws SystemException {
		for (Inappropiate inappropiate : findByClassPK(classPK)) {
			remove(inappropiate);
		}
	}

	/**
	 * Removes all the inappropiates where className = &#63; from the database.
	 *
	 * @param className the class name
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByClassName(String className) throws SystemException {
		for (Inappropiate inappropiate : findByClassName(className)) {
			remove(inappropiate);
		}
	}

	/**
	 * Removes all the inappropiates where groupId = &#63; and className = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByGroupIdClassName(long groupId, String className)
		throws SystemException {
		for (Inappropiate inappropiate : findByGroupIdClassName(groupId,
				className)) {
			remove(inappropiate);
		}
	}

	/**
	 * Removes all the inappropiates from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (Inappropiate inappropiate : findAll()) {
			remove(inappropiate);
		}
	}

	/**
	 * Returns the number of inappropiates where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUuid(String uuid) throws SystemException {
		Object[] finderArgs = new Object[] { uuid };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_UUID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

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
	 * Returns the number of inappropiates where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUUID_G(String uuid, long groupId)
		throws SystemException {
		Object[] finderArgs = new Object[] { uuid, groupId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_UUID_G,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_1);
			}
			else {
				if (uuid.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_UUID_G_UUID_3);
				}
				else {
					query.append(_FINDER_COLUMN_UUID_G_UUID_2);
				}
			}

			query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (uuid != null) {
					qPos.add(uuid);
				}

				qPos.add(groupId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of inappropiates where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByGroupId(long groupId) throws SystemException {
		Object[] finderArgs = new Object[] { groupId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_GROUPID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_GROUPID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of inappropiates where classPK = &#63;.
	 *
	 * @param classPK the class p k
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByClassPK(long classPK) throws SystemException {
		Object[] finderArgs = new Object[] { classPK };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_CLASSPK,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_CLASSPK_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classPK);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_CLASSPK,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of inappropiates where className = &#63;.
	 *
	 * @param className the class name
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByClassName(String className) throws SystemException {
		Object[] finderArgs = new Object[] { className };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_CLASSNAME,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

			if (className == null) {
				query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_1);
			}
			else {
				if (className.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_3);
				}
				else {
					query.append(_FINDER_COLUMN_CLASSNAME_CLASSNAME_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (className != null) {
					qPos.add(className);
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

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_CLASSNAME,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of inappropiates where groupId = &#63; and className = &#63;.
	 *
	 * @param groupId the group ID
	 * @param className the class name
	 * @return the number of matching inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countByGroupIdClassName(long groupId, String className)
		throws SystemException {
		Object[] finderArgs = new Object[] { groupId, className };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_GROUPIDCLASSNAME,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INAPPROPIATE_WHERE);

			query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_GROUPID_2);

			if (className == null) {
				query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_1);
			}
			else {
				if (className.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_3);
				}
				else {
					query.append(_FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(groupId);

				if (className != null) {
					qPos.add(className);
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

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_GROUPIDCLASSNAME,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of inappropiates.
	 *
	 * @return the number of inappropiates
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_INAPPROPIATE);

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
	 * Initializes the inappropiate persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.liferay.lms.model.Inappropiate")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Inappropiate>> listenersList = new ArrayList<ModelListener<Inappropiate>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Inappropiate>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(InappropiateImpl.class.getName());
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
	private static final String _SQL_SELECT_INAPPROPIATE = "SELECT inappropiate FROM Inappropiate inappropiate";
	private static final String _SQL_SELECT_INAPPROPIATE_WHERE = "SELECT inappropiate FROM Inappropiate inappropiate WHERE ";
	private static final String _SQL_COUNT_INAPPROPIATE = "SELECT COUNT(inappropiate) FROM Inappropiate inappropiate";
	private static final String _SQL_COUNT_INAPPROPIATE_WHERE = "SELECT COUNT(inappropiate) FROM Inappropiate inappropiate WHERE ";
	private static final String _FINDER_COLUMN_UUID_UUID_1 = "inappropiate.uuid IS NULL";
	private static final String _FINDER_COLUMN_UUID_UUID_2 = "inappropiate.uuid = ?";
	private static final String _FINDER_COLUMN_UUID_UUID_3 = "(inappropiate.uuid IS NULL OR inappropiate.uuid = ?)";
	private static final String _FINDER_COLUMN_UUID_G_UUID_1 = "inappropiate.uuid IS NULL AND ";
	private static final String _FINDER_COLUMN_UUID_G_UUID_2 = "inappropiate.uuid = ? AND ";
	private static final String _FINDER_COLUMN_UUID_G_UUID_3 = "(inappropiate.uuid IS NULL OR inappropiate.uuid = ?) AND ";
	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 = "inappropiate.groupId = ?";
	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 = "inappropiate.groupId = ?";
	private static final String _FINDER_COLUMN_CLASSPK_CLASSPK_2 = "inappropiate.classPK = ?";
	private static final String _FINDER_COLUMN_CLASSNAME_CLASSNAME_1 = "inappropiate.className IS NULL";
	private static final String _FINDER_COLUMN_CLASSNAME_CLASSNAME_2 = "inappropiate.className = ?";
	private static final String _FINDER_COLUMN_CLASSNAME_CLASSNAME_3 = "(inappropiate.className IS NULL OR inappropiate.className = ?)";
	private static final String _FINDER_COLUMN_GROUPIDCLASSNAME_GROUPID_2 = "inappropiate.groupId = ? AND ";
	private static final String _FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_1 = "inappropiate.className IS NULL";
	private static final String _FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_2 = "inappropiate.className = ?";
	private static final String _FINDER_COLUMN_GROUPIDCLASSNAME_CLASSNAME_3 = "(inappropiate.className IS NULL OR inappropiate.className = ?)";
	private static final String _ORDER_BY_ENTITY_ALIAS = "inappropiate.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Inappropiate exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Inappropiate exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(InappropiatePersistenceImpl.class);
	private static Inappropiate _nullInappropiate = new InappropiateImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Inappropiate> toCacheModel() {
				return _nullInappropiateCacheModel;
			}
		};

	private static CacheModel<Inappropiate> _nullInappropiateCacheModel = new CacheModel<Inappropiate>() {
			public Inappropiate toEntityModel() {
				return _nullInappropiate;
			}
		};
}