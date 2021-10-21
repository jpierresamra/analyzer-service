package com.analyzer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.dao.UserDeviceDAO;

import common.obj.config.DBUtil;
import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.UserDeviceGridVO;
import common.obj.vo.analyzer.UserDeviceVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public class UserDeviceDAOImpl implements UserDeviceDAO
{
	@Autowired
	private DataSource dataSource;
	
	@Override
	public UserDeviceGridVO getUserDeviceGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt     = null;
		ResultSet rs			    = null;
		Connection con				= null;
		UserDeviceGridVO userDeviceGrid = new UserDeviceGridVO();
		String whereCondition = " where udt.business_id = ? ";
		try
		{
			if(gridParamsVO.getAppId() > 0)
			{
				whereCondition += " and udt.app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and udt.env_id = ? ";
			
			String queryTotal = "SELECT COUNT(udt.user_device_id) as total from user_device udt " + whereCondition;
			String query = "SELECT * FROM user_device udt " + whereCondition;
			
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			int count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				userDeviceGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement(query);
			count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				UserDeviceVO userDevice = new UserDeviceVO();
				userDevice.setDeviceId(String.valueOf(rs.getInt("user_device_id")));
				userDevice.setOsVersion(rs.getString("os_version"));
				userDevice.setDeviceModel(rs.getString("device_model"));
				userDevice.setDeviceOS(rs.getString("device_os"));
				userDevice.setIsVirtual(rs.getString("is_virtual"));
				userDevice.setManufacturer(rs.getString("manufacturer"));
				userDevice.setUniqueDeviceID(rs.getString("unique_device_id"));
				userDevice.setNotificationID(rs.getString("notification_id"));
				userDevice.setAppId(rs.getInt("app_id"));
				userDevice.setEnvId(rs.getInt("env_id"));
				userDevice.setCountry(rs.getString("country"));
				userDevice.setArea(rs.getString("area"));
				userDevice.setLatitude(rs.getDouble("latitude"));
				userDevice.setLongitude(rs.getDouble("longitude"));
				userDevice.setLastActiveDate(new Date(rs.getTimestamp("last_active_date").getTime()));
				userDevice.setCreationDate(new Date(rs.getTimestamp("creation_date").getTime()));
				userDeviceGrid.getUserDeviceList().add(userDevice);
			}
			return userDeviceGrid;
		}
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " getUserDeviceGrid() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

}
