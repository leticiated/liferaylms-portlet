package com.liferay.lms.service.persistence;


import java.util.ArrayList;
import java.util.List;

import com.liferay.lms.model.Inappropiate;
import com.liferay.lms.model.P2pActivity;
import com.liferay.lms.model.P2pActivityCorrections;
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
	public List<User> findByWorkNotDone(int reviewSearch, int inapropValue, long actId, long groupId, boolean exist, boolean correctionsCompleted, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		String className=P2pActivity.class.getName();
		if(reviewSearch == 2){
			className=P2pActivityCorrections.class.getName();
		}
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WORK_NOT_DONE);
			sql = replaceLimit(sql, start, end);
			sql = replaceWorkNotDone(sql, correctionsCompleted, reviewSearch, inapropValue);
		
			if(log.isDebugEnabled()){
				log.debug("findByWorkNotDone sql: " + sql);
			}
			
			SQLQuery q = session.createSQLQuery(sql);
			q.addEntity("User_",PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.model.impl.UserImpl"));
			QueryPos qPos = QueryPos.getInstance(q);					
			qPos.add(groupId);
			qPos.add(actId);	
			
			if(correctionsCompleted){				
				if(inapropValue == 2 && reviewSearch==2){
					qPos.add(groupId);
					qPos.add(P2pActivity.class.getName());
					qPos.add(actId);
					qPos.add(groupId);
					qPos.add(P2pActivityCorrections.class.getName());
				}else{
					qPos.add(groupId);
					qPos.add(className);	
				}
				
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
	public List<User> findByWithWithoutInappropiate(int reviewSearch, long actId, long groupId, boolean exist, boolean correctionsCompleted, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITH_WITHOUT_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2p(sql, exist, reviewSearch);
			sql = replaceExistsP2pCorrectionInapropiate(sql, reviewSearch);
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
	public List<User> findByWithWithoutInappropiateUserTeams(int reviewSearch, long actId, long groupId, boolean exist,  boolean correctionsCompleted, long userId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITH_WITHOUT_INAPPROPIATE_USERTEAMS);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2p(sql, exist, reviewSearch);
			sql = replaceExistsP2pCorrectionInapropiate(sql, reviewSearch);
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
	public List<User> findByInappropiate(int reviewSearch, long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2pCorrectionInapropiate(sql, reviewSearch);
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
	public List<User> findByNoInappropiate(int reviewSearch, long groupId, String className, boolean exists, boolean correctionsCompleted, long actId, int start, int end){
		List<User> users = new ArrayList<User>();
		Session session = null;
		try{
			
			session = openSessionLiferay();
			String sql = CustomSQLUtil.get(FIND_BY_WITHOUT_INAPPROPIATE);
			sql = replaceLimit(sql, start, end);
			sql = replaceExistsP2pCorrectionInapropiate(sql, reviewSearch);	
							
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
	
	private String replaceExistsP2p(String sql, boolean exist, int reviewSearch){
		
		if(!exist && reviewSearch == 0){
			sql = sql.replace("INNER JOIN lms_p2pactivity pa ON pa.userid = u.userid", "");
			sql = sql.replace("and pa.actId = ?", "AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)");
			sql = sql.replace("and pa.p2pActivityId in (select p2pActivityId from lms_p2pactivitycorrections corr inner join lms_inappropiate ina on corr.p2pActivityCorrectionsId=ina.classPK)","");
		}		
	
		return sql;
	}
	private String replaceExistsP2pCorrectionInapropiate(String sql, int reviewSearch){
		//todas
		if(reviewSearch == 0){
			sql = sql.replace("and pa.p2pActivityId in (select p2pActivityId from lms_p2pactivitycorrections corr inner join lms_inappropiate ina on corr.p2pActivityCorrectionsId=ina.classPK)","");
		}
		 //no
		else if(reviewSearch == 2){
			sql = sql.replace("and pa.p2pActivityId in (select p2pActivityId from lms_p2pactivitycorrections corr inner join lms_inappropiate ina on corr.p2pActivityCorrectionsId=ina.classPK)",
					"and pa.p2pActivityId NOT in (select p2pActivityId from lms_p2pactivitycorrections corr inner join lms_inappropiate ina on corr.p2pActivityCorrectionsId=ina.classPK)");
		}				
		return sql;
	}
	//En el caso de que sea para el filtro de Todos los estados modificamos la query
	private String replaceWorkNotDone(String sql, boolean corrCompleted, int reviewSearch, int inapropValue){		
		
		String sqlActivity= "AND u.userid NOT IN (SELECT DISTINCT u.userid " +  
						"FROM " + 
						"user_ u " + 
						"INNER JOIN lms_p2pactivity pa ON pa.userid = u.userid " + 
						"INNER JOIN lms_inappropiate inap ON inap.classpk = pa.p2pActivityId " + 				
						"WHERE pa.actId = ? AND " + 
						"inap.groupid = ? AND inap.className LIKE ?	)";
		
		String sqlCorrections= "AND u.userid NOT IN (SELECT DISTINCT u.userid " +  
						"FROM " + 
						"user_ u " + 
						"INNER JOIN lms_p2pactivity pa ON pa.userid = u.userid " + 
						"INNER JOIN lms_p2pactivitycorrections pc ON pc.p2pactivityid = pa.p2pactivityid " + 
						"INNER JOIN lms_inappropiate inap ON inap.classpk = pc.p2pActivityCorrectionsId " + 				
						"WHERE pa.actId = ? AND " + 
						"inap.groupid = ? AND inap.className LIKE ?	) ";
		
		if(corrCompleted && reviewSearch != 2){
			sql = sql.replace("AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)", sqlActivity); 
			
		}		
		else if(corrCompleted && reviewSearch == 2 && inapropValue !=2){
			sql = sql.replace("AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)", sqlCorrections);
		}
		//En el caso de que se busquen todos los estados y no tiene inappropiate ni de actividades ni de correcciones
		else if(corrCompleted && reviewSearch == 2 && inapropValue ==2){
			sql = sql.replace("AND  u.userId NOT IN (SELECT userid FROM lms_p2pactivity WHERE actId = ?)", sqlActivity + " " + sqlCorrections);
		}
		return sql;
	}
	
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
