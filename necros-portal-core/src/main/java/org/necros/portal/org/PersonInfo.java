/**
 * 
 */
package org.necros.portal.org;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author  weiht
 */
@SuppressWarnings("serial")
@Embeddable
public class PersonInfo implements Serializable {
	@Column(name="full_name", length=200, nullable=false)
	private String name;
	@Column(name="alt_name", length=200)
	private String altName;
	@Column(name="person_gender", length=10)
	private String gender;
	@Column(name="birth_date")
	private Date birthDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAltName() {
		return altName;
	}
	
	public void setAltName(String altName) {
		this.altName = altName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}