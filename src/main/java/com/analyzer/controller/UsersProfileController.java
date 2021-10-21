package com.analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.analyzer.bo.UsersProfileBO;

import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.UserDeviceVO;
import common.obj.vo.exceptions.BOException;

@RestController
@RequestMapping("/analyzerServiceApi")
public class UsersProfileController
{
	@Autowired
	private UsersProfileBO usersProfileBO;
	
	
	/*@PostMapping("/unsecure/RegisterProfileUser")
	public @ResponseBody ResponseEntity<?> registerProfileUser(@RequestBody UserDeviceVO userDevice) throws Exception
	{
		try
		{
			UserDeviceVO usersProfileResponse = usersProfileBO.registerProfileUser(userDevice);
			return ResponseEntity.ok(usersProfileResponse);
		} 
		catch (BOException e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".registerProfileUser", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorDescription());
		}
		catch(Exception e2)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".registerProfileUser", e2);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
		}
	}*/
}
