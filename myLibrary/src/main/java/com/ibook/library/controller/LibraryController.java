package com.ibook.library.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.ibook.library.cst.Constants;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.service.CacheService;
import com.ibook.library.service.LibraryService;
import com.ibook.library.util.NumberUtil;
import com.ibook.library.util.Page;
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
      /*  UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        if(null==userInfo){
            return "redirect:/user/index";
        } 
        List<UserLibraryVo> list=libraryService.getUserLibraryList(userInfo.getId());
        List<Book> bookList=libraryService.getBookList(userInfo.getId());
        request.setAttribute("userInfo", userInfo);
        request.setAttribute("libraryList", list);
        request.setAttribute("bookList", bookList);
        return "userInfo.jsp";*/
        return "redirect:/user/index";
    }
    
    /**
     * 
     * 用户图书列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user/userBook")
    public void userBook(final HttpServletRequest request, final HttpServletResponse response) {     
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray();
        String callback = request.getParameter("callback");
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【登录为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            List<Book> bookList=libraryService.getBookList(userInfo.getId());
            for(Book book:bookList){
                JSONObject obj = JSONObject.fromObject(book);
                array.add(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "查询成功");
            result.put("array",array);
        } catch (Exception e) {
            logger.error("我的图书查询异常 " + e);
            callback = request.getParameter("callback");
            result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        getJson(request, response, callback, result.toString());

    }
    
    /**
     * 
     * 用户图书馆
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user/userLibrary")
    public void userLibrary(final HttpServletRequest request, final HttpServletResponse response) {     
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray();
        String callback = request.getParameter("callback");
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【登录为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            List<UserLibraryVo> list=libraryService.getUserLibraryList(userInfo.getId());
            for(UserLibraryVo userLibraryVo:list){
                JSONObject obj = JSONObject.fromObject(userLibraryVo);
                array.add(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "查询成功");
            result.put("array",array);
        } catch (Exception e) {
            logger.error("我的图书馆查询异常 " + e);
            callback = request.getParameter("callback");
            result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        getJson(request, response, callback, result.toString());

    }
    
    @RequestMapping(value = "/user/mylibrary")
    public String libraryBooks(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        int userId=0;
        int pageIndex=1;
        int pageSize=24;
        String query="";

        if(null!=userInfo){
            userId=userInfo.getId();
        }
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
        }
        if (request.getParameter("pageIndex") != null) {
            pageIndex =  NumberUtil.getInt(request.getParameter("pageIndex"), 1);
        }
        if (request.getParameter("pageSize") != null) {
            pageSize =  NumberUtil.getInt(request.getParameter("pageSize"), 21);
        }
        Page<BookVo> page =new Page<BookVo>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page=libraryService.getLibraryBookList(userId, query,page);
        request.setAttribute("userInfo", userInfo);
        request.setAttribute("page", page);
        request.setAttribute("query", query);
        return "myLibrary.jsp";
    }

    /**
     * 
     * 图书查询
     * @param request
     * @param response
     */
    @RequestMapping(value = "/searchBooks")
    public void searchBooks(final HttpServletRequest request, final HttpServletResponse response) {     
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray();
        String callback = request.getParameter("callback");
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            int userId=0;
            int pageIndex=1;
            int pageSize=24;
            String query="";

            if(null!=userInfo){
                userId=userInfo.getId();
            }
            if (request.getParameter("query") != null) {
                query = request.getParameter("query");
            }
            if (request.getParameter("pageIndex") != null) {
                pageIndex =  NumberUtil.getInt(request.getParameter("pageIndex"), 1);
            }
            if (request.getParameter("pageSize") != null) {
                pageSize =  NumberUtil.getInt(request.getParameter("pageSize"), 21);
            }
            Page<BookVo> page =new Page<BookVo>();
            page.setPageIndex(pageIndex);
            page.setPageSize(pageSize);
            page=libraryService.getLibraryBookList(userId, query,page);
            List<BookVo> bookList=page.getList();
            for(BookVo bookVo:bookList){
                JSONObject obj = JSONObject.fromObject(bookVo);
                array.add(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "查询成功");
            result.put("array",array);
            result.put("totlePages", page.getTotlePages());
            result.put("pageIndex",pageIndex);
            result.put("pageSize",pageSize);
            result.put("totle",page.getTotle());
        } catch (Exception e) {
            logger.error("图书查询异常 " + e);
            callback = request.getParameter("callback");
            result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        getJson(request, response, callback, result.toString());

    }
    
    @RequestMapping(value = "/user/index")
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
        List<BookVo> bookList=null;
        if(null!=userInfo){
            request.setAttribute("userInfo", userInfo);
            List<Book> myBookList=libraryService.getBookList(userInfo.getId());
            Page<BookVo> page =new Page<BookVo>();
            page.setPageIndex(1);
            page.setPageSize(24);
            page=libraryService.getLibraryBookList(userInfo.getId(), "",page);
            bookList=page.getList();
            request.setAttribute("myBookList", myBookList);
        }else{
            request.setAttribute("myBookList", new ArrayList<Book>());
            bookList=libraryService.getBookList("");
        }
        int bookCount=libraryService.getBookCount();
        int libraryCount=libraryService.getLibraryCount();
        int userCount=libraryService.getUserCount();

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
 
    
    @RequestMapping(value = "/user/userLend")
    public void userlendLog(final HttpServletRequest request, final HttpServletResponse response) {     
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray();
        String callback = request.getParameter("callback");
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【登录为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            List<BookLogMessageVo> list=libraryService.getBookLogMessageVos(userInfo.getId());
            for(BookLogMessageVo bookLogMessageVo:list){
                JSONObject obj = JSONObject.fromObject(bookLogMessageVo);
                array.add(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "查询成功");
            result.put("array",array);
        } catch (Exception e) {
            logger.error("我的借阅记录查询异常 " + e);
            callback = request.getParameter("callback");
            result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        getJson(request, response, callback, result.toString());

    }
    
    /**
     * 用户信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "/user")
    public void user(final HttpServletRequest request, final HttpServletResponse response) {     
        JSONObject result = new JSONObject();
        String callback = request.getParameter("callback");
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【登录为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            JSONObject obj = JSONObject.fromObject(userInfo);
            obj.put("password", "");//密码置空
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "查询成功");
            result.put("userInfo",obj);
        } catch (Exception e) {
            logger.error("我的借阅记录查询异常 " + e);
            callback = request.getParameter("callback");
            result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "靠,服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        getJson(request, response, callback, result.toString());

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
            String city = "北京市";
            if (request.getParameter("city") != null) {
                city = request.getParameter("city");
            }
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            Map<Integer,UserLibraryVo> myLibMap=null;
            if(null!=userInfo){
                List<UserLibraryVo> myLibrarys=libraryService.getUserLibraryList(userInfo.getId());
                myLibMap=new HashMap<Integer,UserLibraryVo>(myLibrarys.size());
                for(UserLibraryVo userLibraryVo:myLibrarys){
                    myLibMap.put(userLibraryVo.getLibraryId(), userLibraryVo);
                }
            }else{
                myLibMap=new HashMap<Integer,UserLibraryVo>(0);
            }

            List<Library> list=libraryService.getLibrarys(query,city);
            for(Library library:list){
                JSONObject obj = new JSONObject();
                obj.put("id", library.getId());
                obj.put("name", library.getName());
                obj.put("desc", library.getDesc());
                obj.put("addr", library.getAddress());
                obj.put("longitude", library.getLongitude());
                obj.put("latitude", library.getLatitude());
                obj.put("type", library.getType());
                if(myLibMap.containsKey(library.getId())){
                    obj.put("bookCount", myLibMap.get(library.getId()).getBookCount());
                    obj.put("userCount", myLibMap.get(library.getId()).getUserCount());
                    obj.put("userLibraryId", myLibMap.get(library.getId()).getId());
                    obj.put("myLib",true);
                }else{
                    obj.put("bookCount", cacheService.getLibraryBookCount(library.getId()));
                    obj.put("userCount", cacheService.getLibraryUserCount(library.getId()));
                    obj.put("myLib",false);
                }
                array.add(obj);
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
            result.put("libraryCount", libraryCount);
            result.put("userCount", userCount);
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
