package com.analyzer.bo;

import common.obj.vo.analyzer.DevicesWidgetsDataVO;
import common.obj.vo.analyzer.GeolocationListVO;
import common.obj.vo.analyzer.WidgetDataVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.user.SecureUserVO;

public interface AppActivityBO
{
	public WidgetDataVO getNewDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getActiveDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getInactiveDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getCrashDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getSessionsNumberWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getSessionsLengthWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getIosDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public WidgetDataVO getAndroidDevicesWidget(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	
	public DevicesWidgetsDataVO getDevicesWidgets(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	public GeolocationListVO getGeolocation(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	
	
}
