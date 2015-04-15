
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
@Table(name="optinruleview")
public class Optinruleview implements Serializable {
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
    @NotEmpty
    private String rulename;

	public Integer getProgramruleuserid() {
		return programruleuserid;
	}

    public Integer getProgramid() {
        return programid;
    }

	public Integer getUserid() {
		return userid;
	}

	public Integer getRuleid() {
		return ruleid;
	}

	public String getRulename() {
		return rulename;
	}
    
}
