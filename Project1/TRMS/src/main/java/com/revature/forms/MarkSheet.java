package com.revature.forms;

public class MarkSheet {

	private Integer  empId;
	private Double  subA;
	private Double  subB;
	private Double  subC;
	private Double  subD;
	private Double  total;
	private Double  percent;

	
	
	public MarkSheet() {
		super();
	}
	
	public MarkSheet(Integer empId, Double markA, Double markB,
			         Double markC, Double markD) {
		this();
		this.empId     = empId;
		this.subA      = markA;
		this.subB      = markB;
		this.subC      = markC;
		this.subD      = markD;
		this.total     = markA + markB + markC + markD;
		this.percent   = (this.total / 400.0 ) * 100;
	}
	
	
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	
	public Double getSubA() {
		return subA;
	}

	public void setSubA(Double markA) {
		this.subA = markA;
	}
	
	public Double getSubB() {
		return subB;
	}

	public void setSubB(Double markB) {
		this.subB = markB;
	}

	public Double getSubC() {
		return subC;
	}

	public void setSubC(Double markC) {
		this.subC = markC;
	}
	
	public Double getSubD() {
		return subD;
	}

	public void setSubD(Double markD) {
		this.subD = markD;
	}
	
	public Double getTotal() {
		return total;
	}
	
	public Double getPercent() {
		return percent;
	}
	
}
