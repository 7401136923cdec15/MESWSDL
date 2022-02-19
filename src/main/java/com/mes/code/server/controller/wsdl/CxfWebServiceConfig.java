package com.mes.code.server.controller.wsdl;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mes.code.server.service.MESService;

/**
 * cxf配置类
 * 
 * @author oKong
 *
 */
@Configuration
public class CxfWebServiceConfig {
	@Autowired
	private Bus bus;
	@Autowired
	private MESService MESService;

	@Bean("cxfServletRegistration")
	public ServletRegistrationBean<CXFServlet> dispatcherServlet() {
		// 注册servlet 拦截/ws 开头的请求 不设置 默认为：/services/*
		return new ServletRegistrationBean<CXFServlet>(new CXFServlet(), "/ws/*");
	}

	/*
	 * 发布endpoint
	 */
	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, MESService);
		endpoint.publish("/MESService");// 发布地址

		return endpoint;
	}
}
