package com.revature.service;

import com.revature.forms.ReimbursalForm;
import com.revature.userdef.UserDef;

public interface ManagerService {
	boolean isManagerApproved(Integer EmpId, String name);
	boolean isDirectorApproved(Integer EmpId, String name);
	boolean fwdApproval (ReimbursalForm form,  UserDef ud);
	boolean approvalChecklistManager(ReimbursalForm form);
	boolean approvalChecklistDirector(ReimbursalForm form);
	boolean setApproval (UserDef f);
}
