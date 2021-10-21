package com.analyzer.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.analyzer.security.util.JwtUtil;
import com.hazelcast.core.HazelcastInstance;
import common.obj.vo.user.SecureUserVO;
import io.jsonwebtoken.Claims;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	HazelcastInstance hazelcastInstance;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
	{
		final String authorizationHeader = request.getHeader("Authorization");
		CustomRequestWrapper wrapped     = new CustomRequestWrapper(((HttpServletRequest) request));
		String username 				 = null;
		String jwt      				 = null;
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
		{
			jwt		 = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
		}

		if (request.getRequestURI().toString().contains("/secure"))
		{
			String body 		= IOUtils.toString(request.getReader());
			JSONObject bodyJson = null;
			try
			{
				bodyJson = new JSONObject(body);
				wrapped.resetInputStream(bodyJson.toString().getBytes("UTF-8"));

			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			Claims tokenClaims   = jwtUtil.getClaimsFromToken(jwt);
			String tokenUserName = tokenClaims.getSubject();
			String tokenUserId   = tokenClaims.getId();
			String buinessId     = (String) tokenClaims.get("businessId");
			String roleId        = (String) tokenClaims.get("roleId");
			String type          = (String) tokenClaims.get("type");
			Authentication storedAuthentication = null;
			
			if(type.equals("R"))
			{
				 storedAuthentication = (Authentication) hazelcastInstance.getMap("customerMap").get(tokenUserId);
			}
			else
			{
				 storedAuthentication = (Authentication) hazelcastInstance.getMap("map").get(tokenUserId);
			}
			SecureUserVO secureUserVO = null;
			if(storedAuthentication == null)
				secureUserVO = new SecureUserVO();
			else
			{
				secureUserVO = (SecureUserVO) storedAuthentication.getPrincipal();
				if(secureUserVO == null)
					secureUserVO = new SecureUserVO();
			}

			secureUserVO.setToken(jwt);
			secureUserVO.setUserName(tokenUserName);
			secureUserVO.setId(Integer.parseInt(tokenUserId));
			secureUserVO.setBusinessId(Integer.parseInt(buinessId));
			if(roleId != null)
			{
				secureUserVO.setRoleId(Integer.parseInt(roleId));
			}
			if(!type.equals("R"))
			{
				secureUserVO.setRoleId(Integer.parseInt(roleId));
			}
			if (jwtUtil.validateToken(jwt, secureUserVO))
			{
				/**
				 * Whenever we want this microservice to read the permissions stored in the
				 * hazelcast, we can enable these 2 lines Collection list = (Collection)
				 * hazelcastInstance.getMap("map").get(jwt); Authentication authentication = new
				 * UsernamePasswordAuthenticationToken(secureUserVO, null, list);
				 */
				Authentication authentication = new UsernamePasswordAuthenticationToken(secureUserVO, null, null);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		}
		if (request.getRequestURI().toString().contains("/secure"))
		{
			chain.doFilter(wrapped, response);
		} 
		else
		{
			chain.doFilter(request, response);
		}
	}
}
