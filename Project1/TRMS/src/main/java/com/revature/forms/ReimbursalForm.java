package com.revature.forms;

public class ReimbursalForm {

	private Integer    empId;
	private Double 	   percentage;
	private Double     paidTution;
	private String     manager;
	private MarkSheet  markSheet;
	private String     message;

	
	public ReimbursalForm() {
		super();
	}
	
	public ReimbursalForm(Integer empId, Double percentage, Double paidTution,
			         String manager, MarkSheet markSheet, String message) {
		this();
		this.empId       = empId;
		this.percentage  = percentage;
		this.paidTution  = paidTution;
		this.manager     = manager;
		this.markSheet   = markSheet;
		this.message     = message;
	}
	
	/* Employee ID, Manager, MarkSheet should not be modified.
	   But percentage (entered wrong) and PaidTution can change  */
	
	public Integer getEmpId() {
		return empId;
	}

	public Double getPercent() {
		return percentage;
	}

	public void setPercent(Double percentage) {
		this.percentage = percentage;
	}
	
	public Double getPaidTution() {
		return paidTution;
	}

	public void setPaidTution(Double paidTution) {
		this.paidTution = paidTution;
	}
	
	public String getManager() {
		return manager;
	}

	public MarkSheet getMarkSheet() {
		return markSheet;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
