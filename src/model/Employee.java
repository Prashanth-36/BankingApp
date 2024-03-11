package model;

public class Employee extends User {

	private int branchId;

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "Employee [branchId=" + branchId + ", " + super.toString() + "]";
	}
}
