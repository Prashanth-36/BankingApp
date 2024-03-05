package inputlayer;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import logicallayer.EmployeeHandler;
import model.Customer;
import model.Employee;
import utility.ActiveStatus;
import utility.Gender;
import utility.Privilege;
import utility.UserType;
import utility.Validate;

public class EmployeeView {
	static Logger logger = Logger.getLogger(Runner.class.getName());

	private Employee profile;

	public EmployeeView(Employee profile) {
		this.profile = profile;
	}

	EmployeeHandler employeeHandler = new EmployeeHandler();

	public void handler() {
		Scanner sc = InputScanner.getScanner();

		logger.log(Level.INFO, "Welcome " + profile.getName());
		boolean run = true;
		while (run) {
			logger.info("1.Manage Customer");
			logger.info("Enter your choice:");
			int choise = sc.nextInt();
			sc.nextLine();
			switch (choise) {
			case 1:
				manageCustomer();
				break;
			default:
				run = false;
				break;
			}
		}
	}

	public void manageCustomer() {
		Scanner sc = InputScanner.getScanner();
		boolean run = true;
		while (run) {
			logger.info("1.Add Customer");
			logger.info("2.Get Customer");
			logger.info("3.Get Branch Customers");
			logger.info("4.Remove Customer");
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
				if (profile.getPrivilege() == Privilege.ADMIN) {
					logger.info("Enter branch id:");
					int branchId = sc.nextInt();
					sc.nextLine();
					getCustomers(branchId);
				} else {
					getCustomers(profile.getBranchId());
				}
				break;
			case 4:
				removeCustomer();
				break;

			default:
				run = false;
				break;
			}
		}
	}

	private void getCustomers(int branchId) {
		try {
			List<Customer> customers = employeeHandler.getCustomers(branchId);
			customers.forEach(c -> logger.info(c.toString()));
		} catch (CustomException e) {
			logger.log(Level.INFO, "Customer fetch failed!", e);
		} catch (Exception e) {
			logger.log(Level.INFO, "Exception!", e);
		}
	}

	private void removeCustomer() {
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter customer id:");
		int customerId = sc.nextInt();
		sc.nextLine();
		try {
			employeeHandler.removeCustomer(customerId);
		} catch (CustomException e) {
			logger.log(Level.INFO, "Customer Deletion failed!", e);
		} catch (Exception e) {
			logger.log(Level.INFO, "Exception!", e);
		}
	}

	public void getCustomer() {
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter customer id:");
		int customerId = sc.nextInt();
		sc.nextLine();
		try {
			Customer customer = employeeHandler.getCustomer(customerId);
			logger.info(customer.toString());
		} catch (CustomException e) {
			logger.log(Level.INFO, "Customer fetch failed!", e);
		} catch (Exception e) {
			logger.log(Level.INFO, "Exception!", e);
		}
	}

	public void addCustomer() {
		Customer customer = getCustomerDetails();
		try {
			employeeHandler.addCustomer(customer);
		} catch (InvalidOperationException | CustomException e) {
			logger.log(Level.SEVERE, "Customer creation failed!", e);
		} catch (Exception e) {
			logger.log(Level.INFO, "Exception!", e);
		}
	}

	private Customer getCustomerDetails() {
		Customer customer = new Customer();
		Scanner sc = InputScanner.getScanner();
		logger.info("Enter name: ");
		String name = sc.nextLine();
		customer.setName(name);
		logger.info("Enter dob yyyy-mm-dd: ");
		LocalDate dob = LocalDate.parse(sc.nextLine());
		customer.setDob(dob);
		logger.info("Enter gender (MALE/FEMALE):");
		customer.setGender(Gender.valueOf(sc.nextLine().toUpperCase()));
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
		String pan = sc.nextLine();
		while (!Validate.pan(pan)) {
			logger.warning("Enter valid pan number");
			pan = sc.nextLine();
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

	public Employee getProfile() {
		return profile;
	}

	public void setProfile(Employee profile) {
		this.profile = profile;
	}
}
