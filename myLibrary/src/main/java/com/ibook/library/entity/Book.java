package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书籍信息
 * 
 * @author xiaojianyu
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7162904091160794548L;

    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** isbn10-国际标准书号 **/
    @Column(name = "isbn10")
    private String isbn10;

    /** isbn13-国际标准书号 **/
    @Column(name = "isbn13")
    private String isbn13;

    /** 标题 **/
    @Column(name = "title")
    private String title;

    /** 源标题 **/
    @Column(name = "origin_title")
    private String originTitle;

    /** alt提示标题 **/
    @Column(name = "alt_title")
    private String altTitle;

    /** 副标题 **/
    @Column(name = "sub_title")
    private String subTitle;

    /** 作者 **/
    @Column(name = "author")
    private String author;

    /** 作者介绍 **/
    @Column(name = "author_intro")
    private String authorIntro;

    /** 译者 **/
    @Column(name = "translator")
    private String translator;

    /** 出版年份 **/
    @Column(name = "pubdate")
    private String pubdate;

    /** 出版社 **/
    @Column(name = "publisher")
    private String publisher;

    /** 摘要 **/
    @Column(name = "summary")
    private String summary;

    /** 豆瓣 api url **/
    @Column(name = "url")
    private String url;

    /** 豆瓣网 url **/
    @Column(name = "alt")
    private String alt;

    /** 小图url **/
    @Column(name = "small_img")
    private String smallImg;

    /** 中图url **/
    @Column(name = "medium_img")
    private String mediumImg;

    /** 大图url **/
    @Column(name = "large_img")
    private String largeImg;

    /** 页数 **/
    @Column(name = "pages")
    private String pages;

    /** 价格 **/
    @Column(name = "price")
    private String price;

    /** 标签 **/
    @Column(name = "tags")
    private String tags;

    /** 平均得分 **/
    @Column(name = "average")
    private float average;

    /** 投票人数 **/
    @Column(name = "num_raters")
    private int numRaters;

    /** 拥有者 **/
    @Column(name = "owner_user_id")
    private int ownerUserId;

    /** 拥有者passport **/
    @Column(name = "owner_passport")
    private String ownerPassport;
    
    /** 上传者 **/
    @Column(name = "upload_user_id")
    private int  uploadUserId;
    
    /** 上传者passport **/
    @Column(name = "upload_passport")
    private String uploadPassport;
    
    /** 借阅者 **/
    @Column(name = "borrow_user_Id")
    private int borrowUserId;
    
    /** 借阅者passport **/
    @Column(name = "borrow_passport")
    private String borrowPassport;
    
    /**0-自由的书，1-锁定的书，2-私人的书**/
    @Column(name = "status")
    private int status;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public String getAltTitle() {
        return altTitle;
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public String getMediumImg() {
        return mediumImg;
    }

    public void setMediumImg(String mediumImg) {
        this.mediumImg = mediumImg;
    }

    public String getLargeImg() {
        return largeImg;
    }

    public void setLargeImg(String largeImg) {
        this.largeImg = largeImg;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public int getNumRaters() {
        return numRaters;
    }

    public void setNumRaters(int numRaters) {
        this.numRaters = numRaters;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(int borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(int uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getOwnerPassport() {
        return ownerPassport;
    }

    public void setOwnerPassport(String ownerPassport) {
        this.ownerPassport = ownerPassport;
    }

    public String getUploadPassport() {
        return uploadPassport;
    }

    public void setUploadPassport(String uploadPassport) {
        this.uploadPassport = uploadPassport;
    }

    public String getBorrowPassport() {
        return borrowPassport;
    }

    public void setBorrowPassport(String borrowPassport) {
        this.borrowPassport = borrowPassport;
    }

}
