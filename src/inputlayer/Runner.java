package inputlayer;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import logicallayer.SessionHandler;
import model.Customer;
import model.Employee;
import model.User;
import persistentdao.DBConnection;
import utility.UserType;
import utility.Utils;

public class Runner {

	static Logger logger = Logger.getLogger(Runner.class.getName());

	public static void main(String[] args) {
		Utils.customLogger(logger, "Runner.log");
		try (Scanner sc = InputScanner.getScanner()) {
			boolean run = true;
			while (run) {
				logger.log(Level.INFO, "");
				logger.log(Level.INFO, "1.Login");
				logger.log(Level.INFO, "2.Exit");
				logger.log(Level.INFO, "Enter your choice: ");
				int ch = sc.nextInt();
				sc.nextLine();
				switch (ch) {
				case 1: {
					login();
					break;
				}
				case 2: {
					run = false;
					logger.log(Level.INFO, "Thank You!");
					try {
						DBConnection.closeConnection();
						InputScanner.closeScanner();
					} catch (CustomException e) {
						logger.log(Level.SEVERE, "DB Connection close failed", e);
					}
					break;
				}
				default:
					logger.log(Level.INFO, "invalid input!");
					break;
				}
			}
		}
	}

	private static void login() {
		Scanner scanner = InputScanner.getScanner();
		logger.log(Level.INFO, "Enter your user id: ");
		int id = scanner.nextInt();
		scanner.nextLine();
		logger.log(Level.INFO, "Enter password:");
		Console console = System.console();
		String password;
		if (console != null) {
			password = new String(console.readPassword());
		} else {
			password = scanner.next();
		}
		SessionHandler handler = new SessionHandler();
		try {
			User currentUser = handler.authenticate(id, password);
			if (currentUser == null) {
				logger.log(Level.SEVERE, "Invalid user id and password!");
				return;
			}
			redirect(currentUser);
		} catch (CustomException e) {
			logger.log(Level.SEVERE, "Authentication failed!", e);
		}
	}

	private static void redirect(User currentUser) throws CustomException {
		if (currentUser.getType() == UserType.USER && currentUser instanceof Customer) {
			CustomerView view = new CustomerView((Customer) currentUser);
			view.handler();
		} else if (currentUser.getType() == UserType.EMPLOYEE && currentUser instanceof Employee) {
			Employee employee = (Employee) currentUser;
			switch (employee.getPrivilege()) {
			case EMPLOYEE: {
				EmployeeView view = new EmployeeView(employee);
				view.handler();
				break;
			}
			case ADMIN: {
				AdminView view = new AdminView(employee);
				view.handler();
				break;
			}
			default:
				throw new CustomException("Invalid employee privilege!");
			}
		} else {
			throw new CustomException("Couldn't authorize user");
		}
	}

	public static void name() {

	}
}