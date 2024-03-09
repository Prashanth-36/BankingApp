package model;

import utility.ActiveStatus;
import utility.UserType;

public class Customer extends User {
	private long aadhaarNo;
	private String panNo;

	public Customer() {
	}

	public Customer(int userId, String name, long dob, String address, long number, ActiveStatus status,
			String password, UserType type, String location, String city, String state, String email, long aadhaarNo,
			String panNo) {
		super(userId, name, dob, number, status, password, type, location, city, state, email);
		this.aadhaarNo = aadhaarNo;
		this.panNo = panNo;
	}

	public long getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(long aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	@Override
	public String toString() {
		return "Customer [aadhaarNo=" + aadhaarNo + ", panNo=" + panNo + ", " + super.toString() + "]";
	}

}
