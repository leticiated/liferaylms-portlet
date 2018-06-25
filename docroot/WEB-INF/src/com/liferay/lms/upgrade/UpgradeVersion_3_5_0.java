package com.liferay.lms.upgrade;


import java.io.IOException;
import java.sql.SQLException;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

public class UpgradeVersion_3_5_0 extends UpgradeProcess {
	private static Log log = LogFactoryUtil.getLog(UpgradeVersion_3_4_0.class);
	
	public int getThreshold() {
		return 340;
	}

	protected void doUpgrade() throws Exception {
		log.info("Actualizando version a 3.5"); 
		System.out.println("START CREATE INAPPROPRIATE TABLE");
		String createAsynchronousTable = "CREATE TABLE lms_inappropiate ("
				+ " uuid_ VARCHAR(75) NULL DEFAULT NULL, "
				+ " inappropiateId BIGINT(20) NOT NULL PRIMARY KEY, "
				+ " userId BIGINT(20) NULL DEFAULT NULL, "
				+ " userName VARCHAR(75) NULL DEFAULT NULL, "
				+ " className VARCHAR(75) NULL DEFAULT NULL, "
				+ " classPK BIGINT(20) NULL DEFAULT NULL, "
				+ " groupId BIGINT(20) NULL DEFAULT NULL, "
				+ " companyId BIGINT(20) NULL DEFAULT NULL, "
				+ " reason TEXT NULL, "
				+ " createDate DATETIME NULL DEFAULT NULL, "
				+ " modifiedDate DATETIME NULL DEFAULT NULL, "
				+ " PRIMARY KEY ('inappropiateId')"
				+ " ) "
				+ " COLLATE='utf8_general_ci' "
				+ "ENGINE=InnoDB;";
		 //Execute SQL Queries
		 DB db = DBFactoryUtil.getDB();
		 log.warn("Create table activity linked ");
		
		try {
			db.runSQL(createAsynchronousTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
	
}