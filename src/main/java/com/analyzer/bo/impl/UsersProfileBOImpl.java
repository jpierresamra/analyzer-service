package com.analyzer.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.bo.UsersProfileBO;
import com.analyzer.dao.UsersProfileDAO;

import common.obj.vo.analyzer.UserDeviceVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.exceptions.DAOException;

public class UsersProfileBOImpl implements UsersProfileBO
{
	@Autowired
	private UsersProfileDAO usersProfileDAO;
	
	/*@Override
	public UserDeviceVO registerProfileUser(UserDeviceVO userDevice) throws BOException
	{
		String uniqueDeviceID = userDevice.getUniqueDeviceID();
		String bundleId	= userDevice.getPackageName();
		UserDeviceVO usersProfileResponse = new UserDeviceVO();
		try
		{
			int appId = this.checkIfAppExists(bundleId);
			if(appId < 1) {
				appId = usersProfileDAO.registerApp(userDevice);
			}
			userDevice.setAppId(appId);
			String deviceId = this.checkIfDeivceExists(uniqueDeviceID);
			if(deviceId == null) {
				deviceId = usersProfileDAO.registerProfileUser(userDevice);
				usersProfileResponse.setStatus(1);
				usersProfileResponse.setDeviceId(deviceId);
				usersProfileResponse.setAppId(appId);
				usersProfileResponse.setErrorDescription("device subscribed seccessfully");
			}
			else 
			{
				usersProfileResponse.setStatus(2);
				usersProfileResponse.setDeviceId(deviceId);
				usersProfileResponse.setAppId(appId);
				usersProfileResponse.setErrorDescription("device alerday exist"); 
			}
			return usersProfileResponse;
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	
	private int checkIfAppExists(String bundleId) throws BOException
	{
		try
		{
			int appId = usersProfileDAO.checkIfAppExists(bundleId);
			return appId;
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}


	private String checkIfDeivceExists(String uniqueDeviceID) throws BOException
	{
		try
		{
			String deviceId = usersProfileDAO.checkIfDeivceExist(uniqueDeviceID);
			return deviceId;
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}*/
}
