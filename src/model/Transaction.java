package model;

import java.sql.Timestamp;

import utility.TransactionType;

public class Transaction {
	private int id;
	private TransactionType type;
	private double amount;
	private int primaryAccount;
	private int transactionalAccount;
	private Timestamp timestamp;
	private String description;

	public Transaction() {
	}

	public Transaction(int id, TransactionType type, double amount, int primaryAccount, int transactionalAccount,
			Timestamp timestamp, String description) {
		super();
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.primaryAccount = primaryAccount;
		this.transactionalAccount = transactionalAccount;
		this.timestamp = timestamp;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(int primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	public int getTransactionalAccount() {
		return transactionalAccount;
	}

	public void setTransactionalAccount(int transactionalAccount) {
		this.transactionalAccount = transactionalAccount;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
