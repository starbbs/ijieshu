package com.ibook.library.util;

import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {
    
    protected static Logger log = LoggerFactory.getLogger("library-front");


    public static void sendWarningMail(String title, String content, String... tos) {
        // logger.info("begin to invoke sendWarningMail");
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.163.com");
            email.setAuthentication("ijieshu@163.com", "1987816");
            if (tos != null) {
                for (String to : tos) {
                    email.addTo(to);
                }
            }
            email.setFrom("ijieshu@163.com", "哎•借书");
            email.setSubject(title);
            email.setCharset("UTF-8");
            email.setHtmlMsg(content);
            email.send();
        } catch (Exception e) {
            log.info("error invoke sendWarningMail error ", e);
        }
    }

    public static void main(String[] args) {
        EmailUtil.sendWarningMail("找回密码", "xxxx", "jxnu163@163.com");
    }
}
