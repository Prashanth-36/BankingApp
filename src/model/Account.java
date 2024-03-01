package model;

import java.time.LocalDate;

public class Account {
	private int accountNo;
	private int customerId;
	private double currentBalance;
	private boolean isPrimaryAccout;
	private LocalDate openDate;
	private int branchId;
	private String status;

	public int getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(int accountNo) {
		this.accountNo = accountNo;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public boolean isPrimaryAccout() {
		return isPrimaryAccout;
	}

	public void setPrimaryAccout(boolean isPrimaryAccout) {
		this.isPrimaryAccout = isPrimaryAccout;
	}

	public LocalDate getOpenDate() {
		return openDate;
	}

	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
