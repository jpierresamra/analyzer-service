package com.analyzer.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import common.obj.vo.notification.NotificationVO;

@Service
public class NotificationService
{
	@Value("${notification.rest.api.key}")
	private String API_KEY;
	
	@Value("${google.fcm.url}")
	private String FCM_URL;
	
	public NotificationVO sendToASpecificSegmentService(NotificationVO notificationVO)
	{
		
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post     = new HttpPost(FCM_URL);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Authorization", "key="+API_KEY);
			String messageIPS = "{\n" + 
					"  \"notification\": {\n" + 
					"    \"title\": \""+notificationVO.getNotificationTitle()+"\",\n" + 
					"    \"body\": \""+notificationVO.getNotificationMessage()+"\"\n" + 
					"  },\n" + 
					"  \"data\" : {\n" + 
					"    \"title\": \""+notificationVO.getNotificationTitle()+"\",\n" + 
					"    \"body\": \""+notificationVO.getNotificationMessage()+"\",\n" + 
					"    \"sound\": \"default\",\n" + 
					"    \"type\": \"signing\",\n" + 
					"    \"userId\": \"string required\",\n" + 
					"    \"documentID\": \"a513af7f-1e9d-49a5-a3e2-2fe7878d5636\",\n" + 
					"    \"documentHash\": \"string required\",\n" + 
					"    \"timestamp\": 1001001011\n" + 
					"},\n" + 
					"  \"registration_ids\":"+notificationVO.getNotificationIds()+"}";
			post.setEntity(new StringEntity(messageIPS.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			System.out.println(response);
			System.out.println(messageIPS);
		}
		catch (Throwable t) {
			t.printStackTrace();
			notificationVO.setNotificationStatus("Failed");
		}
		notificationVO.setNotificationStatus("Success");
		return notificationVO;
	}
}
