package inputlayer;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.SessionHandler;
import model.Customer;
import model.Employee;
import model.User;
import utility.Utils;

public class Runner {

	static Logger logger = Logger.getLogger(Runner.class.getName());

	public static void main(String[] args) {
		Utils.customLogger(logger, "Runner.log");
		try (Scanner sc = InputScanner.getScanner()) {
			boolean run = true;
			while (run) {
				try {
					logger.log(Level.INFO, "\n1.Login");
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
						InputScanner.closeScanner();
						break;
					}
					default:
						logger.log(Level.INFO, "invalid input!");
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
			redirect(currentUser);
		} catch (CustomException | InvalidValueException | InvalidOperationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private static void redirect(User currentUser) throws CustomException {
		switch (currentUser.getType()) {
		case USER: {
			CustomerView view = new CustomerView((Customer) currentUser);
			view.handler();
			break;
		}
		case ADMIN: {
			AdminView view = new AdminView((Employee) currentUser);
			view.handler();
			break;
		}
		case EMPLOYEE: {
			EmployeeView view = new EmployeeView((Employee) currentUser);
			view.handler();
			break;
		}
		default:
			throw new CustomException("Invalid employee privilege!");
		}
	}

}