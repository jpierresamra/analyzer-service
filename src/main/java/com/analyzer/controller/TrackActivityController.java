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

import com.analyzer.bo.TrackActivityBO;

import common.obj.util.GlobalLogger;
import common.obj.vo.analyzer.TrackActivityGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.user.SecureUserVO;

@RestController
@RequestMapping("/analyzerServiceApi")
public class TrackActivityController
{
	@Autowired
	private TrackActivityBO trackActivityBO;
	
	@PostMapping("/secure/GetTrackActivityGrid")
	public @ResponseBody ResponseEntity<?> GetTrackActivityGrid(@RequestBody GridParamsVO gridParam) throws Exception
	{
		try
		{
			SecureUserVO sUser 	  = (SecureUserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			TrackActivityGridVO applicationGridVO = trackActivityBO.getTrackActivityGrid(sUser, gridParam);
			return ResponseEntity.ok(applicationGridVO);
		} 
		catch (Exception e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".GetTrackActivityGrid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AnalyzerServiceApi Error GetTrackActivityGrid");
		}
	}
	
	/*@PostMapping("/unsecure/TrackViewActivity")
	public @ResponseBody ResponseEntity<?> trackViewActivity(@RequestBody TrackActivityVO trackActivity) throws Exception
	{
		try
		{
			String trackId = trackActivityBO.trackViewActivity(trackActivity);
			TrackActivityVO trackViewActivityResponse = new TrackActivityVO();
			trackViewActivityResponse.setTrackId(trackId);
			return ResponseEntity.ok(trackViewActivityResponse);
		} 
		catch (BOException e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".trackViewActivity", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorDescription());
		}
		catch(Exception e2)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".trackViewActivity", e2);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
		}
	}
	
	@PostMapping("/unsecure/UnTrackViewActivity")
	public @ResponseBody ResponseEntity<?> unTrackViewActivity(@RequestBody TrackActivityVO trackActivity) throws Exception
	{
		try
		{
			trackActivityBO.unTrackViewActivity(trackActivity);
			TrackActivityVO trackViewActivityResponse = new TrackActivityVO();
			return ResponseEntity.ok(trackViewActivityResponse);
		} 
		catch (BOException e)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".UnTrackViewActivity", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrorDescription());
		}
		catch(Exception e2)
		{
			GlobalLogger.throwing(this.getClass().getName() + ".UnTrackViewActivity", e2);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
		}
	}*/
}
