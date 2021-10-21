package com.analyzer.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLType;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.analyzer.dao.ApplicationDAO;
import com.analyzer.service.NotificationService;

import common.obj.adapters.profile.util.Profile;
import common.obj.config.DBUtil;
import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;
import common.obj.vo.notification.NotificationVO;
import common.obj.vo.user.SecureUserVO;

public class ApplicationDAOImpl implements ApplicationDAO
{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private NotificationService notificationService;
	
	
	@Value("${db.type}")
	private String dbType;
	
	//private static final String PUBLISH_EVENT_ID_SEQ = "PUBLISH_EVENT_ID";

	@Override
	public String sendNotificationByGeo(SecureUserVO sUser, NotificationVO notificationVO) throws DAOException {
		List<String> notificationIds  = new ArrayList<String>();
		String response			      = "Success";
		PreparedStatement pstmt		  = null;
		ResultSet rs	    		  = null;
		Connection con 		    	  = null;
		String deviceOS				  = notificationVO.getNotificationOS();
		String country				  = notificationVO.getNotificationCountry();
		try {
			String query = "SELECT notification_id FROM user_device WHERE device_os = ? AND country = ? ";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setString(1, deviceOS);
			pstmt.setString(2, country);
			rs 			 = pstmt.executeQuery();
			while(rs.next()) {
				notificationIds.add("\""+rs.getString("notification_id")+"\"");
			}
			notificationVO.setNotificationIds(notificationIds);
			
			System.out.println(notificationVO.getNotificationIds().toString());
			this.notificationService.sendToASpecificSegmentService(notificationVO);
		} 
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + ".sendNotificationByGeo", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return response;
	}

	@Override
	public ApplicationFlowVO getFlowApplication(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException {
		ApplicationFlowVO applicationFlowVO 		=  new ApplicationFlowVO();//new ApplicationFlowVO();
		
		// these 2 hashmaps contain all the childrens pages/clicks/fragements that can belong to one of the starters application pages
		HashMap<String, HashMap<String, ApplicationFlowVO>> mainFlatMapRepresentation = new HashMap<String, HashMap<String, ApplicationFlowVO>>();
		HashMap<String, ApplicationFlowVO> subFlatMapRepresentation = new HashMap<String, ApplicationFlowVO>();
		
		// contains only the activities/pages that are the starters of the flow chart
		HashMap<String, ApplicationFlowVO> startersApplicationFlowMap = new HashMap<String, ApplicationFlowVO>();
		
		PreparedStatement pstmt		  		= null;
		ResultSet rs	    		  		= null;
		Connection con 		    	  		= null;
		String whereCondition = " WHERE atr.app_id = ? and atr.business_id = ? and atr.track not in ('START','OUT','END') ";
		try {
			Profile.initialize();
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " AND atr.env_id = ? ";
			
			String query = "SELECT pmp.mapping_id, atr.env_id, atr.from_path, atr.path, pmp.page_nickname, atr.track, atr.platform, atr.action, atr.action_name, "
					+ " pmp.page_image, count(atr.track_id) AS COUNT_PAGE_ACCESSED "
					+ " FROM activity_track atr left outer join page_mapping pmp on atr.business_id = pmp.business_id and atr.path = pmp.page_name  "
					+ " and atr.app_id = pmp.app_id "
					+ whereCondition
					+ " GROUP BY atr.from_path, atr.path, atr.track, atr.platform, atr.action, atr.action_name ";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setInt(1, gridParamsVO.getAppId());
			pstmt.setInt(2, sUser.getBusinessId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(3, gridParamsVO.getEnvId());
			rs 			 = pstmt.executeQuery();
			String path = null;
			String pageNickName = null;
			String from_path = null;
			String actionName = null;
			String pageImage = null;
			String action = null;
			while(rs.next()) {
				
				
				path	  	   = rs.getString("path");
				pageNickName = rs.getString("page_nickname");
				from_path	  	   = rs.getString("from_path");
				actionName  = rs.getString("action_name");
				pageImage  = rs.getString("page_image");
				action = rs.getString("action");
				
				// If this record belongs to a certain parent path
				applicationFlowVO = new ApplicationFlowVO();
				applicationFlowVO.setAppId(gridParamsVO.getAppId());
				applicationFlowVO.setEnvId(rs.getInt("env_id"));
				applicationFlowVO.setPageAction(rs.getString("action"));
				applicationFlowVO.setCountPageView(rs.getString("COUNT_PAGE_ACCESSED"));
				applicationFlowVO.setMappingId(rs.getInt("mapping_id"));
				if(from_path != null && !from_path.trim().equals(""))
				{
					// get the hashmap where we are filling the children of the parent path whose name is from_path
					subFlatMapRepresentation = mainFlatMapRepresentation.get(from_path);
					// If it is not already initialized, then create a new hashmap
					if(subFlatMapRepresentation == null)
					{
						subFlatMapRepresentation = new HashMap<String, ApplicationFlowVO>();
						mainFlatMapRepresentation.put(from_path, subFlatMapRepresentation);
					}
					if(action.equals("Click"))
					{
						applicationFlowVO.setPageNickName(actionName);
						applicationFlowVO.setPageName(actionName);
					}
					else
					{
						applicationFlowVO.setPageNickName(pageNickName);
						applicationFlowVO.setPageName(path);
						applicationFlowVO.setPageImage(pageImage);
					}

					subFlatMapRepresentation.put(path + actionName, applicationFlowVO);
				}
				else
				{
					applicationFlowVO.setPageNickName(pageNickName);
					applicationFlowVO.setPageName(path);
					applicationFlowVO.setPageImage(pageImage);
					startersApplicationFlowMap.put(path, applicationFlowVO);
				}
			}
			
			Iterator<ApplicationFlowVO> it = startersApplicationFlowMap.values().iterator();
			while(it.hasNext())
			{
				applicationFlowVO = it.next();
				this.setChildrenOfStarterAppFlow(applicationFlowVO, mainFlatMapRepresentation);
			}
			return applicationFlowVO;
		} 
		catch (Exception ex) {
			GlobalLogger.throwing(this.getClass().getName() + ".getFlowApplication", ex);
			ex.printStackTrace();
			throw new DAOException();
		} 
		finally {
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public PageMappingGridVO getPageMapping(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt     	  = null;
		ResultSet rs			    	  = null;
		Connection con					  = null;
		PageMappingGridVO pageMappingGrid = new PageMappingGridVO();
		String whereCondition 			  = " WHERE pm.business_id = ? ";
		try {
			if(gridParamsVO.getAppId() > 0) {
				whereCondition += " AND pm.app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " AND pm.env_id = ? ";
			
			String queryTotal = "SELECT COUNT(pm.mapping_id) as total from page_mapping pm " + whereCondition;
			String query = "SELECT * FROM page_mapping pm " + whereCondition;
			
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			int count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pageMappingGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			pstmt = con.prepareStatement(query);
			count = 1;
			pstmt.setInt(count++, sUser.getBusinessId());
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PageMappingVO pageMapping = new PageMappingVO();
				pageMapping.setMappingId(rs.getInt("mapping_id"));
				pageMapping.setPageName(rs.getString("page_name"));
				pageMapping.setPageNickName(rs.getString("page_nickname"));
				pageMapping.setPageAction(rs.getString("page_action"));
				pageMapping.setPageImage(rs.getString("page_image"));
				pageMapping.setCreationDate(rs.getString("creation_date"));
				pageMapping.setUpdateDate(rs.getString("update_date"));
				pageMapping.setAppId(rs.getInt("app_id"));
				pageMappingGrid.getPageMappingList().add(pageMapping);
			}
			return pageMappingGrid;
		}
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + " getPageMapping() ", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public PageMappingVO addPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException {
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		ResultSet rsForId = null;
		PreparedStatement pstmtForId = null;
		try {
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO page_mapping(app_id, business_id, page_name, page_nickname, page_action, page_image, creation_date, update_date)"
						 + " VALUES(?,?,?,?,?,?,?,?)";
			
			String autoIncrementQuery = "select last_insert_id()";
			
			pstmt    = con.prepareStatement(query);
			int counter = 1;
			pstmt.setInt(counter++, pageMappingVO.getAppId());
			pstmt.setInt(counter++, sUser.getBusinessId());
 			pstmt.setString(counter++, pageMappingVO.getPageName());
			pstmt.setString(counter++, pageMappingVO.getPageNickName());
			pstmt.setString(counter++, pageMappingVO.getPageAction());
			if(pageMappingVO.getPageImage() != null && pageMappingVO.getPageImage().trim().length() > 0)
			{
				InputStream in = IOUtils.toInputStream(pageMappingVO.getPageImage(), "UTF-8");
				pstmt.setBlob(counter++, in);
			}
			else
			{
				pstmt.setNull(counter++, Types.BLOB);
			}
			pstmt.setString(counter++, LocalDateTime.now().toString());
			pstmt.setString(counter++, LocalDateTime.now().toString());
			pstmt.executeUpdate();
			
			pstmtForId = con.prepareStatement(autoIncrementQuery);
			rsForId = pstmtForId.executeQuery();
			if(rsForId.next())
			{
				pageMappingVO.setMappingId(rsForId.getInt(1));
			}
		} 
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + " addPageMapping() ", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.releaseStatement(pstmtForId, rsForId);
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return pageMappingVO;
	}
	
	private void setChildrenOfStarterAppFlow(ApplicationFlowVO starterAppFlow, HashMap<String, HashMap<String, ApplicationFlowVO>> mainMap)
	{
		try
		{
			ApplicationFlowVO aChild = null;
			HashMap<String, ApplicationFlowVO> childrenMap = mainMap.get(starterAppFlow.getPageName());
			if(childrenMap != null)
			{
				Iterator<ApplicationFlowVO> childrenIt = childrenMap.values().iterator();
				while(childrenIt.hasNext())
				{
					aChild = childrenIt.next();
					starterAppFlow.getChilds().add(aChild);
					this.setChildrenOfStarterAppFlow(aChild, mainMap);
				}
			}
		}
		catch(Exception ex)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getFlowApplication", ex);
			throw ex;
		}
		
	}

	@Override
	public PageMappingVO editPageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException {
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		try {
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "update page_mapping set page_nickname = ?, page_image = ?, page_action = ?, page_name = ?, update_date = ? where mapping_id = ?";
			pstmt    = con.prepareStatement(query);
			int counter = 1;
			pstmt.setString(counter++, pageMappingVO.getPageNickName());
			if(pageMappingVO.getPageImage() != null && pageMappingVO.getPageImage().trim().length() > 0)
			{
				InputStream in = IOUtils.toInputStream(pageMappingVO.getPageImage(), "UTF-8");
				pstmt.setBlob(counter++, in);
			}
			else
			{
				pstmt.setNull(counter++, Types.BLOB);
			}
			pstmt.setString(counter++, pageMappingVO.getPageAction());
			pstmt.setString(counter++, pageMappingVO.getPageName());
			pstmt.setString(counter++, LocalDateTime.now().toString());
			pstmt.setInt(counter++, pageMappingVO.getMappingId());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + " editPageMapping() ", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.closeAll(con, pstmt);
		}
		return pageMappingVO;
	}
	
	
	@Override
	public PageMappingVO deletePageMapping(SecureUserVO sUser, PageMappingVO pageMappingVO) throws DAOException {
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		try {
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "delete from page_mapping where mapping_id = ?";
			pstmt    = con.prepareStatement(query);
			int counter = 1;
			pstmt.setInt(counter++, pageMappingVO.getMappingId());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + " deletePageMapping() ", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.closeAll(con, pstmt);
		}
		return pageMappingVO;
	}
	/*@Override
	public TrackActivityGridVO getApplicationGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException
	{
		return new TrackActivityGridVO();
	    //GridParamsVO   applicationParam = (GridParamsVO) gridParamsVO;
//		PreparedStatement pstmt     = null;
//		ResultSet rs			    = null;
//		Connection con				= null;
//		ApplicationGridVO applicationGrid = new ApplicationGridVO();
//		try
//		{
//			String queryTotal = "SELECT COUNT(app_management_id) as total from app_management";
//			String query = "SELECT * FROM app_management";
//			
//			con = dataSource.getConnection();
//			pstmt = con.prepareStatement(queryTotal);
//			rs = pstmt.executeQuery();
//			while (rs.next())
//			{
//				applicationGrid.setTotalNumber(rs.getString("total"));
//			}
//			rs.close();
//			pstmt.close();
//			
//			pstmt = con.prepareStatement(query);
//			rs = pstmt.executeQuery();
//			while (rs.next())
//			{
//				TrackActivityVO trackActivity = new TrackActivityVO();
//				trackActivity.setAppId(rs.getInt("app_management_id"));
//				trackActivity.setPackageName(rs.getString("bundle_id"));
//				trackActivity.setVersion(rs.getString("version"));
//				trackActivity.setVersionCode(rs.getString("version_code"));
//				trackActivity.setAppName(rs.getString("app_name"));
//				trackActivity.setCreationDate(rs.getString("creation_date"));
//				applicationGrid.getApplicationList().add(trackActivity);
//			}
//			return applicationGrid;
//		}
//		catch (Exception ex)
//		{
//			GlobalLogger.throwing( this.getClass().getName() + " getApplicationGrid() ", ex);
//			throw new DAOException();
//		} 
//		finally
//		{
//			DBUtil.releaseAll(con, pstmt, rs);
//		}
	}

	@Override
	public TrackActivityGridVO getDeviceGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException
	{
	    //GridParamsVO   applicationParam = (GridParamsVO) gridParamsVO;
		PreparedStatement pstmt     = null;
		ResultSet rs			    = null;
		Connection con				= null;
		TrackActivityGridVO applicationGrid = new TrackActivityGridVO();
		try
		{
			String queryTotal = "SELECT COUNT(user_device_id) as total from user_device";
			String query = "SELECT * FROM user_device";
			
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				applicationGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				TrackActivityVO trackActivity = new TrackActivityVO();
				trackActivity.setDeviceId(String.valueOf(rs.getInt("user_device_id")));
				trackActivity.setOsVersion(rs.getString("os_version"));
				trackActivity.setDeviceModel(rs.getString("device_model"));
				trackActivity.setDeviceOS(rs.getString("device_os"));
				trackActivity.setIsVirtual(rs.getString("is_virtual"));
				trackActivity.setManufacturer(rs.getString("manufacturer"));
				trackActivity.setUniqueDeviceID(rs.getString("unique_device_id"));
				trackActivity.setNotificationID(rs.getString("notification_id"));
				trackActivity.setAppId(rs.getInt("app_id"));
				trackActivity.setEnvId(rs.getInt("env_id"));
				trackActivity.setCountry(rs.getString("country"));
				trackActivity.setArea(rs.getString("area"));
				trackActivity.setLatitude(rs.getDouble("latitude"));
				trackActivity.setLongitude(rs.getDouble("longitude"));
				trackActivity.setLastActiveDate(rs.getString("last_active_date"));
				trackActivity.setCreationDate(rs.getString("creation_date"));
				applicationGrid.getTrackActivityList().add(trackActivity);
			}
			return applicationGrid;
		}
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " getDeviceGrid() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public TrackActivityGridVO getEventGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException
	{
	    //GridParamsVO   applicationParam = (GridParamsVO) gridParamsVO;
		PreparedStatement pstmt     = null;
		ResultSet rs			    = null;
		Connection con				= null;
		TrackActivityGridVO applicationGrid = new TrackActivityGridVO();
		try
		{
			String queryTotal = "SELECT COUNT(publish_event_id) as total from publish_event";
			String query = "SELECT * FROM publish_event";
			
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				applicationGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				TrackActivityVO trackActivity = new TrackActivityVO();
				trackActivity.setPublishEventId(rs.getInt("publish_event_id"));
				trackActivity.setAppId(rs.getInt("app_management_id"));
				trackActivity.setPath(rs.getString("path"));
				trackActivity.setOnTheSpot(rs.getBoolean("on_the_spot"));
				trackActivity.setEventTime(rs.getString("event_time"));
				trackActivity.setTrack(rs.getString("track"));
				trackActivity.setType(rs.getString("type")); 
				trackActivity.setMessage(rs.getString("message")); 
				trackActivity.setTitle(rs.getString("title")); 
				trackActivity.setOffer(rs.getString("offer")); 
				trackActivity.setDiscount(rs.getString("discount")); 
				trackActivity.setCreationDate(rs.getString("creation_date")); 
				applicationGrid.getTrackActivityList().add(trackActivity);
			}
			return applicationGrid;
		}
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " getEventGrid() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO) throws DAOException
	{
	    //GridParamsVO   applicationParam = (GridParamsVO) gridParamsVO;
		PreparedStatement pstmt     = null;
		ResultSet rs			    = null;
		Connection con				= null;
		TrackActivityGridVO applicationGrid = new TrackActivityGridVO();
		try
		{
			String queryTotal = "SELECT COUNT(track_activity_id) as total from track_activity";
			String query = "SELECT * FROM track_activity";
			
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(queryTotal);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				applicationGrid.setTotalNumber(rs.getString("total"));
			}
			rs.close();
			pstmt.close();
			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				TrackActivityVO trackActivity = new TrackActivityVO();
				trackActivity.setTrackId(rs.getString("track_activity_id"));
				trackActivity.setAppId(rs.getInt("app_management_id"));
				trackActivity.setDeviceId(rs.getString("user_device_id"));
				trackActivity.setPath(rs.getString("path"));
				trackActivity.setTrack(rs.getString("track"));
				trackActivity.setCreationDate(rs.getString("track_view_date")); 
				applicationGrid.getTrackActivityList().add(trackActivity);
			}
			return applicationGrid;
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

	@Override
	public TrackActivityVO getTrackActivityById(SecureUserVO sUser, TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt = null;
		ResultSet rs 			= null;
		Connection con 		    = null;
		int appId 			= trackActivity.getAppId();
		String deviceId			= trackActivity.getDeviceId().trim();
		String path				= trackActivity.getPath().trim();
		try
		{
			String query = "SELECT track_activity_count_id, view_enter_record, date FROM track_activity_count WHERE app_management_id = ? AND user_device_id = ? AND path = ?";
			con  		 = dataSource.getConnection();
			pstmt		 = con.prepareStatement(query);
			pstmt.setInt(1, appId);
			pstmt.setString(2, deviceId);
			pstmt.setString(3, path);
			rs 			 = pstmt.executeQuery();
			if(rs.next())
			{
				trackActivity.setTrackActivityCountId(rs.getInt("track_activity_count_id"));
				trackActivity.setViewEnterRecord(rs.getInt("view_enter_record"));
				trackActivity.setLastActiveDate(rs.getString("date"));
			}
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + ".getTrackActivityById", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return trackActivity;
	}

	@Override
	public SelectResultVO getBundleIdSelect(SecureUserVO secureUserVO) throws DAOException
	{
		SelectResultVO selectResultVO   = new SelectResultVO();
		SelectBoxOption selectBoxOption = null;
		PreparedStatement pstmt         = null;
		ResultSet rs			        = null;
		Connection con 				    = null;
		try
		{
			ArrayList<SelectBoxOption> returnList = new ArrayList<SelectBoxOption>();
			
			String query = "SELECT app_management_id, bundle_id FROM app_management";
			
			con    = dataSource.getConnection();
			pstmt  = con.prepareStatement(query);
			rs     = pstmt.executeQuery();
			
			while (rs.next())
			{
				selectBoxOption = new SelectBoxOption();
				selectBoxOption.setValue(rs.getString("app_management_id"));
				selectBoxOption.setLabel(rs.getString("bundle_id"));
				returnList.add(selectBoxOption);
			}
			selectResultVO.setSelectBoxOption(returnList);
			return selectResultVO;
		} 
		catch (Exception ex)
		{
			GlobalLogger.throwing( this.getClass().getName() + " getBundleIdSelect() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public TrackActivityVO createNewEvent(SecureUserVO sUser, TrackActivityVO trackActivity) throws DAOException
	{
		PreparedStatement pstmt  = null;
		Connection con			 = null;
		ResultSet rs			 = null;
		try
		{
			Profile.initialize();
			con			 = DBUtil.getConnection(this.dataSource, false, Connection.TRANSACTION_READ_COMMITTED, this.dbType);
			String query = "INSERT INTO publish_event(publish_event_id, app_management_id, path, on_the_spot, event_time, track, type, message, title, offer, discount, creation_date)"
						 + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt    = con.prepareStatement(query);
			int dbId = DBUtil.getNextId(con, this.dbType, PUBLISH_EVENT_ID_SEQ);
			int counter = 1;
			pstmt.setInt(counter++, dbId);
			pstmt.setInt(counter++, trackActivity.getAppId());
			pstmt.setString(counter++, trackActivity.getPath());
			pstmt.setBoolean(counter++, trackActivity.isOnTheSpot());
			pstmt.setObject(counter++, LocalDateTime.now());
			pstmt.setString(counter++, trackActivity.getTrack());
			pstmt.setString(counter++, trackActivity.getType());
			pstmt.setString(counter++, trackActivity.getMessage());
			pstmt.setString(counter++, trackActivity.getTitle());
			pstmt.setString(counter++, trackActivity.getOffer());
			pstmt.setString(counter++, trackActivity.getDiscount());
			pstmt.setObject(counter++, LocalDateTime.now());
			pstmt.executeUpdate();
			pstmt.close();
		} 
		catch (Exception ex)
		{
 			GlobalLogger.throwing( this.getClass().getName() + " createNewEvent() ", ex);
			throw new DAOException();
		} 
		finally
		{
			DBUtil.releaseAll(con, pstmt, rs);
		}
		return trackActivity;
	}*/
}
