
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
@Table(name="programruleuser")
public class Programruleuser implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "programruleuserid")
    private Integer programruleuserid;

    @NotNull
    private Integer programid;

    @NotNull
    private Integer userid;

    @NotNull
    private Integer ruleid;

    @NotNull
    private Integer rulevalue;
    
	@NotNull
    @NotEmpty
    private String ruleuserdesc;

    private Integer rulehigh;
    private Integer rulelow;

	public Integer getProgramruleuserid() {
		return programruleuserid;
	}

	public void setProgramruleuserid(Integer programruleuserid) {
		this.programruleuserid = programruleuserid;
	}

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

	public Integer getRuleid() {
		return ruleid;
	}

	public void setRuleid(Integer ruleid) {
		this.ruleid = ruleid;
	}

	public Integer getRulevalue() {
		return rulevalue;
	}

	public void setRulevalue(Integer rulevalue) {
		this.rulevalue = rulevalue;
	}

	public String getRuleuserdesc() {
		return ruleuserdesc;
	}

	public void setRuleuserdesc(String ruleuserdesc) {
		this.ruleuserdesc = ruleuserdesc;
	}

	public Integer getRulehigh() {
		return rulehigh;
	}

	public void setRulehigh(Integer rulehigh) {
		this.rulehigh = rulehigh;
	}

	public Integer getRulelow() {
		return rulelow;
	}

	public void setRulelow(Integer rulelow) {
		this.rulelow = rulelow;
	}
    
}
