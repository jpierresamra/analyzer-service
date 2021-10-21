package com.analyzer.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.bo.TrackActivityBO;
import com.analyzer.dao.TrackActivityDAO;

import common.obj.vo.analyzer.TrackActivityGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public class TrackActivityBOImpl implements TrackActivityBO
{
	@Autowired
	private TrackActivityDAO trackActivityDAO;

	@Override
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException {
		try
		{
			return trackActivityDAO.getTrackActivityGrid(sUser, gridParamsVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	/*@Override
	public String trackViewActivity(TrackActivityVO trackActivity) throws BOException
	{
		String trackId = null;
		try
		{
			trackId = trackActivityDAO.trackViewActivity(trackActivity);
			this.trackActivityCount(trackActivity);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
		return trackId;
	}

	private void trackActivityCount(TrackActivityVO trackActivity) throws BOException
	{
		try
		{
			trackActivityDAO.trackActivityCount(trackActivity);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public void unTrackViewActivity(TrackActivityVO trackActivity) throws BOException
	{
		try
		{
			TrackActivityVO trackViewActivityResponse = trackActivityDAO.unTrackViewActivity(trackActivity);
			trackActivityDAO.publishEvent(trackViewActivityResponse);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}*/
}
