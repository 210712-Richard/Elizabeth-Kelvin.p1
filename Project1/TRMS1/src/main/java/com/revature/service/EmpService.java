package com.revature.service;

import java.time.LocalDate;

import com.revature.forms.MarkSheet;

public interface EmpService {

	boolean CreateAppForm(Integer empId, Double Percent, Double PaidTuition, String manager,
            MarkSheet ms, String descrpt, String course,  String messg);
	
	boolean CreateMarksForm(Integer empId, String mrkA, String mrkB, String mrkC, String mrkD);
	 
	String GetStatus(String username);
}
