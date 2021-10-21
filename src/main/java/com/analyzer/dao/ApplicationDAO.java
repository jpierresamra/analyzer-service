package com.analyzer.dao;

import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.notification.NotificationVO;
import common.obj.vo.user.SecureUserVO;

public interface ApplicationDAO
{

	public String sendNotificationByGeo(SecureUserVO sUser, NotificationVO notificationVO) throws DAOException;
	/*public TrackActivityGridVO getApplicationGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException;
	
	public TrackActivityGridVO getEventGrid(SecureUserVO sUser, GridParamsVO gridParamsVO)  throws DAOException;
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException;
	public TrackActivityVO getTrackActivityById(SecureUserVO sUser, TrackActivityVO trackActivity) throws DAOException;
	public SelectResultVO getBundleIdSelect(SecureUserVO secureUserVO)  throws DAOException;
	public TrackActivityVO createNewEvent(SecureUserVO sUser, TrackActivityVO trackActivity) throws DAOException;*/

	public ApplicationFlowVO getFlowApplication(SecureUserVO sUser, GridParamsVO gridParam) throws DAOException;
	public PageMappingVO addPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException;
	public PageMappingVO editPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException;
	public PageMappingVO deletePageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException;
	public PageMappingGridVO getPageMapping(SecureUserVO sUser, GridParamsVO gridParam)  throws DAOException;
}
