package inputLayer;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import CustomExceptions.CustomException;
import persistentLayer.DBConnection;
import utility.Utils;

public class Runner {

	static Logger logger = Logger.getLogger(Runner.class.getName());

	public static void main(String[] args) {
		Utils.customLogger(logger, "Runner.log");
		try (Scanner sc = new Scanner(System.in)) {
			boolean run = true;
			while (run) {
				logger.log(Level.INFO, "");
				logger.log(Level.INFO, "1.Login");
				logger.log(Level.INFO, "2.Exit");
				logger.log(Level.INFO, "Enter your choice: ");
				int ch = sc.nextInt();
				switch (ch) {
				case 1: {
					login();
					break;
				}
				case 2: {
					run = false;
					try {
						DBConnection.close();
					} catch (CustomException e) {
						logger.log(Level.SEVERE, "DB Connection close failed", e);
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Excepion occured!", e);
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
		try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Enter your user id: ");
			int id = sc.nextInt();
			logger.log(Level.INFO, "Enter password:");
			Console console = System.console();
			String password;
			if (console != null) {
				password = new String(console.readPassword());
			} else {
				password = sc.next();
			}
//			UserHandler handler = new UserHandler();
//			handler.validateCredentials(id,password);
		}
	}
}
