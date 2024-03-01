package persistentLayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import CustomExceptions.CustomException;
import model.Customer;
import utility.ActiveStatus;

public class CustomerDao implements CustomerManager {

	@Override
	public void addCustomer(Customer customer) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement userStatement = connection.prepareStatement(
				"INSERT INTO user(name, dob, number,password,status,type,location,city,state) values(?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement customerStatement = connection
						.prepareStatement("INSERT INTO customer(id,aadhaarNo,panNo) VALUES(?,?,?)");) {

			userStatement.setString(1, customer.getName());
			userStatement.setDate(2, Date.valueOf(customer.getDob()));
			userStatement.setLong(3, customer.getNumber());
			userStatement.setString(4, customer.getPassword());
			userStatement.setString(5, customer.getStatus());
			userStatement.setString(6, customer.getType().name());
			userStatement.setString(7, customer.getLocation());
			userStatement.setString(8, customer.getCity());
			userStatement.setString(9, customer.getState());

			customerStatement.setLong(2, customer.getAadhaarNo());
			customerStatement.setString(3, customer.getPanNo());

			try {
				connection.setAutoCommit(false);
				int userAffectedRows = userStatement.executeUpdate();
				if (userAffectedRows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys();) {
						if (resultSet.next()) {
							customerStatement.setInt(1, resultSet.getInt(0));
						}
					}
				}
				customerStatement.execute();
				connection.commit();
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
			ResultSet resultSet = statement.executeQuery();
		} catch (SQLException e) {
			throw new CustomException("Customer Deletion failed!", e);
		}
	}

	@Override
	public Customer getCustomer(int id) {
		// TODO Auto-generated method stub
		return null;
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

}
