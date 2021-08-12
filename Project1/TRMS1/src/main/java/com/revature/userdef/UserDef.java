package com.revature.userdef;
import java.io.Serializable;
import java.util.Objects;
import com.revature.userdef.UserType;

public class UserDef implements Serializable {
	// literally only created the UID to get rid of the warning, and it doesn't matter at all.
	private static final long serialVersionUID = -6426075925303078798L;
	private Integer empId;
	private String  username;
	private String  password;
	private String  email;
	private String  manager;
	private Double  tuitionAmnt;
	private UserType userType;
	private PaymentStatus  paymentStatus;
	private Double netPaid;

	
	
	public UserDef() {
		super();
	}
	
	public UserDef(Integer empId, String username, String password,
			       String email, UserType userType, String manager,
			       Double tuitionAmnt, PaymentStatus paymentStatus) {
		this();
		this.empId         = empId;
		this.email         = email;
		this.username      = username;
		this.password      = password;
		this.tuitionAmnt   = tuitionAmnt;
		this.paymentStatus = paymentStatus;
		this.userType      = userType;
		this.manager       = manager;
	}
	
	
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getTuitionAmnt() {
		return tuitionAmnt;
	}
	
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public void setTuitionAmnt(Double tuitionAmnt) {
		this.tuitionAmnt = tuitionAmnt;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	public Double getNetPaid() {
		return netPaid;
	}
	
	public void setNetPaid(Double netPaid) {
		if (netPaid == 0) 
			this.netPaid = 0.0;
		else if (this.netPaid + netPaid >= 1000.0)
			this.netPaid = 1000.0;
		else
			this.netPaid += netPaid;
	}
	
	//@Override
	public int hashCode(Integer empId, String username, String password,
			            String email, Double tuitionAmnt, String paymentStatus) {
		return Objects.hash();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDef other = (UserDef) obj;
		return Objects.equals(empId, other.empId) && Objects.equals( password, other.password)
				&& Objects.equals(paymentStatus, other.paymentStatus) && Objects.equals(email, other.email)
				&& Objects.equals(manager, other.manager) && Objects.equals(userType, other.userType)
				&& Objects.equals(tuitionAmnt, other.tuitionAmnt) && Objects.equals(username, other.username);
				//&& Objects.equals(netPaid, other.netPaid);
	}
	
	@Override
	public String toString() {
		return "UserDef [empId=" + empId + ", username=" + username + ", email=" + email + ", password=" + password
				+  ", tuitionAmnt=" + tuitionAmnt + ", paymentStatus" + paymentStatus + "]";
	}

	
	
}
