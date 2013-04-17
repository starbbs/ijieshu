package com.ibook.library.util;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.protocol.Protocol;


public class PostUrl  {
	  private String path;

	  private HashMap params;

	  private String host;

	  private int port;

	  private Protocol protocol;

	  private String requetsBody;

	  private String encoding;

	  private String contentType;

	  /**
	   * @return Returns the host.
	   */
	  public String getHost() {
	    return host;
	  }

	  /**
	   * @param host
	   *          The host to set.
	   */
	  public void setHost(String host) {
	    this.host = host;
	  }

	  /**
	   * @return Returns the path.
	   */
	  public String getPath() {
	    return path;
	  }

	  /**
	   * @param path
	   *          The path to set.
	   */
	  public void setPath(String path) {
	    this.path = path;
	  }

	  /**
	   * @return Returns the port.
	   */
	  public int getPort() {
	    return port;
	  }

	  /**
	   * @param port
	   *          The port to set.
	   */
	  public void setPort(int port) {
	    this.port = port;
	  }

	  /**
	   * @return Returns the protocol.
	   */
	  public Protocol getProtocol() {
	    return protocol;
	  }

	  /**
	   * @param protocol
	   *          The protocol to set.
	   */
	  public void setProtocol(Protocol protocol) {
	    this.protocol = protocol;
	  }

	  /**
	   * @return
	   */
	  public HashMap getParams() {
	    return params;
	  }

	  /**
	   * @param params
	   */
	  public void setParams(HashMap params) {
	    this.params = params;
	  }

	  /**
	   * Clone method
	   */
	  public Object clone() {
		  PostUrl url = null;
	    try {
	      url = (PostUrl) super.clone();
	    } catch (CloneNotSupportedException e) {
	      e.printStackTrace();
	      url = new PostUrl();
	    }
	    return url;
	  }

	  public String getRequetsBody() {
	    return requetsBody;
	  }

	  public void setRequetsBody(String requetsBody) {
	    this.requetsBody = requetsBody;
	  }

	  public String getEncoding() {
	    return encoding;
	  }

	  public void setEncoding(String encoding) {
	    this.encoding = encoding;
	  }

	  public String getContentType() {
	    return contentType;
	  }

	  public void setContentType(String contentType) {
	    this.contentType = contentType;
	  }

	  public String toString() {
	    StringBuffer sb = new StringBuffer("http url is ");
	    sb.append("protocol: " + protocol);
	    sb.append(", host: " + host);
	    sb.append(", port: " + port);
	    sb.append(", path: " + path);
	    if (params != null && !params.keySet().isEmpty()) {
	      sb.append(", params[");
	      Iterator i = params.keySet().iterator();
	      while (i.hasNext()) {
	        Object key = i.next();
	        sb.append("&" + key + "=" + params.get(key));
	      }
	      sb.append("]");
	    }
	    return sb.toString();
	  }
	}
