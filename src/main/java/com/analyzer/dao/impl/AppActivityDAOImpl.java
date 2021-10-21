package com.analyzer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.analyzer.dao.AppActivityDAO;

import common.obj.config.DBUtil;
import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.GeolocationListVO;
import common.obj.vo.analyzer.GeolocationVO;
import common.obj.vo.analyzer.PieDataVO;
import common.obj.vo.analyzer.WidgetDataVO;
import common.obj.vo.analyzer.WidgetEntryVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.DAOException;

public class AppActivityDAOImpl implements AppActivityDAO
{
	@Autowired
	private DataSource dataSource;
	
	@Value("${db.type}")
	private String dbType;
	
	@Override
	public WidgetDataVO getNewDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
			{
				if(whereCondition != null && whereCondition.trim().length() > 0)
					whereCondition += " and app_id = ? ";
				else
					whereCondition += " where app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			

			
			String query = "SELECT count(user_device_id) as new_devices, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date FROM user_device "
					+ whereCondition
					+ " group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("new_devices");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("new_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getNewDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	
	@Override
	public WidgetDataVO getActiveDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
			{
				if(whereCondition != null && whereCondition.trim().length() > 0)
					whereCondition += " and app_id = ? ";
				else
					whereCondition += " where app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(distinct user_device_id) as active_devices, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date "
					+ "FROM activity_track "
					+ whereCondition
					+ " group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("active_devices");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("active_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getActiveDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	

	@Override
	public WidgetDataVO getInactiveDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " where last_active_date < date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
			{
				if(whereCondition != null && whereCondition.trim().length() > 0)
					whereCondition += " and app_id = ? ";
				else
					whereCondition += " where app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(distinct user_device_id) as inactive_devices "
					+ "FROM user_device "
					+ whereCondition;
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("inactive_devices");
				entry.setY(rs.getString("inactive_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getInactiveDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	
	@Override
	public WidgetDataVO getSessionsNumberWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}
			
			if(gridParamsVO.getAppId() > 0)
				whereCondition += " and app_id = ? ";
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(user_device_id) as sessions_number, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date "
					+ "FROM activity_track where track = 'START' "
					+ whereCondition
					+ "group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("sessions_number");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("sessions_number"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getSessionsNumberWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
	
	@Override
	public WidgetDataVO getSessionsLengthWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}
			
			if(gridParamsVO.getAppId() > 0)
				whereCondition += " and app_id = ? ";
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = " select max(tbl.time_diff) as max_session_length, min(tbl.time_diff) as min_session_length, FORMAT(avg(tbl.time_diff),2) as avg_session_length, "
					+ " DATE_FORMAT(tbl.creation_date, \""+groupByDateFormat+"\") as entry_date from (SELECT user_device_id, track, creation_date, "
					+ " TIME_FORMAT(TIMEDIFF(creation_date,lag(creation_date) over (partition by user_device_id order by user_device_id, creation_date)),\"%i.%s\") as time_diff "
					+ " FROM bitsdb.activity_track where track in ('START','END')  " + whereCondition
					+ " order by user_device_id, creation_date) as tbl where tbl.track = 'END' "
					+ " group by DATE_FORMAT(tbl.creation_date,\""+groupByDateFormat+"\") order by tbl.creation_date ";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			while (rs.next()){
				entry = new WidgetEntryVO();
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("avg_session_length"));
				data.getAvgList().add(entry);
				
				entry = new WidgetEntryVO();
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("min_session_length"));
				data.getMinList().add(entry);
				
				entry = new WidgetEntryVO();
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("max_session_length"));
				data.getMaxList().add(entry);
			}
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getSessionsLengthWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public WidgetDataVO getCrashDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = "";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " where creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
			{
				if(whereCondition != null && whereCondition.trim().length() > 0)
					whereCondition += " and app_id = ? ";
				else
					whereCondition += " where app_id = ? ";
			}
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(crash_exception_id) as crash_devices, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date "
					+ "FROM device_crash_exception "
					+ whereCondition
					+ " group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("crash_devices");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("crash_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getActiveDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public WidgetDataVO getIosDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = " where device_os = 'IOS' ";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
				whereCondition += " and app_id = ? ";
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(user_device_id) as new_devices, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date FROM user_device "
					+ whereCondition
					+ " group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("new_devices");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("new_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getNewDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public WidgetDataVO getAndroidDevicesWidget(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		WidgetDataVO data = new WidgetDataVO();
		String whereCondition = " where device_os = 'Android' ";
		try {
			String groupByDateFormat = null;
			if(gridParamsVO.getDateGranular().equals("Monthly"))
				groupByDateFormat = "%Y-%m-01";
			else if(gridParamsVO.getDateGranular().equals("Yearly"))
				groupByDateFormat = "%Y-12-31";
			else if(gridParamsVO.getDateGranular().equals("Hourly"))
				groupByDateFormat = "%Y-%m-%d %H";
			else
				groupByDateFormat = "%Y-%m-%d";
			
			if(gridParamsVO.getDateRange() != null)
			{
				if(gridParamsVO.getDateRange().equals("Today"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 day) ";
				else if(gridParamsVO.getDateRange().equals("Last Week"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 week) ";			
				else if(gridParamsVO.getDateRange().equals("Last Month"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 3 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 3 month) ";
				else if(gridParamsVO.getDateRange().equals("Last 6 Months"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 6 month) ";
				else if(gridParamsVO.getDateRange().equals("Last Year"))
					whereCondition += " and creation_date >= date_sub(current_date(), INTERVAL 1 year) ";
			}

			if(gridParamsVO.getAppId() > 0)
				whereCondition += " and app_id = ? ";
			if(gridParamsVO.getEnvId() > 0)
				whereCondition += " and env_id = ? ";
			
			String query = "SELECT count(user_device_id) as new_devices, DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") as entry_date FROM user_device "
					+ whereCondition
					+ " group by DATE_FORMAT(creation_date, \""+groupByDateFormat+"\") order by creation_date";
			con = dataSource.getConnection();
			int count = 1;
			pstmt = con.prepareStatement(query);
			if(gridParamsVO.getAppId() > 0)
				pstmt.setInt(count++, gridParamsVO.getAppId());
			if(gridParamsVO.getEnvId() > 0)
				pstmt.setInt(count++, gridParamsVO.getEnvId());
			rs = pstmt.executeQuery();
			WidgetEntryVO entry = null;
			int totalValue = 0;
			while (rs.next()){
				entry = new WidgetEntryVO();
				totalValue += rs.getInt("new_devices");
				entry.setX(rs.getString("entry_date"));
				entry.setY(rs.getString("new_devices"));
				data.getList().add(entry);
			}
			data.setTotalValue(String.valueOf(totalValue));
			return data;
		} catch (Exception ex) {
			GlobalLogger.log(Level.SEVERE, this.getClass().getName() + " getNewDevicesWidget() ", ex);
			throw new DAOException();
		}
		finally{
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}

	@Override
	public GeolocationListVO getGeolocation(GridParamsVO gridParamsVO) throws DAOException {
		PreparedStatement pstmt       	  = null;
		ResultSet rs			      	  = null;
		PreparedStatement pstmtTwo    	  = null;
		ResultSet rsTwo			      	  = null;
		PreparedStatement pstmtThree      = null;
		ResultSet rsThree		      	  = null;
		Connection con				      = null;
		GeolocationVO geolocationVO       = null;
		PieDataVO pieData                 = null;
		GeolocationListVO geolocationList = new GeolocationListVO();
		ArrayList<PieDataVO> piArrayList  = null;
		int businessId					  = gridParamsVO.getBusinessId();
		try {
			String queryDistincCountry = "SELECT DISTINCT country FROM user_device WHERE business_id = ? ";
			String queryDisctincOS     = "SELECT DISTINCT device_os FROM user_device WHERE business_id = ? ";
			String query      		   = "SELECT COUNT(device_os) AS deviceCount, latitude, longitude "
									   + "FROM user_device WHERE country = ? AND device_os = ? AND business_id = ? ";
			con   = dataSource.getConnection();
			pstmt = con.prepareStatement(queryDistincCountry);
			pstmt.setInt(1, businessId);
			rs    = pstmt.executeQuery();
			
			while (rs.next()) {
				geolocationVO  = new GeolocationVO();
				piArrayList    = new ArrayList<PieDataVO>();
				String country = rs.getString("country");
				geolocationVO.setTitle(country);
				geolocationVO.setHeight(20);
				geolocationVO.setWidth(20);
				pstmtTwo = con.prepareStatement(queryDisctincOS);
				pstmtTwo.setInt(1, businessId);
				rsTwo    = pstmtTwo.executeQuery();
				while (rsTwo.next()) {
					int count = 1;
					pieData	        = new PieDataVO();
					String deviceOS = rsTwo.getString("device_os");
					pstmtThree 	    = con.prepareStatement(query);
					pstmtThree.setString(count++, country);
					pstmtThree.setString(count++, deviceOS);
					pstmtThree.setInt(count++, businessId);
					rsThree   = pstmtThree.executeQuery();
					pieData.setCategory(deviceOS);
					pieData.setCountry(country);
					if (rsThree.next()) {
						geolocationVO.setLatitude(rsThree.getDouble("latitude"));
						geolocationVO.setLongitude(rsThree.getDouble("longitude"));
						pieData.setValue(rsThree.getInt("deviceCount"));
					}
					piArrayList.add(pieData);
					rsThree.close();
					pstmtThree.close();
					geolocationVO.setPieData(piArrayList);
				}
				rsTwo.close();
				pstmtTwo.close();
				geolocationList.getGeolocationList().add(geolocationVO);
			}
			rs.close();
			pstmt.close();
			return geolocationList;
		}
		catch (Exception ex) {
			GlobalLogger.throwing( this.getClass().getName() + " getGeolocation() ", ex);
			throw new DAOException();
		} 
		finally {
			DBUtil.releaseAll(con, pstmt, rs);
		}
	}
}
