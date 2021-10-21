package com.analyzer.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.analyzer.dao.UsersProfileDAO;

public class UsersProfileDAOImpl implements UsersProfileDAO
{
	@Autowired
	private DataSource dataSource;
	
	@Value("${db.type}")
	private String dbType;
	
	private static final String USERS_PROFILE_ID_SEQ = "USERS_PROFILE_ID";
	private static final String APP_MANAGEMENT_ID_SEQ = "APP_MANAGEMENT_ID";

	/*@Override
	public String registerProfileUser(UserDeviceVO userDevice) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		String deviceId		     = null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO user_device(user_device_id, app_management_id, os_version, device_model, device_os, is_virtual, manufacturer, unique_device_id, notification_id, last_active_date)"
						 + " VALUES(?,?,?,?,?,?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, USERS_PROFILE_ID_SEQ);
			int counter = 1;
			deviceId    = String.valueOf(dbId);
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, userDevice.getAppId());
			pstmt.setString(counter++, userDevice.getOsVersion());
			pstmt.setString(counter++, userDevice.getDeviceModel());
			pstmt.setString(counter++, userDevice.getDeviceOS());
			pstmt.setString(counter++, userDevice.getIsVirtual());
			pstmt.setString(counter++, userDevice.getManufacturer());
			pstmt.setString(counter++, userDevice.getUniqueDeviceID());
			pstmt.setString(counter++, userDevice.getNotificationID());
			pstmt.setObject(counter++, LocalDateTime.now());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " registerProfileUser() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return deviceId;
	}
	
	@Override
	public int registerApp(UserDeviceVO userDevice) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		int appId		    	 = -1;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO app_management(app_management_id, bundle_id, version, version_code, app_name)"
						 + " VALUES(?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, APP_MANAGEMENT_ID_SEQ);
			int counter = 1;
			appId    = dbId;
			pstmt.setInt(counter++, dbId);
			pstmt.setString(counter++, userDevice.getPackageName());
			pstmt.setString(counter++, userDevice.getVersion());
			pstmt.setString(counter++, userDevice.getVersionCode());
			pstmt.setString(counter++, userDevice.getAppName());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " registerApp() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return appId;
	}

	@Override
	public String checkIfDeivceExist(String uniqueDeviceID) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		String deviceId 		= null;
		try
		{
			String query = "SELECT user_device_id FROM user_device WHERE unique_device_id = ? ";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setString(1, uniqueDeviceID);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				deviceId = rs.getString("user_device_id");
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".checkIfDeivceExist", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return deviceId;
	}

	@Override
	public int checkIfAppExists(String bundleId) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		int appId 		= -1;
		try
		{
			String query = "SELECT app_management_id FROM app_management WHERE bundle_id = ? ";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setString(1, bundleId);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				appId = rs.getInt("app_management_id");
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".checkIfAppExists", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return appId;
	}*/

}
