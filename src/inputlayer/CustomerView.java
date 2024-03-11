package inputlayer;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import customexceptions.CustomException;
import customexceptions.InsufficientFundException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.CustomerHandler;
import model.Account;
import model.Customer;
import model.Transaction;

public class CustomerView {
	static Logger logger = Logger.getLogger(Runner.class.getName());

	private Customer profile;

	public CustomerView(Customer currentUser) {
		this.setProfile(currentUser);
	}

	Scanner sc = InputScanner.getScanner();

	CustomerHandler customerHandler = new CustomerHandler();

	public void handler() {
		logger.log(Level.INFO, "Welcome " + profile.getName());
		boolean run = true;
		while (run) {
			try {
				logger.info("\n1.Get current balance");
				logger.info("2.Withdraw");
				logger.info("3.Deposit");
				logger.info("4.Money Transfer");
				logger.info("5.Get Transaction Statement");
				logger.info("6.Get all accounts");
				logger.info("7.set primary account");
				logger.info("8.Get Total balance");
				logger.info("9.Change MPIN");
				logger.info("Enter your choise:");
				int choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {
				case 1:
					getCurrentBalance();
					break;

				case 2:
					withdrawl();
					break;

				case 3:
					deposit();
					break;

				case 4:
					moneyTransfer();
					break;

				case 5:
					getStatement();
					break;

				case 6:
					getAllAccounts();
					break;

				case 7:
					setPrimaryAccount();
					break;

				case 8:
					getTotalBalance();
					break;

				case 9:
					changeMpin();
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

	private void changeMpin() {
		logger.info("Enter account no:");
		int accountNo = sc.nextInt();
		sc.nextLine();
		logger.info("Enter your current MPIN ");
		String currentPin = sc.nextLine();
		logger.info("Enter your new MPIN");
		String newPin = sc.nextLine();
		try {
			customerHandler.changeMpin(profile.getUserId(), accountNo, currentPin, newPin);
		} catch (InvalidValueException | CustomException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getTotalBalance() {
		try {
			double balance = customerHandler.getTotalBalance(profile.getUserId());
			logger.info("Total Available balance: " + balance);
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void setPrimaryAccount() {
		logger.info("Enter account no to set as primary:");
		int accountNo = sc.nextInt();
		sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			customerHandler.setPrimaryAccount(profile.getUserId(), mpin, accountNo);
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getAllAccounts() {
		try {
			Map<Integer, Account> accounts = customerHandler.getAccounts(profile.getUserId());
			accounts.forEach((k, v) -> logger.info(v.toString()));
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getStatement() {
		logger.info("Enter your account no:");
		int accountNo = sc.nextInt();
		logger.info("Enter months min 1, max 6 months: ");
		int months = sc.nextInt();
		logger.info("Enter limit to display at a time (default 10):");
		int limit;
		String customLimit = sc.nextLine();
		limit = customLimit.isEmpty() ? 10 : Integer.parseInt(customLimit);
		sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			int totalPages = customerHandler.getTransactionPageCount(profile.getUserId(), mpin, accountNo, months,
					limit);
			if (totalPages == 0) {
				logger.info("No Transaction to Display.");
			} else {
				int pageNo = 1;
				while (true) {
					List<Transaction> transactions = customerHandler.getTransactions(profile.getUserId(), mpin,
							accountNo, months, pageNo, limit);
					transactions.forEach(t -> logger.info(t.toString()));
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
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void moneyTransfer() {
		logger.info("Enter your account no:");
		int accountNo = sc.nextInt();
		logger.info("Enter beneficiary account no:");
		int beneficiary = sc.nextInt();
		logger.info("Enter amount:");
		double amount = sc.nextDouble();
		sc.nextLine();
		logger.info("Description (State reason if any):");
		String description = sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			customerHandler.moneyTransfer(profile.getUserId(), mpin, accountNo, beneficiary, amount, description);
		} catch (CustomException | InvalidValueException | InsufficientFundException | InvalidOperationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void deposit() {
		logger.info("Enter account no:");
		int accountNo = sc.nextInt();
		logger.info("Enter amount to deposit:");
		double amount = sc.nextDouble();
		sc.nextLine();
		logger.info("Description (State reason if any):");
		String description = sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			customerHandler.deposit(profile.getUserId(), mpin, accountNo, amount, description);
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void withdrawl() {
		logger.info("Enter account no:");
		int accountNo = sc.nextInt();
		logger.info("Enter amount to withdraw:");
		double amount = sc.nextDouble();
		sc.nextLine();
		logger.info("Description (State reason if any):");
		String description = sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			customerHandler.withdrawl(profile.getUserId(), mpin, accountNo, amount, description);
		} catch (CustomException | InvalidValueException | InsufficientFundException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void getCurrentBalance() {
		logger.info("Enter account number:");
		int accountNo = sc.nextInt();
		sc.nextLine();
		logger.info("Enter your mpin:");
		String mpin = sc.nextLine();
		try {
			double balance = customerHandler.getCurrentBalance(profile.getUserId(), mpin, accountNo);
			logger.info("Current balance: " + balance);
		} catch (CustomException | InvalidValueException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public Customer getProfile() {
		return profile;
	}

	public void setProfile(Customer profile) {
		this.profile = profile;
	}

}
