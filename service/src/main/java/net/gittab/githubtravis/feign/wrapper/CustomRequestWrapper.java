package net.gittab.githubtravis.feign.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.Charsets;

/**
 * CustomRequestWrapper.
 *
 * @author xiaohua zhou
 **/
public class CustomRequestWrapper extends HttpServletRequestWrapper {

	private String body;

	/**
	 * Constructs a request object wrapping the given request.
	 * @param request The request to wrap
	 * @throws IllegalArgumentException if the request is null
	 * @throws IOException io exception
	 */
	public CustomRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				char[] charBuffer = new char[4096];
				int bytesRead;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
		}
		catch (IOException ex) {
			throw ex;
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ex) {
					throw ex;
				}
			}
		}
		this.body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				this.body.getBytes(StandardCharsets.UTF_8));
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(
				new InputStreamReader(this.getInputStream(), Charsets.UTF_8));
	}

	@Override
	public String getParameter(String name) {
		return super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return super.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return super.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return super.getParameterValues(name);
	}

	public String getBody() {
		return this.body;
	}

	/**
	 * 设置自定义post参数.
	 * @param paramMap params map
	 */
	public void setParamsMaps(Map<String, Object> paramMap) {
		Map<String, Object> paramBodyMap = new HashMap<>(1000);
		if (!StringUtils.isEmpty(this.body)) {
			paramBodyMap = JSONObject.parseObject(this.body, Map.class);
		}
		paramBodyMap.putAll(paramMap);
		this.body = JSONObject.toJSONString(paramBodyMap);
	}

}
