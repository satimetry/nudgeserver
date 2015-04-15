
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
@Table(name="programuser")
public class Programuser implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "programuserid")
    private Integer programuserid;

	@NotNull
    @NotEmpty
    private Integer programid;

	@NotNull
    @NotEmpty
    private Integer userid;

	@NotNull
    @NotEmpty
    private String roletype;

    private Integer msgtotalcount;
    private Integer msgunreadcount;
    private Integer ruleoptincount;
    private Integer pointcount;
    
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getProgramuserid() {
		return programuserid;
	}

	public void setProgramuserid(Integer programuserid) {
		this.programuserid = programuserid;
	}

	public Integer getProgramid() {
		return programid;
	}

	public void setProgramid(Integer programid) {
		this.programid = programid;
	}

	public String getRoletype() {
		return roletype;
	}

	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}

	public Integer getMsgtotalcount() {
		return msgtotalcount;
	}

	public void setMsgtotalcount(Integer msgtotalcount) {
		this.msgtotalcount = msgtotalcount;
	}

	public Integer getMsgunreadcount() {
		return msgunreadcount;
	}

	public void setMsgunreadcount(Integer msgunreadcount) {
		this.msgunreadcount = msgunreadcount;
	}

	public Integer getRuleoptincount() {
		return ruleoptincount;
	}

	public void setRuleoptincount(Integer ruleoptincount) {
		this.ruleoptincount = ruleoptincount;
	}

	public Integer getPointcount() {
		return pointcount;
	}

	public void setPointcount(Integer pointcount) {
		this.pointcount = pointcount;
	}


    
}
