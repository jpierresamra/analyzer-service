package com.analyzer.bo;

import common.obj.vo.analyzer.UserDeviceGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.user.SecureUserVO;

public interface UserDeviceBO
{
	public UserDeviceGridVO getUserDeviceGrid(SecureUserVO sUser, GridParamsVO gridParamsVO)  throws BOException;
}
