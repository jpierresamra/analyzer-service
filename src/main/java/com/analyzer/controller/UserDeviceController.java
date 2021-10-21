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

import com.analyzer.bo.UserDeviceBO;

import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.UserDeviceGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.user.SecureUserVO;

@RestController
@RequestMapping("/analyzerServiceApi")
public class UserDeviceController {

	@Autowired
	private UserDeviceBO userDeviceBO;
	
	@PostMapping("/secure/GetUserDeviceGrid")
	public @ResponseBody ResponseEntity<?> getUserDeviceGrid(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDeviceGridVO userDeviceGrid = userDeviceBO.getUserDeviceGrid(sUser, gridParam);
			return ResponseEntity.ok(userDeviceGrid);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetUserDeviceGrid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetUserDeviceGrid");
		}
	}
}
