package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Employee;
import utility.ActiveStatus;

public interface EmployeeManager {

	void addEmployee(Employee employee) throws CustomException;

	Employee getEmployee(int id) throws CustomException, InvalidValueException;

	void removeEmployee(int id) throws CustomException;

	List<Employee> getEmployees(int branchId, int offset, int limit, ActiveStatus status) throws CustomException;

	int getEmployeesCount(int branchId, ActiveStatus status) throws CustomException;

}
