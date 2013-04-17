package com.ibook.library.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.rubyeye.xmemcached.exception.MemcachedException;

import com.ibook.library.cache.Cache;
import com.ibook.library.cst.Constants;
import com.ibook.library.dao.LibraryDao;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.LibraryBook;
import com.ibook.library.entity.UserBookLog;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.entity.UserMessage;
import com.ibook.library.service.CacheService;
import com.ibook.library.vo.BookVo;

@Service
public class CacheServiceImpl implements CacheService {

    protected static Logger logger = LoggerFactory.getLogger("library");
    
    @Autowired
    private Cache cache;
    
    @Autowired
    private LibraryDao libraryDao;
    
    /**失效时间常量 2天**/
    private static final int EXPIRE=60*60*24*2;//失效时间为2天
 
    /**失效时间常量一 1分钟**/
    private static final int EXPIRE_1_MINUTE=60*60*24*2;//失效时间为1分钟
    
    public UserInfo getUserInfo(String passport) {
        String cacheKey = Constants.CACHE_KEY_USER_INFO +passport;
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) cache.getFromMemcached(cacheKey);
            if(null==userInfo){
                userInfo=libraryDao.getUserInfo(passport);
                if(null!=userInfo){
                    putUserInfo(userInfo);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserInfo cacheKey:"+cacheKey, e);
            userInfo=libraryDao.getUserInfo(passport);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserInfo cacheKey:"+cacheKey, e);
            userInfo=libraryDao.getUserInfo(passport);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserInfo cacheKey:"+cacheKey, e);
            userInfo=libraryDao.getUserInfo(passport);
        }catch (Exception e) {
            logger.error("memcached exception,getUserInfo cacheKey:"+cacheKey, e);
        }
        return userInfo;
    }

    public void putUserInfo(UserInfo userInfo) {
        String cacheKey = Constants.CACHE_KEY_USER_INFO +userInfo.getPassport();
        try {
            cache.putToMemcached(cacheKey,EXPIRE, userInfo);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserInfo cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserInfo cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserInfo cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserInfo cacheKey:"+cacheKey, e);
        }
    }

    public UserInfo saveUserInfo(String passport, String password) {
        UserInfo userInfo=new UserInfo();
        userInfo.setPassport(passport);
        userInfo.setPassword(password);
        userInfo.setDescripiton("法国传奇美食家布里亚-萨瓦兰将自己跌宕起伏的一生化作美食奇谈，讲述美食背后的故事，所写的主题与人们的日常生活息息相关。");
        userInfo.setNick("厨房里的哲学家");
        userInfo.setImg("http://img3.douban.com/mpic/s24628502.jpg");
        Integer id=libraryDao.saveUserInfo(userInfo);
        if(null!=id){
            userInfo.setId(id);
        }else{
            return null;
        }
        return userInfo;
    }

    public Library saveLibrary(Library library) {
        Integer id=libraryDao.saveLibrary(library);
        if(null!=id){
            library.setId(id);
        }else{
            return null;
        }
        return library;
    }

    public UserLibrary saveUserLibrary(UserLibrary userLibrary) {
        Integer id=libraryDao.saveUserLibrary(userLibrary);
        if(null!=id){
            userLibrary.setId(id);
            removeUserLibrary(userLibrary.getUserId());
        }else{
            return null;
        }
        return userLibrary;
    }

    public List<UserLibrary> getUserLibraryList(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_LIBRARY +userId;
        List<UserLibrary> list = null;
        try {
            list = (List<UserLibrary>) cache.getFromMemcached(cacheKey);
            if(null==list){
                list=libraryDao.getUserLibraryList(userId);
                if(null!=list){
                    putUserLibraryList(userId, list);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserInfo cacheKey:"+cacheKey, e);
            list=libraryDao.getUserLibraryList(userId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserInfo cacheKey:"+cacheKey, e);
            list=libraryDao.getUserLibraryList(userId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserInfo cacheKey:"+cacheKey, e);
            list=libraryDao.getUserLibraryList(userId);
        }catch (Exception e) {
            logger.error("memcached exception,getUserInfo cacheKey:"+cacheKey, e);
            list=new ArrayList<UserLibrary>(0);
        }
        return list;
    }

    public void putUserLibraryList(int userId,List<UserLibrary> list) {
        String cacheKey = Constants.CACHE_KEY_USER_LIBRARY +userId;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, list);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserLibraryList cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserLibraryList cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserLibraryList cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserLibraryList cacheKey:"+cacheKey, e);
        }
    }

    public void removeUserLibrary(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_LIBRARY +userId;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserLibraryList cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserLibraryList cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserLibraryList cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserLibraryList cacheKey:"+cacheKey, e);
        }
    }
    
    public Library getLibrary(int id) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY +id;
        Library library = null;
        try {
            library = (Library) cache.getFromMemcached(cacheKey);
            if(null==library){
                library=libraryDao.getLibrary(id);
                if(null!=library){
                    putLibrary(library,library.getId());
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(id);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(id);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(id);
        } catch (Exception e) {
            logger.error("memcached exception,getLibrary cacheKey:"+cacheKey, e);
        }
        return library;
    }

    public void putLibrary(Library library,int id) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY +id;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, library);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putLibrary cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putLibrary cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putLibrary cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,putLibrary cacheKey:"+cacheKey, e);
        }
    }
    
    public void putLibrary(Library library) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY +library.getName();
        try {
            cache.putToMemcached(cacheKey,EXPIRE, library);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putLibrary cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putLibrary cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putLibrary cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,putLibrary cacheKey:"+cacheKey, e);
        }
    }

    public Library getLibrary(String name) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY +name;
        Library library = null;
        try {
            library = (Library) cache.getFromMemcached(cacheKey);
            if(null==library){
                library=libraryDao.getLibrary(name);
                if(null!=library){
                    putLibrary(library);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(name);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(name);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getLibrary cacheKey:"+cacheKey, e);
            library=libraryDao.getLibrary(name);
        } catch (Exception e) {
            logger.error("memcached exception,getLibrary cacheKey:"+cacheKey, e);
        }
        return library;
    }

    public Book saveBook(Book book) {
        Integer id=libraryDao.saveBook(book);
        if(null!=id){
            book.setId(id);
            removeBookList(book.getOwnerUserId());
        }else{
            return null;
        }
        return book;
    }

    public List<Book> getBookList(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK +userId;
        List<Book> list = null;
        try {
            list = (List<Book>) cache.getFromMemcached(cacheKey);
            if(null==list){
                list=libraryDao.getUserBookList(userId);
                if(null!=list){
                    putBookList(userId, list);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getBookList cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookList(userId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getBookList cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookList(userId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getBookList cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookList(userId);
        } catch (Exception e) {
            logger.error("memcached exception,getBookList cacheKey:"+cacheKey, e);
            list=new ArrayList<Book>(0);
        }
        return list;
    }

    public void putBookList(int userId, List<Book> list) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK +userId;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, list);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putBookList cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putBookList cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putBookList cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,putBookList cacheKey:"+cacheKey, e);
        }
    }

    public void removeBookList(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK +userId;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeBookList cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeBookList cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeBookList cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,removeBookList cacheKey:"+cacheKey, e);
        }
    }

    public List<BookVo> getLibraryBookList(int userId, String query) {
        List<BookVo> list=new ArrayList<BookVo>();
        Set<Integer> mybookIds=null;
        if(0!=userId){
            mybookIds=libraryDao.getBookIdList(userId, query);
            for(Integer bookId:mybookIds){
                BookVo bookVo=new BookVo();
                Book book=getBook(bookId);
                BeanUtils.copyProperties(book,bookVo);
                List<UserLibrary> libraryList=getUserLibraryList(book.getOwnerUserId());
                Set<Integer> libraryids=new HashSet<Integer>(libraryList.size());
                for(UserLibrary userLibrary:libraryList){
                    libraryids.add(userLibrary.getLibraryId());
                }
                list.add(bookVo);
            } 
        }
        Set<Integer> bookIds=libraryDao.getBookIdList(query);
        for(Integer bookId:bookIds){
            if(null==mybookIds || !mybookIds.contains(bookId)){
                BookVo bookVo=new BookVo();
                Book book=getBook(bookId);
                BeanUtils.copyProperties(book,bookVo);
                List<UserLibrary> libraryList=getUserLibraryList(book.getOwnerUserId());
                Set<Integer> libraryids=new HashSet<Integer>(libraryList.size());
                for(UserLibrary userLibrary:libraryList){
                    libraryids.add(userLibrary.getLibraryId());
                }
                list.add(bookVo);             
            }
        }
        return list;
    }

    public Integer saveLibraryBook(LibraryBook libraryBook) {
        return libraryDao.saveLibraryBook(libraryBook);
    }

    public Book getBook(int id) {
        String cacheKey = Constants.CACHE_KEY_BOOK +id;
        Book book = null;
        try {
            book = (Book) cache.getFromMemcached(cacheKey);
            if(null==book){
                book=libraryDao.getBook(id);
                if(null!=book){
                    putBook(book);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getBook cacheKey:"+cacheKey, e);
            book=libraryDao.getBook(id);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getBook cacheKey:"+cacheKey, e);
            book=libraryDao.getBook(id);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getBook cacheKey:"+cacheKey, e);
            book=libraryDao.getBook(id);
        } catch (Exception e) {
            logger.error("memcached exception,getBook cacheKey:"+cacheKey, e);
        }
        return book;
    }

    public void putBook(Book book) {
        String cacheKey = Constants.CACHE_KEY_BOOK +book.getId();
        try {
            cache.putToMemcached(cacheKey,EXPIRE, book);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putBook cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putBook cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putBook cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putBook cacheKey:"+cacheKey, e);
        }
    }

    public UserBookLog saveUserBookLog(UserBookLog userBookLog) {
        Integer id= libraryDao.saveUserBookLog(userBookLog);
        if(null==id){
           return null; 
        }
        removeUserBookLogs(userBookLog.getBorrowUserId()); 
        removeUserBookLogs(userBookLog.getOwnerUserId());
        userBookLog.setId(id);
        return userBookLog;
    }

    public UserMessage saveUserMessage(UserMessage userMessage) {
        Integer id= libraryDao.saveUserMessage(userMessage);
        if(null==id){
           return null; 
        }
        userMessage.setId(id);
        removeUserMessages(userMessage.getLogId());
        return userMessage;
    }

    public boolean updateBook(Book book) {
        boolean flag=libraryDao.updateBook(book);
        if(flag){
            removeBook(book.getId());
        }
        return flag;
    }

    public void removeBook(int id) {
        String cacheKey = Constants.CACHE_KEY_BOOK +id;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeBook cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeBook cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeBook cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,removeBook cacheKey:"+cacheKey, e);
        }
    }

    public List<UserBookLog> getUserBookLogs(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOGS +userId;
        List<UserBookLog> list = null;
        try {
            list = (List<UserBookLog>) cache.getFromMemcached(cacheKey);
            if(null==list){
                list=libraryDao.getUserBookLogList(userId);
                if(null!=list){
                    putUserBookLogs(userId, list);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserBookLogs cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookLogList(userId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserBookLogs cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookLogList(userId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserBookLogs cacheKey:"+cacheKey, e);
            list=libraryDao.getUserBookLogList(userId);
        }catch (Exception e) {
            logger.error("memcached exception,getUserBookLogs cacheKey:"+cacheKey, e);
            list=new ArrayList<UserBookLog>(0);
        }
        return list;
    }

    public void removeUserBookLogs(int userId) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOGS +userId;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeUserBookLogs cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeUserBookLogs cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeUserBookLogs cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,removeUserBookLogs cacheKey:"+cacheKey, e);
        }
    }

    public void putUserBookLogs(int userId, List<UserBookLog> list) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOGS +userId;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, list);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserBookLogs cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserBookLogs cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserBookLogs cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserBookLogs cacheKey:"+cacheKey, e);
        }
    }

    public List<UserMessage> getUserMessages(int userId) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeUserMessages(int logId) {
        String cacheKey = Constants.CACHE_KEY_USER_LOG_MSG +logId;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeUserMessages cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeUserMessages cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeUserMessages cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,removeUserMessages cacheKey:"+cacheKey, e);
        }
    }

    public List<Library> getLibrarys(String query) {
        return libraryDao.getLibrarys(query);
    }

    public boolean lockLibraryBook(int bookId) {
        return libraryDao.lockLibraryBook(bookId);
    }

    public List<UserMessage> getUserLogMessages(int logId) {
        String cacheKey = Constants.CACHE_KEY_USER_LOG_MSG +logId;
        List<UserMessage> list = null;
        try {
            list = (List<UserMessage>) cache.getFromMemcached(cacheKey);
            if(null==list){
                list=libraryDao.getUserMessages(logId);
                if(null!=list){
                    putUserLogMessages(logId, list);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserLogMessages cacheKey:"+cacheKey, e);
            list=libraryDao.getUserMessages(logId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserLogMessages cacheKey:"+cacheKey, e);
            list=libraryDao.getUserMessages(logId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserLogMessages cacheKey:"+cacheKey, e);
            list=libraryDao.getUserMessages(logId);
        } catch (Exception e) {
            logger.error("memcached exception,getUserLogMessages cacheKey:"+cacheKey, e);
            list=new ArrayList<UserMessage>(0);
        }
        return list;
    }

    public void putUserLogMessages(int logId, List<UserMessage> list) {
        String cacheKey = Constants.CACHE_KEY_USER_LOG_MSG +logId;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, list);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserLogMessages cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserLogMessages cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserLogMessages cacheKey:"+cacheKey, e);
        } catch (Exception e) {
            logger.error("memcached exception,putUserLogMessages cacheKey:"+cacheKey, e);
        }
    }

    public UserMessage getUserMessage(int id) {
        String cacheKey = Constants.CACHE_KEY_USER_MSG +id;
        UserMessage userMessage = null;
        try {
            userMessage = (UserMessage) cache.getFromMemcached(cacheKey);
            if(null==userMessage){
                userMessage=libraryDao.getUserMessage(id);
                if(null!=userMessage){
                    putUserMessage(userMessage);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserMessage cacheKey:"+cacheKey, e);
            userMessage=libraryDao.getUserMessage(id);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserMessage cacheKey:"+cacheKey, e);
            userMessage=libraryDao.getUserMessage(id);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserMessage cacheKey:"+cacheKey, e);
            userMessage=libraryDao.getUserMessage(id);
        }catch (Exception e) {
            logger.error("memcached exception,getUserMessage cacheKey:"+cacheKey, e);
        }
        return userMessage;
    }

    public void putUserMessage(UserMessage userMessage) {
        String cacheKey = Constants.CACHE_KEY_USER_MSG +userMessage.getId();
        try {
            cache.putToMemcached(cacheKey,EXPIRE, userMessage);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserMessage cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserMessage cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserMessage cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserMessage cacheKey:"+cacheKey, e);
        }
    }

    public UserBookLog getUserBookLog(int logId) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOG +logId;
        UserBookLog userBookLog = null;
        try {
            userBookLog = (UserBookLog) cache.getFromMemcached(cacheKey);
            if(null==userBookLog){
                userBookLog=libraryDao.getUserBookLog(logId);
                if(null!=userBookLog){
                    putUserBookLog(userBookLog);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserBookLog cacheKey:"+cacheKey, e);
            userBookLog=libraryDao.getUserBookLog(logId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserBookLog cacheKey:"+cacheKey, e);
            userBookLog=libraryDao.getUserBookLog(logId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserBookLog cacheKey:"+cacheKey, e);
            userBookLog=libraryDao.getUserBookLog(logId);
        }catch (Exception e) {
            logger.error("memcached exception,getUserBookLog cacheKey:"+cacheKey, e);
        }
        return userBookLog;
    }

    public void putUserBookLog(UserBookLog userBookLog) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOG +userBookLog.getId();
        try {
            cache.putToMemcached(cacheKey,EXPIRE, userBookLog);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserBookLog cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserBookLog cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserBookLog cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserBookLog cacheKey:"+cacheKey, e);
        }
    }

    public void removeUserBookLog(int id) {
        String cacheKey = Constants.CACHE_KEY_USER_BOOK_LOG +id;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeUserBookLog cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeUserBookLog cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeUserBookLog cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,removeUserBookLog cacheKey:"+cacheKey, e);
        }
    }
    
    public boolean updateUserBookLog(UserBookLog userBookLog) {
        boolean flag=libraryDao.updateUserBookLog(userBookLog);
        if(flag){
            removeUserBookLog(userBookLog.getId());
            removeUserBookLogs(userBookLog.getOwnerUserId());
            removeUserBookLogs(userBookLog.getBorrowUserId());
        }
        return flag;
    }

    public boolean unLockLibraryBook(int bookId) {
        return libraryDao.unLockLibraryBook(bookId);
    }

    public boolean deleteUserLibrary(int id, int userId) {
        boolean flag=libraryDao.delUserLibrary(id);
        if(flag){
            removeUserLibrary(userId);
        }
        return flag;
    }

    public boolean deleteLibrartBook(int libraryId, int userId) {
        boolean flag=libraryDao.delLibraryBook(libraryId, userId);
        return flag;
    }

    public int getLibraryBookCount(int libraryId) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_BOOK_COUNT +libraryId;
        Integer count = null;
        try {
            count = (Integer) cache.getFromMemcached(cacheKey);
            if(null==count){
                count=libraryDao.getLibraryBookCount(libraryId);
                putibraryBookCount(libraryId,count);
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getLibraryBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryBookCount(libraryId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getLibraryBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryBookCount(libraryId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getLibraryBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryBookCount(libraryId);
        }catch (Exception e) {
            logger.error("memcached exception,getLibraryBookCount cacheKey:"+cacheKey, e);
        }
        return null==count?0:count;
    }

    public int getBookCount() {
        String cacheKey = Constants.CACHE_KEY_BOOK_COUNT;
        Integer count = null;
        try {
            count = (Integer) cache.getFromMemcached(cacheKey);
            if(null==count){
                count=libraryDao.getBookCount();
                putBookCount(count);
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getBookCount();
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getBookCount();
        } catch (MemcachedException e) {
            logger.error("memcached exception,getBookCount cacheKey:"+cacheKey, e);
            count=libraryDao.getBookCount();
        }catch (Exception e) {
            logger.error("memcached exception,getBookCount cacheKey:"+cacheKey, e);
        }
        return null==count?0:count;
    }

    public void putibraryBookCount(int libraryId, Integer count) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_BOOK_COUNT +libraryId;
        try {
            cache.putToMemcached(cacheKey,EXPIRE, count);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putibraryBookCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putibraryBookCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putibraryBookCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putibraryBookCount cacheKey:"+cacheKey, e);
        }
    }

    public void putBookCount(Integer count) {
        String cacheKey = Constants.CACHE_KEY_BOOK_COUNT;
        try {
            cache.putToMemcached(cacheKey,EXPIRE_1_MINUTE, count);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putibraryBookCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putibraryBookCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putibraryBookCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putibraryBookCount cacheKey:"+cacheKey, e);
        }
    }

    public void removeLibraryBookCount(int libraryId) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_BOOK_COUNT +libraryId;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeLibraryBookCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeLibraryBookCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeLibraryBookCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,removeLibraryBookCount cacheKey:"+cacheKey, e);
        }
    }

    public void removeBookCount() {
        String cacheKey = Constants.CACHE_KEY_BOOK_COUNT;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeBookCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeBookCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeBookCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,removeBookCount cacheKey:"+cacheKey, e);
        }
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        boolean flag=libraryDao.updateUserInfo(userInfo);
        if(flag){
            removeUserInfo(userInfo.getPassport());
        }
        return flag;
    }

    public void removeUserInfo(String passport) {
        String cacheKey = Constants.CACHE_KEY_USER_INFO +passport;
        try {
            cache.remove(cacheKey);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,removeUserInfo cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,removeUserInfo cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,removeUserInfo cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,removeUserInfo cacheKey:"+cacheKey, e);
        }
    }

    public int getLibraryUserCount(int libraryId) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_USER_COUNT +libraryId;
        Integer count = null;
        try {
            count = (Integer) cache.getFromMemcached(cacheKey);
            if(null==count){
                count=libraryDao.getLibraryUserCount(libraryId);
                putLibraryUserCount(libraryId,count);
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getLibraryUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryUserCount(libraryId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getLibraryUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryUserCount(libraryId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getLibraryUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryUserCount(libraryId);
        }catch (Exception e) {
            logger.error("memcached exception,getLibraryUserCount cacheKey:"+cacheKey, e);
        }
        return null==count?0:count;
    }

    public void putLibraryUserCount(int libraryId, int count) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_USER_COUNT +libraryId;
        try {
            cache.put(cacheKey, EXPIRE_1_MINUTE, count);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putLibraryUserCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putLibraryUserCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putLibraryUserCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putLibraryUserCount cacheKey:"+cacheKey, e);
        }
    }

    @Override
    public UserLibrary getUserLibrary(int userId, int libraryId) {
        String cacheKey = Constants.CACHE_KEY_USER_LIBRARY +userId+libraryId;
        UserLibrary userLibrary = null;
        try {
            userLibrary = (UserLibrary) cache.getFromMemcached(cacheKey);
            if(null==userLibrary){
                userLibrary=libraryDao.getUserLibrary(userId, libraryId);
                if(null!=userLibrary){
                    putUserLibrary(userLibrary);
                }
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserLibrary cacheKey:"+cacheKey, e);
            userLibrary=libraryDao.getUserLibrary(userId, libraryId);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserLibrary cacheKey:"+cacheKey, e);
            userLibrary=libraryDao.getUserLibrary(userId, libraryId);
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserLibrary cacheKey:"+cacheKey, e);
            userLibrary=libraryDao.getUserLibrary(userId, libraryId);
        }catch (Exception e) {
            logger.error("memcached exception,getUserLibrary cacheKey:"+cacheKey, e);
        }
        return userLibrary;
    }

    @Override
    public void putUserLibrary(UserLibrary userLibrary) {
        String cacheKey = Constants.CACHE_KEY_USER_LIBRARY +userLibrary.getUserId()+userLibrary.getLibraryId();
        try {
            cache.put(cacheKey, EXPIRE, userLibrary);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserLibrary cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserLibrary cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserLibrary cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserLibrary cacheKey:"+cacheKey, e);
        }
    }

    @Override
    public List<BookVo> getBookList(String query) {
        List<BookVo> list=new ArrayList<BookVo>();
        Set<Integer> bookIds=libraryDao.getBookIdList(query);
        for(Integer bookId:bookIds){
            BookVo bookVo=new BookVo();
            Book book=getBook(bookId);
            BeanUtils.copyProperties(book,bookVo);
            List<UserLibrary> libraryList=getUserLibraryList(book.getOwnerUserId());
            Set<Integer> libraryids=new HashSet<Integer>(libraryList.size());
            for(UserLibrary userLibrary:libraryList){
                libraryids.add(userLibrary.getLibraryId());
            }
            list.add(bookVo);
        }
        return list;
    }

    @Override
    public int getUserCount() {
        String cacheKey = Constants.CACHE_KEY_USER_COUNT;
        Integer count = null;
        try {
            count = (Integer) cache.getFromMemcached(cacheKey);
            if(null==count){
                count=libraryDao.getUserCount();
                putUserCount(count);
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getUserCount();
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getUserCount();
        } catch (MemcachedException e) {
            logger.error("memcached exception,getUserCount cacheKey:"+cacheKey, e);
            count=libraryDao.getUserCount();
        }catch (Exception e) {
            logger.error("memcached exception,getUserCount cacheKey:"+cacheKey, e);
        }
        return null==count?0:count;
    }

    public void putUserCount(Integer count) {
        String cacheKey = Constants.CACHE_KEY_USER_COUNT;
        try {
            cache.putToMemcached(cacheKey,EXPIRE_1_MINUTE, count);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putUserCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putUserCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putUserCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putUserCount cacheKey:"+cacheKey, e);
        }
    }
    
    @Override
    public int getLibraryCount() {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_COUNT;
        Integer count = null;
        try {
            count = (Integer) cache.getFromMemcached(cacheKey);
            if(null==count){
                count=libraryDao.getLibraryCount();
                putLibraryCount(count);
            }
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,getLibraryCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryCount();
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,getLibraryCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryCount();
        } catch (MemcachedException e) {
            logger.error("memcached exception,getLibraryCount cacheKey:"+cacheKey, e);
            count=libraryDao.getLibraryCount();
        }catch (Exception e) {
            logger.error("memcached exception,getLibraryCount cacheKey:"+cacheKey, e);
        }
        return null==count?0:count;
    }
    
    public void putLibraryCount(Integer count) {
        String cacheKey = Constants.CACHE_KEY_LIBRARY_COUNT;
        try {
            cache.putToMemcached(cacheKey,EXPIRE_1_MINUTE, count);
        } catch (TimeoutException e) {
            logger.error("memcached  timeout,putLibraryCount cacheKey:"+cacheKey, e);
        } catch (InterruptedException e) {
            logger.error("memcached interrupt,putLibraryCount cacheKey:"+cacheKey, e);
        } catch (MemcachedException e) {
            logger.error("memcached exception,putLibraryCount cacheKey:"+cacheKey, e);
        }catch (Exception e) {
            logger.error("memcached exception,putLibraryCount cacheKey:"+cacheKey, e);
        }
    }
}
