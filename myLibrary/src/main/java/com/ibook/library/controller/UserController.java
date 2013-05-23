package com.ibook.library.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.ibook.library.cst.Constants;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.service.CacheService;
import com.ibook.library.service.LibraryService;
import com.ibook.library.util.MD5;
import com.ibook.library.util.StringUtil;
import com.ibook.library.vo.UserLibraryVo;

/**
 * 
 * @author xiaojianyu
 *
 */
@Controller
public class UserController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger("library");

    @Autowired
    private CacheService cacheService;
    @Autowired
    private LibraryService libraryService;
    
    /**
     * 用户注册
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/userRegister")
    public String userRegister(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String passport = "";
            String password = "";
            if (request.getParameter("passport") != null) {
                passport = request.getParameter("passport");
            }
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
            }
            if (StringUtil.isEmpty(passport) || StringUtil.isEmpty(password)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【账号或密码不能为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return null;
            }
            UserInfo userInfo = cacheService.getUserInfo(passport);
            if (null != userInfo) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【账号已经被注册】空即是色，色即是空!");
                getJson(request, response, callback, result.toString());
                return null;
            }
            userInfo = cacheService.saveUserInfo(passport, MD5.getMD5Str(password));
            if (null == userInfo) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【注册失败】我们关心的，不是你是否失败了，而是你对失败能否无怨!");
                getJson(request, response, callback, result.toString());
                return null;
            }
            // 注册成功
            putUserGlobal(request, response, passport);
//            result.put(Constants.STATUS, 1);
//            result.put(Constants.MSG, "【注册成功】!");
//            getJson(request, response, callback, result.toString());
        } catch (Exception e) {
            logger.error("用户注册异常 " + e);
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
        return "forward:/user/index";
    }

    /**
     * 用户登录
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/userLogin")
    public void userLogin(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String passport = "";
            String password = "";
            if (request.getParameter("passport") != null) {
                passport = request.getParameter("passport");
            }
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
            }
            if (StringUtil.isEmpty(passport) || StringUtil.isEmpty(password)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【账号或密码不能为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            UserInfo userInfo = cacheService.getUserInfo(passport);
            if (null == userInfo) {
                userInfo = cacheService.saveUserInfo(passport, MD5.getMD5Str(password));
                if (null == userInfo) {
                    result.put(Constants.STATUS, -1);
                    result.put(Constants.MSG, "【注册失败】我们关心的，不是你是否失败了，而是你对失败能否无怨!");
                    getJson(request, response, callback, result.toString());
                    return;
                }
            }
            if (!MD5.getMD5Str(password).equals(userInfo.getPassword())) {
                logger.info("密码错误");
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【密码错误】圣杯在古老的玫瑰林下面等待!");
                getJson(request, response, callback, result.toString());
                return;
            }
            // 登录成功
            putUserGlobal(request, response, passport);
            result= JSONObject.fromObject(userInfo);
            result.put(Constants.STATUS, 1);
            result.put("password", "");
            result.put(Constants.MSG, "【登录成功】!");
            getJson(request, response, callback, result.toString());
        } catch (Exception e) {
            logger.error("用户登录异常 " + e);
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
     * 用户登录
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/login")
    public String login(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String passport = "";
            String password = "";
            if (request.getParameter("passport") != null) {
                passport = request.getParameter("passport");
            }
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
            }
            if (StringUtil.isEmpty(passport) || StringUtil.isEmpty(password)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【账号或密码不能为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return null;
            }
            UserInfo userInfo = cacheService.getUserInfo(passport);
            if (null == userInfo) {
                userInfo = cacheService.saveUserInfo(passport, MD5.getMD5Str(password));
                if (null == userInfo) {
                    result.put(Constants.STATUS, -1);
                    result.put(Constants.MSG, "【注册失败】我们关心的，不是你是否失败了，而是你对失败能否无怨!");
                    getJson(request, response, callback, result.toString());
                    return null;
                }
            }
            if (!MD5.getMD5Str(password).equals(userInfo.getPassword())) {
                logger.info("密码错误");
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【密码错误】圣杯在古老的玫瑰林下面等待!");
                getJson(request, response, callback, result.toString());
                return null;
            }
            // 登录成功
            putUserGlobal(request, response, passport);
//            result.put(Constants.STATUS, 1);
//            result.put(Constants.MSG, "【登录成功】!");
//            getJson(request, response, callback, result.toString());
        } catch (Exception e) {
            logger.error("用户登录异常 " + e);
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
        return "forward:/user/index";
    }
    
    /**
     * 用户退出
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/loginOut")
    public String userLoginOut(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            // 退出成功
            removeUserGlobal(request, response);
        } catch (Exception e) {
            logger.error("用户退出异常 " + e);
        }
        return "redirect:/user/index";
    }
    
    /**
     * 找回密码
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/findPassword")
    public void findPassword(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String passport = "";
            if (request.getParameter("passport") != null) {
                passport = request.getParameter("passport");
            }
            if (StringUtil.isEmpty(passport)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【账号不能为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            boolean flag=libraryService.findPassword(passport);
            if (!flag) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【密码重置失败】请稍后再试!");
                getJson(request, response, callback, result.toString());
                return;
            }
            // 密码重置成功
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "【密码已发送至注册账号邮箱，请查阅邮件找回秘密】!");
            getJson(request, response, callback, result.toString());
        } catch (Exception e) {
            logger.error("用户注册异常 " + e);
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
     * 修改密码
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/modifyPassword")
    public void modifyPassword(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String password = "";
            if (request.getParameter("password") != null) {
                password = request.getParameter("password");
            }
            if (StringUtil.isEmpty(password)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【密码不能为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            userInfo.setPassword(MD5.getMD5Str(password));
            boolean flag=libraryService.updateUserInfo(userInfo);
            if (!flag) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【密码修改失败】请稍后再试!");
                getJson(request, response, callback, result.toString());
                return;
            }
            // 密码修改成功
            result.put(Constants.STATUS, 1);
            result.put(Constants.MSG, "【密码修改成功】我又安全啦!");
            getJson(request, response, callback, result.toString());
        } catch (Exception e) {
            logger.error("用户注册异常 " + e);
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
     * 用户添加图书馆
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/addLibrary")
    public void addLibrary(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }          
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String libraryName = "";
            String libraryAddr = "";
            String libraryDesc = "";
            String longitude = "";
            String latitude = "";
            String citys="";
            Library library=new Library();
            if (request.getParameter("libraryName") != null) {
                libraryName = request.getParameter("libraryName");
                library.setName(libraryName);
            }
            if (request.getParameter("libraryAddr") != null) {
                libraryAddr = request.getParameter("libraryAddr");
                library.setAddress(libraryAddr);
            }
            if (request.getParameter("libraryDesc") != null) {
                libraryDesc = request.getParameter("libraryDesc");
                library.setDesc(libraryDesc);
            }
            if (request.getParameter("longitude") != null) {//东经
                longitude = request.getParameter("longitude");
                library.setLongitude(Float.valueOf(longitude));
            }
            if (request.getParameter("latitude") != null) {//北纬
                latitude = request.getParameter("latitude");
                library.setLatitude(Float.valueOf(latitude));
            }
            if (request.getParameter("citys") != null) {//北纬
                citys = request.getParameter("citys");
                String[] address=citys.split("==");
//              province+"^"+.city+"^"+.district+"^"+.street+"^"+.streetNumber
                library.setProvice(address.length>0?address[0]:"");
                library.setCity(address.length>1?address[1]:"");
                library.setDistrict(address.length>2?address[2]:"");
                library.setStreet(address.length>3?address[3]:"");
                library.setStreetNumber(address.length>4?address[4]:"");
            }
            if (StringUtil.isEmpty(libraryName)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书馆名字为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            UserLibrary userLibrary=libraryService.saveUserLibrary(library, userInfo.getId());
            if(null==userLibrary){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书馆添加失败】亲爱的,我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书馆添加成功】OY,可以出海啦!");
                getJson(request, response, callback, result.toString()); 
            }
        } catch (Exception e) {
            logger.error("用户拥有图书馆异常 " + e);
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
     * 用户查询图书馆
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/library")
    public void getUserLibrarys(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            List<UserLibraryVo> list=libraryService.getUserLibraryList(userInfo.getId());
            if(null==list){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书馆查询失败】额,我是来打酱油的!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书馆查询成功】我想拥有一所大房子,面朝大海!");
                result.put(Constants.USERLIBRARYLIST, list);               
                getJson(request, response, callback, result.toString()); 
            }
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
     * 用户添加图书
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/addBook")
    public void addBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String identity = "";
            if (request.getParameter("identity") != null) {
                identity = request.getParameter("identity");
            }
            if (StringUtil.isEmpty(identity)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            Book book=libraryService.saveBook(userInfo.getId(),userInfo.getPassport(), identity);
            if(null==book){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书添加失败】亲爱的,我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书添加成功】OY,又上了一个台阶!");
                getJson(request, response, callback, result.toString()); 
            }
        } catch (Exception e) {
            logger.error("用户拥有图书馆异常 " + e);
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
     * 借阅图书申请
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/borrowBook")
    public void borrowBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String bookId = "";
            String msg="";
            
            if(userInfo.getBorrowedLimit()<=0){
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【达到同时借阅本数上限】先看完书再借吧！");
                getJson(request, response, callback, result.toString());
                return;
            }
            
            if (request.getParameter("id") != null) {
                bookId = request.getParameter("id");
            }
            if (request.getParameter("msg") != null) {
                msg = request.getParameter("msg");
            }
            if (StringUtil.isEmpty(bookId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            if (StringUtil.isEmpty(msg)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【消息为空】发个消息撒,我才好认识你呀！");
                getJson(request, response, callback, result.toString());
                return;
            }
            boolean flag=libraryService.saveBorrowBookRequest(userInfo.getId(),userInfo.getPassport(),userInfo.getBorrowedLimit(),Integer.valueOf(bookId), msg);
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【申请成功】代书板可以查看申请哦！");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借阅申请又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("用户拥有图书馆异常 " + e);
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
     * 发送消息
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/sendMessage")
    public void sendMessage(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String oldMsgId = "";
            String msg="";
            if (request.getParameter("id") != null) {
                oldMsgId = request.getParameter("id");
            }
            if (request.getParameter("msg") != null) {
                msg = request.getParameter("msg");
            }
            if (StringUtil.isEmpty(oldMsgId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【原消息id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            if (StringUtil.isEmpty(msg)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【消息为空】发个消息撒,我才好认识你呀！");
                getJson(request, response, callback, result.toString());
                return;
            }
            int msgId=libraryService.sendMessage(userInfo.getId(),Integer.valueOf(oldMsgId), msg);
            if(msgId>0){
                result.put(Constants.STATUS, 1);
                result.put("nick", userInfo.getNick());
                result.put("msgId", msgId);
                result.put(Constants.MSG, "【消息发送成功】遇见一位爱书之人,美哉!");
                
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【消息发送又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("用户拥有图书馆异常 " + e);
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
     * 图书已归还
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/revertBook")
    public void revertBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.revertBook(userInfo.getId(), Integer.valueOf(logId));
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书状态更新成功】遇见一位爱书之人,美哉!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【图书状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("图书状态更新异常 " + e);
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
     * 退出图书馆
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/quitLibrary")
    public void quitLibrary(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String userLibraryId = "";
            String libraryId="";
            if (request.getParameter("userLibraryId") != null) {
                userLibraryId = request.getParameter("userLibraryId");
            }
            if (request.getParameter("libraryId") != null) {
                libraryId = request.getParameter("libraryId");
            }
            if (StringUtil.isEmpty(userLibraryId)||StringUtil.isEmpty(libraryId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【用户图书馆id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.quitLibrary(Integer.valueOf(userLibraryId), Integer.valueOf(libraryId), userInfo.getId());
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【退出图书馆成功】");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【退出图书馆又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("图书状态更新异常 " + e);
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
     * 修改昵称
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/updateNick")
    public void updateNick(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String nick = "";
            if (request.getParameter("nick") != null) {
                nick = request.getParameter("nick");
            }
            if (StringUtil.isEmpty(nick)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【昵称为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }
            userInfo.setNick(nick);
            boolean flag=libraryService.updateUserInfo(userInfo);
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【修改昵称成功】");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【修改昵称又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("图书状态更新异常 " + e);
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
     * 同意图书借出
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/approveBorrowBook")
    public void approveBorrowBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.approveBorrowBook(userInfo.getId(), Integer.valueOf(logId));
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书同意借出】遇见一位爱书之人,美哉!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借出状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("借出状态更新异常 " + e);
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
     * 拒绝图书借出
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/rejectBorrowBook")
    public void rejectBorrowBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.rejectBorrowBook(userInfo.getId(), Integer.valueOf(logId));
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书拒绝借出】");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借出状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("借出状态更新异常 " + e);
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
     * 赠送图书
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/presentBook")
    public void presentBook(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.presentBook(userInfo.getId(), Integer.valueOf(logId));
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【图书已赠送成功】遇见一位爱书之人,美哉!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借出状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("借出状态更新异常 " + e);
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
     * 对借阅者差评
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/appraiseBad")
    public void appraiseBad(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.appraiseTheBorrowed(userInfo.getId(), Integer.valueOf(logId),Constants.RELIABLE_STATUS_NO);
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【给了对方一个不靠评价】");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借出状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("借出状态更新异常 " + e);
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
     * 对借阅者好评
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/user/appraiseGood")
    public void appraiseGood(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            UserInfo userInfo=getLoginUserInfo(cacheService,request, response);
            if(null==userInfo){
                return ;
            }
            JSONObject result = new JSONObject();
            // 解决跨域
            String callback = request.getParameter("callback");
            String logId = "";
            if (request.getParameter("id") != null) {
                logId = request.getParameter("id");
            }
            if (StringUtil.isEmpty(logId)) {
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借书记录id为空】悟空你又调皮了-_-");
                getJson(request, response, callback, result.toString());
                return;
            }

            boolean flag=libraryService.appraiseTheBorrowed(userInfo.getId(), Integer.valueOf(logId),Constants.RELIABLE_STATUS_YES);
            if(flag){
                result.put(Constants.STATUS, 1);
                result.put(Constants.MSG, "【给了对方一个靠谱评价】遇见一位爱书之人,美哉!");
                getJson(request, response, callback, result.toString());
            }else{
                result.put(Constants.STATUS, -1);
                result.put(Constants.MSG, "【借出状态更新又罢工啦】我错了,原谅我吧!");
                getJson(request, response, callback, result.toString());
            }
        } catch (Exception e) {
            logger.error("借出状态更新异常 " + e);
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
