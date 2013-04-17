package com.ibook.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 * @author xiaojianyu
 *
 */
public class StringUtil {

    public static final String ZERO_STRING = "".intern();
    
    private static Pattern vidPattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");

	public static String nullToString(String str) {
		return str == null ? "" : str;
	}

	public static String strHandle(String str) {
		return nullToString(str).trim();
	}

	public static boolean isLegalRegistVid(String nid) {
		return vidPattern.matcher(nid).matches();
	}
    
    /**
     * 按指定字符串来分割源字符串返回数组
     *
     * @param source String
     * @param sign   String
     * @return 返回值数组型
     * @Exception
     */
    public static String[] getArray(String source, String sign) {
        // 按特定子串s为标记，将子串截成数组。
        int count = getCount(source, sign);
        int j = 0;
        String[] arr = new String[count];
        for (int i = 0; i < count; i++) {
            if (source.indexOf(sign) != -1) {
                j = source.indexOf(sign);
                arr[i] = source.substring(0, j);
                source = source.substring(j + 1);
            } else {
                arr[i] = source;
            }
        }
        return arr;
    }

    /**
     * 查找指定字符串在源字符串中出现的次数
     *
     * @param source String
     * @param sign   String
     * @return 返回值Int型 为含指定字符串次数
     * @Exception
     */
    public static int getCount(String source, String sign) {
        // 查找某一字符串中str，特定子串s的出现次数
        if (source == null)
            return 0;
        StringTokenizer s = new StringTokenizer(source, sign);
        return s.countTokens();
    }

    /**
     * Checks if a String is not empty ("") and not null.
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Checks if a String is empty ("") or null.
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if ( null == str)
            return true;
        return "".equals(str.trim());
    }

    /**
     * 第一个参数，传入的是要截的中英文字符串，第二个参数，要截取的长度。
     *
     * @param str
     * @param subBytes
     * @return str
     */
    public static String subString(String str, int subBytes) {
        //int byteLen = str.length();
        //if(byteLen == subBytes) return str;
        int bytes = 0; // 用来存储字符串的总字节数
        for (int i = 0; i < str.length(); i++) {
            if (bytes == subBytes) {
                return str.substring(0, i);
            }
            char c = str.charAt(i);
            if (c < 256) {
                bytes += 1; // 英文字符的字节数看作1
            } else {
                bytes += 2; // 中文字符的字节数看作2
                if (bytes - subBytes == 1) {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }
    /**
     * 汉字当1位
     * @param str
     * @param beginIndex 
     * @param endIndex
     * @return
     */
    public static String subString(String str, int beginIndex , int endIndex) {
        if (endIndex <= beginIndex) return null;
        
        int bytesLen = str.length();
        String subStr = null;
        int bytes = 0; // 用来存储字符串的总字节数
        for (int i = 0; i < str.length(); i++) {
            if (bytes == beginIndex) {
                subStr =  str.substring(i, bytesLen);
                bytes = i ; 
                break;
            }
            char c = str.charAt(i);
            if (c < 256) {
                bytes += 1; // 英文字符的字节数看作1
            } else {
                bytes += 2; // 中文字符的字节数看作2
                if (bytes - beginIndex == 1) {
                    subStr =  str.substring(i,bytesLen);
                    bytes = i ; 
                    break;
                }
            }
        }
        
        String retStr = subString(subStr,endIndex - bytes);
        return retStr;
    }
    /**
     * 第一个参数，传入的是要截的中英文字符串，第二个参数，要截取的长度。
     *
     * @param str
     * @param subBytes
     * @return str
     */
    public static String subStringWithMore(String str,
                                           int subBytes) {
        int bytes = 0; // 用来存储字符串的总字节数
        for (int i = 0; i < str.length(); i++) {
            if (bytes == subBytes) {
                if (i < str.length() - 1)
                    return str.substring(0, i) + "...";
                else
                    return str.substring(0, i) + "....";
            }
            char c = str.charAt(i);
            if (c < 256) {
                bytes += 1; // 英文字符的字节数看作1
            } else {
                bytes += 2; // 中文字符的字节数看作2
                if (bytes - subBytes == 1) {
                    if (i < str.length() - 1)
                        return str.substring(0, i) + "...";
                    else
                        return str.substring(0, i) + "....";
                }
            }
        }
        return str;
    }
    /**
     * 功能：获取字符串长度，一个汉字等于两个字符
     * @param strParameter
     * @return
     */
    public static int getStrLength(String str) {
        if (str == null || str.length() == 0)
            return 0;

        int bytes = 0; // 用来存储字符串的总字节数
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 256) {
                bytes += 1; // 英文字符的字节数看作1
            } else {
                bytes += 2; // 中文字符的字节数看作2
            }
        }

        return bytes;
    }
    /**
     * 功能：验证字符串长度是否符合要求，一个汉字等于两个字符
     *
     * @param strParameter 要验证的字符串
     * @param limitLength  验证的长度
     * @return 符合长度ture 超出范围false
     */
    public static boolean validateStrByLength(String strParameter, int limitLength) {
        int temp_int = getStrLength(strParameter);

        return temp_int <= limitLength;
    }

  

 
    /**
     * 模式匹配
     *
     * @param content
     * @param group   0:包含开始和结尾，1：仅仅包含最终结果
     */
    public static List<String> getRegex(String content, String regex, int group) {
        if (content == null || content.equals("")) {
            return new ArrayList<String>();
        }
        try {
            List<String> list = new ArrayList<String>();
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            if (matcher != null) {
                // 开始匹配
                while (matcher.find()) {
                    String str = matcher.group(group);
                    // 得到结果
                    if (str != null && str.length() > 0) {
                        list.add(str);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println(e + " getRegex");
            return null;
        }
    }

	public static String ISO2GBK(String str) {
		String s;
		try {
			s = new String(str.getBytes("ISO8859-1"), "GBK");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}

	public static String GBK2ISO(String str) {
		String s;
		try {
			s = new String(str.getBytes("GBK"), "ISO8859-1");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}
	
	
	public static String UTF2GBK(String str) {
		String s;
		try {
			s = new String(str.getBytes("UFT-8"), "GBK");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}

	public static String GBK2UTF(String str) {
		String s;
		try {
			s = new String(str.getBytes("GBK"), "UTF-8");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}
	public static String UTF2ISO(String str) {
		String s;
		try {
			s = new String(str.getBytes("UFT-8"), "ISO8859-1");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}

	public static String ISO2UTF(String str) {
		String s;
		try {
			s = new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (Exception e) {
			s = str;
		}
		return s;
	}
	
	public static String gbk2Unicode(String str) {
		StringBuffer ret = new StringBuffer();
		String tmp = null;
		try {
			byte[] bb = str.getBytes("UTF-16");
			for (int i = 3; i < bb.length; i += 2) {
				// i++;
				if (bb[i - 1] != 0)
					tmp = toHEXString(bb[i - 1]) + toHEXString(bb[i]);
				else
					tmp = "00" + toHEXString(bb[i]);
				ret.append("\\u").append(tmp);
			}
			return ret.toString();
		} catch (Exception e) {
			// TODO: handle exception
			return str;
		}
	}

	public static String unicode2GBK(String str) {
		String[] s = str.split("\\\\u");
		StringBuffer ret = new StringBuffer();
		for (int i = 1; i < s.length; i++) {
			ret.append((char) Integer.parseInt(s[i], 16));
		}
		return ret.toString();
	}

	private static String toHEXString(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
				.charAt(b & 0xF));
	}

    public static String asciiReverse(String data) {
        if (data == null)
            return null;

        int len = data.length();

        if (len == 0)
            return data;

        StringBuilder str_buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char in = data.charAt(i);

            if (in > -1 && in < 128) {
                in = (char) (127 - in);
            }
            str_buf.append(in);
        }

        return str_buf.toString();
    }

    /**
     * 按指定长度裁减字符串用于显示（英文字符1,中文字符2）
     * note: 函数对中文做了特殊处理，因此如果输入中含有中文，应该调用此函数，否则，直接通过String的length操作判断
     * @param str
     * @param splitLen
     * @return
     */
    public static String splitDisplayString(String str, int splitLen) {
        if (str == null)
            return null;

        char[] result = new char[splitLen];
        int dislpayLen = 0;
        int j = 0;
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch > '\u00FF')
                dislpayLen = dislpayLen + 2;
            else
                dislpayLen++;
            if (dislpayLen <= splitLen)
                result[j++] = ch;
            else
                break;
        }
        return new String(result, 0, j);
    }

    /**
     * 计算字符串字节数（英文字符1,中文字符2）
     * note: 函数对中文做了特殊处理，因此如果输入中含有中文，应该调用此函数，否则，直接通过String的length操作判断
     * @param str
     * @return
     */
    public static int displayLength(String str) {
        int dislpayLen = 0;
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch > '\u00FF')
                dislpayLen = dislpayLen + 2;
            else
                dislpayLen++;
        }
        return dislpayLen;
    }

    /**
     * 验证一个可能含有中文的字符串长度是否在指定的长度范围内
     * note: 函数对中文做了特殊处理，因此如果输入中含有中文，应该调用此函数，否则，直接通过String的length操作判断
     * @param str
     * @param maxLength
     * @return 如果大于最大值，则返回false，否则返回true
     */
    public static boolean validateMaxLength(String str, int maxLength) {
        int length = str.length();

        // 只有字符串在最大长度和最大长度的1/2之间才去获取实际长度
        if (length <= maxLength && length > maxLength / 2) {
            return !(StringUtil.displayLength(str) > maxLength);
        }
        return (length <= maxLength);
    }

    /**
     * 验证一个可能含有中文的字符串长度是否在指定的长度范围内
     * note: 函数对中文做了特殊处理，因此如果输入中含有中文，应该调用此函数，否则，直接通过String的length操作判断
     * @param str
     * @param maxLength
     * @return 如果小于最小值，则返回false，否则返回true
     */
    public static boolean validateMinLength(String str, int minLength) {
        int length = str.length();

        if (length >= minLength)
            return true;
        // 只有字符串在最大长度和最大长度的1/2之间才去获取实际长度
        if (length < minLength && length >= minLength / 2) {
            return !(StringUtil.displayLength(str) < minLength);
        }
        return (length > minLength);
    }

    /**
     * 过滤有可能出现的xss攻击的非法字符
     * @param str
     * @return
     */
    public static String filterXss(String str) {
        str = str.replaceAll("<", "");
        str = str.replaceAll(">", "");
        str = str.replaceAll("=", "");
        return str;
    }

	public static String escape(String src) {   
        int i;   
        char j;   
        StringBuffer tmp = new StringBuffer();   
        tmp.ensureCapacity(src.length() * 6);   
        for (i = 0; i < src.length(); i++) {   
            j = src.charAt(i);   
            if (Character.isDigit(j) || Character.isLowerCase(j)   
                    || Character.isUpperCase(j))   
                tmp.append(j);   
            else if (j < 256) {   
                tmp.append("%");   
                if (j < 16)   
                    tmp.append("0");   
                tmp.append(Integer.toString(j, 16));   
            } else {   
                tmp.append("%u");   
                tmp.append(Integer.toString(j, 16));   
            }   
        }   
        return tmp.toString();   
    }   

    public static String unescape(String src) {   
        StringBuffer tmp = new StringBuffer();   
        tmp.ensureCapacity(src.length());   
        int lastPos = 0, pos = 0;   
        char ch;   
        while (lastPos < src.length()) {   
            pos = src.indexOf("%", lastPos);   
            if (pos == lastPos) {   
                if (src.charAt(pos + 1) == 'u') {   
                    ch = (char) Integer.parseInt(src   
                            .substring(pos + 2, pos + 6), 16);   
                    tmp.append(ch);   
                    lastPos = pos + 6;   
                } else {   
                    ch = (char) Integer.parseInt(src   
                            .substring(pos + 1, pos + 3), 16);   
                    tmp.append(ch);   
                    lastPos = pos + 3;   
                }   
            } else {   
                if (pos == -1) {   
                    tmp.append(src.substring(lastPos));   
                    lastPos = src.length();   
                } else {   
                    tmp.append(src.substring(lastPos, pos));   
                    lastPos = pos;   
                }   
            }   
        }   
        return tmp.toString();   
    }   

    public static String getCharAndNumr(int length)     
    {     
        String val = "";     
                 
        Random random = new Random();     
        for(int i = 0; i < length; i++)     
        {     
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字     
                     
            if("char".equalsIgnoreCase(charOrNum)) // 字符串     
            {     
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
                val += (char) (choice + random.nextInt(26));     
            }     
            else if("num".equalsIgnoreCase(charOrNum)) // 数字     
            {     
                val += String.valueOf(random.nextInt(10));     
            }     
        }     
                 
        return val;     
    }
    
    public static void main(String[] args) {

    }
    
    
}
