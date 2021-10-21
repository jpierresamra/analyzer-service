package com.analyzer.bo;

import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.notification.NotificationVO;
import common.obj.vo.user.SecureUserVO;

public interface ApplicationBO
{

	public String sendNotificationByGeo(SecureUserVO sUser, NotificationVO notificationVO) throws BOException;
	public ApplicationFlowVO getFlowApplication(SecureUserVO sUser, GridParamsVO gridParam) throws BOException;
	public PageMappingVO addEditPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws BOException;
	public PageMappingVO deletePageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws BOException;
	public PageMappingGridVO getPageMapping(SecureUserVO sUser, GridParamsVO gridParam)  throws BOException;
	
	/*public TrackActivityGridVO getApplicationGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException;
	
	public TrackActivityGridVO getEventGrid(SecureUserVO sUser,  GridParamsVO gridParamsVO)  throws BOException;
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO)  throws BOException;
	public TrackActivityVO getTrackActivityById(SecureUserVO sUser, TrackActivityVO trackActivity) throws BOException;
	public SelectResultVO getBundleIdSelect(SecureUserVO secureUserVO) throws BOException;
	public TrackActivityVO createNewEvent(SecureUserVO sUser, TrackActivityVO trackActivity)  throws BOException;*/
}
