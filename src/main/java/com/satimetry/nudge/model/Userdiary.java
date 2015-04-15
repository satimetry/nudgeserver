
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
@Table(name="userdiary")
public class Userdiary implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userdiaryid")
    private Integer userdiaryid;

    @NotNull
    private Integer programid;

    @NotNull
    private Integer userid;

	@NotNull
    @NotEmpty
    private String diarylabel;
	
	@NotNull
    @NotEmpty
    private String diarytxt;

	@NotNull
    private Timestamp diarydate;
	
    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }
    
	public Integer getUserdiaryid() {
		return userdiaryid;
	}

	public void setUserdiaryid(Integer userdiaryid) {
		this.userdiaryid = userdiaryid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getDiarylabel() {
		return diarylabel;
	}

	public void setDiarylabel(String diarylabel) {
		this.diarylabel = diarylabel;
	}
	
	public String getDiarytxt() {
		return diarytxt;
	}

	public void setDiarytxt(String diarytxt) {
		this.diarytxt = diarytxt;
	}
	
	public Timestamp getDiarydate() {
		return diarydate;
	}

	public void setDiarydate(Timestamp diarydate) {
		this.diarydate = diarydate;
	}
	
}
