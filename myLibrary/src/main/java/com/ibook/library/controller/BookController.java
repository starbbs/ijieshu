package com.ibook.library.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

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
import com.ibook.library.service.CacheService;
import com.ibook.library.service.LibraryService;
import com.ibook.library.util.StringUtil;
import com.ibook.library.util.WebResourceReader;
import com.ibook.library.vo.BookVo;

/**
 * 
 * @author xiaojianyu
 *
 */
@Controller
public class BookController extends BaseController{

    protected static Logger logger = LoggerFactory.getLogger("library");
 
    @Autowired
    private CacheService cacheService;
    @Autowired
    private LibraryService libraryService;
    /**
     * 豆瓣搜书
     * @throws IOException
     */
    @RequestMapping(value = "/book/search")
    public void searchBooks(
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            //解决跨域
            String callback =request.getParameter("callback");  
            String query = "";
            if(request.getParameter("query")!=null){
                query = request.getParameter("query");
            }
            if(StringUtil.isEmpty(query)){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG,"【条件为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            String books=WebResourceReader.doGet("http://api.douban.com/v2/book/search?q="+URLEncoder.encode(query.trim(),"utf-8"));
            logger.info(query.trim()+":"+books);
            response.getWriter().print(books);
        } catch (Exception e) {
            logger.error("书籍查询异常 " + e);
            String callback =request.getParameter("callback"); 
            JSONObject result = new JSONObject();
            try {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG,"服务歇菜了!");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }    
            getJson(request, response, callback, result.toString());
        }
    }
  
    @RequestMapping(value = "/book/mySearch")
    public void index(final HttpServletRequest request, final HttpServletResponse response)throws IOException {
        JSONObject result = new JSONObject();
        JSONArray array=new JSONArray();
        //解决跨域
        String callback =request.getParameter("callback");  
        String query="";
        if (request.getParameter("query") != null) {
            query = request.getParameter("query");
        }
        List<BookVo> bookList=libraryService.getBookList(query);
        try {
            for(BookVo bookVo:bookList){
                JSONObject obj = new JSONObject();
                obj.put("id", bookVo.getId());
                obj.put("title", bookVo.getTitle());
                obj.put("ownerUserId", bookVo.getOwnerUserId());
                obj.put("status", bookVo.getStatus());
                obj.put("mediumImg", bookVo.getMediumImg());
                obj.put("alt", bookVo.getAlt());
                array.add(obj);
            }
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "【查询成功】");
            result.put("array", array);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        getJson(request, response, callback, result.toString());
    }
}
