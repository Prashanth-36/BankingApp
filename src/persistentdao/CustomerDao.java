package persistentdao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import customexceptions.CustomException;
import model.Customer;
import persistentlayer.CustomerManager;
import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;

public class CustomerDao implements CustomerManager {

	@Override
	public void addCustomer(Customer customer) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement userStatement = connection.prepareStatement(
				"INSERT INTO user(name, dob, number,password,status,type,location,city,state,email,gender) values(?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement customerStatement = connection
						.prepareStatement("INSERT INTO customer(id,aadhaarNo,panNo) VALUES(?,?,?)");) {

			userStatement.setString(1, customer.getName());
			userStatement.setDate(2, Date.valueOf(customer.getDob()));
			userStatement.setLong(3, customer.getNumber());
			userStatement.setString(4, customer.getPassword());
			userStatement.setString(5, ActiveStatus.ACTIVE.name());
			userStatement.setString(6, customer.getType().name());
			userStatement.setString(7, customer.getLocation());
			userStatement.setString(8, customer.getCity());
			userStatement.setString(9, customer.getState());
			userStatement.setString(10, customer.getEmail());
			userStatement.setString(11, customer.getGender().name());

			customerStatement.setLong(2, customer.getAadhaarNo());
			customerStatement.setString(3, customer.getPanNo());

			try {
				connection.setAutoCommit(false);
				int userAffectedRows = userStatement.executeUpdate();
				if (userAffectedRows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys();) {
						if (resultSet.next()) {
							customerStatement.setInt(1, resultSet.getInt(1));
							customerStatement.execute();
							connection.commit();
						}
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new CustomException("Customer Creation failed!", e);
			} finally {
				connection.setAutoCommit(true);
			}

		} catch (SQLException e) {
			throw new CustomException("Customer Creation failed!", e);
		}
	}

	@Override
	public void removeCustomer(int customerId) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection
				.prepareStatement("UPDATE user SET status = " + ActiveStatus.INACTIVE + " where customerId = ?");) {
			statement.setInt(1, customerId);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Customer Deletion failed!", e);
		}
	}

	@Override
	public Customer getCustomer(int id) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT u.*,aadhaarNo,panNo FROM user u JOIN customer c on u.id=c.id WHERE u.id = ?")) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSetToCustomer(resultSet);
				}
				return null;
			}
		} catch (SQLException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

	@Override
	public List<Customer> getCustomers(int branchId) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT u.*,panNo,aadhaarNo FROM user u JOIN customer c on u.id=c.id JOIN account a on a.customerId=u.id WHERE branchId = ?")) {
			statement.setInt(1, branchId);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Customer> customers = new ArrayList<Customer>();
				while (resultSet.next()) {
					customers.add(resultSetToCustomer(resultSet));
				}
				return customers;
			}
		} catch (SQLException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

	@Override
	public int getCustomerId(String panNo) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM customer where panNo = ?");) {
			statement.setString(1, panNo);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id");
			}
			return -1;
		} catch (SQLException e) {
			throw new CustomException("Validation failed!", e);
		}
	}

	public static Customer resultSetToCustomer(ResultSet customerRecord) throws SQLException {
		Customer customer = new Customer();
		customer.setUserId(customerRecord.getInt("id"));
		customer.setDob(customerRecord.getDate("dob").toLocalDate());
		customer.setLocation(customerRecord.getString("location"));
		customer.setCity(customerRecord.getString("city"));
		customer.setState(customerRecord.getString("state"));
		customer.setStatus(ActiveStatus.valueOf(customerRecord.getString("status")));
		customer.setName(customerRecord.getString("name"));
		customer.setEmail(customerRecord.getString("email"));
		customer.setNumber(customerRecord.getLong("number"));
		customer.setType(UserType.valueOf(customerRecord.getString("type")));
		customer.setPanNo(customerRecord.getString("panNo"));
		customer.setAadhaarNo(customerRecord.getLong("aadhaarNo"));
		customer.setGender(Gender.valueOf(customerRecord.getString("gender")));
		return customer;
	}

}
