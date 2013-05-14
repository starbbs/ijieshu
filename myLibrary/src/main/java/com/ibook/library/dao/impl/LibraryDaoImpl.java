package com.ibook.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ibook.library.cst.Constants;
import com.ibook.library.dao.LibraryDao;
import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.LibraryBook;
import com.ibook.library.entity.PresentBookLog;
import com.ibook.library.entity.UserBookLog;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.entity.UserMessage;
import com.ibook.library.util.StringUtil;

/**
 * 数据库操作封装层
 * @author xiaojianyu
 *
 */
@Repository
public class LibraryDaoImpl extends BaseDaoImpl implements LibraryDao {
    
  private static Logger logger = LoggerFactory.getLogger("library");
  
  @Autowired
  @Qualifier("activeDataSource")
  private DataSource dataSource;
  
  public DataSource getDataSource() {
      return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
  }
    public Integer saveBook(Book book)  {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(book);
            logger.info("saveBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveBook ERROR"+e);
        }
        return id;
    }

    public Book getBook(int id)  {
        Book book=null;
        long startTime = System.currentTimeMillis();
        try {
            book=(Book)super.get(Book.class.getName(), id);
            logger.info("getBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getBook ERROR"+e);
        }
        return book;
    }

    public Integer saveUserBookLog(UserBookLog userBookLog)  {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(userBookLog);
            logger.info("saveUserBookLog SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveUserBookLog ERROR"+e);
        }
        return id;
    }

    public Integer saveUserInfo(UserInfo userInfo)  {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(userInfo);
            logger.info("saveUserInfo SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveUserInfo ERROR"+e);
        }
        return id;
    }

    public UserInfo getUserInfo(int id)  {
        UserInfo userInfo=null;
        long startTime = System.currentTimeMillis();
        try {
            userInfo=(UserInfo)super.get(UserInfo.class.getName(), id);
            logger.info("getUserInfo SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getUserInfo ERROR"+e);
        }
        return userInfo;
    }

    public UserInfo getUserInfo(String passport)  {
        long startTime = System.currentTimeMillis();
        UserInfo userInfo=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_info where passport='"+passport+"'";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                userInfo=new UserInfo();
                userInfo.setDescripiton(rs.getString("descripiton"));
                userInfo.setId(rs.getInt("id"));
                userInfo.setImg(rs.getString("img"));
                userInfo.setNick(rs.getString("nick"));
                userInfo.setPassport(rs.getString("passport"));
                userInfo.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserInfo SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return userInfo;
    }

    public boolean updateUserInfo(UserInfo userInfo)  {
        long startTime = System.currentTimeMillis();
        try {
            super.update(userInfo);
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
            return false;
        }
        logger.info("updateUserInfo SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return true;
    }

    public Integer saveUserMessage(UserMessage userMessage)  {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(userMessage);
            logger.info("saveUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveUserMessage ERROR"+e);
        }
        return id;
    }

    public List<UserMessage> getUserMessage(int messageDirection, int userId)  {
        long startTime = System.currentTimeMillis();
        List<UserMessage> list=new ArrayList<UserMessage>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String user_type="";
        if(Constants.MESSAGE_DIRECTION_FROM==messageDirection){
            user_type="from_user_id";
        }else{
            user_type="to_user_id";
        }
        String sql = "select * from user_message where "+user_type+"="+userId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserMessage userMessage = new UserMessage();
                userMessage.setId(rs.getInt("id"));
                userMessage.setFromUserId(rs.getInt("from_user_ud"));
                userMessage.setToUserId(rs.getInt("to_user_id"));
                userMessage.setOldMsgId(rs.getInt("old_msg_d"));
                userMessage.setLogId(rs.getInt("log_id"));
                userMessage.setMsg(rs.getString("msg"));
                list.add(userMessage);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public List<UserMessage> getUserMessages(int logId)  {
        long startTime = System.currentTimeMillis();
        List<UserMessage> list=new ArrayList<UserMessage>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_message where log_id="+logId+" order by id desc";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserMessage userMessage = new UserMessage();
                userMessage.setId(rs.getInt("id"));
                userMessage.setFromUserId(rs.getInt("from_user_id"));
                userMessage.setToUserId(rs.getInt("to_user_id"));
                userMessage.setOldMsgId(rs.getInt("old_msg_id"));
                userMessage.setLogId(rs.getInt("log_id"));
                userMessage.setMsg(rs.getString("msg"));
                list.add(userMessage);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }
    
    public boolean delUserMessage(int messageDirection, int userId)  {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            String user_type="";
            if(Constants.MESSAGE_DIRECTION_FROM==messageDirection){
                user_type="from_user_id";
            }else{
                user_type="to_user_id";
            }
            final String SQL = "delete from user_message where "+user_type+"="+userId;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("delUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("delUserMessage ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }

    public boolean delUserMessage(int id)  {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            final String SQL = "delete from user_message where id="+id;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("delUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("delUserMessage ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }

    public UserLibrary getUserLibrary(int id)  {
        UserLibrary userLibrary=null;
        long startTime = System.currentTimeMillis();
        try {
            userLibrary=(UserLibrary)super.get(UserLibrary.class.getName(), id);
            logger.info("getUserLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getUserLibrary ERROR"+e);
        }
        return userLibrary;
    }

    public boolean delUserLibrary(int id)  {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            final String SQL = "delete from user_library where id="+id;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("delUserLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("delUserLibrary ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }

    public Integer saveLibrary(Library library) {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(library);
            logger.info("saveLiarary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveLiarary ERROR"+e);
        }
        return id;
    }


    public Integer saveUserLibrary(UserLibrary userLibrary) {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(userLibrary);
            logger.info("saveUserLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveUserLibrary ERROR"+e);
        }
        return id;
    }

    
    public List<UserLibrary> getUserLibraryList(int userId) {
        long startTime = System.currentTimeMillis();
        List<UserLibrary> list=new ArrayList<UserLibrary>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_library where user_id="+userId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserLibrary userLibrary = new UserLibrary();
                userLibrary.setId(rs.getInt("id"));
                userLibrary.setLibraryId(rs.getInt("library_id"));
                userLibrary.setUserId(userId);
                userLibrary.setLibraryName(rs.getString("library_name"));
                userLibrary.setLibraryDesc(rs.getString("library_desc"));
                list.add(userLibrary);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserLibraryList SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public Library getLibrary(int id) {
        Library library=null;
        long startTime = System.currentTimeMillis();
        try {
            library=(Library)super.get(Library.class.getName(), id);
            logger.info("getLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getBook ERROR"+e);
        }
        return library;
    }

    public Library getLibrary(String name) {
        long startTime = System.currentTimeMillis();
        Library library=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from library where library_name='"+name+"'";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                library=new Library();
                library.setId(rs.getInt("id"));
                library.setName(name);
                library.setPhoto(rs.getString("photo"));
                library.setDesc(rs.getString("library_desc"));
                library.setCreateUserId(rs.getInt("create_user_id"));
                library.setForPublic(rs.getBoolean("for_public"));
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return library;
    }

    public List<Book> getUserBookList(int userId) {
        long startTime = System.currentTimeMillis();
        List<Book> list=new ArrayList<Book>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql = "select * from book where owner_user_id="+userId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            result = ps.executeQuery();
            
            while (result.next()) {  
                Book book = new Book();
//                 BeanUtils.copyProperties(book, result);
                 book.setAlt(result.getString("alt"));
                 book.setAltTitle(result.getString("alt_title"));
                 book.setAuthor(result.getString("author"));
                 book.setAuthorIntro(result.getString("author_intro"));                
                 book.setNumRaters(result.getInt("num_raters"));                 
                 book.setIsbn10(result.getString("isbn10"));
                 book.setIsbn13(result.getString("isbn13"));               
                 book.setLargeImg(result.getString("large_img"));
                 book.setMediumImg(result.getString("medium_img"));
                 book.setSmallImg(result.getString("small_img"));      
                 book.setOriginTitle(result.getString("origin_title"));
                 book.setPages(result.getString("pages"));
                 book.setPrice(result.getString("price"));
                 book.setPubdate(result.getString("pubdate"));
                 book.setPublisher(result.getString("publisher"));
                 book.setSubTitle(result.getString("sub_title"));
                 book.setSummary(result.getString("summary"));               
                 book.setTags(result.getString("tags"));  
                 book.setTranslator(result.getString("translator"));
                 book.setTitle(result.getString("title"));
                 book.setUrl(result.getString("url"));
                 book.setAverage(result.getFloat("average"));
                 book.setBorrowUserId(result.getInt("borrow_user_id"));
                 book.setOwnerUserId(result.getInt("owner_user_id"));
                 book.setId(result.getInt("id"));
                 list.add(book);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(result, ps, conn);
        }
        logger.info("getUserBookList SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public Set<Integer> getBookIdList(int userId, String query) {
        long startTime = System.currentTimeMillis();
        Set<Integer> list=new HashSet<Integer>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql = "select DISTINCT(lb.book_id) book_id from user_library ul,library_book lb where ul.user_id="+userId
                +" and ul.library_id=lb.library_id ";
        if(!StringUtil.isEmpty(query)){
            sql=sql+" and lb.title like '%"+query+"%'";
            sql=sql+" order by lb.title";
        }else{
            sql=sql+" order by lb.id desc";
        }
        sql=sql+" limit 20";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            result = ps.executeQuery();          
            while (result.next()) {  
                 list.add(result.getInt("book_id"));
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(result, ps, conn);
        }
        logger.info("getBookIdList SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public Integer saveLibraryBook(LibraryBook libraryBook) {
        Integer id=null;
        long startTime = System.currentTimeMillis();
        try {
            id=(Integer)save(libraryBook);
            logger.info("saveLibraryBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("saveLibraryBook ERROR"+e);
        }
        return id;
    }

    public boolean updateBook(Book book) {
        long startTime = System.currentTimeMillis();
        try {
            update(book);
            logger.info("updateBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("updateBook ERROR"+e);
        }
        return true;
    }

    public List<UserBookLog> getUserBookLogList(int userid) {
        long startTime = System.currentTimeMillis();
        List<UserBookLog> list=new ArrayList<UserBookLog>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
//        String sql = "select * from user_book_log where status="+Constants.BOOK_LOG_STATUS_APPLY+" and owner_user_id="+userid+" order by id desc";
        String sql = "select * from user_book_log where status="+Constants.BOOK_LOG_STATUS_APPLY+" and (owner_user_id="+userid+" or borrow_user_id="+userid+") order by id desc";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserBookLog userBookLog = new UserBookLog();
                userBookLog.setId(rs.getInt("id"));
                userBookLog.setBookId(rs.getInt("book_id"));
                userBookLog.setBorrowReliable(rs.getInt("borrow_reliable"));
                userBookLog.setBorrowTalk(rs.getString("borrow_talk"));
                userBookLog.setBorrowUserId(rs.getInt("borrow_user_id"));
                userBookLog.setBorrowPassport(rs.getString("borrow_passport"));
                userBookLog.setOwneReliable(rs.getInt("owner_reliable"));
                userBookLog.setOwnerTalk(rs.getString("owner_talk"));
                userBookLog.setOwnerUserId(rs.getInt("owner_user_id"));
                userBookLog.setOwnerPassport(rs.getString("owner_passport"));
                userBookLog.setStatus(rs.getInt("status"));
                list.add(userBookLog);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserBookLogList SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public List<Library> getLibrarys(String query) {
        long startTime = System.currentTimeMillis();
        List<Library> list=new ArrayList<Library>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql ="";
        if(StringUtil.isEmpty(query)){
             sql = "select * from library  order by library_name";
        }else{
            sql = "select * from library where library_name like '%"+query+"%' order by library_name";
        }
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Library library = new Library();
                library.setId(rs.getInt("id"));
                library.setCreateUserId(rs.getInt("create_user_id"));
                library.setDesc(rs.getString("library_desc"));
                library.setName(rs.getString("library_name"));
                library.setPhoto(rs.getString("photo"));
                library.setLongitude(rs.getFloat("longitude"));
                library.setLatitude(rs.getFloat("latitude"));
                library.setAddress(rs.getString("address"));
                library.setForPublic(rs.getBoolean("for_public"));

                list.add(library);
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getLibrarys SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    public boolean lockLibraryBook(int bookId) {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            final String SQL = "update library_book set locked=true where book_id="+bookId;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("lockLibraryBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("lockLibraryBook ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }


    public boolean unLockLibraryBook(int bookId) {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            final String SQL = "update library_book set locked=false where book_id="+bookId;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("unLockLibraryBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("unLockLibraryBook ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }
    
    public UserMessage getUserMessage(int id) {
        UserMessage userMessage=null;
        long startTime = System.currentTimeMillis();
        try {
            userMessage=(UserMessage)super.get(UserMessage.class.getName(), id);
            logger.info("getUserMessage SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getUserMessage ERROR"+e);
        }
        return userMessage;
    }

    public UserBookLog getUserBookLog(int id) {
        UserBookLog userBookLog=null;
        long startTime = System.currentTimeMillis();
        try {
            userBookLog=(UserBookLog)super.get(UserBookLog.class.getName(), id);
            logger.info("getUserBookLog SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("getUserBookLog ERROR"+e);
        }
        return userBookLog;
    }

    public boolean updateUserBookLog(UserBookLog userBookLog) {
        long startTime = System.currentTimeMillis();
        try {
            update(userBookLog);
            logger.info("updateUserBookLog SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("updateUserBookLog ERROR"+e);
        }
        return true;
    }

    public boolean delLibraryBook(int libraryId, int userId) {
        Connection conn = null;
        PreparedStatement smt = null;
        long startTime = System.currentTimeMillis();
        try {
            final String SQL = "delete from library_book where owner_user_id="+userId+" and library_id="+libraryId;
            conn = dataSource.getConnection(); 
            smt = conn.prepareStatement(SQL);
            smt.executeUpdate();
            logger.info("delLibraryBook SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("delLibraryBook ERROR"+e);
            return false;
        }finally {
            try {
                smt.close();
                conn.close();
            } catch (SQLException e) {
                logger.error("finally ERROR"+e);
            }
        }
        return true;
    }

    public int getLibraryBookCount(int libraryId) {
        long startTime = System.currentTimeMillis();
        int count=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(1) count from library_book where library_id="+libraryId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getLibraryBookCount SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return count;
    }

    public int getBookCount() {
        long startTime = System.currentTimeMillis();
        int count=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(1) count from book";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getBookCount SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return count;
    }

    public int getLibraryUserCount(int libraryId) {
        long startTime = System.currentTimeMillis();
        int count=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(1) count from user_library where library_id="+libraryId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getLibraryUserCount SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return count;
    }

    @Override
    public UserLibrary getUserLibrary(int userId, int libraryId) {
        long startTime = System.currentTimeMillis();
        UserLibrary userLibrary=null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_library where user_id="+userId +" and library_id="+libraryId;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                userLibrary=new UserLibrary();
                userLibrary.setId(rs.getInt("id"));
                userLibrary.setLibraryId(rs.getInt("library_id"));
                userLibrary.setUserId(userId);
                userLibrary.setLibraryName(rs.getString("library_name"));
                userLibrary.setLibraryDesc(rs.getString("library_desc"));
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserLibrary SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return userLibrary;
    }

    @Override
    public Set<Integer> getBookIdList(String query) {
        long startTime = System.currentTimeMillis();
        Set<Integer> list=new HashSet<Integer>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String sql = "select DISTINCT(lb.book_id) book_id from library_book lb ";
        if(!StringUtil.isEmpty(query)){
            sql=sql+" where lb.title like '%"+query+"%'";
            sql=sql+" order by lb.title";
        }else{
            sql=sql+" order by lb.id desc";
        }
        sql=sql+" limit 21";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            result = ps.executeQuery();          
            while (result.next()) {  
                 list.add(result.getInt("book_id"));
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(result, ps, conn);
        }
        logger.info("getBookIdList1 SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return list;
    }

    @Override
    public int getUserCount() {
        long startTime = System.currentTimeMillis();
        int count=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(1) count from user_info";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getUserCount SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return count;
    }

    @Override
    public int getLibraryCount() {
        long startTime = System.currentTimeMillis();
        int count=0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(1) count from library";
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count=rs.getInt("count");
            }
        } catch (Exception e) {
            logger.error("sql ERROR"+e);
        } finally {
            clean(rs, ps, conn);
        }
        logger.info("getLibraryCount SUCCESS time:"+(System.currentTimeMillis() - startTime));
        return count;
    }

    @Override
    public boolean savePresentBookLog(PresentBookLog presentBookLog) {
        long startTime = System.currentTimeMillis();
        try {
            save(presentBookLog);
            logger.info("savePresentBookLog SUCCESS time:"+(System.currentTimeMillis() - startTime));
        }catch(Exception e) {
            logger.error("savePresentBookLog ERROR"+e);
            return false;
        }
        return true;
    }

}
