package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用户消息
 * @author xiaojianyu
 *
 */
@Entity
@Table(name = "user_message")
public class UserMessage implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2872814870783140736L;
    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /**发送方用户id**/
    @Column(name = "log_id")
    private int logId;
    
    /**发送方用户id**/
    @Column(name = "from_user_id")
    private int fromUserId;
    
    /**接收方用户id**/
    @Column(name = "to_user_id")
    private int toUserId;
    
    /**回复的消息id**/
    @Column(name = "old_msg_id")
    private int oldMsgId;
    
    /**发送消息**/
    @Column(name = "msg")
    private String msg;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getFromUserId() {
        return fromUserId;
    }
    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }
    public int getToUserId() {
        return toUserId;
    }
    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getOldMsgId() {
        return oldMsgId;
    }
    public void setOldMsgId(int oldMsgId) {
        this.oldMsgId = oldMsgId;
    }
    public int getLogId() {
        return logId;
    }
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    
}
