/**
 * 
 */
package com.ibook.library.util;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.protocol.Protocol;


public class HttpUrl implements Cloneable {
    private String path;

    private HashMap<Object , Object> params;

    private String host;

    private int port;

    private Protocol protocol;

    private String requetsBody;

    private String encoding;

    private String contentType;

    
    /**
     * 
     */
    public HttpUrl() {
        super();
    }
    

    /**
     * @param host
     * @param port
     * @param protocol
     */
    public HttpUrl(String host, int port, Protocol protocol) {
        super();
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }


    /**
     * @return Returns the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            The host to set.
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
     *            The path to set.
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
     *            The port to set.
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
     *            The protocol to set.
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * @return
     */
    public HashMap<Object , Object> getParams() {
        return params;
    }

    /**
     * @param params
     */
    public void setParams(HashMap<Object , Object> params) {
        this.params = params;
    }

    /**
     * Clone method
     */
    public Object clone() {
        HttpUrl url = null;
        try {
            url = (HttpUrl) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            url = new HttpUrl();
        }
        return url;
    }
    /**
     * 
     * @return .
     */
    public String getRequetsBody() {
        return requetsBody;
    }
    /**
     * 
     * @param requetsBody .
     */
    public void setRequetsBody(String requetsBody) {
        this.requetsBody = requetsBody;
    }
    /**
     * 
     * @return .
     */
    public String getEncoding() {
        return encoding;
    }
    /**
     * 
     * @param encoding .
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     * 
     * @return .
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * 
     * @param contentType .
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    /**
     * 
     * @see java.lang.Object#toString() .
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("http url is ");
        sb.append("protocol: " + protocol);
        sb.append("host: " + host);
        sb.append(", port: " + port);
        sb.append(", path: " + path);
        if (params != null && !params.keySet().isEmpty()) {
            sb.append(", params[");
            Iterator<Object> i = params.keySet().iterator();
            while (i.hasNext()) {
                Object key = i.next();
                sb.append(key + "=" + params.get(key));
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
