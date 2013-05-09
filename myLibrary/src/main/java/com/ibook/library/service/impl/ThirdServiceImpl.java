package com.ibook.library.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.ibook.library.entity.Book;
import com.ibook.library.service.ThirdService;
import com.ibook.library.util.StringUtil;
import com.ibook.library.util.WebResourceReader;

@Service
public class ThirdServiceImpl implements ThirdService {

    protected static Logger logger = LoggerFactory.getLogger("library");
    
    public Book getBookFromDouban(String identity) {
        
        String bookStr=WebResourceReader.doGet("http://api.douban.com/v2/book/"+identity);
        if(StringUtil.isEmpty(bookStr)){
            return null;
        }
        logger.info("http://api.douban.com/v2/book/"+identity);
        logger.info(bookStr);
        
        Book book =new Book();
        try {
            JSONObject  result= new JSONObject();
            result=result.getJSONObject(bookStr);
            book.setAlt(result.getString("alt"));
            book.setAltTitle(result.getString("alt_title"));
            book.setAuthor(result.getString("author"));
            book.setAuthorIntro(result.getString("author_intro"));
            
            JSONObject rating=result.getJSONObject("rating");
            book.setAverage(Double.valueOf(rating.getDouble("average")).floatValue());
            book.setNumRaters(rating.getInt("numRaters"));
            
//            book.setBorrowUserId(result.getInt(""))
            book.setIsbn10(result.getString("isbn10"));
            book.setIsbn13(result.getString("isbn13"));
            
            JSONObject images=result.getJSONObject("images");
            book.setLargeImg(images.getString("large"));
            book.setMediumImg(images.getString("medium"));
            book.setSmallImg(images.getString("small"));
            
            
            book.setOriginTitle(result.getString("origin_title"));
            book.setPages(result.getString("pages"));
            book.setPrice(result.getString("price"));
            book.setPubdate(result.getString("pubdate"));
            book.setPublisher(result.getString("publisher"));
            book.setSubTitle(result.getString("subtitle"));
            book.setSummary(StringUtil.subString(result.getString("summary"), 512));
            
            JSONArray tags=result.getJSONArray("tags");
            book.setTags(tags.toString());
            book.setTranslator(result.getString("translator"));
            book.setTitle(result.getString("title"));
            book.setUrl(result.getString("url"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return book;
    }

    public List<Book> getBooksFromDouban(String query) {
        String books=WebResourceReader.doGet("http://api.douban.com/v2/book/search?q="+query);
        return null;
    }

}
