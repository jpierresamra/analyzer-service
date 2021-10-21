package com.analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.analyzer.bo.AppActivityBO;

import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.DevicesWidgetsDataVO;
import common.obj.vo.analyzer.GeolocationListVO;
import common.obj.vo.analyzer.WidgetDataVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.user.SecureUserVO;

@RestController
@RequestMapping("/analyzerServiceApi")
public class AppActivityController
{
	@Autowired
	private AppActivityBO appActivityBO;
	
	@PostMapping("/secure/GetDevicesWidgets")
	public @ResponseBody ResponseEntity<?> getDevicesWidgets(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			DevicesWidgetsDataVO data = appActivityBO.getDevicesWidgets(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getDevicesWidgets", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getDevicesWidgets");
		}
	}
	
	@PostMapping("/secure/GetNewDevicesWidget")
	public @ResponseBody ResponseEntity<?> getNewDevicesWidget(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			WidgetDataVO data = appActivityBO.getNewDevicesWidget(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getNewDevicesWidget", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getNewDevicesWidget");
		}
	}
	
	@PostMapping("/secure/GetActiveDevicesWidget")
	public @ResponseBody ResponseEntity<?> getActiveDevicesWidget(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			WidgetDataVO data = appActivityBO.getActiveDevicesWidget(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getActiveDevicesWidget", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getActiveDevicesWidget");
		}
	}
	
	@PostMapping("/secure/GetCrashDevicesWidget")
	public @ResponseBody ResponseEntity<?> getCrashDevicesWidget(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			WidgetDataVO data = appActivityBO.getCrashDevicesWidget(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getCrashDevicesWidget", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getCrashDevicesWidget");
		}
	}
	
	@PostMapping("/secure/GetSessionsNumberWidget")
	public @ResponseBody ResponseEntity<?> getSessionsNumberWidget(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			WidgetDataVO data = appActivityBO.getSessionsNumberWidget(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getSessionsNumberWidget", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getSessionsNumberWidget");
		}
	}
	
	@PostMapping("/secure/GetSessionsLengthWidget")
	public @ResponseBody ResponseEntity<?> getSessionsLengthWidget(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			WidgetDataVO data = appActivityBO.getSessionsLengthWidget(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getSessionsLengthWidget", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getSessionsLengthWidget");
		}
	}
	
	@PostMapping("/secure/GetGeolocation")
	public @ResponseBody ResponseEntity<?> getGeolocation(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			GeolocationListVO data = appActivityBO.getGeolocation(sUser, gridParam);
			return ResponseEntity.ok(data);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".getGeolocation", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error getGeolocation");
		}
	}

}
