package com.analyzer.controller;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.analyzer.bo.ApplicationBO;

import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.notification.NotificationVO;
import common.obj.vo.user.SecureUserVO;

@RestController
@RequestMapping("/analyzerServiceApi")
public class ApplicationController
{
	@Autowired
	private ApplicationBO applicationBO;
	
	@PostMapping("/secure/SendNotificationByGeo")
	public @ResponseBody ResponseEntity<?> sendNotificationByGeo(@RequestBody NotificationVO notificationVO) throws Exception
	{
		GlobalLogger.log(Level.INFO, "sendNotificationByGeo  :: ");
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String response 	  = applicationBO.sendNotificationByGeo(sUser, notificationVO);
			NotificationVO notificationResponse = new NotificationVO();
			notificationResponse.setStatus(1);
			notificationResponse.setErrorDescription(response);
			return ResponseEntity.ok(notificationResponse);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".sendNotificationByGeo", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ApplicationController Error sendNotificationByGeo");
		}
	}
	
	@PostMapping("/secure/GetFlowApplication")
	public @ResponseBody ResponseEntity<?> getFlowApplication(@RequestBody GridParamsVO gridParam) throws Exception
	{
	
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ApplicationFlowVO applicationFlowVO 	  = applicationBO.getFlowApplication(sUser, gridParam);
			return ResponseEntity.ok(applicationFlowVO);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetFlowApplication", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ApplicationController Error GetFlowApplication");
		}
	}
	
	@PostMapping("/secure/GetPageMapping")
	public @ResponseBody ResponseEntity<?> getPageMapping(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			PageMappingGridVO pageMappingGrid = applicationBO.getPageMapping(sUser, gridParam);
			return ResponseEntity.ok(pageMappingGrid);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetPageMapping", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetPageMapping");
		}
	}
	
	@PostMapping("/secure/AddEditPageMapping")
	public @ResponseBody ResponseEntity<?> addEditPageMapping(@RequestBody PageMappingVO pageMappingVO) throws Exception
	{
		try
		{
			SecureUserVO sUser 	   = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			PageMappingVO response = applicationBO.addEditPageMapping(sUser, pageMappingVO);
			return ResponseEntity.ok(response);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".AddEditPageMapping", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error AddEditPageMapping");
		}
	}
	
	@PostMapping("/secure/DeletePageMapping")
	public @ResponseBody ResponseEntity<?> deletePageMapping(@RequestBody PageMappingVO pageMappingVO) throws Exception
	{
		try
		{
			SecureUserVO sUser 	   = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			PageMappingVO response = applicationBO.deletePageMapping(sUser, pageMappingVO);
			return ResponseEntity.ok(response);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".DeletePageMapping", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error DeletePageMapping");
		}
	}
	
	/*@PostMapping("/secure/GetEventGrid")
	public @ResponseBody ResponseEntity<?> GetEventGrid(@RequestBody GridParamsVO gridParam) throws Exception
	{
		GlobalLogger.log(Level.INFO, "GetEventGrid  :: " + gridParam.getBusinessId());
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TrackActivityGridVO applicationGridVO = applicationBO.getEventGrid(sUser, gridParam);
			return ResponseEntity.ok(applicationGridVO);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetEventGrid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetEventGrid");
		}
	}
	
	@PostMapping("/secure/GetTrackActivityGrid")
	public @ResponseBody ResponseEntity<?> GetTrackActivityGrid(@RequestBody GridParamsVO gridParam) throws Exception
	{
		GlobalLogger.log(Level.INFO, "GetEventGrid  :: " + gridParam.getBusinessId());
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TrackActivityGridVO applicationGridVO = applicationBO.getTrackActivityGrid(sUser, gridParam);
			return ResponseEntity.ok(applicationGridVO);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetEventGrid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetEventGrid");
		}
	}
	
	@PostMapping("/secure/GetTrackActivityById")
	public @ResponseBody ResponseEntity<?> GetTrackActivityById(@RequestBody TrackActivityVO trackActivity) throws Exception
	{
		GlobalLogger.log(Level.INFO, "GetTrackActivityById  :: " + trackActivity.getBusinessId());
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TrackActivityVO trackViewActivityResponse = applicationBO.getTrackActivityById(sUser, trackActivity);
			return ResponseEntity.ok(trackViewActivityResponse);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetTrackActivityById", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetTrackActivityById");
		}
	}
	
	@PostMapping("/secure/GetBundleIdSelect")
	public @ResponseBody ResponseEntity<?> GetBundleIdSelect(@RequestBody SecureUserVO secureUserVO) throws Exception
	{
		GlobalLogger.log(Level.INFO, "GetBundleIdSelect  :: " + secureUserVO.getBusinessId());
		try 
		{
		    SelectResultVO selectResultVO = applicationBO.getBundleIdSelect(secureUserVO);
	    	return ResponseEntity.ok(selectResultVO);
		}
		catch (BOException e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetBundleIdSelect", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorDescription());
		}
		catch(Exception e2)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetBundleIdSelect", e2);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
		}
	}
	
	@PostMapping("/secure/CreateNewEvent")
	public @ResponseBody ResponseEntity<?> CreateNewEvent(@RequestBody TrackActivityVO trackActivity) throws Exception
	{
		GlobalLogger.log(Level.INFO, "CreateNewEvent  :: " + trackActivity.getBusinessId());
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TrackActivityVO trackViewActivityResponse = applicationBO.createNewEvent(sUser, trackActivity);
			return ResponseEntity.ok(trackViewActivityResponse);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".CreateNewEvent", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error CreateNewEvent");
		}
	}*/
}
