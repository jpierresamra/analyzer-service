package com.analyzer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.analyzer.bo.AppActivityBO;
import com.analyzer.bo.ApplicationBO;
import com.analyzer.bo.TrackActivityBO;
import com.analyzer.bo.UserDeviceBO;
import com.analyzer.bo.UsersProfileBO;
import com.analyzer.bo.impl.AppActivityBOImpl;
import com.analyzer.bo.impl.ApplicationBOImpl;
import com.analyzer.bo.impl.TrackActivityBOImpl;
import com.analyzer.bo.impl.UserDeviceBOImpl;
import com.analyzer.bo.impl.UsersProfileBOImpl;
import com.analyzer.dao.AppActivityDAO;
import com.analyzer.dao.ApplicationDAO;
import com.analyzer.dao.TrackActivityDAO;
import com.analyzer.dao.UserDeviceDAO;
import com.analyzer.dao.UsersProfileDAO;
import com.analyzer.dao.impl.AppActivityDAOImpl;
import com.analyzer.dao.impl.ApplicationDAOImpl;
import com.analyzer.dao.impl.TrackActivityDAOImpl;
import com.analyzer.dao.impl.UserDeviceDAOImpl;
import com.analyzer.dao.impl.UsersProfileDAOImpl;
import com.analyzer.security.filter.JwtRequestFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
   @Bean
   public UsersProfileBO getUsersProfileBOImpl()
   {
		return new UsersProfileBOImpl();
   }
		
   @Bean
   public UsersProfileDAO getUsersProfileDAOImpl()
   {
		return new UsersProfileDAOImpl();
   }
   
   @Bean
   public TrackActivityBO getTrackActivityBOImpl()
   {
	   return new TrackActivityBOImpl();
   }
   
   @Bean
   public TrackActivityDAO getTrackActivityDAOImpl()
   {
	   return new TrackActivityDAOImpl();
   }
   
   @Bean
   public ApplicationBO getApplicationBOImpl()
   {
	   return new ApplicationBOImpl();
   }
   
   @Bean
   public ApplicationDAO getApplicationDAOImpl()
   {
	   return new ApplicationDAOImpl();
   }
   
   @Bean
   public AppActivityBO getAppActivityBOImpl()
   {
	   return new AppActivityBOImpl();
   }
   
   @Bean
   public AppActivityDAO getAppActivityDAOImpl()
   {
	   return new AppActivityDAOImpl();
   }
   
   @Bean
   public UserDeviceBO getUserDeviceBOImpl()
   {
	   return new UserDeviceBOImpl();
   }
   
   @Bean
   public UserDeviceDAO getUserDeviceDAOImpl()
   {
	   return new UserDeviceDAOImpl();
   }
   
		   
   @Autowired
   private JwtRequestFilter jwtRequestFilter;
   
   @Override
   protected void configure(HttpSecurity http) throws Exception
   {
	   http.csrf().disable().authorizeRequests().antMatchers("/analyzerServiceApi/unsecure/*","/hystrix","/hystrix/*","/hystrix/**","/webjars","/webjars/**","/webjars/*","/actuator","/actuator/**","/actuator/*","/proxy.stream","/proxy.stream/**","/proxy.stream/*").permitAll().
				anyRequest().authenticated().and().
				exceptionHandling().and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	   http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
   }
}


