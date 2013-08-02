/**
 * 
 */
package org.necros.portal.org;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Embeddable
public class ComplexContact implements Serializable {
	@Column(name="contact_address", length=200)
	private String address;
	@Column(name="contact_postal", length=20)
	private String postalCode;
	@Column(name="contact_tel", length=50)
	private String tel;
	@Column(name="contact_fax", length=50)
	private String fax;
	@Column(name="contact_email", length=200)
	private String email;
	@Column(name="contact_mobile", length=60)
	private String mobile;
	@Column(name="contact_qq", length=60)
	private String qq;
	@Column(name="contact_live_messenger", length=200)
	private String liveMessenger;
	@Column(name="contact_skype", length=200)
	private String skype;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getLiveMessenger() {
		return liveMessenger;
	}

	public void setLiveMessenger(String liveMessenger) {
		this.liveMessenger = liveMessenger;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}
}
