package com.analyzer;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.hazelcast.config.Config;
import com.hazelcast.config.UserCodeDeploymentConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@ComponentScan(basePackages = {"com.analyzer"})
@Configuration
@SpringBootApplication
@EnableDiscoveryClient
public class AnalyzerServiceApplication {

	@Value("${request.timeout}")
	private int requestTimeOut;
	
	@Bean
	//Don't go directly to service go to server discovery first and get the service
	@LoadBalanced
	public WebClient.Builder getWenClientBuilder()
	{
	    HttpClient httpClient = HttpClient.create()
                				.tcpConfiguration(client ->
                				client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, requestTimeOut)
                				.doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(10))
                                .addHandlerLast(new WriteTimeoutHandler(10))));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));   
		return WebClient.builder().clientConnector(connector);
	}
	
//	@Bean
//	public Config hazelcastConfig() 
//	{
//	    Config config = new Config();
//	    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
//	    config.getNetworkConfig().getJoin().getEurekaConfig()
//	          .setEnabled(true)
//	          .setProperty("self-registration", "true")
//	          .setProperty("namespace", "hazelcast");
//	    return config;
//	}
	

   @Bean
    public Config hazelCastConfig() {
        final Config config = new Config();            
        config.setClassLoader(Thread.currentThread().getContextClassLoader());

        final UserCodeDeploymentConfig distCLConfig = config.getUserCodeDeploymentConfig();
        distCLConfig.setEnabled(true)
          .setClassCacheMode(UserCodeDeploymentConfig.ClassCacheMode.ETERNAL)
          .setProviderMode(UserCodeDeploymentConfig.ProviderMode.LOCAL_CLASSES_ONLY);

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(hazelCastConfig());
    }
	
	@Bean
	public MessageSource messageSource() {
	    Locale.setDefault(Locale.ENGLISH);
	    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	    messageSource.addBasenames("classpath:messages");
	    return messageSource;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AnalyzerServiceApplication.class, args);
	}
}
