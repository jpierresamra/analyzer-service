package com.analyzer.dao;

import common.obj.vo.analyzer.TrackActivityGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public interface TrackActivityDAO
{
	/*public String trackViewActivity(TrackActivityVO trackActivity) throws DAOException;
	public void trackActivityCount(TrackActivityVO trackActivity) throws DAOException;
	public TrackActivityVO unTrackViewActivity(TrackActivityVO trackActivity) throws DAOException;
	public void publishEvent(TrackActivityVO trackViewActivityResponse) throws DAOException;*/
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException;
}
