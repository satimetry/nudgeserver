
package com.satimetry.nudge.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(name="msg")
public class Msg implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "msgid")
    private Integer msgid;

    @NotNull
    private Integer programid;

    @NotNull
    private Integer userid;

    private Integer ruleid;

	@NotNull
    @NotEmpty
    private String rulename;

	@NotNull
    private Timestamp ruledate;

	@NotNull
    @NotEmpty
    private String msgtxt;

    private byte issent;
    private byte isread;
    private String urldesc;

    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }
    
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getMsgid() {
		return msgid;
	}

	public void setMsgid(Integer msgid) {
		this.msgid = msgid;
	}

	public String getRulename() {
		return rulename;
	}

	public void setRulename(String rulename) {
		this.rulename = rulename;
	}

	public Timestamp getRuledate() {
		return ruledate;
	}

	public void setRuledate(Timestamp ruledate) {
		this.ruledate = ruledate;
	}

	public String getMsgtxt() {
		return msgtxt;
	}

	public void setMsgtxt(String msgtxt) {
		this.msgtxt = msgtxt;
	}

	public byte getIssent() {
		return issent;
	}

	public void setIssent(byte issent) {
		this.issent = issent;
	}

	public byte getIsread() {
		return isread;
	}

	public void setIsread(byte isread) {
		this.isread = isread;
	}

	public String getUrldesc() {
		return urldesc;
	}

	public void setUrldesc(String urldesc) {
		this.urldesc = urldesc;
	}

	public Integer getRuleid() {
		return ruleid;
	}

	public void setRuleid(Integer ruleid) {
		this.ruleid = ruleid;
	}
    
}
