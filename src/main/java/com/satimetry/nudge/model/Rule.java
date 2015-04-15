
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
@Table(name="rule")
public class Rule implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ruleid")
    private Integer ruleid;

	@NotNull
    @NotEmpty
    private String rulename;

	@NotNull
    @NotEmpty
    private String ruledesc;
	
	@NotNull
    @NotEmpty
    private String ruletype;

	@NotNull
    @NotEmpty
    private String awardtype;

	@NotNull
    private String pollname;

	@NotNull
    @NotEmpty
    private Integer parentruleid;

	public Integer getRuleid() {
		return ruleid;
	}

	public void setRuleid(Integer ruleid) {
		this.ruleid = ruleid;
	}

	public String getRulename() {
		return rulename;
	}

	public void setRulename(String rulename) {
		this.rulename = rulename;
	}

	public String getRuledesc() {
		return ruledesc;
	}

	public void setRuledesc(String ruledesc) {
		this.ruledesc = ruledesc;
	}

	public String getRuletype() {
		return ruletype;
	}

	public void setRuletype(String ruletype) {
		this.ruletype = ruletype;
	}

	public String getAwardtype() {
		return awardtype;
	}

	public void setAwardtype(String awardtype) {
		this.awardtype = awardtype;
	}

	public String getPollname() {
		return pollname;
	}

	public void setPollname(String pollname) {
		this.pollname = pollname;
	}

	public Integer getParentruleid() {
		return parentruleid;
	}

	public void setParentruleid(Integer parentruleid) {
		this.parentruleid = parentruleid;
	}


    
}
