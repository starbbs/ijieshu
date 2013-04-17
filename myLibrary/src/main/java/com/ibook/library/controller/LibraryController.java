package com.ibook.library.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibook.library.cst.Constants;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.service.CacheService;
import com.ibook.library.service.LibraryService;
import com.ibook.library.vo.BookLogMessageVo;
import com.ibook.library.vo.BookVo;
import com.ibook.library.vo.UserLibraryVo;

/**
 * 
 * @author xiaojianyu
 *
 */
@Controller
public class LibraryController extends BaseController{

    protected static Logger logger = LoggerFactory.getLogger("library");
    
    @Autowired
    private CacheService cacheService;
    @Autowired
    private LibraryService libraryService;
  
    @RequestMapping(value = "/")
    public String first(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        if(null==userInfo){
            return "redirect:/user/index";
        } 
        String url="";
        if (request.getParameter("url") != null) {
            url = request.getParameter("url");
            return "redirect:"+url.trim();
        }
        return "userInfo.jsp";
    }
    
    @RequestMapping(value = "/user/myself")
    public String userInfo(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        if(null==userInfo){
            return "redirect:/user/index";
        } 
        List<UserLibraryVo> list=libraryService.getUserLibraryList(userInfo.getId());
        List<Book> bookList=libraryService.getBookList(userInfo.getId());
        request.setAttribute("userInfo", userInfo);
        request.setAttribute("libraryList", list);
        request.setAttribute("bookList", bookList);
        return "userInfo.jsp";
    }
    
    @RequestMapping(value = "/user/mylibrary")
    public String libraryBooks(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        int userId=0;
        if(null!=userInfo){
            userId=userInfo.getId();
        }
        String query="";
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
        }
        List<BookVo> bookList=libraryService.getLibraryBookList(userId, query);
        request.setAttribute("bookList", bookList);
        request.setAttribute("userInfo", userInfo);
        return "myLibrary.jsp";
    }

    @RequestMapping(value = "/user/index")
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        if(null!=userInfo){
            request.setAttribute("userInfo", userInfo);
        }
        String query="";
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
        }
        int bookCount=libraryService.getBookCount();
        int libraryCount=libraryService.getLibraryCount();
        int userCount=libraryService.getUserCount();

        List<BookVo> bookList=libraryService.getBookList(query);
        request.setAttribute("bookList", bookList);
        request.setAttribute("bookCount", bookCount);
        request.setAttribute("libraryCount", libraryCount);
        request.setAttribute("userCount", userCount);
        return "index.jsp";
    }
    
    @RequestMapping(value = "/user/myLend")
    public String lendLog(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        if(null==userInfo){
            return "redirect:/user/index";
        }
        List<BookLogMessageVo> list=libraryService.getBookLogMessageVos(userInfo.getId());
        request.setAttribute("BookLogMessageVoList", list);
        request.setAttribute("userInfo", userInfo);
        return "myMessage.jsp";
    }
    
    /**
     * 查询图书馆
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/library/queryLibrary")
    public void queryLibrary(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            JSONArray array=new JSONArray();
            // 解决跨域
            String callback = request.getParameter("callback");
            String query = "";
            if (request.getParameter("query") != null) {
                query = request.getParameter("query");
            }

            /*if (StringUtil.isEmpty(query)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【查询条件为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }*/
            List<Library> list=libraryService.getLibrarys(query);
            for(Library library:list){
                JSONObject obj = new JSONObject();
                obj.put("id", library.getId());
                obj.put("name", library.getName());
                obj.put("desc", library.getDesc());
                obj.put("addr", library.getAddress());
                obj.put("longitude", library.getLongitude());
                obj.put("latitude", library.getLatitude());
                obj.put("bookCount", cacheService.getLibraryBookCount(library.getId()));
                obj.put("userCount", cacheService.getLibraryUserCount(library.getId()));
                array.put(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "【查询成功】有料哦!");
            result.put("array", array);
            getJson(request, response, callback, result.toString());
            return;
        } catch (Exception e) {
            logger.error("图书馆查询异常 " + e);
            String callback = request.getParameter("callback");
            JSONObject result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            getJson(request, response, callback, result.toString());
        }
    }
    
    /**
     * 查询图书馆的整体统计信息
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/library/summary")
    public void summary(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            int bookCount=libraryService.getBookCount();
            int libraryCount=libraryService.getLibraryCount();
            int userCount=libraryService.getUserCount();
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "【查询成功】有料哦!");
            result.put("bookCount", bookCount);
            result.put("bookCount", libraryCount);
            result.put("bookCount", userCount);
            getJson(request, response, callback, result.toString());
            return;
        } catch (Exception e) {
            logger.error("图书馆统计信息查询异常 " + e);
            String callback = request.getParameter("callback");
            JSONObject result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            getJson(request, response, callback, result.toString());
        }
    }
}
