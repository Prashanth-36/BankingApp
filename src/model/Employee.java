package model;

import java.time.LocalDate;

import utility.Privilege;
import utility.UserType;

public class Employee extends User {
	private int branchId;
	private Privilege privilege;

	public Employee(int userId, String name, LocalDate dob, long number, String status, String password,
			UserType type, String location, String city, String state, int branchId, Privilege privilege) {

		super(userId, name, dob, number, status, password, type, location, city, state);
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
}
