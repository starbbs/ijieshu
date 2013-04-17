package com.ibook.library.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate 基础访问实现类
 * @author xiaojianyu
 */
public class BaseDaoImpl extends HibernateDaoSupport {
	
    @Autowired
    public final void setHibernateTemplate0(HibernateTemplate hibernateTemplate) {
        super.setHibernateTemplate(hibernateTemplate);
    }
    

	public Serializable  save(Object entity) throws DataAccessException{
		return this.getHibernateTemplate().save(entity);
	}


	public void  update(Object entity) throws DataAccessException{
         this.getHibernateTemplate().update(entity);
    }
	
	public Object  get(String entityName, Serializable id) throws DataAccessException{
        return this.getHibernateTemplate().get(entityName, id);
   }
	
	/**
     * 进行链接关闭
     * @param conn
     * @param ps
     * @param rs
     */
    public static void clean(ResultSet rs, Statement stmt, Connection conn){
        try {
          if(rs != null){
            rs.close();
            rs = null;
          }
        }catch (SQLException ex) {}
        try {
          if(stmt != null){
            stmt.close();
            stmt = null;
          }
        }catch (SQLException ex) {}
        try {
          if(conn != null){
            conn.close();
            conn = null;
          }
        }catch (SQLException ex) {}
      }

      public static void clean(Statement stmt, Connection conn){
        try {
          if(stmt != null){
            stmt.close();
            stmt = null;
          }
        }catch (SQLException ex) {}
        try {
          if(conn != null){
            conn.close();
            conn = null;
          }
        }catch (SQLException ex) {}
      }

      public static void clean(ResultSet rs, Statement stmt){
        try {
          if(rs != null){
            rs.close();
            rs = null;
          }
        }catch (SQLException ex) {}
        try {
          if(stmt != null){
            stmt.close();
            stmt = null;
          }
        }catch (SQLException ex) {}
      }
}
