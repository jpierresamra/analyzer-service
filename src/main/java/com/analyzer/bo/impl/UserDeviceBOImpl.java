package com.analyzer.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.bo.UserDeviceBO;
import com.analyzer.dao.UserDeviceDAO;

import common.obj.vo.analyzer.UserDeviceGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public class UserDeviceBOImpl implements UserDeviceBO
{
	@Autowired
	private UserDeviceDAO userDeviceDAO;
	
	@Override
	public UserDeviceGridVO getUserDeviceGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException
	{
		try
		{
			return userDeviceDAO.getUserDeviceGrid(sUser, gridParamsVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
}
