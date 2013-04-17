package com.ibook.library.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HttpClientUtil {
    /**
     * 
     */
    private static final Log log = LogFactory.getLog("active-front");

    /**
     * 默认字符集
     */
    public static final String DefaultEncoding = "utf-8"; 
    /**
     * 
     */
    private MultiThreadedHttpConnectionManager httpClientManager;
    /**
     * 
     */
    private HttpClient httpClient;
    /**
     * 
     */
    private static HttpClientUtil _instance;

    /**
     * @return
     * @throws Exception
     */
    public static HttpClientUtil getInstance() throws Exception {
        if (_instance == null) {
            synchronized (HttpClientUtil.class) {
                if (_instance == null) {
                    _instance = new HttpClientUtil();
                }
            }
        }
        return _instance;
    }

    /**
     * 
     * @throws Exception .
     */
    private HttpClientUtil() throws Exception {
        init();
    }

    /**
     * 
     * @throws Exception .
     */
    private void init() throws Exception {
        httpClientManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = httpClientManager.getParams();
        params.setStaleCheckingEnabled(true);
        params.setMaxTotalConnections(320);
        params.setDefaultMaxConnectionsPerHost(160);
        params.setConnectionTimeout(3000);
        params.setSoTimeout(3000);

        httpClient = new HttpClient(httpClientManager);
        
    }

    public PostMethod post(HttpUrl httpUrl,Cookie[] cookies) throws Exception {
        httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        HttpState initialState = new HttpState();
        initialState.addCookies(cookies);
        httpClient.setState(initialState);
        
        return this.post(httpUrl);
    }
    /**
     * 
     * @param httpUrl
     * @throws Exception
     */
    public PostMethod post(HttpUrl httpUrl) throws Exception {

        if (httpUrl == null)
            return null;

        HostConfiguration config = new HostConfiguration();
        config.setHost(httpUrl.getHost(), httpUrl.getPort());

        PostMethod post = new PostMethod(httpUrl.getPath());
        
        String encoding = httpUrl.getEncoding();
        if (encoding != null) {
            post.getParams().setContentCharset(encoding);
        }
        
        setParams(httpUrl.getParams(), post);

        
        Cookie[] cookies = httpClient.getState().getCookies();
        if (cookies != null && cookies.length > 0) {
            log.debug("Present cookies : ");
            for (int i = 0; i < cookies.length; i++) {
                log.debug(i + " : " + cookies[i].toExternalForm() + " - domain :" + cookies[i].getDomain()
                        + " - value :" + cookies[i].getValue());
            }
        }
        
        int result = httpClient.executeMethod(config, post);

        log.debug("HttpClient.executeMethod returns result = [" + result + "]");
        log.debug("HttpClient.executeMethod returns :");
        log.debug(post.getResponseBodyAsString());

        if (result != 200)
            throw new Exception("wrong HttpClient.executeMethod post method !");

        return post;
    }

    /**
     * 
     * @param httpUrl
     * @return
     * @throws Exception .
     */
    public PostMethod postNew(HttpUrl httpUrl) throws Exception {
        if (httpUrl == null)
            return null;

        HostConfiguration config = new HostConfiguration();
        config.setHost(httpUrl.getHost(), httpUrl.getPort());

        PostMethod post = new PostMethod(httpUrl.getPath());

        post.setRequestHeader("User-Agent", "SOHUSnsBot");
        String encoding = httpUrl.getEncoding();
        if (encoding != null) {
            post.getParams().setContentCharset(encoding);
        }

        StringRequestEntity requestEntity = new StringRequestEntity(httpUrl.getRequetsBody(), httpUrl.getContentType(),
                encoding);
        post.setRequestEntity(requestEntity);

        int result = httpClient.executeMethod(config, post);

        if (log.isDebugEnabled()) {
            log.debug("HttpClient.executeMethod returns result = [" + result + "]");
        }
        if (result != 200)
            throw new Exception("wrong HttpClient.executeMethod post method !");

        return post;
    }
    
    public GetMethod get(PostUrl postUrl) throws Exception {

		if (null == postUrl)
			return null;

		HostConfiguration config = new HostConfiguration();
		config.setHost(postUrl.getHost(), postUrl.getPort());
		GetMethod get = new GetMethod(postUrl.getPath());
//		if(null != postUrl.getParams()){
//			setParams(postUrl.getParams(), get);
//		}
		int result = httpClient.executeMethod(config,get);
		log.info("HttpClient.executeMethod :"+postUrl.getHost()+postUrl.getPath()+" [" + result+ "]");
		if (result != 200)
			throw new Exception("wrong HttpClient.executeMethod get method !");

		return get;
	}
    /**
     * HTTP Get
     * @param httpUrl
     * @return
     * @throws Exception .
     */
    public GetMethod get(HttpUrl httpUrl) throws Exception {

        if (httpUrl == null)
            return null;

        HostConfiguration config = new HostConfiguration();
        config.setHost(httpUrl.getHost(), httpUrl.getPort());

        GetMethod get = new GetMethod(httpUrl.getPath());
        get.setRequestHeader("User-Agent", "SOHUSnsBot");
        get.setRequestHeader("Referer", "http://bai.sohu.com/");
        setParams(httpUrl.getParams(), get);
        
        if (log.isDebugEnabled()) {
            log.debug("query : " + config.getHostURL() + get.getPath() + "?"+ get.getQueryString());
            
        }
        int result = httpClient.executeMethod(config, get);
        if (log.isDebugEnabled()) {
            log.debug("HttpClient.executeMethod returns result = [" + result + "]");
           
        }
        if (result != 200)
            throw new Exception("wrong HttpClient.executeMethod get method !");

        return get;
    }
    
    /**
     * 
     * @param httpUrl
     * @param encoding
     * @return
     * @throws Exception
     */
    public String get(HttpUrl httpUrl, String encoding) throws Exception {
        GetMethod get = this.get(httpUrl);
        if (get != null) {
            String result = getResponseAsString(get.getResponseBodyAsStream(), encoding);
            return result;
        } else
            return null;
    }
    /**
     * 
     * @param params
     * @param post .
     */
    private void setParams(HashMap<Object , Object> params, PostMethod post) {
        if (params != null && params.size() > 0) {
            Iterator<Object> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                post.addParameter(key, (String) params.get(key));
            }
        }
    }
    /**
     * 
     * @param params
     * @param get .
     */
    private void setParams(HashMap<Object , Object> params, GetMethod get) {
        StringBuffer strParams = new StringBuffer();

        if (params != null && params.size() > 0) {
            Iterator<Object> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                strParams.append(key);
                strParams.append("=");
                strParams.append(params.get(key));
                strParams.append("&");
            }
        }

        if (strParams.lastIndexOf("&") == strParams.length())
            strParams.deleteCharAt(strParams.length());

        get.setQueryString(strParams.toString());
    }
    
    
    /**
     * 
     * @param host
     * @param port
     * @param path
     * @param xml
     * @return
     * @throws Exception
     */
    public String postXML(String host, int port, String path,String xml) throws Exception {
    	//log.debug("start post");
        HttpUrl httpUrl = new HttpUrl();		
        httpUrl.setHost(host);
        httpUrl.setPath(path);
        httpUrl.setPort(port);  
        HostConfiguration config = new HostConfiguration();
        config.setHost(httpUrl.getHost(), httpUrl.getPort());

        PostMethod post = new PostMethod(httpUrl.getPath());
        setParams(httpUrl.getParams(), post);

        StringRequestEntity requestEntity = new StringRequestEntity(xml, null, "utf-8");
        post.setRequestEntity(requestEntity);
        Cookie[] cookies = httpClient.getState().getCookies();
        if (cookies != null && cookies.length > 0) {
           // log.debug("Present cookies : ");
            for (int i = 0; i < cookies.length; i++) {
                log.debug(i + " : " + cookies[i].toExternalForm() + " - domain :" + cookies[i].getDomain()
                        + " - value :" + cookies[i].getValue());
            }
        }
        
        int result = httpClient.executeMethod(config, post);
        if (result != 200)
            throw new Exception("wrong HttpClient.executeMethod post method !");

        return getResponseAsString(post.getResponseBodyAsStream(), DefaultEncoding);
    }
    /**
     * 
     * @param is
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String getResponseAsString(InputStream is, String encoding) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		if (encoding != null) {
			in = new BufferedReader(new InputStreamReader(is, encoding));
		} else {
			in = new BufferedReader(new InputStreamReader(is));
		}
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		String response=sb.toString();
		//log.info("response is "+response);
		return response;
	}
    
}
