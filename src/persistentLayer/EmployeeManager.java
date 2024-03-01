package persistentLayer;

import java.util.List;

import CustomExceptions.CustomException;
import model.Employee;

public interface EmployeeManager {

	void addEmployee(Employee employee) throws CustomException;

	Employee getEmployee(int id);

	List<Employee> getEmployees(int branchId);

	List<Employee> getAllEmployees();

	void removeEmployee(int id);

}
