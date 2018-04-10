package com.liferay.lms.service.persistence;


import java.util.ArrayList;
import java.util.List;

import com.liferay.lms.model.P2pActivity;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class InappropiateFinderImpl extends BasePersistenceImpl<P2pActivity> implements InappropiateFinder{
	
	Log log = LogFactoryUtil.getLog(InappropiateFinderImpl.class);
	 	
	
	public static final String FIND_BY_INAPPROPIATE=
			P2pActivityFinder.class.getName() +
		        ".findByInappropiate";
	public static final String FIND_BY_WITHOUT_INAPPROPIATE=
			P2pActivityFinder.class.getName() +
		        ".findByWithoutInappropiate";
	/**
	 * Get all user with inappropiate works. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user with Inappropiate
	 */
	public List<User> findByInappropiate(long groupId, String className, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			
			session = openSession();
			String sql = CustomSQLUtil.get(FIND_BY_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			if(log.isDebugEnabled()){
				log.debug("sql: " + sql);
				//log.debug("userId: " + userId);
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("user", User.class);
			QueryPos qPos = QueryPos.getInstance(q);			
			qPos.add(groupId);
			qPos.add(className);
							
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
		return users;
		
	}
	
	/**
	 * Get all user without inappropiate works. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user without Inappropiate
	 */
	public List<User> findByNoInappropiate(long groupId, String className, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			
			session = openSession();
			String sql = CustomSQLUtil.get(FIND_BY_WITHOUT_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			if(log.isDebugEnabled()){
				log.debug("sql: " + sql);
				//log.debug("userId: " + userId);
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("user", User.class);
			QueryPos qPos = QueryPos.getInstance(q);			
			qPos.add(groupId);
			qPos.add(className);
							
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSession(session);
	    }
	
		return users;
		
	}
	
	private String replaceLimit(String sql, int start, int end){
		if(start < 0 && end < 0){
			sql = sql.replace("LIMIT [$START$], [$END$]", "");
		}else{
			sql = sql.replace("[$START$]", String.valueOf(start));
			sql = sql.replace("[$END$]", String.valueOf(start+end));
		}
		return sql;
	}
}
