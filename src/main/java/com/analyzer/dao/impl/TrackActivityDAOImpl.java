package com.analyzer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.analyzer.dao.TrackActivityDAO;
import com.analyzer.service.NotificationService;

import common.obj.config.DBUtil;
import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.TrackActivityGridVO;
import common.obj.vo.analyzer.TrackActivityVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.common.GridSearchParamVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.user.SecureUserVO;

public class TrackActivityDAOImpl implements TrackActivityDAO
{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private NotificationService notificationService;
	
	@Value("${db.type}")
	private String dbType;
	
	@Override
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt     = null;
		ResultSet rs			    = null;
		Connection con				= null;
		String whereCondition		= " where atr.business_id = ? ";
		int deviceId = -1;
		TrackActivityGridVO trackActivityGrid = new TrackActivityGridVO();
		try
		{
			
			ArrayList<GridSearchParamVO> searchList  = gridParamsVO.getSearchList();
			GridSearchParamVO searchParam = null;
			if(searchList != null)
			{
				Iterator<GridSearchParamVO> it = searchList.iterator();
				while(it.hasNext())
				{
					searchParam = it.next();
					if(searchParam.getKey().equals("deviceId"))
					{
						whereCondition += " and atr.user_device_id = ? ";
						deviceId = Integer.parseInt(searchParam.getValue());
					}
				}
			}
			String queryTotal = "SELECT COUNT(atr.track_id) as total from activity_track atr " + whereCondition;
			String query = "SELECT * FROM activity_track atr " + whereCondition;
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			int count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(deviceId > 0)
				pstmt.setInt(count++, deviceId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				trackActivityGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement(query);
			count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(deviceId > 0)
				pstmt.setInt(count++, deviceId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				TrackActivityVO trackActivity = new TrackActivityVO();
				trackActivity.setTrackId(rs.getString("track_id"));
				trackActivity.setAppId(rs.getInt("app_id"));
				trackActivity.setEnvId(rs.getInt("env_id"));
				trackActivity.setDeviceId(rs.getString("user_device_id"));
				trackActivity.setPath(rs.getString("path"));
				trackActivity.setTrack(rs.getString("track"));
				trackActivity.setCreationDate(new Date(rs.getTimestamp("creation_date").getTime())); 
				trackActivityGrid.getTrackActivityList().add(trackActivity);
			}
			return trackActivityGrid;
		}
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " getTrackActivityGrid() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	
	/*@Override
	public String trackViewActivity(TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		String trackId		     = null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO track_activity(track_activity_id, app_management_id, user_device_id, path, track_view_date)"
						 + " VALUES(?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, TRACK_ACTIVITY_ID_SEQ);
			int counter = 1;
			trackId    = String.valueOf(dbId);
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, trackActivity.getAppId());
			pstmt.setString(counter++, trackActivity.getDeviceId());
			pstmt.setString(counter++, trackActivity.getPath());
			pstmt.setObject(counter++, trackActivity.getDate());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " trackViewActivity() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return trackId;
	}
	
	@Override
	public TrackActivityVO unTrackViewActivity(TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		trackActivity.setTrack("OUT");
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO track_activity(track_activity_id, app_management_id, user_device_id, path, track, track_view_date)"
						 + " VALUES(?,?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, TRACK_ACTIVITY_ID_SEQ);
			int counter = 1;
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, trackActivity.getAppId());
			pstmt.setString(counter++, trackActivity.getDeviceId());
			pstmt.setString(counter++, trackActivity.getPath());
			pstmt.setString(counter++, trackActivity.getTrack());
			pstmt.setObject(counter++, trackActivity.getDate());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " unTrackViewActivity() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return trackActivity;
	}

	@Override
	public void trackActivityCount(TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		int appId 			= trackActivity.getAppId();
		String deviceId			= trackActivity.getDeviceId().trim();
		String path				= trackActivity.getPath().trim();
		try
		{
			String query = "SELECT track_activity_count_id, view_enter_record FROM track_activity_count WHERE app_management_id = ? AND user_device_id = ? AND path = ?";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setInt(1, appId);
			pstmt.setString(2, deviceId);
			pstmt.setString(3, path);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				int trackActivityCountId = rs.getInt("track_activity_count_id");
				int viewEnterRecord = rs.getInt("view_enter_record");
				this.updateTrackActivityCount(trackActivityCountId,viewEnterRecord);
			}
			else
			{
				this.addTrackActivityCount(trackActivity);
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".trackActivityCount", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	private void updateTrackActivityCount(int trackActivityCountId, int viewEnterRecord)
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 			= null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "UPDATE track_activity_count SET view_enter_record = ?, date = ?  WHERE track_activity_count_id = ?";
			pstmt = con.prepareStatement(query);
			int  updateViewEnterRecord = viewEnterRecord + 1;
			pstmt.setInt(1, updateViewEnterRecord);
			pstmt.setObject(2, LocalDateTime.now());
			pstmt.setInt(3, trackActivityCountId);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".updateTrackActivityCount", ex);
		} finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	
	private void addTrackActivityCount(TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO track_activity_count(track_activity_count_id, app_management_id, user_device_id, path, view_enter_record, date)"
						 + " VALUES(?,?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, TRACK_ACTIVIT_COUNT_ID_SEQ);
			int counter = 1;
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, trackActivity.getAppId());
			pstmt.setString(counter++, trackActivity.getDeviceId());
			pstmt.setString(counter++, trackActivity.getPath());
			pstmt.setInt(counter++, 1);
			pstmt.setObject(counter++, trackActivity.getDate());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " addTrackActivityCount() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public void publishEvent(TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		int appId 			= trackActivity.getAppId();
		String track		    = trackActivity.getTrack().trim();
		String path				= trackActivity.getPath().trim();
		try
		{
			String query = "SELECT on_the_spot, publish_event_id, type, message, title  FROM publish_event WHERE app_management_id = ? AND track = ? AND path = ? ";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setInt(1, appId);
			pstmt.setString(2, track);
			pstmt.setString(3, path);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				Boolean onTheSpot  = rs.getBoolean("on_the_spot");
				int publishEventId = rs.getInt("publish_event_id");
				String type 	   = rs.getString("type");
				String title	   = rs.getString("title");
				String message     = rs.getString("message");
				trackActivity.setPublishEventId(publishEventId);
				if(onTheSpot) 
				{
					this.sendEvent(trackActivity,onTheSpot,type,title,message);
				}
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".publishEvent", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	private void sendEvent(TrackActivityVO trackActivity, Boolean onTheSpot, String type, String title, String message) throws DAOException
	{
		NotificationVO notificationVO = new NotificationVO();
		int appId         		  = trackActivity.getAppId();
		String deviceId       		  = trackActivity.getDeviceId();
		int publishEventId    		  = trackActivity.getPublishEventId();
		String notificationId 		  = this.getNotificationIdByDeviceIdAndAppId(appId, deviceId);
		Gson gson 					  = new Gson();
		if(onTheSpot) {
			String responseObject = gson.toJson(trackActivity);
			ArrayList<String> notificationIds = new ArrayList<String>();
			notificationIds.add("\""+notificationId+"\"");
			notificationVO.setNotificationType(type);
			notificationVO.setNotificationTitle(title);
			notificationVO.setNotificationMessage(message);
			notificationVO.setNotificationData(responseObject);
			notificationVO.setNotificationIds(notificationIds);
			this.notificationService.sendToASpecificSegmentService(notificationVO);
			this.savedEvent(trackActivity,publishEventId,notificationVO);
		}
	}

	private void savedEvent(TrackActivityVO trackActivity, int publishEventId, NotificationVO notificationVO) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO sent_event(sent_event_id, publish_event_id, app_management_id, user_device_id, path, data, title, message, type)"
						 + " VALUES(?,?,?,?,?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, SENT_EVENT_ID_SEQ);
			int counter = 1;
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, publishEventId);
			pstmt.setInt(counter++, trackActivity.getAppId());
			pstmt.setString(counter++, trackActivity.getDeviceId());
			pstmt.setString(counter++, trackActivity.getPath());
			pstmt.setString(counter++, notificationVO.getNotificationData());
			pstmt.setString(counter++, notificationVO.getNotificationTitle());
			pstmt.setString(counter++, notificationVO.getNotificationMessage());
			pstmt.setString(counter++, notificationVO.getNotificationType());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " savedEvent() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	private String getNotificationIdByDeviceIdAndAppId(int appId, String deviceId) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		String notificationId 	= null;
		try
		{
			String query = "SELECT notification_id FROM user_device WHERE app_management_id = ? AND user_device_id = ?";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setInt(1, appId);
			pstmt.setString(2, deviceId);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				notificationId = rs.getString("notification_id");
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".getNotificationIdByDeviceIdAndAppId", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return notificationId;
	}*/
}
