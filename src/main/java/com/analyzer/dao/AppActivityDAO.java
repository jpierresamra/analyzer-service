package com.analyzer.dao;

import common.obj.vo.analyzer.GeolocationListVO;
import common.obj.vo.analyzer.WidgetDataVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;

public interface AppActivityDAO
{
	public WidgetDataVO getNewDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getActiveDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getInactiveDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getCrashDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getSessionsNumberWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getSessionsLengthWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getIosDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public WidgetDataVO getAndroidDevicesWidget(GridParamsVO gridParamsVO) throws DAOException;
	public GeolocationListVO getGeolocation(GridParamsVO gridParamsVO) throws DAOException;
}
