package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "library")
public class Library implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -175781762866630543L;

    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "library_name")
    private String name;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "library_desc")
    private String desc;
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "create_user_id")
    private int createUserId;

    /** 东经longitude **/
    @Column(name = "longitude")
    private float longitude;
    
    /** 北纬longitude **/
    @Column(name = "latitude")
    private float latitude;
    
    /** 身份provice **/
    @Column(name = "provice")    
    private String provice;
    
    /** 城市city **/
    @Column(name = "city")    
    private String city;
    
    /** 区县district **/
    @Column(name = "district")
    private String district;
    
    /** 街道stret **/
    @Column(name = "street")   
    private String street;
    
    /** 街号streetNumber **/
    @Column(name = "street_number")   
    private String streetNumber;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    
}
