package com.analyzer.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.analyzer.bo.ApplicationBO;
import com.analyzer.dao.ApplicationDAO;

import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.notification.NotificationVO;
import common.obj.vo.user.SecureUserVO;

public class ApplicationBOImpl implements ApplicationBO
{
	@Autowired
	private ApplicationDAO applicationDAO;

	@Override
	public String sendNotificationByGeo(SecureUserVO sUser, NotificationVO notificationVO) throws BOException {
		try
		{
			return applicationDAO.sendNotificationByGeo(sUser, notificationVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public ApplicationFlowVO getFlowApplication(SecureUserVO sUser, GridParamsVO gridParam) throws BOException {
		try
		{
			return applicationDAO.getFlowApplication(sUser, gridParam);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public PageMappingVO addEditPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws BOException {
		try
		{
			if(pageMappingVO.getMappingId() > 0)
				return applicationDAO.editPageMapping(sUser, pageMappingVO);
			else
				return applicationDAO.addPageMapping(sUser, pageMappingVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	@Override
	public PageMappingVO deletePageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws BOException {
		try
		{
			return applicationDAO.deletePageMapping(sUser, pageMappingVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public PageMappingGridVO getPageMapping(SecureUserVO sUser, GridParamsVO gridParam) throws BOException {
		try
		{
			return applicationDAO.getPageMapping(sUser, gridParam);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}
	
	/*@Override
	public TrackActivityGridVO getApplicationGrid(SecureUserVO sUser,  GridParamsVO gridParamsVO) throws BOException
	{
		try
		{
			return applicationDAO.getApplicationGrid(sUser, gridParamsVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public TrackActivityGridVO getEventGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException
	{
		try
		{
			return applicationDAO.getEventGrid(sUser, gridParamsVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws BOException
	{
		try
		{
			return applicationDAO.getTrackActivityGrid(sUser, gridParamsVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public TrackActivityVO getTrackActivityById(SecureUserVO sUser, TrackActivityVO trackActivity) throws BOException
	{
		try
		{
			return applicationDAO.getTrackActivityById(sUser, trackActivity);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public SelectResultVO getBundleIdSelect(SecureUserVO secureUserVO) throws BOException
	{
		try
		{
			return applicationDAO.getBundleIdSelect(secureUserVO);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}

	@Override
	public TrackActivityVO createNewEvent(SecureUserVO sUser, TrackActivityVO trackActivity) throws BOException
	{
		try
		{
			return applicationDAO.createNewEvent(sUser, trackActivity);
		} 
		catch (DAOException e)
		{
			throw new BOException(e.getErrorCode(), e.getErrorDescription());
		}
	}*/
}
