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
import model.Employee;
import persistentlayer.EmployeeManager;
import utility.ActiveStatus;
import utility.Gender;
import utility.Privilege;
import utility.UserType;

public class EmployeeDao implements EmployeeManager {

	@Override
	public void addEmployee(Employee employee) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement userStatement = connection.prepareStatement(
				"INSERT INTO user(name, dob, number,password,status,type,location,city,state,email,gender) values(?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO employee(id,branchId,privilege) VALUES (?,?,?)");) {
			userStatement.setString(1, employee.getName());
			userStatement.setDate(2, Date.valueOf(employee.getDob()));
			userStatement.setLong(3, employee.getNumber());
			userStatement.setString(4, employee.getPassword());
			userStatement.setString(5, ActiveStatus.ACTIVE.name());
			userStatement.setString(6, employee.getType().name());
			userStatement.setString(7, employee.getLocation());
			userStatement.setString(8, employee.getCity().toLowerCase());
			userStatement.setString(9, employee.getState().toLowerCase());
			userStatement.setString(10, employee.getEmail());
			userStatement.setString(11, employee.getGender().name());

			statement.setInt(2, employee.getBranchId());
			statement.setString(3, employee.getPrivilege().toString());

			try {
				connection.setAutoCommit(false);
				int rows = userStatement.executeUpdate();
				if (rows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
						if (resultSet.next()) {
							statement.setInt(1, resultSet.getInt(1));
							statement.executeUpdate();
							connection.commit();
						}
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new CustomException("Employee Creation failed!", e);
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new CustomException("Employee creation failed!", e);
		}
	}

	@Override
	public Employee getEmployee(int id) {
		return null;
	}

	@Override
	public List<Employee> getEmployees(int branchId, int offset, int limit) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(
				"SELECT u.*,e.branchId,e.privilege from user u join employee e on e.id=u.id where branchId = ? limit ?,?");) {
			statement.setInt(1, branchId);
			statement.setInt(2, offset);
			statement.setInt(3, limit);
			try (ResultSet resultSet = statement.executeQuery();) {
				List<Employee> employees = new ArrayList<Employee>();
				while (resultSet.next()) {
					employees.add(resultSetToEmployee(resultSet));
				}
				return employees;
			}
		} catch (SQLException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public void removeEmployee(int id) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement statement = connection.prepareStatement("UPDATE user SET status = ? WHERE id = ?")) {
			statement.setString(1, ActiveStatus.INACTIVE.name());
			statement.setInt(2, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new CustomException("Employee deletion failed!", e);
		}
	}

	public static Employee resultSetToEmployee(ResultSet employeeRecord) throws SQLException {
		Employee employee = new Employee();
		employee.setUserId(employeeRecord.getInt("id"));
		employee.setDob(employeeRecord.getDate("dob").toLocalDate());
		employee.setLocation(employeeRecord.getString("location"));
		employee.setCity(employeeRecord.getString("city"));
		employee.setState(employeeRecord.getString("state"));
		employee.setStatus(ActiveStatus.valueOf(employeeRecord.getString("status")));
		employee.setName(employeeRecord.getString("name"));
		employee.setNumber(employeeRecord.getLong("number"));
		employee.setEmail(employeeRecord.getString("email"));
		employee.setType(UserType.valueOf(employeeRecord.getString("type")));
		employee.setBranchId(employeeRecord.getInt("branchId"));
		employee.setPrivilege(Privilege.valueOf(employeeRecord.getString("privilege")));
		employee.setGender(Gender.valueOf(employeeRecord.getString("gender")));
		return employee;
	}

}
