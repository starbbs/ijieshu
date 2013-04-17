package com.ibook.library.cst;

public class Constants {
    /** 分隔符 **/
    public static final String CACHE_SPLIT = "^"; 
    /** 用户信息 */
    public static final String CACHE_KEY_USER_INFO = "user_info^";
    /** 用户图书馆 */
    public static final String CACHE_KEY_USER_LIBRARY = "user_library^"; 
    /** 用户图书 */
    public static final String CACHE_KEY_USER_BOOK = "user_book^"; 
    /** 用户图书借阅记录集 */
    public static final String CACHE_KEY_USER_BOOK_LOGS = "user_book_logs^";
    /** 借阅记录 */
    public static final String CACHE_KEY_USER_BOOK_LOG = "user_book_log^";
    /** 用户log消息 */
    public static final String CACHE_KEY_USER_LOG_MSG = "user_log_msg^";
    /** 用户消息 */
    public static final String CACHE_KEY_USER_MSG = "user_msg^";
    /** 图书馆 */
    public static final String CACHE_KEY_LIBRARY = "library^";  
    /** 图书馆 馆藏书籍数 */
    public static final String CACHE_KEY_LIBRARY_BOOK_COUNT = "library_book_count^";  
    /** 图书馆 馆君数 */
    public static final String CACHE_KEY_LIBRARY_USER_COUNT = "library_user_count^";  
    /** 图书 */
    public static final String CACHE_KEY_BOOK = "book^";  
    /** 图书数 */
    public static final String CACHE_KEY_BOOK_COUNT = "book_count^";  
    /** 图书馆数 */
    public static final String CACHE_KEY_LIBRARY_COUNT = "library_count^";  
    /** 馆君数 */
    public static final String CACHE_KEY_USER_COUNT = "user_count^"; 
    
    /**0-自由可借阅的书**/
    public static final int BOOK_STATUS_FREE=0;
    /**1-借阅过程中的书**/
    public static final int BOOK_STATUS_LOCK=1;
    /**2-不可以被借阅的书**/
    public static final int BOOK_STATUS_PRIVATE=2;
    
    /**0-申请中**/
    public static final int BOOK_LOG_STATUS_APPLY=0;
    /**1-待归还**/
    public static final int BOOK_LOG_STATUS_BORROWED=1;
    /**2-借阅完成**/
    public static final int BOOK_LOG_STATUS_DONE=2;
    
    /**0-发送消息方**/
    public static final int MESSAGE_DIRECTION_FROM=0;
    /**1-接收消息方**/
    public static final int MESSAGE_DIRECTION_TO=1;
    
    /**0- 未评价-借书动作评价**/
    public static final int RELIABLE_STATUS_NULL=0;
    /**1-靠谱-借书动作评价**/
    public static final int RELIABLE_STATUS_YES=1;
    /**2-不靠谱-借书动作评价**/
    public static final int RELIABLE_STATUS_NO=2;
    
    /**json返回常量-状态**/
    public static final String STATUS="status";
    /**json返回常量-消息**/
    public static final String MSG="msg";
    /**json返回常量-用户图书馆列表**/
    public static final String USERLIBRARYLIST="user_library_list";    
    /**session 常量-用户名**/
    public static final String USER_NAME="user_name";
    /**常量-用户id**/
    public static final String USER_ID="user_id";    
    /**des加密的key**/
    public static final String DES_KEY="yuxiaojian_ibook^";
    
}
