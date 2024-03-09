package model;

import utility.ActiveStatus;
import utility.Privilege;
import utility.UserType;

public class Employee extends User {

	private int branchId;
	private Privilege privilege;

	public Employee() {
	}

	public Employee(int userId, String name, long dob, long number, ActiveStatus status, String password, UserType type,
			String location, String city, String state, int branchId, Privilege privilege, String email) {

		super(userId, name, dob, number, status, password, type, location, city, state, email);
		this.branchId = branchId;
		this.privilege = privilege;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	@Override
	public String toString() {
		return "Employee [branchId=" + branchId + ", privilege=" + privilege + ", " + super.toString() + "]";
	}
}
