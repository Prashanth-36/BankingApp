package inputlayer;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.AdminHandler;
import model.Branch;
import model.Employee;
import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;
import utility.Validate;

public class AdminView extends EmployeeView {

	static Logger logger = Logger.getLogger(Runner.class.getName());

	AdminHandler adminHandler = new AdminHandler();

	Scanner sc = InputScanner.getScanner();

	public AdminView(Employee employee) {
		super(employee);
	}

	public void handler() {
		logger.log(Level.INFO, "Welcome " + getProfile().getName());
		boolean run = true;
		while (run) {
			try {
				logger.log(Level.INFO, "\n1.Manage Employee");
				logger.log(Level.INFO, "2.Manage Customer");
				logger.log(Level.INFO, "3.Manage Branch");
				logger.log(Level.INFO, "4.Manage Accounts");
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
				case 4:
					manageAccounts();
					break;

				default:
					run = false;
					break;
				}
			} catch (InputMismatchException e) {
				sc.nextLine();
				logger.log(Level.SEVERE, "InputMismatchException", e);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	private void manageBranch() {
		boolean run = true;
		while (run) {
			logger.info("\n1.Add Branch");
			logger.info("2.get Branch");
			logger.info("3.get Branches");
			logger.info("4.remove Branch");
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
				getBranches();
				break;
			case 4:
				removeBranch();
				break;
			default:
				run = false;
				break;
			}
		}
	}

	private void getBranches() {
		try {
			logger.info("Choose is (0)-Inactive / (1)-Active branch:");
			int isActive = sc.nextInt();
			ActiveStatus statusArray[] = ActiveStatus.values();
			Utils.checkRange(0, isActive, statusArray.length - 1,
					"Invalid input required 0 for inactive and 1 for active");
			ActiveStatus status = statusArray[isActive];
			Map<Integer, Branch> branches = adminHandler.getBranches(status);
			branches.forEach((k, v) -> logger.info(v.toString()));
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void removeBranch() {
		logger.info("Enter branch id:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			adminHandler.removeBranch(id);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getBranch() {
		logger.info("Enter branch id:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			Branch branch = adminHandler.getBranch(id);
			logger.info(branch.toString());
		} catch (InvalidValueException | CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void addBranch() {
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
		try {
			adminHandler.addBranch(branch);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void manageEmployee() {
		boolean manageEmployee = true;
		while (manageEmployee) {
			logger.log(Level.INFO, "\n1.Add Employee");
			logger.log(Level.INFO, "2.Get Branch Employees");
			logger.log(Level.INFO, "3.Get Employee");
			logger.log(Level.INFO, "4.Remove Employee");
			logger.log(Level.INFO, "5.Change Employee status");
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
				getEmployee();
				break;

			case 4:
				removeEmployee();
				break;

			case 5:
				setEmployeeStatus();
				break;

			default:
				manageEmployee = false;
				break;
			}
		}
	}

	private void setEmployeeStatus() {
		logger.info("Enter employee id:");
		int id = sc.nextInt();
		logger.info("Choose status to change (0)-INACTIVE / (1)-ACTIVE:");
		int statusInt = sc.nextInt();
		sc.nextLine();
		ActiveStatus status = ActiveStatus.values()[statusInt];
		try {
			adminHandler.setEmployeeStatus(id, status);
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getEmployee() {
		Scanner scanner = InputScanner.getScanner();
		logger.info("Enter employee id:");
		int id = scanner.nextInt();
		try {
			Employee employee = adminHandler.getEmployee(id);
			logger.info(employee.toString());
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void removeEmployee() {
		logger.info("Enter employee id to remove:");
		int id = sc.nextInt();
		sc.nextLine();
		try {
			adminHandler.removeEmployee(id);
		} catch (CustomException | InvalidOperationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getEmployees() {
		try {
			logger.info("Enter branch id:");
			int branchId = sc.nextInt();
			logger.info("Choose is (0)-Inactive / (1)-Active employee:");
			int isActive = sc.nextInt();
			sc.nextLine();
			ActiveStatus statusArray[] = ActiveStatus.values();
			Utils.checkRange(0, isActive, statusArray.length - 1,
					"Invalid input required 0 for inactive and 1 for active");
			ActiveStatus status = statusArray[isActive];
			logger.info("Enter limit to display at a time (default 10):");
			int limit;
			String customLimit = sc.nextLine();
			limit = customLimit.isEmpty() ? 10 : Integer.parseInt(customLimit);
			int totalPages = adminHandler.getEmployeesPageCount(branchId, limit, status);
			if (totalPages == 0) {
				logger.info("No employees to display!");
			} else {
				int pageNo = 1;
				while (true) {
					Map<Integer, Employee> employees = adminHandler.getEmployees(branchId, pageNo, limit, status);
					employees.forEach((k, v) -> logger.info(v.toString()));
					if (totalPages == 1) {
						break;
					}
					logger.log(Level.INFO, "Enter next page number 1-" + totalPages + ":");
					String input = sc.nextLine();
					try {
						pageNo = Integer.parseInt(input);
						if (pageNo < 1 || pageNo > totalPages) {
							break;
						}
					} catch (NumberFormatException e) {
						break;
					}
				}
			}
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void addEmployee() {
		try {
			Employee employeeDetails = getEmployeeDetails();
			adminHandler.addEmployee(employeeDetails);
			logger.log(Level.INFO, "Employee Created!");
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Employee getEmployeeDetails() {
		Employee employee = new Employee();
		logger.info("Enter name: ");
		String name = sc.nextLine();
		employee.setName(name);
		logger.info("Enter dob yyyy-mm-dd: ");
		LocalDate date = LocalDate.parse(sc.nextLine());
		long dob = Utils.getMillis(date);
		employee.setDob(dob);
		logger.info("Enter gender (0)-FEMALE / (1)-MALE:");
		int genderInt = sc.nextInt();
		try {
			Utils.checkRange(0, genderInt, 1, "Enter 0 for female and 1 for male");
		} catch (InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		employee.setGender(Gender.values()[genderInt]);
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
		logger.info("Enter Type (1)-Admin / (2)-Employee:");
		int typeInt = sc.nextInt();
		UserType userType[] = UserType.values();
		try {
			Utils.checkRange(1, typeInt, 2, "Enter 1 for admin and 2 for employee");
		} catch (InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		employee.setType(userType[typeInt]);
		logger.info("Enter branch id");
		int branchId = sc.nextInt();
		sc.nextLine();
		employee.setBranchId(branchId);
		employee.setStatus(ActiveStatus.ACTIVE);
		return employee;
	}

}