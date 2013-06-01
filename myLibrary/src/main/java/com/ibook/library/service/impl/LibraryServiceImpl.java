package com.ibook.library.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibook.library.cst.Constants;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.LibraryBook;
import com.ibook.library.entity.PresentBookLog;
import com.ibook.library.entity.UserBookLog;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.entity.UserMessage;
import com.ibook.library.service.CacheService;
import com.ibook.library.service.LibraryService;
import com.ibook.library.service.ThirdService;
import com.ibook.library.util.AppraiseCallBack;
import com.ibook.library.util.EmailUtil;
import com.ibook.library.util.MD5;
import com.ibook.library.util.Page;
import com.ibook.library.util.StringUtil;
import com.ibook.library.vo.BookLogMessageVo;
import com.ibook.library.vo.BookVo;
import com.ibook.library.vo.UserLibraryVo;
import com.ibook.library.vo.UserMessageVo;

@Service
public class LibraryServiceImpl implements LibraryService {
    protected static Logger logger = LoggerFactory.getLogger("library");

    private ExecutorService executorService = Executors.newFixedThreadPool(1024);

    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private ThirdService thirdService;

    public UserLibrary saveUserLibrary(Library library,int userId) {
        Library oldLibrary=cacheService.getLibrary(library.getName());
        if(null==oldLibrary){
            //创建新的图书馆
            library.setPhoto("");
            library.setCreateUserId(userId);
            library=cacheService.saveLibrary(library);
        }else{
            library=oldLibrary;
        }
        UserLibrary userLibrary=null;
        //拥有该图书馆
        if(null!=oldLibrary){
            userLibrary=cacheService.getUserLibrary(userId, oldLibrary.getId());           
        }
        if(null==userLibrary){
            userLibrary=new UserLibrary();
            userLibrary.setLibraryId(library.getId());
            userLibrary.setLibraryName(library.getName());
            userLibrary.setLibraryDesc(library.getDesc());
            userLibrary.setUserId(userId);
//            userLibrary.setNick(nick);
            userLibrary=cacheService.saveUserLibrary(userLibrary);
            if(null!=library &&null!=userLibrary){
                //新增图书馆，同步更新用户的书存入图书馆
                executorService.execute(new Library2BookCommand(userLibrary));
            } 
        }else{
            logger.info("ERROR 已经加入图书馆");
        }
        return userLibrary;
    }

    public List<UserLibraryVo> getUserLibraryList(int userId) {
        List<UserLibrary> list=cacheService.getUserLibraryList(userId);
        if(null==list){
            return null;
        }
        List<UserLibraryVo> voList=new ArrayList<UserLibraryVo>();
        for(UserLibrary userLibrary:list){
            UserLibraryVo userLibraryVo=new UserLibraryVo();
            BeanUtils.copyProperties(userLibrary, userLibraryVo);
            userLibraryVo.setBookCount(cacheService.getLibraryBookCount(userLibrary.getLibraryId()));
            userLibraryVo.setUserCount(cacheService.getLibraryUserCount(userLibrary.getLibraryId()));
            voList.add(userLibraryVo);
        }
        return voList;
    }

    public Book saveBook(int userId,String passport,String identity) {
        Book book=thirdService.getBookFromDouban(identity);
        book.setOwnerUserId(userId);
        book.setUploadUserId(userId);
        book.setOwnerPassport(passport);
        book.setUploadPassport(passport);
        book.setStatus(Constants.BOOK_STATUS_FREE);
        book=cacheService.saveBook(book);
        if(null!=book){
            //新增图书时，该书存入用户所拥有的图书馆内
            executorService.execute(new Book2LibraryCommand(book));
        }
        return book;
    }

    public List<Book> getBookList(int userId) {
        List<Book> list=cacheService.getBookList(userId);
        return list;
    }

    public Page<BookVo> getLibraryBookList(int userId, String query,Page<BookVo> page){
        page=cacheService.getLibraryBookList(userId, query,page);
        return page;
    }
    
    /**
     * 新增书时，更新书到用户所在的图书馆
     * 
     * @author xiaojianyu
     */
    public class Book2LibraryCommand implements Runnable {
        
        private Book book;
        
        public Book2LibraryCommand(Book book) {
            this.book=book;
        }

        public void run() {
            List<UserLibrary> list=cacheService.getUserLibraryList(book.getOwnerUserId());
            for(UserLibrary userLibrary:list){
                LibraryBook libraryBook=new LibraryBook();
                libraryBook.setBookId(book.getId());
                libraryBook.setImg(book.getMediumImg());
                libraryBook.setLibraryId(userLibrary.getLibraryId());
                libraryBook.setOwnerUserId(book.getOwnerUserId());
                libraryBook.setTitle(book.getTitle());
                libraryBook.setUrl(book.getAlt());
                cacheService.saveLibraryBook(libraryBook);
                cacheService.removeLibraryBookCount(userLibrary.getLibraryId());
            }
        }

    }
    
    /**
     * 新增书时，更新分类
     * 
     * @author xiaojianyu
     */
    public class Book2TagCommand implements Runnable {
        
        private Book book;
        
        public Book2TagCommand(Book book) {
            this.book=book;
        }

        public void run() {
            List<UserLibrary> list=cacheService.getUserLibraryList(book.getOwnerUserId());
            for(UserLibrary userLibrary:list){
                LibraryBook libraryBook=new LibraryBook();
                libraryBook.setBookId(book.getId());
                libraryBook.setImg(book.getMediumImg());
                libraryBook.setLibraryId(userLibrary.getLibraryId());
                libraryBook.setOwnerUserId(book.getOwnerUserId());
                libraryBook.setTitle(book.getTitle());
                libraryBook.setUrl(book.getAlt());
                cacheService.saveLibraryBook(libraryBook);
                cacheService.removeLibraryBookCount(userLibrary.getLibraryId());
            }
        }

    }
       
    /**
     * 删除用户图书馆的某本书
     * 
     * @author xiaojianyu
     */
    public class RemoveBook2LibraryCommand implements Runnable {
        
        private int  bookId;
        
        private int userId;
        
        public RemoveBook2LibraryCommand(int  bookId,int userId) {
            this.bookId=bookId;
            this.userId=userId;
        }

        public void run() {
            List<UserLibrary> list=cacheService.getUserLibraryList(userId);
            cacheService.delUserLibraryBook(bookId, userId);//图书馆的书同步删除;
            for(UserLibrary userLibrary:list){//情统计缓存
                cacheService.removeLibraryBookCount(userLibrary.getLibraryId());
            }
            
        }

    }
    
    /**
     * 新增图书馆,用户的书更新到图书馆
     * 
     * @author xiaojianyu
     */
    public class Library2BookCommand implements Runnable {
        
        private UserLibrary userLibrary;
        
        public Library2BookCommand(UserLibrary userLibrary) {
            this.userLibrary=userLibrary;
        }

        public void run() {
            List<Book> list=cacheService.getBookList(userLibrary.getUserId());
            for(Book book:list){
                LibraryBook libraryBook=new LibraryBook();
                libraryBook.setBookId(book.getId());
                libraryBook.setImg(book.getMediumImg());
                libraryBook.setLibraryId(userLibrary.getLibraryId());
                libraryBook.setOwnerUserId(book.getOwnerUserId());
                libraryBook.setTitle(book.getTitle());
                libraryBook.setUrl(book.getAlt());
                cacheService.saveLibraryBook(libraryBook);
            }
            cacheService.removeLibraryBookCount(userLibrary.getLibraryId());
        }

    }
    
    @PostConstruct
    public void registerShutDownHook() {
        logger.info("LibraryServiceImpl register shutdown hook");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    executorService.shutdown();
                    logger.info("LibraryServiceImpl shutdown");
                } catch (Exception e) {
                    logger.error("LibraryServiceImpl CardService error", e);
                }
            }
        });
    }

    public boolean saveBorrowBookRequest(int userId,String passport,int borrowedLimit,int bookId, String msg) {
        if(borrowedLimit<=0){
            return false;
        }
        Book book=cacheService.getBook(bookId);
        if(null==book ||book.getStatus()!=Constants.BOOK_STATUS_FREE || userId==book.getOwnerUserId()){
            return false;
        }
        //保存借书申请记录
        UserBookLog userBookLog=new UserBookLog();
        userBookLog.setBookId(book.getId());
        userBookLog.setOwnerUserId(book.getOwnerUserId());
        userBookLog.setOwnerPassport(book.getOwnerPassport());
        userBookLog.setBorrowUserId(userId);
        userBookLog.setBorrowPassport(passport);
        userBookLog.setStatus(Constants.BOOK_LOG_STATUS_APPLY);
        userBookLog=cacheService.saveUserBookLog(userBookLog);
        if(null==userBookLog){
            return false;
        }
        UserMessage userMessage=new UserMessage();
        userMessage.setLogId(userBookLog.getId());
        userMessage.setFromUserId(userId);
        userMessage.setMsg(msg);
        userMessage.setToUserId(book.getOwnerUserId());
        userMessage=cacheService.saveUserMessage(userMessage);
        if(null==userMessage){
            return false;
        }
        this.decrUserBorrowedLimit(userBookLog.getBorrowPassport());
        cacheService.lockLibraryBook(bookId);//图书馆的书同步锁定
        book.setBorrowPassport(passport);
        book.setBorrowUserId(userId);
        book.setStatus(Constants.BOOK_STATUS_LOCK);//锁定这本书
        return cacheService.updateBook(book);
    }

    public List<Library> getLibrarys(String query,String city) {
        return cacheService.getLibrarys(query,city);
    }

    public List<BookLogMessageVo> getBookLogMessageVos(int userId) {
        List<UserBookLog> list=cacheService.getUserBookLogs(userId);
        if(null==list){
            return null;
        }
        List<BookLogMessageVo> volist=new ArrayList<BookLogMessageVo>(list.size());
        UserInfo userInfo=null;
        UserInfo otherUser=null;
        for(UserBookLog userBookLog:list){         
            BookLogMessageVo bookLogMessageVo=new BookLogMessageVo();
            Book book=cacheService.getBook(userBookLog.getBookId());
            bookLogMessageVo.setLogId(userBookLog.getId());
            bookLogMessageVo.setStatus(userBookLog.getStatus());
            bookLogMessageVo.setOwnerPassport(userBookLog.getOwnerPassport());
            bookLogMessageVo.setOwnerUserId(userBookLog.getOwnerUserId());
            bookLogMessageVo.setBorrowUserId(userBookLog.getBorrowUserId());
            bookLogMessageVo.setBorrowPassport(userBookLog.getBorrowPassport());
            if(userBookLog.getOwnerUserId()==userId){
                bookLogMessageVo.setMyBook(true);
                if(null==userInfo){
                    userInfo=cacheService.getUserInfo(userBookLog.getOwnerPassport()); 
                }
                bookLogMessageVo.setOwnerNick(userInfo.getNick());
                
                otherUser=cacheService.getUserInfo(userBookLog.getBorrowPassport()); 
                if(null!=otherUser){
                    bookLogMessageVo.setBorrowNick(otherUser.getNick());
                    bookLogMessageVo.setBorrowReliableNum(otherUser.getReliableNum());
                    if(userBookLog.getBorrowReliable()==Constants.RELIABLE_STATUS_NULL && otherUser.getUnReliableNum()<3){
                        //不靠谱次数达到3次才显示给用户看
                        bookLogMessageVo.setBorrowUnReliableNum(0); 
                    }else{
                        bookLogMessageVo.setBorrowUnReliableNum(otherUser.getUnReliableNum());
                    }                   
                }             
            }else{
                bookLogMessageVo.setMyBook(false);
                if(null==userInfo){
                    userInfo=cacheService.getUserInfo(userBookLog.getBorrowPassport()); 
                }
                bookLogMessageVo.setBorrowReliableNum(userInfo.getReliableNum());
                bookLogMessageVo.setBorrowUnReliableNum(userInfo.getUnReliableNum());
                bookLogMessageVo.setBorrowNick(userInfo.getNick());
                
                otherUser=cacheService.getUserInfo(userBookLog.getOwnerPassport()); 
                if(null!=otherUser){
                    bookLogMessageVo.setOwnerNick(otherUser.getNick());
                    bookLogMessageVo.setOwnerReliableNum(otherUser.getReliableNum());
                    bookLogMessageVo.setOwnerUnReliableNum(otherUser.getUnReliableNum());  
                }
               
            }
 
            if(null!=book){
                bookLogMessageVo.setBookId(book.getId());
                bookLogMessageVo.setBookImg(book.getSmallImg());
                bookLogMessageVo.setTitle(book.getTitle());
            }
            List<UserMessage> msgList=cacheService.getUserLogMessages(userBookLog.getId());
            bookLogMessageVo.setMsgId(0);
            List<UserMessageVo> messages=new ArrayList<UserMessageVo>(msgList.size());
            for(UserMessage userMessage:msgList){
                if(userMessage.getId()>bookLogMessageVo.getMsgId()){
                    bookLogMessageVo.setMsgId(userMessage.getId());
                }
                UserMessageVo userMessageVo=new UserMessageVo();
                BeanUtils.copyProperties(userMessage, userMessageVo);
                if(userMessageVo.getFromUserId()==userInfo.getId()){
                    userMessageVo.setFromUserNick(userInfo.getNick());
                    userMessageVo.setFromUserPassport(userInfo.getPassport());
                    if(null!=otherUser){
                        userMessageVo.setToUserNick(otherUser.getNick());
                        userMessageVo.setToUserPassport(otherUser.getPassport());                     
                    }
                }else{
                    userMessageVo.setToUserNick(userInfo.getNick());
                    userMessageVo.setToUserPassport(userInfo.getPassport());
                    if(null!=otherUser){
                        userMessageVo.setFromUserNick(otherUser.getNick());
                        userMessageVo.setFromUserPassport(otherUser.getPassport());                    
                    }
                }
                messages.add(userMessageVo);
            }
            bookLogMessageVo.setMessages(messages);
            volist.add(bookLogMessageVo);
        }
        return volist;
    }

    public int sendMessage(int userId, int oldMsgId, String msg) {
        UserMessage oldUserMessage=cacheService.getUserMessage(oldMsgId);
        if(null==oldUserMessage){
            return 0;
        }
        UserMessage userMessage=new UserMessage();
        userMessage.setLogId(oldUserMessage.getLogId());
        userMessage.setFromUserId(userId);
        userMessage.setMsg(msg);
        userMessage.setToUserId(oldUserMessage.getFromUserId());
        userMessage.setOldMsgId(oldMsgId);
        userMessage=cacheService.saveUserMessage(userMessage);
        if(null==userMessage){
            return 0;
        }
        return userMessage.getId();
    }

    public boolean revertBook(int userId, int logId) {
        UserBookLog userBookLog=cacheService.getUserBookLog(logId);
        if(null==userBookLog || userBookLog.getOwnerUserId()!=userId){
            return false;
        }
        userBookLog.setStatus(Constants.BOOK_LOG_STATUS_REVERT);
        boolean flag=cacheService.updateUserBookLog(userBookLog);//更新借阅记录为已归还
        if(!flag){
            logger.error("revertBook ERROR,更新借阅记录失败");
            return false;
        }
        Book book=cacheService.getBook(userBookLog.getBookId());
        if(null==book){
            logger.error("revertBook ERROR,查找图书失败");
            return false;
        }
        book.setStatus(Constants.BOOK_STATUS_FREE);//解锁该书
        flag=cacheService.updateBook(book);
        if(!flag){
            logger.error("revertBook ERROR,图书解锁失败");
            return false;
        }
        this.incrUserBorrowedLimit(userBookLog.getBorrowPassport());
        return cacheService.unLockLibraryBook(userBookLog.getBookId());//图书馆的书同步解锁;
    }

    public boolean approveBorrowBook(int userId, int logId) {
        UserBookLog userBookLog=cacheService.getUserBookLog(logId);
        if(null==userBookLog || userBookLog.getOwnerUserId()!=userId){
            return false;
        }
        userBookLog.setStatus(Constants.BOOK_LOG_STATUS_BORROWED);
        boolean flag=cacheService.updateUserBookLog(userBookLog);//更新借阅记录为已借出
        if(!flag){
            logger.error("approveBorrowBook ERROR,更新借阅记录失败");
            return false;
        }
        return true;
    }
    
    public boolean rejectBorrowBook(int userId, int logId) {
        UserBookLog userBookLog=cacheService.getUserBookLog(logId);
        if(null==userBookLog || userBookLog.getOwnerUserId()!=userId){
            return false;
        }
        userBookLog.setStatus(Constants.BOOK_LOG_STATUS_REJECT);
        boolean flag=cacheService.updateUserBookLog(userBookLog);//更新借阅记录为拒绝借出
        if(!flag){
            logger.error("rejectBorrowBook ERROR,更新借阅记录失败");
            return false;
        }
        Book book=cacheService.getBook(userBookLog.getBookId());
        if(null==book){
            logger.error("rejectBorrowBook ERROR,查找图书失败");
            return false;
        }
        book.setStatus(Constants.BOOK_STATUS_FREE);//解锁该书
        flag=cacheService.updateBook(book);
        if(!flag){
            logger.error("rejectBorrowBook ERROR,图书解锁失败");
            return false;
        }
        this.incrUserBorrowedLimit(userBookLog.getBorrowPassport());
        return cacheService.unLockLibraryBook(userBookLog.getBookId());//图书馆的书同步解锁;
    }
    
    public AppraiseCallBack appraiseTheBorrowed(int userId,int logId,int reliable) {
        AppraiseCallBack appraiseCallBack=null;
        UserBookLog userBookLog=cacheService.getUserBookLog(logId);
        if(null==userBookLog || userBookLog.getOwnerUserId()!=userId){
            return AppraiseCallBack.FAILURE;
        }
        int oldReliable=userBookLog.getBorrowReliable();//原来的借阅评价记录               
        userBookLog.setBorrowReliable(reliable);
        boolean flag=cacheService.updateUserBookLog(userBookLog);//更新借阅者的靠谱信息
        if(!flag){
            logger.error("rejectBorrowBook ERROR,更新借阅记录失败");
            return AppraiseCallBack.FAILURE;
        }
        UserInfo userInfo=cacheService.getUserInfo(userBookLog.getBorrowPassport());
        if(Constants.RELIABLE_STATUS_YES==reliable){
            userInfo.setReliableNum(userInfo.getReliableNum()+1);
            if(Constants.RELIABLE_STATUS_YES==oldReliable){
                return AppraiseCallBack.Duplicate;
            }else if(Constants.RELIABLE_STATUS_NO==oldReliable){
                userInfo.setUnReliableNum(userInfo.getUnReliableNum()>1?userInfo.getUnReliableNum()-1:0);
                appraiseCallBack=AppraiseCallBack.RESCUCCES;
            }else{
                appraiseCallBack=AppraiseCallBack.SCUCCES;
            }
        }else{
            userInfo.setUnReliableNum(userInfo.getUnReliableNum()+1);
            if(Constants.RELIABLE_STATUS_NO==oldReliable){
                return AppraiseCallBack.Duplicate;
            }else if(Constants.RELIABLE_STATUS_YES==oldReliable){
                userInfo.setReliableNum(userInfo.getReliableNum()>1?userInfo.getReliableNum()-1:0);
                appraiseCallBack=AppraiseCallBack.RESCUCCES;            
            }else{
                appraiseCallBack=AppraiseCallBack.SCUCCES;
            }
        }
        flag=cacheService.updateUserInfo(userInfo);//更新用户靠谱、不靠谱统计数
        if(!flag){
            return AppraiseCallBack.FAILURE;
        }
        return appraiseCallBack;
    }
    
    public boolean presentBook(int userId, int logId) {
        UserBookLog userBookLog=cacheService.getUserBookLog(logId);
        if(null==userBookLog || userBookLog.getOwnerUserId()!=userId){
            return false;
        }
        userBookLog.setStatus(Constants.BOOK_LOG_STATUS_PRESENTED);
        boolean flag=cacheService.updateUserBookLog(userBookLog);//更新借阅记录为已赠送
        if(!flag){
            logger.error("presentBook ERROR,更新借阅记录失败");
            return false;
        }
        Book book=cacheService.getBook(userBookLog.getBookId());
        if(null==book){
            logger.error("presentBook ERROR,查找图书失败");
            return false;
        }
        
        PresentBookLog presentBookLog=new PresentBookLog();
        presentBookLog.setFromUserId(book.getOwnerUserId());
        presentBookLog.setToUserId(book.getBorrowUserId());
        presentBookLog.setBookId(book.getId());
        flag=cacheService.savePresentBookLog(presentBookLog);
        if(!flag){
            logger.error("presentBook ERROR,图书赠送失败");
            return false;
        }
        book.setStatus(Constants.BOOK_STATUS_FREE);//解锁该书
        book.setOwnerUserId(book.getBorrowUserId());
        book.setOwnerPassport(book.getBorrowPassport());
        flag=cacheService.updateBook(book);
        if(!flag){
            logger.error("presentBook ERROR,图书解锁失败");
            return false;
        }
        cacheService.removeBookList(userId);//清用户图书列表缓存
        cacheService.removeBookList(book.getBorrowUserId());//清用户图书列表缓存
        executorService.execute(new Book2LibraryCommand(book));//增加获赠的图书到图书馆
        executorService.execute(new RemoveBook2LibraryCommand(book.getId(),userId));//从图书馆删除用户赠送的图书
        return this.incrUserBorrowedLimit(userBookLog.getBorrowPassport());
    }
    
    public boolean quitLibrary(int userLibraryId,int libraryId,int userId) {
        boolean flag=cacheService.deleteUserLibrary(userLibraryId,userId);//删除用户加入的图书馆记录
        if(!flag){
            return false;
        }
        flag=cacheService.delLibraryBook(libraryId, userId);//删除用户在该图书馆的书籍
        if(flag){
            cacheService.removeLibraryBookCount(libraryId);
        }
        return flag;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        boolean flag=cacheService.updateUserInfo(userInfo);
        return flag;
    }

    public boolean findPassword(String passport) {
        String password=StringUtil.getCharAndNumr(8);
        UserInfo userInfo=cacheService.getUserInfo(passport);
        if(null==userInfo){
            return false;   
        }
        userInfo.setPassword(MD5.getMD5Str(password));
        boolean flag=cacheService.updateUserInfo(userInfo);
        if(flag){
            EmailUtil.sendWarningMail("找回密码", "您的新密码为 "+password+" 请登录后修改密码! <a href='http://www.ijieshu.com>哎♥借书<a/>'", passport);
        }
        return flag;
    }

    @Override
    public List<BookVo> getBookList(String query) {
        List<BookVo> list=cacheService.getBookList(query);
        return list;
    }

    @Override
    public int getUserCount() {
        return cacheService.getUserCount();
    }

    @Override
    public int getLibraryCount() {
        return cacheService.getLibraryCount();
    }

    @Override
    public int getBookCount() {
        return cacheService.getBookCount();
    }

    @Override
    public boolean incrUserBorrowedLimit(String passport) {
        UserInfo userInfo=cacheService.getUserInfo(passport);
        if(null==userInfo){
            return false;   
        }
        userInfo.setBorrowedLimit(userInfo.getBorrowedLimit()+1);
        boolean flag=cacheService.updateUserInfo(userInfo);
        return flag;
    }

    @Override
    public boolean decrUserBorrowedLimit(String passport) {
        UserInfo userInfo=cacheService.getUserInfo(passport);
        if(null==userInfo){
            return false;   
        }
        userInfo.setBorrowedLimit(userInfo.getBorrowedLimit()-1);
        boolean flag=cacheService.updateUserInfo(userInfo);
        return flag;
    }

    @Override
    public boolean delBook(int userId, int bookId) {        
        Book book=cacheService.getBook(bookId);
        if(book.getStatus()!=Constants.BOOK_STATUS_FREE || book.getOwnerUserId()!=userId){
            return false;
        }
        book.setStatus(Constants.BOOK_STATUS_PRIVATE);
        cacheService.updateBook(book);
        cacheService.removeBookList(userId);//清用户图书列表缓存
        executorService.execute(new RemoveBook2LibraryCommand(book.getId(),userId));//从图书馆删除用户的图书
        return true;
    }
}
