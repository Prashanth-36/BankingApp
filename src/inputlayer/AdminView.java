package inputlayer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import logicallayer.AdminHandler;
import model.Branch;
import model.Employee;
import utility.ActiveStatus;
import utility.Gender;
import utility.Privilege;
import utility.UserType;
import utility.Validate;

public class AdminView extends EmployeeView {

	static Logger logger = Logger.getLogger(Runner.class.getName());

	AdminHandler adminHandler = new AdminHandler();

	public AdminView(Employee employee) {
		super(employee);
	}

	public void handler(Employee employee) {
		Scanner sc = InputScanner.getScanner();

		logger.log(Level.INFO, "Welcome " + employee.getName());
		boolean run = true;
		while (run) {
			logger.log(Level.INFO, "1.Manage Employee");
			logger.log(Level.INFO, "2.Manage Customer");
			logger.log(Level.INFO, "3.Manage Branch");
			logger.log(Level.INFO, "Enter your choise:");
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice) {
			case 1:
				manageEmployee();
				break;
			case 2:
				manageCustomer();
				break;
			case 3:
				manageBranch();
				break;

			default:
				run = false;
				break;
			}
		}

	}

	private void manageBranch() {
		Scanner sc = InputScanner.getScanner();
		boolean run = true;
		while (run) {
			logger.info("1.Add Branch");
			logger.info("2.get Branches");
			logger.info("3.remove Branch");
			logger.info("Enter your choice:");
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice) {
			case 1:
				addBranch();
				break;
			case 2:
				getBranch();
				break;
			case 3:
				removeBranch();
				break;
			default:
				run = false;
				break;
			}
		}
	}

	private void removeBranch() {
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter branch id:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			adminHandler.removeBranch(id);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Branch Deletion failed!", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private void getBranch() {
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter branch id:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			Branch branch = adminHandler.getBranch(id);
			logger.info(branch.toString());
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Branch fetch failed!", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private void addBranch() {
		Scanner sc = InputScanner.getScanner();
		Branch branch = new Branch();
		logger.info("Enter branch location:");
		String location = sc.nextLine();
		branch.setLocation(location);
		logger.info("Enter branch city:");
		String city = sc.nextLine();
		branch.setCity(city);
		logger.info("Enter branch State:");
		String state = sc.nextLine();
		branch.setState(state);
		logger.info("Enter branch ifsc code:");
		String ifsc = sc.nextLine();
		branch.setIfsc(ifsc);
		try {
			adminHandler.addBranch(branch);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Branch Creation Failed", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private void manageEmployee() {
		Scanner sc = InputScanner.getScanner();
		boolean manageEmployee = true;
		while (manageEmployee) {
			logger.log(Level.INFO, "\n1.Add Employee");
			logger.log(Level.INFO, "2.Get Branch Employees");
			logger.log(Level.INFO, "3.Remove Employee");
			int choise = sc.nextInt();
			sc.nextLine();
			switch (choise) {
			case 1:
				addEmployee();
				break;

			case 2:
				getEmployees();
				break;

			case 3:
				removeEmployee();
				break;

			default:
				manageEmployee = false;
				break;
			}
		}
	}

	private void removeEmployee() {
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter employee id to remove:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			adminHandler.removeEmployee(id);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Employee deletion failed!", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private void getEmployees() {
		Scanner sc = InputScanner.getScanner();
		try {
			logger.info("Enter branch id:");
			int branchId = sc.nextInt();
			sc.nextLine();
			boolean display = true;
			int page = 0;
			int limit = 10;
			while (display) {
				List<Employee> employees = adminHandler.getEmployees(branchId, page * limit, limit);
				employees.forEach(e -> logger.info(e.toString()));
				logger.log(Level.INFO, "Enter n for next page.");
				char next = sc.next().charAt(0);
				if (next == 'n' || next == 'N') {
					page++;
				} else {
					display = false;
				}
			}
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Employee fetch failed!", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private void addEmployee() {
		try {
			Employee employeeDetails = getEmployeeDetails();
			adminHandler.addEmployee(employeeDetails);
			logger.log(Level.INFO, "Employee Created!");
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Employee creation failed!", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception!", e);
		}
	}

	private Employee getEmployeeDetails() {
		Employee employee = new Employee();
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter name: ");
		String name = sc.nextLine();
		employee.setName(name);
		logger.info("Enter dob yyyy-mm-dd: ");
		LocalDate dob = LocalDate.parse(sc.nextLine());
		employee.setDob(dob);
		logger.info("Enter gender (MALE/FEMALE):");
		employee.setGender(Gender.valueOf(sc.nextLine().toUpperCase()));
		logger.info("Enter email:");
		String email = sc.nextLine();
		while (!Validate.email(email)) {
			logger.warning("Enter valid email:");
			email = sc.nextLine();
		}
		employee.setEmail(email);
		logger.info("Enter password:");
		String password = sc.nextLine();
		employee.setPassword(password);
		logger.info("Enter mobile number:");
		String number = sc.nextLine();
		while (!Validate.mobile(number)) {
			logger.warning("Enter valid mobile number:");
			number = sc.nextLine();
		}
		employee.setNumber(Long.valueOf(number));
		logger.info("Enter location:");
		String location = sc.nextLine();
		employee.setLocation(location);
		logger.info("Enter city:");
		String city = sc.nextLine();
		employee.setCity(city);
		logger.info("Enter state:");
		String state = sc.nextLine();
		employee.setState(state);
		logger.info("\nEnter privilege from below:");
		Arrays.stream(Privilege.values()).forEach(e -> logger.info(e.name()));
		Privilege privilege = Privilege.valueOf(sc.next().toUpperCase());
		employee.setPrivilege(privilege);
		logger.info("Enter branch id");
		int branchId = sc.nextInt();
		sc.nextLine();
		employee.setBranchId(branchId);
		employee.setType(UserType.EMPLOYEE);
		employee.setStatus(ActiveStatus.ACTIVE);
		return employee;
	}

}
