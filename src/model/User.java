
package model;

import java.time.LocalDate;

import utility.UserType;

public class User {
	private int userId;
	private String name;
	private LocalDate dob;
	private long number;
	private String status;
	private String password;
	private UserType type;
	private String location;
	private String city;
	private String state;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public User() {
	}

	public User(int userId, String name, LocalDate dob, long number, String status, String password, UserType type,
			String location, String city, String state) {
		super();
		this.userId = userId;
		this.name = name;
		this.dob = dob;
		this.number = number;
		this.status = status;
		this.password = password;
		this.type = type;
		this.location = location;
		this.city = city;
		this.state = state;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}
