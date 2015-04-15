
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
@Table(name="userobs")
public class Userobs implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userobsid")
    private Integer userobsid;

    @NotNull
    private Integer programid;

    @NotNull
    private Integer userid;

	@NotNull
    @NotEmpty
    private String obsname;

	@NotNull
    @NotEmpty
    private String obsvalue;

	@NotNull
    @NotEmpty
    private String obsdesc;

	@NotNull
    private Timestamp obsdate;

	@NotNull
    @NotEmpty
    private String obstype;

    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }
    
	public Integer getUserobsid() {
		return userobsid;
	}

	public void setUserobsid(Integer userobsid) {
		this.userobsid = userobsid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getObsname() {
		return obsname;
	}

	public void setObsname(String obsname) {
		this.obsname = obsname;
	}

	public String getObsvalue() {
		return obsvalue;
	}

	public void setObsvalue(String obsvalue) {
		this.obsvalue = obsvalue;
	}

	public String getObsdesc() {
		return obsdesc;
	}

	public void setObsdesc(String obsdesc) {
		this.obsdesc = obsdesc;
	}

	public Timestamp getObsdate() {
		return obsdate;
	}

	public void setObsdate(Timestamp obsdate) {
		this.obsdate = obsdate;
	}

	public String getObstype() {
		return obstype;
	}

	public void setObstype(String obstype) {
		this.obstype = obstype;
	}
    
}
