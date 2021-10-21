package com.analyzer.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.bo.AppActivityBO;
import com.analyzer.dao.AppActivityDAO;

import common.obj.vo.analyzer.DevicesWidgetsDataVO;
import common.obj.vo.analyzer.GeolocationListVO;
import common.obj.vo.analyzer.WidgetDataVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public class AppActivityBOImpl implements AppActivityBO
{
	@Autowired
	private AppActivityDAO appActivityDAO;

	@Override
	public WidgetDataVO getNewDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getNewDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getActiveDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getActiveDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getInactiveDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getInactiveDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getIosDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getIosDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getAndroidDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getAndroidDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getSessionsNumberWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getSessionsNumberWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public WidgetDataVO getSessionsLengthWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getSessionsLengthWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public WidgetDataVO getCrashDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getCrashDevicesWidget(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public DevicesWidgetsDataVO getDevicesWidgets(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		DevicesWidgetsDataVO retVal = new DevicesWidgetsDataVO();
		retVal.setNewDevicesWidget(this.getNewDevicesWidget(sUser, gridParamsVO));
		retVal.setActiveDevicesWidget(this.getActiveDevicesWidget(sUser, gridParamsVO));
		retVal.setInactiveDevicesWidget(this.getInactiveDevicesWidget(sUser, gridParamsVO));
		retVal.setIosDevicesWidget(this.getIosDevicesWidget(sUser, gridParamsVO));
		retVal.setAndroidDevicesWidget(this.getAndroidDevicesWidget(sUser, gridParamsVO));
		retVal.setCrashDevicesWidget(this.getCrashDevicesWidget(sUser, gridParamsVO));
		return retVal;
	}

	@Override
	public GeolocationListVO getGeolocation(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try {
			return appActivityDAO.getGeolocation(gridParamsVO);
		} catch (DAOException e) {
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

}
