package com.ibook.library.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibook.library.cst.Constants;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.service.CacheService;
import com.ibook.library.util.DESUtil;
import com.ibook.library.util.StringUtil;
import com.ibook.library.util.ToolUtil;

public class BaseController {
    
    private static Logger logger = LoggerFactory.getLogger("library");


    /**
     * 保存用户登录的session
     * @param request
     * @param passport
     */
    protected void putUserGlobal(final HttpServletRequest request,final HttpServletResponse response,String passport){
        request.getSession().setAttribute(Constants.USER_NAME, passport);
        Cookie cookie=new Cookie(Constants.USER_NAME,DESUtil.getIntance().encryptStr(passport));
        cookie.setMaxAge(60*60*24*30);//存30天
        cookie.setDomain(".ijieshu.com");
        cookie.setPath("/");
        response.addCookie(cookie); 
    }
    
    /**
     * 删除用户登录的session
     * @param request
     * @param passport
     */
    protected void removeUserGlobal(final HttpServletRequest request,final HttpServletResponse response){
        request.getSession().removeAttribute(Constants.USER_NAME);
        Cookie cookie=new Cookie(Constants.USER_NAME,null);
        cookie.setMaxAge(0);//
        cookie.setDomain(".ijieshu.com");
        cookie.setPath("/");
        response.addCookie(cookie); 
    }
    
    /**
     * 保存用户登录的session
     * @param request
     * @param passport
     */
    protected String getUserGlobal(final HttpServletRequest request,final HttpServletResponse response){
        String passport=(String)request.getSession().getAttribute(Constants.USER_NAME);
        if(StringUtil.isEmpty(passport)){//session 不存在
            passport=getCookieValue(request,Constants.USER_NAME);
            if(StringUtil.isEmpty(passport)){//cookie也不存在
                return null;
            }
            try {
                passport = DESUtil.getIntance().decryptStr(passport);//cookie存的passport解密
            } catch (Exception e) {
                removeUserGlobal(request, response);
                return null;
            }
        }
        putUserGlobal(request, response, passport);
        return passport;
    }
    
    protected UserInfo getLoginUserInfo(CacheService cacheService,final HttpServletRequest request, final HttpServletResponse response){
        UserInfo userInfo=null;
        String passport = getUserGlobal(request, response);
        if(null==passport){
            return null;
        }
        userInfo=cacheService.getUserInfo(passport);
        return userInfo;
    }
    
    protected void printGBKNoCache(HttpServletResponse response, String result) {
        response.setContentType("text/html; charset=GBK");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getOutputStream().write(result.getBytes("GBK"));
            response.flushBuffer();
        }
        catch (IOException e) {
        }
    }
    
  //组装json 解决跨域
    protected void getJson(HttpServletRequest request, HttpServletResponse response, String callback,String result){
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            StringBuilder sb = new StringBuilder();
            if(StringUtil.isEmpty(callback))
            {
                sb.append(result).toString();
            }else{
                sb.append(ToolUtil.filterHtml(callback)).append("(").append(result).append(");").toString();
            }
            
            byte[] data = sb.toString().getBytes("utf-8");
            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
        } catch (Exception e) {
            logger.error("print result error!", e);
        }
    }

    /**
     * 根据Cookie的name返回value
     * 
     * @param request
     * @return
     */
    protected  String getCookieValue(HttpServletRequest request , String cookieName) {
        Cookie[] cookies = request.getCookies();
        String key = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    key = cookies[i].getValue();
                }
            }
        }
        return key;
    }
}
