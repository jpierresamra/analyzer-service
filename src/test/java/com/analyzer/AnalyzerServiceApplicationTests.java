package com.analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.obj.vo.analyzer.ApplicationFlowVO;
import common.obj.vo.analyzer.PageMappingGridVO;
import common.obj.vo.analyzer.PageMappingVO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
class AnalyzerServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	public static HttpHeaders headers;
	public static ObjectMapper mapper;
	public static PageMappingVO pageMappingVO;
	
	@BeforeAll
	public static void initialize() throws Exception {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVJZCI6IjU3IiwiYnVzaW5lc3NJZCI6IjMiLCJ0eXBlIjoiQiIsImV4cCI6MTYzNDY3MjE0NywiaWF0IjoxNjM0NjM2MTQ3LCJqdGkiOiI3NCJ9.N-izruLckNKAIDt7YbVsSnsGM6u1TOIh6Qfm79wZiMI");
        mapper = new ObjectMapper();
        pageMappingVO = null;
	}
	
	@Test
	public void testFlowApplicationRequests() throws Exception {
		ResponseEntity<String> resp = null;
        String bodyJson = "{'search':'applicationflow','totalElements':0,'totalPages':0,'sortColName':'firstName','offset':0,'sortOrder':'DESC','sortField':'','pageNumber':0,'pageSize':10,'secureUser':null,'dateGranular':'Daily','dateRange':'Last 3 Months','appId':2,'envId':-1}";
		resp = this.restTemplate.postForEntity("http://localhost:"+port+"/analyzer-service/analyzerServiceApi/secure/GetFlowApplication", new HttpEntity<String>(bodyJson, headers), String.class);
		assertEquals(HttpStatus.OK, resp.getStatusCode(), "Application Flow Request returned not OK");
		assertNotNull(resp.getBody(), "Application Flow Response is null");
		ApplicationFlowVO response = null;
		try {
			response = mapper.readValue(resp.getBody(), ApplicationFlowVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertNotNull(response, "Application Flow response object was not mapped from the response json string");
	}
	
	@Test
	public void testGetPageMappingRequests() throws Exception {
		ResponseEntity<String> resp = null;
        String bodyJson = "{'search':'pageName','totalElements':0,'totalPages':0,'sortColName':'firstName','offset':0,'sortOrder':'DESC','sortField':'','pageNumber':0,'pageSize':10,'secureUser':null,'appId':2}";
		resp = this.restTemplate.postForEntity("http://localhost:"+port+"/analyzer-service/analyzerServiceApi/secure/GetPageMapping", new HttpEntity<String>(bodyJson, headers), String.class);
		assertEquals(HttpStatus.OK, resp.getStatusCode(), "Page Mapping Request returned not OK");
		assertNotNull(resp.getBody(), "Page Mapping Response is null");
		PageMappingGridVO response = null;
		try {
			response = mapper.readValue(resp.getBody(), PageMappingGridVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertNotNull(response, "Page Mapping response object was not mapped from the response json string");
	}
	
	@Test
	public void testAddPageMappingRequests() throws Exception {
		ResponseEntity<String> resp = null;
		pageMappingVO = new PageMappingVO();
		pageMappingVO.setAppId(2);
		pageMappingVO.setBusinessId(3);
		pageMappingVO.setPageName("From Test Case");
		pageMappingVO.setPageNickName("From Test Case Nickname");
		pageMappingVO.setPageAction("Nav");
		String bodyJson = mapper.writeValueAsString(pageMappingVO);
		resp = this.restTemplate.postForEntity("http://localhost:"+port+"/analyzer-service/analyzerServiceApi/secure/AddEditPageMapping", new HttpEntity<String>(bodyJson, headers), String.class);
		assertEquals(HttpStatus.OK, resp.getStatusCode(), "Add Page Mapping Request returned not OK");
		assertNotNull(resp.getBody(), "Add Page Mapping Response is null");
		System.out.println("resp.getBody()"+resp.getBody());
		try {
			pageMappingVO = mapper.readValue(resp.getBody(), PageMappingVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertNotNull(pageMappingVO, "Add Page Mapping response object was not mapped from the response json string");
	}
	
	@Test
	public void testEditPageMappingRequests() throws Exception {
		ResponseEntity<String> resp = null;
		
		if(pageMappingVO != null)
		{
			pageMappingVO.setPageNickName("From Test Case Nickname Updated");
			String bodyJson = mapper.writeValueAsString(pageMappingVO);
			resp = this.restTemplate.postForEntity("http://localhost:"+port+"/analyzer-service/analyzerServiceApi/secure/AddEditPageMapping", new HttpEntity<String>(bodyJson, headers), String.class);
			assertEquals(HttpStatus.OK, resp.getStatusCode(), "Edit Page Mapping Request returned not OK");
			assertNotNull(resp.getBody(), "Edit Page Mapping Response is null");
			
			pageMappingVO = null;
			try {
				pageMappingVO = mapper.readValue(resp.getBody(), PageMappingVO.class);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			assertNotNull(pageMappingVO, "Edit Page Mapping response object was not mapped from the response json string");
		}
		else
			assertTrue(false, "Missing Add Page Mapping to Edit");
	}
	
	@Test
	public void testDeletePageMappingRequests() throws Exception {
		ResponseEntity<String> resp = null;
		
		if(pageMappingVO != null)
		{
	        String bodyJson = mapper.writeValueAsString(pageMappingVO);
			resp = this.restTemplate.postForEntity("http://localhost:"+port+"/analyzer-service/analyzerServiceApi/secure/DeletePageMapping", new HttpEntity<String>(bodyJson, headers), String.class);
			assertEquals(HttpStatus.OK, resp.getStatusCode(), "Delete Page Mapping Request returned not OK");
			assertNotNull(resp.getBody(), "Delete Page Mapping Response is null");
			
			pageMappingVO = null;
			try {
				pageMappingVO = mapper.readValue(resp.getBody(), PageMappingVO.class);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			assertNotNull(pageMappingVO, "Delete Page Mapping response object was not mapped from the response json string");
		}
	}
}
