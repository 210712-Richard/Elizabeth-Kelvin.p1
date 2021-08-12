package com.revature.forms;
import java.time.LocalDate;

public class ReimbursalForm {

	private Integer    empId;         // Employee ID
	private Double 	   percentage;    // obtained percentage 
	private Double     paidTuition;    // Total tuition amount paid
	private String     manager;       // whom form is with
	private MarkSheet  markSheet;     // mark sheet in grade or marks
	private String     description;   // Some message 
	private String     courseType;    // University/Seminar/Certification
	private String     message;       // for manager to respond
	private LocalDate  submitTime;    // auto computed time of submission

	
	public ReimbursalForm() {
		super();
	}
	
	public ReimbursalForm(Integer empId, Double percentage, Double paidTuition,
			              String manager, MarkSheet markSheet, String description, 
			              String courseType, String message, LocalDate submitTime) {
		this();
		this.empId       = empId;
		this.percentage  = percentage;
		this.paidTuition = paidTuition;
		this.manager     = manager;
		this.markSheet   = markSheet;
		this.description = description;
		this.courseType  = courseType;
		this.message     = message;
		this.submitTime  = submitTime;
	}
	
	/* Employee ID should not be modified.
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
	
	public Double getPaidTuition() {
		return paidTuition;
	}

	public void setPaidTuition(Double paidTution) {
		this.paidTuition = paidTution;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getcourseType() {
		return courseType;
	}
	
	public String getManager() {
		return manager;
	}
	
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public MarkSheet getMarkSheet() {
		return markSheet;
	}
	
	public void setMarkSheet(Integer empId, String mrkA, String mrkB, String mrkC, String mrkD) {
		this.markSheet = new MarkSheet(empId, mrkA, mrkB, mrkC, mrkD);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public LocalDate getSubmitTime() {
		return submitTime;
	}
}
