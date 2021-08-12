package com.revature.forms;

import java.util.Hashtable;

public class MarkSheet {

	private Integer  empId;
	private String  subA;
	private String  subB;
	private String  subC;
	private String  subD;

	
	
	public MarkSheet() {
		super();
	}
	
	//if entered A-F, calculate grade percent else mark percent
	public MarkSheet(Integer empId, String markA, String markB,
			String markC, String markD) {
		this();
		this.empId     = empId;
		this.subA      = markA;
		this.subB      = markB;
		this.subC      = markC;
		this.subD      = markD;
	}
	
	
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	
	public String getSubA() {
		return subA;
	}

	public void setSubA(String markA) {
		this.subA = markA;
	}
	
	public String getSubB() {
		return subB;
	}

	public void setSubB(String markB) {
		this.subB = markB;
	}

	public String getSubC() {
		return subC;
	}

	public void setSubC(String markC) {
		this.subC = markC;
	}
	
	public String getSubD() {
		return subD;
	}

	public void setSubD(String markD) {
		this.subD = markD;
	}
	
	public boolean isNumeric(String str) { 
		try {  
		    Double.parseDouble(str);  
		    return true;
		} catch(NumberFormatException e){  
		    return false;  
		}  
	}
	
	public Double getTotalPercent() {
		return (Integer.parseInt(this.subA) + Integer.parseInt(this.subB) +
				Integer.parseInt(this.subC) +Integer.parseInt(this.subD) ) * 1.0 ;
	}
	
	public Double getTotalGrade() {
		Hashtable<String, Integer> g = new Hashtable<String, Integer>();
        g.put("A", 90);
        g.put("B", 80);
        g.put("C", 70);
        g.put("D", 60);
        
        Integer sum = g.get(this.subA) + g.get(this.subB) + g.get(this.subC) + g.get(this.subD);
		return sum * 1.0;
	}
	
	public Double getPercent() {
		
		if (this.isNumeric(this.subA)) {
			return (this.getTotalPercent() / 400.0 ) * 100;
		}else {
			return (this.getTotalGrade() / 400.0 ) * 100;
		}
	}
	
}
