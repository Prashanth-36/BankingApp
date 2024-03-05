package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import model.Employee;

public interface EmployeeManager {

	void addEmployee(Employee employee) throws CustomException;

	Employee getEmployee(int id);

	void removeEmployee(int id) throws CustomException;

	List<Employee> getEmployees(int branchId, int offset, int limit) throws CustomException;

}
