package com.liferay.lms.service.persistence;


import java.util.ArrayList;
import java.util.List;

import com.liferay.lms.model.Inappropiate;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class InappropiateFinderImpl extends BasePersistenceImpl<Inappropiate> implements InappropiateFinder{
	
	Log log = LogFactoryUtil.getLog(InappropiateFinderImpl.class);
	 	
	
	public static final String FIND_BY_INAPPROPIATE=
			InappropiateFinder.class.getName() +
		        ".findByInappropiate";
	public static final String FIND_BY_WITHOUT_INAPPROPIATE=
			InappropiateFinder.class.getName() +
		        ".findByWithoutInappropiate";
	
	public static final String FIND_BY_WITH_WITHOUT_INAPPROPIATE_USERTEAMS=
			InappropiateFinder.class.getName() +
		        ".findByWithWithoutInappropiateUserTeams";
	
	public static final String FIND_BY_WITH_WITHOUT_INAPPROPIATE=
			InappropiateFinder.class.getName() +
		        ".findByWithWithoutInappropiate";
	
	public static final String FIND_BY_WORK_NOT_DONE=
			InappropiateFinder.class.getName() +
		        ".findByWorkNotDone";
	
	/**
	 *Get all user with work not done. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user with Inappropiate
	 */
	public List<User> findByWorkNotDone(long actId, long groupId, String className, boolean exist, boolean correctionsCompleted, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WORK_NOT_DONE);
			sql = replaceLimit(sql, start, end);
			sql = replaceWorkNotDone(sql, correctionsCompleted);
		
			if(log.isDebugEnabled()){
				log.debug("findByWorkNotDone sql: " + sql);
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);					
			qPos.add(groupId);
			qPos.add(actId);	
			if(correctionsCompleted){				
				
				qPos.add(groupId);
				qPos.add(className);
			}			
			
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	    	closeSessionLiferay(session);
	    }
	
		return users;
		
	}
	
	
	/**
	 *Get all user with and without inappropiate works. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user with Inappropiate
	 */
	public List<User> findByWithWithoutInappropiate(long actId, long groupId, String className, boolean exist, boolean correctionsCompleted, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITH_WITHOUT_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2p(sql, exist);
		//	sql = replaceCorrections(sql, correctionsCompleted);
			if(log.isDebugEnabled()){
				log.debug("findByWithWithoutInappropiate sql: " + sql);
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);					
			qPos.add(groupId);
			qPos.add(actId);	
						
			
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	    	closeSessionLiferay(session);
	    }
	
		return users;
		
	}
	
	/**
	 *Get all user with and without inappropiate works by user userTeams. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user with Inappropiate
	 */
	public List<User> findByWithWithoutInappropiateUserTeams(long actId, long groupId, String className, boolean exist,  boolean correctionsCompleted, long userId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITH_WITHOUT_INAPPROPIATE_USERTEAMS);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2p(sql, exist);
	//		sql = replaceCorrections(sql, correctionsCompleted);
			if(log.isDebugEnabled()){
				log.debug("findByWithWithoutInappropiateUserTeams sql: " + sql);				
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);	
			qPos.add(userId);	
			qPos.add(groupId);
			qPos.add(actId);
			
							
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	    	closeSessionLiferay(session);
	    }
	
		return users;
		
	}
	
	
	/**
	 * Get all user with inappropiate works. 
	 * 
	 * @param groupId .
	 * @param className
	 * @param start Limit start
	 * @param end Limit end
	 * @return List of user with Inappropiate
	 */
	public List<User> findByInappropiate(long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
		//	sql = replaceCorrections(sql, correctionsCompleted);
			if(log.isDebugEnabled()){
				log.debug("sql: " + sql);				
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);			
			qPos.add(groupId);
			qPos.add(actId);
			qPos.add("%"+className+"%");		
							
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	    	closeSessionLiferay(session);
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
	public List<User> findByNoInappropiate(long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITHOUT_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
		//	sql = replaceCorrections(sql, correctionsCompleted);
			
							
			
			if(log.isDebugEnabled()){
				log.debug("sql: " + sql);				
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);			
			qPos.add(groupId);
			qPos.add(groupId);
			qPos.add("%"+className+"%");
			qPos.add(actId);						
							
			users = (List<User>)q.list();
		
		} catch (Exception e) {
	       e.printStackTrace();
	    } finally {
	        closeSessionLiferay(session);
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
	
	private String replaceExistsP2p(String sql, boolean exist){
		
		if(!exist){
			sql = sql.replace("INNER JOIN lms_p2pactivity pa ON pa.userid = u.userid", "");
			sql = sql.replace("and pa.actId = ?", "AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)");
		}
		return sql;
	}
	//En el caso de que sea para el filtro de Todos los estados modificamos la query
	private String replaceWorkNotDone(String sql, boolean corrCompleted){		
		if(corrCompleted){
			sql = sql.replace("AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)", 
						"AND u.userid NOT IN (SELECT DISTINCT u.userid " +  
								"FROM " + 
								"user_ u " + 
								"INNER JOIN lms_p2pactivity pa ON pa.userid = u.userid " + 
								"INNER JOIN lms_inappropiate inap ON inap.classpk = pa.p2pActivityId " + 				
								"WHERE pa.actId = ? AND " + 
								"inap.groupid = ? AND inap.className LIKE ?	)"); 
			
		}
		return sql;
	}
	
	/*private String replaceCorrections(String sql, boolean correctionsCompleted){
		
		if(!correctionsCompleted){
			sql = sql.replace("", "");
		}
		return sql;
	}*/
	
	
	private SessionFactory getPortalSessionFactory() {
		String sessionFactory = "liferaySessionFactory";

		SessionFactory sf = (SessionFactory) PortalBeanLocatorUtil
				.getBeanLocator().locate(sessionFactory);

		return sf;
	}

	public void closeSessionLiferay(Session session) {
		getPortalSessionFactory().closeSession(session);
	}

	public Session openSessionLiferay() throws ORMException {
		return getPortalSessionFactory().openSession();
	}
}
