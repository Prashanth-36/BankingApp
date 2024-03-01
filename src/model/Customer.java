package model;

import java.time.LocalDate;

import utility.UserType;

public class Customer extends User {
	private long aadhaarNo;
	private String panNo;

	public Customer(int userId, String name, LocalDate dob, String address, long number, String status, String password,
			UserType type, String location, String city, String state, long aadhaarNo, String panNo) {
		super(userId, name, dob, number, status, password, type, location, city, state);
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
}
