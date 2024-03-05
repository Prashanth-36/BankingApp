package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customexceptions.CustomException;
import model.User;
import persistentlayer.SessionManager;
import utility.UserType;

public class SessionDao implements SessionManager {

	@Override
	public User authenticate(int userId, String password) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT type FROM user WHERE id = ? AND password = ?");) {
			statement.setInt(1, userId);
			statement.setString(2, password);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					UserType type = UserType.valueOf(result.getString("type"));
					switch (type) {
					case USER:
						try (PreparedStatement userStatement = connection.prepareStatement(
								"SELECT u.*,aadhaarNo,panNo FROM user u JOIN customer c ON u.id=c.id WHERE u.id = ?");) {
							userStatement.setInt(1, userId);
							try (ResultSet customerRecord = userStatement.executeQuery()) {
								if (customerRecord.next()) {
									User customer = CustomerDao.resultSetToCustomer(customerRecord);
									return customer;
								}
							}
						}
						break;

					case EMPLOYEE:
						try (PreparedStatement employeeStatement = connection.prepareStatement(
								"SELECT u.*,branchId,privilege FROM user u JOIN employee e ON u.id=e.id WHERE u.id = ?");) {
							employeeStatement.setInt(1, userId);
							try (ResultSet employeeRecord = employeeStatement.executeQuery()) {
								if (employeeRecord.next()) {
									User employee = EmployeeDao.resultSetToEmployee(employeeRecord);
									return employee;
								}
							}
						}
						break;
					default:
						break;
					}
				}
				return null;
			}
		} catch (SQLException e) {
			throw new CustomException("Authentication failed!", e);
		}
	}

}
