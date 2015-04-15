
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
@Table(name="user")
public class User implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;
	   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userid;

	@NotNull
    @NotEmpty
    private String username;

	@NotNull
    @NotEmpty
    private String password;

	@NotNull
    @NotEmpty
    private Integer sex;

	@NotNull
    @NotEmpty
    private Integer age;

    private String pushoveruser;
    private String fitbitkey;
    private String fitbitsecret;
    private String fitbitappname;
    
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPushoveruser() {
		return pushoveruser;
	}

	public void setPushoveruser(String pushoveruser) {
		this.pushoveruser = pushoveruser;
	}

	public String getFitbitkey() {
		return fitbitkey;
	}

	public void setFitbitkey(String fitbitkey) {
		this.fitbitkey = fitbitkey;
	}

	public String getFitbitsecret() {
		return fitbitsecret;
	}

	public void setFitbitsecret(String fitbitsecret) {
		this.fitbitsecret = fitbitsecret;
	}

	public String getFitbitappname() {
		return fitbitappname;
	}

	public void setFitbitappname(String fitbitappname) {
		this.fitbitappname = fitbitappname;
	}
    
}
