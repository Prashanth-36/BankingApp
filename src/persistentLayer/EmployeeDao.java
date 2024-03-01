package persistentLayer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import CustomExceptions.CustomException;
import model.Employee;
import utility.Privilege;
import utility.UserType;

public class EmployeeDao implements EmployeeManager {

	@Override
	public void addEmployee(Employee employee) throws CustomException {
		Connection connection = DBConnection.getConnection();
		try (PreparedStatement userStatement=connection.prepareStatement("");
				PreparedStatement statement = connection.prepareStatement("INSERT INTO EMPLOYEE ");) {
			statement.setString(1, employee.getName());
			statement.setDate(2, Date.valueOf(employee.getDob()));
			statement.setLong(3, employee.getNumber());
			statement.setString(4, employee.getStatus());
			statement.setString(5, employee.getPassword());
			statement.setString(6, employee.getType().name());
			statement.setString(7, employee.getLocation());
			statement.setString(8, employee.getCity());
			statement.setString(9, employee.getState());
			statement.setInt(10, employee.getBranchId());
//			String name, LocalDate dob, long number, String status, String password,
//			UserType type, String location, String city, String state, int branchId, Privilege privilege) {

		} catch (SQLException e) {
			throw new CustomException("Employee creation failed!",e);
		}
	}

	@Override
	public Employee getEmployee(int id) {
		return null;
	}

	@Override
	public List<Employee> getEmployees(int branchId) {
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return null;
	}

	@Override
	public void removeEmployee(int id) {

	}

}
