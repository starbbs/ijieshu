package com.ibook.library.service;

import java.util.List;

import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.util.AppraiseCallBack;
import com.ibook.library.util.Page;
import com.ibook.library.vo.BookLogMessageVo;
import com.ibook.library.vo.BookVo;
import com.ibook.library.vo.UserLibraryVo;

public interface LibraryService {

    public UserLibrary saveUserLibrary(Library library, int userId);

    public List<UserLibraryVo> getUserLibraryList(int userId);

    public Book saveBook(int userId,String passport, String identity);

    public List<Book> getBookList(int userId);

    public Page<BookVo> getLibraryBookList(int userId, String query,Page<BookVo> page);

    public List<BookVo> getBookList(String query);


    public int sendMessage(int userId, int oldMsgId, String msg);

    /**
     * 图书借阅申请
     * @param userId
     * @param bookId
     * @param msg
     * @return
     */
    public boolean saveBorrowBookRequest(int userId,String passport,int borrowedLimit, int bookId, String msg);

    /**
     * 同意图书借出
     * @param userId
     * @param logId
     * @return
     */
    public boolean approveBorrowBook(int userId, int logId);

    /**
     * 拒绝图书借出
     * @param userId
     * @param logId
     * @return
     */
    public boolean rejectBorrowBook(int userId, int logId);
    
    /**
     * 图书归还
     * @param userId
     * @param logId
     * @return
     */
    public boolean revertBook(int userId, int logId);
    
    /**
     * 图书赠送
     * @param userId 赠送给对方的userId
     * @param logId
     * @return
     */
    public boolean presentBook(int userId, int logId);
    
    /**
     * 对借阅者评价
     * @param logId
     * @param reliable
     * @return
     */
    public AppraiseCallBack appraiseTheBorrowed(int userId,int logId,int reliable);
    
    public List<Library> getLibrarys(String query,String city);

    public List<BookLogMessageVo> getBookLogMessageVos(int userId);

    public boolean quitLibrary(int userLibraryId, int libraryId, int userId);

    public boolean updateUserInfo(UserInfo userInfo);

    public boolean findPassword(String passport);

    public int getUserCount();

    public int getLibraryCount();
    
    public int getBookCount();
    
    /**
     * 增加用户借阅图书数量的上限
     * @param passport
     * @return
     */
    public boolean incrUserBorrowedLimit(String passport);
    
    /**
     * 减小用户借阅图书数量的上限
     * @param passport
     * @return
     */
    public boolean decrUserBorrowedLimit(String passport);
    
    /**
     * 图书下架
     * @param userId
     * @param bookId
     * @return
     */
    public boolean delBook(int userId,int bookId);

}
