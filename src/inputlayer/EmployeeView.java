package inputlayer;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.EmployeeHandler;
import model.Account;
import model.Customer;
import model.Employee;
import utility.ActiveStatus;
import utility.Gender;
import utility.Privilege;
import utility.UserType;
import utility.Utils;
import utility.Validate;

public class EmployeeView {
	static Logger logger = Logger.getLogger(Runner.class.getName());

	private Employee profile;

	public EmployeeView(Employee profile) {
		this.profile = profile;
	}

	Scanner sc = InputScanner.getScanner();
	EmployeeHandler employeeHandler = new EmployeeHandler();

	public void handler() {
		logger.log(Level.INFO, "Welcome " + profile.getName());
		boolean run = true;
		while (run) {
			try {
				logger.info("\n1.Manage Customer");
				logger.info("2.Manage Accounts");
				logger.info("Enter your choice:");
				int choise = sc.nextInt();
				sc.nextLine();
				switch (choise) {
				case 1:
					manageCustomer();
					break;
				case 2:
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

	protected void manageAccounts() {
		boolean run = true;
		while (run) {
			logger.info("\n1.Add Account");
			logger.info("2.Get Account details");
			logger.info("3.Delete Account");
			logger.info("Enter your choice:");
			int ch = sc.nextInt();
			sc.nextLine();
			switch (ch) {
			case 1:
				addAccount();
				break;

			case 2:
				getAccount();
				break;

			case 3:
				deleteAccount();
				break;

			default:
				run = false;
				break;
			}
		}
	}

	private void deleteAccount() {
		logger.info("Enter account no:");
		int accountNo = sc.nextInt();
		sc.nextLine();
		try {
			employeeHandler.deleteAccount(accountNo);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getAccount() {
		logger.info("Enter account number:");
		int accountNo = sc.nextInt();
		sc.nextLine();
		Account account;
		try {
			account = employeeHandler.getAccount(accountNo);
			logger.info(account.toString());
		} catch (InvalidValueException | CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void addAccount() {
		Account account = getAccountDetails();
		try {
			employeeHandler.createAccount(account);
		} catch (CustomException | InvalidValueException | InvalidOperationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void manageCustomer() {
		boolean run = true;
		while (run) {
			logger.info("\n1.Add Customer");
			logger.info("2.Get Customer");
			logger.info("3.Get Branch Customers");
			logger.info("4.Remove Customer");
			logger.info("5.Get Customer ID");
			logger.info("Enter your choice:");
			int ch = sc.nextInt();
			sc.nextLine();
			switch (ch) {
			case 1:
				addCustomer();
				break;

			case 2:
				getCustomer();
				break;

			case 3:
				getCustomers();
				break;

			case 4:
				removeCustomer();
				break;

			case 5:
				getCustomerId();
				break;

			default:
				run = false;
				break;
			}
		}
	}

	private void getCustomerId() {
		logger.info("Enter pan number:");
		String pan = sc.nextLine();
		try {
			int id = employeeHandler.getCustomerId(pan);
			logger.info(id + "");
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getCustomers() {
		int branchId;
		if (profile.getPrivilege() == Privilege.ADMIN) {
			logger.info("Enter branch id:");
			branchId = sc.nextInt();
			sc.nextLine();
		} else {
			branchId = profile.getBranchId();
		}
		try {
			logger.info("Choose is (0)-Inactive / (1)-Active Customer:");
			int isActive = sc.nextInt();
			ActiveStatus statusArray[] = ActiveStatus.values();
			Utils.checkRange(0, isActive, statusArray.length - 1,
					"Invalid input required 0 for inactive and 1 for active");
			ActiveStatus status = statusArray[isActive];
			logger.info("Enter limit to display at a time (default 10):");
			int limit;
			String customLimit = sc.nextLine();
			limit = customLimit.isEmpty() ? 10 : Integer.parseInt(customLimit);
			sc.nextLine();
			int totalPages = employeeHandler.getCustomerPageCount(branchId, limit, status);
			if (totalPages == 0) {
				logger.info("No Customers to Display.");
			} else {
				int pageNo = 1;
				while (true) {
					List<Customer> customers = employeeHandler.getCustomers(branchId, pageNo, limit, status);
					customers.forEach(c -> logger.info(c.toString()));
					if (totalPages == 1) {
						break;
					}
					logger.info("Press Enter page number 1-" + pageNo + ": ");
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
		} catch (InvalidValueException | CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void removeCustomer() {
		logger.info("Enter customer id:");
		int customerId = sc.nextInt();
		sc.nextLine();
		try {
			employeeHandler.removeCustomer(customerId);
		} catch (CustomException e) {
			logger.log(Level.INFO, "Customer Deletion failed!", e);
		}
	}

	public void getCustomer() {
		logger.info("Enter customer id:");
		int customerId = sc.nextInt();
		sc.nextLine();
		try {
			Customer customer = employeeHandler.getCustomer(customerId);
			logger.info(customer.toString());
		} catch (InvalidValueException | CustomException e) {
			logger.log(Level.INFO, "Customer does not exist!", e);
		}
	}

	public void addCustomer() {
		Customer customer = getCustomerDetails();
		try {
			employeeHandler.addCustomer(customer);
		} catch (InvalidOperationException | CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Customer getCustomerDetails() {
		Customer customer = new Customer();
		logger.info("Enter name: ");
		String name = sc.nextLine();
		customer.setName(name);
		logger.info("Enter dob yyyy-mm-dd: ");
		LocalDate date = LocalDate.parse(sc.nextLine());
		long dob = Utils.getMillis(date);
		customer.setDob(dob);
		logger.info("Enter gender (0)-MALE / (1)-FEMALE:");
		int genderInt = sc.nextInt();
		try {
			Utils.checkRange(0, genderInt, 1, "Enter 0 for male and 1 for female");
		} catch (InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		customer.setGender(Gender.values()[genderInt]);
		logger.info("Enter email:");
		String email = sc.nextLine();
		while (!Validate.email(email)) {
			logger.warning("Enter valid email:");
			email = sc.nextLine();
		}
		customer.setEmail(email);
		logger.info("Enter password:");
		String password = sc.nextLine();
		customer.setPassword(password);
		logger.info("Enter mobile number:");
		String number = sc.nextLine();
		while (!Validate.mobile(number)) {
			logger.warning("Enter valid mobile number:");
			number = sc.nextLine();
		}
		customer.setNumber(Long.valueOf(number));
		logger.info("Enter location:");
		String location = sc.nextLine();
		customer.setLocation(location);
		logger.info("Enter city:");
		String city = sc.nextLine();
		customer.setCity(city);
		logger.info("Enter state:");
		String state = sc.nextLine();
		customer.setState(state);
		logger.info("Enter PAN Number");
		String pan = sc.nextLine().toUpperCase();
		while (!Validate.pan(pan)) {
			logger.warning("Enter valid pan number");
			pan = sc.nextLine().toUpperCase();
		}
		customer.setPanNo(pan);
		logger.info("Enter Aadhaar Number");
		long aadhaar = sc.nextLong();
		sc.nextLine();
		customer.setAadhaarNo(aadhaar);
		customer.setType(UserType.USER);
		customer.setStatus(ActiveStatus.ACTIVE);
		return customer;
	}

	private Account getAccountDetails() {
		Account account = new Account();
		logger.info("Enter customer id:");
		int id = sc.nextInt();
		account.setCustomerId(id);
		if (profile.getPrivilege() == Privilege.ADMIN) {
			logger.info("Enter branch id:");
			int branchId = sc.nextInt();
			account.setBranchId(branchId);
		} else {
			account.setBranchId(profile.getBranchId());
		}
		return account;
	}

	public Employee getProfile() {
		return profile;
	}

	public void setProfile(Employee profile) {
		this.profile = profile;
	}
}
