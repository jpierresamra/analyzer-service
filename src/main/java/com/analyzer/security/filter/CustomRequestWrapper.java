package com.analyzer.security.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class CustomRequestWrapper extends HttpServletRequestWrapper {
	private class ResettableServletInputStream extends ServletInputStream {

		private ByteArrayInputStream stream;

	    @Override
	    public int read() throws IOException {
	        return stream.read();
	     }

		@Override
		public boolean isFinished() {
			return stream.available() == 0;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new RuntimeException("Not implemented");
		}
	   }
	private byte[] rawData;
	private ResettableServletInputStream servletStream;

	private HttpServletRequest wrapped;

	private Map<String, String[]> parameterMap;

	public CustomRequestWrapper(HttpServletRequest wrapped) {
		super(wrapped);
		this.wrapped = wrapped;
		this.servletStream = new ResettableServletInputStream();
	}

	public void addParameter(String name, String value) {
		if (parameterMap == null) {
			parameterMap = new HashMap<String, String[]>();
			parameterMap.putAll(wrapped.getParameterMap());
		}
		String[] values = parameterMap.get(name);
		if (values == null) {
			values = new String[0];
		}
		List<String> list = new ArrayList<String>(values.length + 1);
		list.addAll(Arrays.asList(values));
		list.add(value);
		parameterMap.put(name, list.toArray(new String[0]));
	}

	@Override
	public String getParameter(String name) {
		if (parameterMap == null) {
			return wrapped.getParameter(name);
		}

		String[] strings = parameterMap.get(name);
		if (strings != null) {
			return strings[0];
		}
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		if (parameterMap == null) {
			return wrapped.getParameterMap();
		}

		return Collections.unmodifiableMap(parameterMap);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (parameterMap == null) {
			return wrapped.getParameterNames();
		}

		return Collections.enumeration(parameterMap.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		if (parameterMap == null) {
			return wrapped.getParameterValues(name);
		}
		return parameterMap.get(name);
	}
	
	public void resetInputStream(byte[] newRawData) {
		this.rawData = newRawData;
	    servletStream.stream = new ByteArrayInputStream(newRawData);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ServletInputStream getInputStream() throws IOException {
	    if (rawData == null) {
	        rawData = IOUtils.toByteArray(this.wrapped.getReader());
	        servletStream.stream = new ByteArrayInputStream(rawData);
	    }
	    return servletStream;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BufferedReader getReader() throws IOException {
	    if (rawData == null) {
	        rawData = IOUtils.toByteArray(this.wrapped.getReader());
	        servletStream.stream = new ByteArrayInputStream(rawData);
	    }
	    return new BufferedReader(new InputStreamReader(servletStream));
	}

}
