package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户信息
 * @author xiaojianyu
 *
 */
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 2706138287923094538L;
    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /** 昵称 **/
    @Column(name = "nick")
    private String nick;
    
    /** 自我介绍 **/
    @Column(name = "descripiton")
    private String descripiton;
    
    /** passport **/
    @Column(name = "passport")
    private String passport;
    
    /** password **/
    @Column(name = "password")
    private String password;
    
    /** 头像 **/
    @Column(name = "img")
    private String img;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getDescripiton() {
        return descripiton;
    }
    public void setDescripiton(String descripiton) {
        this.descripiton = descripiton;
    }
    public String getPassport() {
        return passport;
    }
    public void setPassport(String passport) {
        this.passport = passport;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    
    
}
