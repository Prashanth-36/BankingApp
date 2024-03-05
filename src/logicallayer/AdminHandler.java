package logicallayer;

import java.util.List;

import customexceptions.CustomException;
import model.Branch;
import model.Employee;
import persistentdao.BranchDao;
import persistentdao.EmployeeDao;
import persistentlayer.BranchManager;
import persistentlayer.EmployeeManager;

public class AdminHandler extends EmployeeHandler {

	EmployeeManager employeeManager = new EmployeeDao();

	BranchManager branchManager = new BranchDao();

	public void addEmployee(Employee employee) throws CustomException {
		employeeManager.addEmployee(employee);
	}

	public List<Employee> getEmployees(int branchId, int limit, int offset) throws CustomException {
		return employeeManager.getEmployees(branchId, limit, offset);
	}

	public void removeEmployee(int id) throws CustomException {
		employeeManager.removeEmployee(id);
	}

	public void addBranch(Branch branch) throws CustomException {
		branchManager.addBranch(branch);
	}

	public Branch getBranch(int branchId) throws CustomException {
		return branchManager.getBranch(branchId);
	}

	public void removeBranch(int branchId) throws CustomException {
		branchManager.removeBranch(branchId);
	}

}
