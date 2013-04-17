package com.ibook.library.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ToolUtil {
    
	protected static Logger log = LoggerFactory.getLogger("active-front");
    
    public static String getExceptionMessage(Exception e) {
        StackTraceElement[] elements = e.getStackTrace();
        StringBuffer message = new StringBuffer("\n");
        message.append(e.getMessage() + "\n");
        for (int i = 0; i < elements.length; i++) {
            message.append(elements[i].toString()).append("\n");
        }
        return message.toString();
    }

    /**
     * 计算当前距离计费周期截至日相差天数
     *
     * @param periodendday int
     * @return int
     * @since 2004/06/03
     */
    public static int daysToPeriodEnd(int periodendday) {
        int daycount = 0;
        String sdate = null;
        try {
            sdate = ToolUtil.dateToStr(ToolUtil.getDate());
        } catch (Exception ex) {
        }
        sdate = sdate.substring(sdate.length() - 2, sdate.length());
        int day = Integer.parseInt(sdate);
        if (day < periodendday) {
            daycount = periodendday - day;
        } else {
            daycount = 30 - (day - periodendday);
        }
        return daycount;
    }

    /**
     * 判断当前日期和指定日期是否在同一个计费周期内（20-21）
     *
     * @param enddate java.util.Date
     * @return boolean  (true:表示在一个周期内false:表示不在一个周期内)
     * @since 2003/04/09
     */
    public static boolean IsSamePeriod(java.util.Date enddate) {
        boolean is = false;
        int sday = 0;
        long StartD = 0;
        long EndD = 0;
        long CurrD = 0;
        Calendar MyDate = Calendar.getInstance();
        MyDate.setTime(enddate);
        sday = MyDate.get(Calendar.DAY_OF_MONTH);
        MyDate.set(Calendar.HOUR_OF_DAY, 0);
        MyDate.set(Calendar.MINUTE, 0);
        MyDate.set(Calendar.SECOND, 0);
        if (sday <= 20) {
            MyDate.set(Calendar.DAY_OF_MONTH, 21);
            EndD = MyDate.getTimeInMillis();
            MyDate.set(Calendar.MONTH, MyDate.get(Calendar.MONTH) - 1);
            MyDate.set(Calendar.DAY_OF_MONTH, 20);
            StartD = MyDate.getTimeInMillis();
        } else if (sday > 20) {
            MyDate.set(Calendar.DAY_OF_MONTH, 20);
            StartD = MyDate.getTimeInMillis();
            MyDate.set(Calendar.MONTH, MyDate.get(Calendar.MONTH) + 1);
            MyDate.set(Calendar.DAY_OF_MONTH, 21);
            EndD = MyDate.getTimeInMillis();
        }
        MyDate.setTime(new java.util.Date());
        log.debug("计费周期开始日期：" + new java.util.Date(StartD));
        log.debug("计费周期结束日期：" + new java.util.Date(EndD));
        CurrD = MyDate.getTimeInMillis();

        if (CurrD >= StartD && CurrD <= EndD) {
            is = true;
        }
        return is;
    }

    /**
     * 获得免费使用的截至日期（取所在的计费周期的截至日期－21号）
     *
     * @param enddate java.util.Date
     * @return java.util.Date
     * @since 2003/09/14
     */
    public static java.util.Date getPeriodDate(java.util.Date thisdate) {
        int sday = 0;
        long StartD = 0;
        long EndD = 0;
//        long CurrD = 0;
        Calendar MyDate = Calendar.getInstance();
        MyDate.setTime(thisdate);
        sday = MyDate.get(Calendar.DAY_OF_MONTH);
        MyDate.set(Calendar.HOUR_OF_DAY, 0);
        MyDate.set(Calendar.MINUTE, 0);
        MyDate.set(Calendar.SECOND, 0);
        if (sday <= 20) {
            MyDate.set(Calendar.DAY_OF_MONTH, 21);
            EndD = MyDate.getTimeInMillis();
            MyDate.set(Calendar.MONTH, MyDate.get(Calendar.MONTH) - 1);
            MyDate.set(Calendar.DAY_OF_MONTH, 20);
            StartD = MyDate.getTimeInMillis();
        } else if (sday > 20) {
            MyDate.set(Calendar.DAY_OF_MONTH, 20);
            StartD = MyDate.getTimeInMillis();
            MyDate.set(Calendar.MONTH, MyDate.get(Calendar.MONTH) + 1);
            MyDate.set(Calendar.DAY_OF_MONTH, 21);
            EndD = MyDate.getTimeInMillis();
        }
        MyDate.setTime(new java.util.Date());
        log.debug("计费周期开始日期：" + new java.util.Date(StartD));
        log.debug("计费周期结束日期：" + new java.util.Date(EndD));
        // CurrD = MyDate.getTimeInMillis();

        return new java.util.Date(EndD);
    }

    /**
     * 得到一个N位的随机数
     *
     * @param Max int
     * @param len int
     * @return	处理后的串 String
     * @since 2003/04/09
     */
    public static String getRandomString(int Max, int len) {
        StringBuffer _str = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            int ind = (int) (Math.random() * Max);
            _str.append(ind);
        }
        return _str.toString().length() > len ? _str.toString().substring(0, len) :
                _str.toString();
    }

    /**
     * 处理null转换成空串
     *
     * @param Col String
     * @return	处理后的串 String
     * @since 2003/04/09
     */
    public static String NullToSp(String Col) {
        if (Col == null) {
            return "";
        } else {
            return Col;
        }
    }

    /**
     * 处理null转换成"0"
     *
     * @param Col String
     * @return	处理后的串 String
     * @author wangjm
     * @since 2003/04/09
     */
    public static String NullToZero(String Col) {
        if (Col == null || Col.equals("")) {
            return "0";
        } else {
            return Col.trim();
        }
    }

    /**
     * 得到系统时间
     *
     * @throws Exception 抛出系统异常
     * @return	返回值java.util.Date型系统时间
     * @author wangjm
     * @since 2003/04/09
     */
    public static java.util.Date getDate() throws Exception {
        java.util.Date date = null;
        Calendar MyDate = Calendar.getInstance();
        MyDate.setTime(new java.util.Date());
        date = MyDate.getTime();
        return date;
    }

    /**
     * 通过指定uitl 日期 得到sql 日期
     *
     * @param udate java.util.Date
     * @throws Exception 抛出系统异常
     * @return	返回值java.sql.Date型系统时间
     * @author wangjm
     * @since 2003/05/10
     */
    public static java.sql.Date getSqlDate(java.util.Date udate) {
        long idate = udate.getTime();
        java.sql.Date sqldate = new java.sql.Date(idate);
        return sqldate;
    }

    /**
     * 通过指定的字符串得到相应的sql日期
     *
     * @param sdate java.util.Date
     * @throws Exception 抛出系统异常
     * @return	返回值java.sql.Date型系统时间
     * @author wangjm
     * @since 2003/05/10
     */
    public static java.sql.Date getSqlDate(String sdate) {
        java.sql.Date sqldate = null;
        try {
            java.util.Date udate = strToDate(sdate);
            long idate = udate.getTime();
            sqldate = new java.sql.Date(idate);
        } catch (Exception e) {
            log.error("调用getSqlDate(String sdate)方法时异常" + e.toString());
        }
        return sqldate;
    }

    /**
     * 通过指定的字符串得到相应的日期
     *
     * @param nian String
     * @param yue  String
     * @param ri   String
     * @throws Exception 抛出系统异常
     * @return	返回值java.util.Date型系统时间
     * @author wangjm
     * @since 2003/05/10
     */
    public static java.util.Date getdate(String nian, String yue, String ri) throws
            Exception {
        Calendar MyDate = Calendar.getInstance();
        try {
            int year = Integer.parseInt(nian);
            int month = Integer.parseInt(yue) - 1;
            int date = Integer.parseInt(ri);
            MyDate.set(year, month, date);
        } catch (Exception e) {

            log.error("调用getdate(string,string,string)方法时异常：" + e.toString());
        }
        return MyDate.getTime();
    }

    /**
     * 通过指定的字符串得到相应的日期
     *
     * @param strDate String 字符串格式须为yyyy-MM-dd HH:mm:ss
     * @throws Exception 抛出系统异常
     * @return	返回值java.util.Date型时间
     */
    public static java.util.Date strToDate(String strDate) throws Exception {
        java.util.Date myDate = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (strDate != null && !strDate.equals(""))
                myDate = df.parse(strDate);
        } catch (Exception e) {
            log.error("调用strToDate(strDate)方法时异常：" + e.toString());
        }
        return myDate;
    }

    /**
     * 通过指定的字符串得到相应的日期
     *
     * @param strDate String 字符串格式须为合法类型
     * @throws Exception 抛出系统异常
     * @author wangxi
     * @Data 2004-06-02
     * @return	返回值java.util.Date型时间
     */
    public static java.util.Date speStrToDate(String strDate, String dateType) throws Exception {
        java.util.Date myDate = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(dateType);
            if (strDate != null && !strDate.equals(""))
                myDate = df.parse(strDate);
        } catch (Exception e) {
            log.error("调用strToDate(strDate)方法时异常：" + e.toString());
        }
        return myDate;
    }

    /**
     * 通过指定的日期得到相应的日期字符串
     *
     * @param date java.util.Date
     * @throws Exception 抛出系统异常
     * @return	返回值String型 yyyy-MM-dd
     */
    public static String dateToStr(java.util.Date date) {
        String strDate = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            strDate = df.format(date);
        }
        return strDate;
    }

    /**
     * 通过指定的日期得到相应的日期字符串
     *
     * @param date java.util.Date
     * @throws Exception 抛出系统异常
     * @return	返回值String型 yyyy-MM-dd HH:mm:ss
     */
    public static String dateToLongStr(java.util.Date date) {
        String strDate = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strDate = df.format(date);
        }
        return strDate;
    }

    /**
     * 按指定日期格式得到系统时间
     *
     * @param format_date String
     * @throws Exception 抛出系统异常
     * @return	返回值String型系统时间如：yyyy-MM-dd HH:mm:ss；yyyyMMddHHmmss；yyyyMMdd
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static String getDate_A(String format_date) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format_date);
        Calendar MyDate = Calendar.getInstance();
        //MyDate.setTime(new java.util.Date());
        String adddate = df.format(MyDate.getTime());
        return adddate;
    }
    //取得制定日期的前一天
    /**
     * @param day 指定日期
     * @param format_date 日期格式
     * @param preOrlast 取前一天还是后一天  true 标示取前一天，false取后一天
     * @return
     * @throws Exception
     */
    public static String getGoalDate(String day,String format_date,boolean preOrlast) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat(format_date);
        Calendar MyDate = Calendar.getInstance();
        if(day.length()==8){
        	MyDate.set(Calendar.YEAR, Integer.parseInt(day.substring(0,4)));
        	MyDate.set(Calendar.MONTH, Integer.parseInt(day.substring(4,6))-1);
        	MyDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.substring(6))-1);
        }
     
        return df.format(MyDate.getTime());
    }

    /**
     * 按指定形式输出字符串
     *
     * @param	d 被处理的数据（double型参数）
     * @param	digit 保留小数点后的位数（int型参数）
     * @param	is boolean型参数(is:true 表示始终保留小数点后一位小数，is:false表示为整数时不保留小数点
     * @return	String 返回值为保留digit位小数的字符串的字符串
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static String Format_d(double d, int digit, boolean is) {
        String str = "";
        String sformat = "";
        try {
            for (int i = 0; i < digit; i++) {
                if (i == 0) {
                    if (is) {
                        sformat = ".0";
                    } else {
                        sformat = ".#";
                    }
                } else {
                    sformat = sformat + "#";
                }
            }
            DecimalFormat df = new DecimalFormat("#,##0" + sformat); //设置输出数值的格式为XX.XX
            str = df.format(d);
        } catch (Exception e) {
            log.error("调用Format_d方法时格式化数据时异常:" + e.toString());
            return d + "";
        }
        return str;
    }

    /**
     * 查找指定字符串在源字符串中出现的次数
     *
     * @param source String
     * @param sign   String
     * @return	返回值Int型 为含指定字符串次数
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static int getCount(String source, String sign) {
        //查找某一字符串中str，特定子串s的出现次数
        if (source == null) return 0;
        StringTokenizer s = new StringTokenizer(source, sign);
        return s.countTokens();
    }

    /**
     * 按指定字符串来分割源字符串返回数组
     *
     * @param source String
     * @param sign   String
     * @return	返回值数组型
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static String[] getArray(String source, String sign) {
        //按特定子串s为标记，将子串截成数组。Jdk 存在bug,所以sign不能是串,否则结果不正确
        int count = getCount(source, sign);
        int len =sign.length();
        int j = 0;
        String[] arr = new String[count];
        for (int i = 0; i < count; i++) {
            if (source.indexOf(sign) != -1) {
                j = source.indexOf(sign);
                arr[i] = source.substring(0, j);
                source = source.substring(j+len );
            } else {
                arr[i] = source;
            }
        }
        return arr;

    }

    /**
     * 截取指定长度的一个字符串
     *
     * @param is (boolean true: 假如大与指定长度，一省略号提示， false:无论怎样都不以省略号形式显示)
     * @param	TxtData 类型(String)
     * @param	i 类型(int)
     * @return	 返回值(String)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static String Trim_len(String TxtData, int i, boolean is) {
        int len = 0;
        String str = "";
        try {
            len = TxtData.length();
            if (len > i) {
                if (is) {
                    str = TxtData.substring(0, i - 2) + "...";
                } else {
                    str = TxtData.substring(0, i);
                }
            } else {
                return TxtData;
            }
        } catch (Exception e) {
            log.error("调用Trim_len()方法时异常：" + e.toString());
        }
        return str;

    }

    /**
     * 判断一个字符串是否为数值
     *
     * @param		TxtData 类型(String)
     * @return		返回值(boolean) (true:字符串为数值 false:字符串不为为数值)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsNumeric(String TxtData) {
        TxtData = TxtData.trim();
        int vl = TxtData.length();
        for (int i = 0; i < vl; i++) {
            char c = TxtData.charAt(i);
            if (!((c >= '0' && c <= '9') || c == '.')) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean IsEmpty(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        if (str.equals("") || str.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串是否为26个字母中的字符
     *
     * @param			TxtData 类型(String)
     * @return		返回值(boolean) (true:字符串26个字母中的字符 false:字符串不全为26个字母中的字符)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsAlpha(String TxtData) {
        TxtData = TxtData.trim();
        int vl = TxtData.length();
        for (int i = 0; i < vl; i++) {
            char c = TxtData.charAt(i);
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个字符串是否为0-9 和26个字母中的字符
     *
     * @param			TxtData 类型(String)
     * @return		返回值(boolean) (true:字符串0-9 和26个字母中的字符 false:字符串不全为0-9 和26个字母中的字符)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsAlNum(String TxtData) {
        TxtData = TxtData.trim();
        int vl = TxtData.length();
        for (int i = 0; i < vl; i++) {
            char c = TxtData.charAt(i);
            if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')
                    && !(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个字符串是否为全长
     *
     * @param			TxtData 类型(String)
     * @param			MaxLen 类型(int)
     * @return		返回值(boolean) (true:字符串为MaxLen false:字符串不等于MaxLen)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsFullLen(String TxtData, int MaxLen) {
        TxtData = TxtData.trim();
        int l = TxtData.length();
        if (l > 0 && l != MaxLen) {
            return false;
        }
        return true;
    }

    /**
     * 判断一个字符串是否为日期型
     *
     * @param			TxtData 类型(String)
     * @return		返回值(boolean) (true:字符串日期型false:字符串不全为日期型)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsDate(String TxtData) {
        TxtData = TxtData.trim();

        if (TxtData.length() > 0) {

            if (!IsNumeric(TxtData) || !IsFullLen(TxtData, 8)) {
                return false;
            }

            int y = new Integer(TxtData.substring(0, 4)).intValue();
            int m = new Integer(TxtData.substring(4, 6)).intValue();
            int d = new Integer(TxtData.substring(6)).intValue();

            try {
                Calendar date = new GregorianCalendar(y, m - 1, d);
                date.setLenient(false);
                date.get(Calendar.DATE);
            } catch (java.lang.IllegalArgumentException e) {
                log.error("调用IsDate()方法时异常:" + e.toString());
                return false;
            }
        }
        return true;
    }

    /**
     * 将数字转换为指定的格式 格式类型类似于
     * ####.000（.后面的0的个数代表保留几位小数）
     * 0.000E0000（返回指定的指数形式）
     * 返回四舍五入后的指定小数位的数字字符串
     * 如：输入 1234.567890 返回 1234.568；或 1.235E0003
     *
     * @param num    String
     * @param format String
     * @return String
     * @throws NumberFormatException
     * @author wangxi
     */
    public static String FormatNum(String num, String format) throws NumberFormatException {
        String rtnum = "0";
        try {
            DecimalFormat df1 = new DecimalFormat(format);
            rtnum = df1.format(new Long(num));
        } catch (NumberFormatException ex) {
            log.error("调用FormatNum()方法时异常:" + ex.toString());
            ex.printStackTrace();
        }
        return rtnum;
    }

    /**
     * 按指定的位数四舍五入指定数字
     *
     * @param num    String
     * @param dotnum String
     * @return String
     * @throws NumberFormatException
     * @author wangxi
     */
    public static float FormatNum(float num, int dotnum) throws NumberFormatException {
        float rtnum = 0;
        try {
            StringBuffer sformat = new StringBuffer("####.0");
            for (int i = 1; i < dotnum; i++) {
                sformat.append("0");
            }
            DecimalFormat df1 = new DecimalFormat(sformat.toString());
            rtnum = Float.parseFloat(df1.format(num));
        } catch (NumberFormatException ex) {
            log.error("调用FormatNum()方法时异常:" + ex.toString());
            ex.printStackTrace();
        }
        return rtnum;
    }


    /**
     * 判断一个字符串是否为时间类型
     *
     * @param		TxtData 类型(String)
     * @return		返回值(boolean) (true:为时间型false:字符串不全为时间)
     * @Exception
     * @author wangjm
     * @since 2003/04/09
     */
    public static boolean IsTime(String TxtData) {
        TxtData = TxtData.trim();

        if (TxtData.length() > 0) {
            if (!IsNumeric(TxtData) || !IsFullLen(TxtData, 6)) {
                return false;
            }
            int h = new Integer(TxtData.substring(0, 2)).intValue();
            int m = new Integer(TxtData.substring(2, 4)).intValue();
            int s = new Integer(TxtData.substring(4)).intValue();
            if (h < 0 || h > 23) {
                return false;
            }
            if (m < 0 || m > 59) {
                return false;
            }
            if (s < 0 || s > 59) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过指定的日期得到相应的日期字符串
     * editby zouzh
     *
     * @param sTitle String
     * @return	返回值String型 yyyy-MM-dd hh:mm:ss
     */
    public static String dealTitle(String sTitle, int iMax, int iStep) {
        String sReturn = "";
        try {
            if (sTitle.length() > iMax) {
                if (iMax > iStep) {
                    sReturn = sTitle.substring(0, iMax - iStep);
                }
                for (int i = 0; i < iStep; i++) {
                    sReturn += ".";
                }
                //log.info("dealTitle() sReturn:" + sReturn);
            } else {
                sReturn = sTitle;
            }
        } catch (Exception e) {
            log.error("CB.dealTitle()例外:" + e);
        }
        return sReturn;
    }

    public static boolean IsWChar(String TxtData) {
        int vl = TxtData.length();
        for (int i = 0; i < vl; i++) {
            char c = TxtData.charAt(i);
            if (c >= ' ' && c <= '~') { //(U0020--U007e)
                return false;
            }
        }
        return true;
    }

    public static boolean IsKChar(String TxtData) {
        int vl = TxtData.length();
        for (int i = 0; i < vl; i++) {
            char c = TxtData.charAt(i);
            if (c >= '?' || c <= '?') { //(UFF66-UFF9F)
                return false;
            }
        }
        return true;
    }

    //处理xml
    //得到NodeList对象
    public Element getElementObject(String filepath) {
        Element root = null;
        try {
            File f = new File(this.getClass().getResource("/").getPath());
            f = new File(f.getPath() + filepath);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.
                    newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            //Document doc = docBuilder.parse(is);
            Document doc = docBuilder.parse(f);
            root = doc.getDocumentElement();

        } catch (Exception e) {
            System.err.println("不能读取属性文件. " + "请确保db.xml在指定的路径中=" + e.toString());
            e.printStackTrace();
        }

        return root;
    }

    /*
     * 用于读取xml文件信息，获得一个element节点的描述值
     */
    public String getValue(Node node) {
        if (node == null)
            return "";
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return "";
        return node.getChildNodes().item(0).getNodeValue();
    }

    /*
     * 用于读取xml文件信息，获得一个element节点的子节点的描述值
     */
    public String getChildNodeValue(Node node, String childName) {
        if (node == null)
            return "";
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return "";
        return getValue(((Element) node).getElementsByTagName(childName).item(0));
    }

    /*
     * 用于读取xml文件信息，得到按名称指定的子节点列表
     */
    public NodeList getElementsByTagName(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return nodeList;
    }

    /**
     * 将串进行MD5加密
     *
     * @param s 原串
     * @return
     * @author 王溪
     * @serialData 2004-07-19
     */

    public static String toMD5String(String sTemp) {
        byte[] bTemp = "".getBytes();
        if (sTemp != null && !sTemp.equals("")) {
            bTemp = sTemp.getBytes();
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            sTemp = new String(md5.digest(bTemp), "iso8859-1");
            //sTemp = md5.digest(bTemp).toString();
        } catch (Exception ex) {
            log.debug("调用cn.com.surekam.common.CB的toMD5String");
            ex.printStackTrace();
        }
        return sTemp;
    }

    /**
     * 将汉字转为UTF8编码的串
     *
     * @param s 原汉字串
     * @return 重新编码后的UTF8编码的串
     * @author 王溪
     * @serialData 2004-07-19
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).
                            toUpperCase());
                }
            }
        }
        return sb.toString();
    }

	public static String filterHtml(String body) {
		if (body==null){
			return "";
		}		
		body = body.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;").replaceAll("'","&quot;");	
		return body;
	}
	
	public static String filterHtmlSpecial(String body){
		if (body==null){
			return "";
		}	
		body = body.replaceAll("<","\\<").replaceAll(">","\\>").replaceAll("\"","\\\\\"").replaceAll("'","\\\\'");
		return body;
		
	}
	
	public static String filterHtmlTag(String body) {
		if (body==null){
			return "";
		}		
		body=body.replaceAll("FRAME","");
		body=body.replaceAll("frame","");
		body=body.replaceAll("Frame","");
		body=body.replaceAll("APPLET","");
		body=body.replaceAll("applet","");
		body=body.replaceAll("Applet","");
		body=body.replaceAll("<SCRIPT","&lt;SCRIPT");
		body=body.replaceAll("<Script","&lt;SCRIPT");
		body=body.replaceAll("<script","&lt;SCRIPT");
		body=body.replaceAll("<BODY>","&lt;body&gt;");
		body=body.replaceAll("<Body>","&lt;body&gt;");
		body=body.replaceAll("<body>","&lt;body&gt;");
		body=body.replaceAll("<form>","&lt;form&gt;");
		body=body.replaceAll("<FORM>","&lt;form&gt;");
		body=body.replaceAll("<Form>","&lt;form&gt;");
		return body;
	}
	
	public static String encodeUrl(String sourceurl){
		String temp="";
		String[] url=ToolUtil.getArray(sourceurl,"&");		
		if(url.length>0){
			temp=url[0];
		}			
		for(int i=1;i<url.length;i++){
			if(url[i].indexOf("=")!=-1){
				try {
					temp=temp + "&" + url[i].substring(0,url[i].indexOf("=")+1)+ URLEncoder.encode(url[i].substring(url[i].indexOf("=")+1),"GBK");
				} catch (UnsupportedEncodingException e) {					
					e.printStackTrace();
				}
			}
			
		}	
		return temp;
	}
	
}
