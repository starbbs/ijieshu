package com.ibook.library.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebResourceReader {
	
    protected static Logger log = LoggerFactory.getLogger("library-front");

	private static String encoding = "gbk";

	public static String read(String url) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader in = null;
		GZIPInputStream gzin = null;
		GetMethod getMethod = null;
		try {
			PostUrl pu = new PostUrl();
			pu.setHost(getHost(url));
			pu.setPath(getPath(url));
			pu.setPort(80);
			
			// 创建GET方法的实例
			getMethod = HttpClientUtil.getInstance().get(pu);
			// 使用系统提供的默认的恢复策略
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

			// 执行getMethod
			int statusCode = getMethod.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.error("读取远程资源文件失败: "+ url);
			}
			InputStream is = getMethod.getResponseBodyAsStream();
			String contEncoding ="";
			Header head = getMethod.getResponseHeader(
					"Content-Encoding");
			if(null!=head){
				contEncoding = head.getValue();
			}
			
			/**
			 * 如果文件使用GZIP压缩，则用GZIP流进行处理
			 */
			if (StringUtils.isNotBlank(contEncoding)
					&& contEncoding.contains("gzip")) {
				// 读取内容
				gzin = new GZIPInputStream(is);
				in = new BufferedReader(new InputStreamReader(gzin, encoding));
			} else {
				in = new BufferedReader(new InputStreamReader(is, encoding));
			}
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			// 处理内容
//			System.out.println(buffer.toString());
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			log.error("Please check your provided http address!");
		} catch (IOException e) {
			log.error("Net work exception :"+e.getMessage());
			// 发生网络异常
		} catch (Exception e) {
			log.error(e.getMessage());
			 
		} finally {
			// 释放连接
		    if(null!=getMethod){
		        getMethod.releaseConnection();
		    }else{
		        log.error("getMethod is NUll exception");
		    }
			
			try {
			    if(null!=in){
			        in.close(); 
			    }else{
			        log.error("in is NUll exception"); 
			    }
				
			} catch (IOException e) {
				log.error("Read remote file exception : "+e.getMessage());
			}
		   /* getMethod.releaseConnection();
            try {
                in.close();
            } catch (IOException e) {
                log.error("Read remote file exception : "+e.getMessage());
            }*/
		}
		return buffer.toString();
	}
	
	public static String getHttpResponse(String url){
		BufferedReader in = null;
		InputStream is = null;
		GetMethod getMethod  = null;
		try {
			
			PostUrl pu = new PostUrl();
			pu.setHost(getHost(url));
			pu.setPath(getPath(url));
			pu.setPort(80);
			
			getMethod = HttpClientUtil.getInstance().get(pu);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			
			int statusCode = getMethod.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.error("HTTP 请求失败: "+ getMethod.getStatusLine());
			}
			
			is = getMethod.getResponseBodyAsStream();
			StringBuffer buffer = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			return buffer.toString();
			
		} catch (IOException e) {
			log.error("Net work exception ["+url+"] :"+e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				is.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			getMethod.releaseConnection();
		}
		return null;
	}
	
	private static String getHost(String url){
		 String regexp = "(http://)?([^/]*)(/?.*)";
		 return url.replaceAll(regexp, "$2");
	}
	
	private static String getQueryStr(String url){
		 if(url.contains("\\?"))
			 return url.split("?")[1];
		 return "";
	}
	
	
	private static String getPath(String url){
		 return url.replaceAll("http://"+getHost(url), "");
	}
	
	public static String doPost(String url, String param) {
		URL url1 = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL(url);
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			writer = new PrintWriter(connection.getOutputStream());
			writer.print(param);
			writer.flush();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			connection.disconnect();
			return sb.toString();
		} catch (IOException e) {
	         log.error("Net work exception ["+url+"] :"+e.getMessage());
	         return null;
//			throw new RuntimeException(e.getMessage(),e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if (writer != null) {
				writer.close();
			}
			if (connection != null)
				connection.disconnect();
		}
	}

	public static String doGet(String url) {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL(url);
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} catch (IOException e) {
	          log.error("Net work exception ["+url+"] :"+e.getMessage());
	          return null;
//	          throw new RuntimeException(e.getMessage(),e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
			if (connection != null)
				connection.disconnect();
		}
	}
	
	public static void main(String[] args) {
		
		String url = "http://hot.vrs.sohu.com/vrs_flash.action?vid=428294&ver=1";
//		System.out.println(WebResourceReader.getHttpResponse(url));
//		System.out.println(WebResourceReader.read("http://tv.sohu.com/frag/309638526/112712_309638526.inc"));
		System.out.println(WebResourceReader.doGet(url));
	}
}