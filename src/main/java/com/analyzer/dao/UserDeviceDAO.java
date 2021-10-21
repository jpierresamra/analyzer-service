package com.analyzer.dao;

import common.obj.vo.analyzer.UserDeviceGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public interface UserDeviceDAO
{
	public UserDeviceGridVO getUserDeviceGrid(SecureUserVO sUser, GridParamsVO gridParamsVO)  throws DAOException;

}
