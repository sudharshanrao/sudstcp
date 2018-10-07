package com.cleartcp.domain;

import java.io.Serializable;
import java.util.Date;

import com.cleartcp.enumeration.Application;

public class CMUser implements Serializable {
	private static final long serialVersionUID = 4158786123124044551L;
	
	// temp data point for backwards compatibility
	private String adminType;

	// user data
	private long id;
	private String loginId;
	private String firstName;
	private String email;
	private String lastName;
	private String userType;
	private String password;
	private Date lastLoginTime;
	private boolean resetPassword;
	
	// company data
	private long companyId;
	private String companyFullName;
	private String companyShortName;
	
	// legal entity data
	private long legalEntityId;
	private String legalEntityFullName;
	private String legalEntityShortName;
	private String legalEntityType;
	
	// Gateway data
	private Application application;
	private AuthAudit authAudit;
	
	public CMUser() {
	}
	
	public CMUser(String loginId, Application applicationId) {
		this.loginId = loginId;
		this.application = applicationId;
	}
	
	public CMUser(String loginId, String password, String applicationId) {
		this.loginId = loginId;
		this.password = password;
		this.application = Application.valueOf(applicationId);
	}
	
	public String getAdminType() {
		return adminType;
	}

	public void setAdminType(String adminType) {
		this.adminType = adminType;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public AuthAudit getAuthAudit() {
		return authAudit;
	}

	public void setAuthAudit(AuthAudit authAudit) {
		this.authAudit = authAudit;
	}

	public String getCompanyFullName() {
		return companyFullName;
	}

	public void setCompanyFullName(String companyFullName) {
		this.companyFullName = companyFullName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLegalEntityFullName() {
		return legalEntityFullName;
	}

	public void setLegalEntityFullName(String legalEntityFullName) {
		this.legalEntityFullName = legalEntityFullName;
	}

	public long getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(long legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getLegalEntityShortName() {
		return legalEntityShortName;
	}

	public void setLegalEntityShortName(String legalEntityShortName) {
		this.legalEntityShortName = legalEntityShortName;
	}

	public String getLegalEntityType() {
		return legalEntityType;
	}

	public void setLegalEntityType(String legalEntityType) {
		this.legalEntityType = legalEntityType;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CMUser other = (CMUser) obj;
		if (application != other.application)
			return false;
		if (loginId == null) {
			if (other.loginId != null)
				return false;
		} else if (!loginId.equals(other.loginId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((loginId == null) ? 0 : loginId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CMUser [loginId=");
		builder.append(loginId);
		builder.append(", email=");
		builder.append(email);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", legalEntityShortName=");
		builder.append(legalEntityShortName);
		builder.append(", legalEntityFullName=");
		builder.append(legalEntityFullName);
		builder.append(", companyShortName=");
		builder.append(companyShortName);
		builder.append(", companyFullName=");
		builder.append(companyFullName);
		builder.append(", id=");
		builder.append(id);
		builder.append(", legalEntityId=");
		builder.append(legalEntityId);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", application=");
		builder.append(application);
		builder.append(", authAudit=");
		builder.append(authAudit);
		builder.append(", lastLoginTime=");
		builder.append(lastLoginTime);
		builder.append(", legalEntityType=");
		builder.append(legalEntityType);
		builder.append(", resetPassword=");
		builder.append(resetPassword);
		builder.append("]");
		return builder.toString();
	}
}