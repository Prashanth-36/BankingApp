package logicalLayer;

import java.util.ArrayList;
import java.util.List;

import CustomExceptions.CustomException;
import model.Transaction;
import persistentLayer.TransactionDao;
import persistentLayer.TransactionManager;
import utility.TransactionType;

public class CustomerHandler {

	static TransactionManager transactionManager = new TransactionDao();

	public double getCurrentBalance(int accountNo) {
		return 0;
	}

	public void deposit(int accountNo, double amount, String description) throws CustomException {
		Transaction transaction = new Transaction();
		transaction.setPrimaryAccount(accountNo);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.CREDIT);
		transaction.setDescription(description);
		transactionManager.initTransaction(transaction);
	}

	public void withdrawl(int accountNo, double amount, String description) throws CustomException {
		Transaction transaction = new Transaction();
		transaction.setPrimaryAccount(accountNo);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.DEBIT);
		transaction.setDescription(description);
		transactionManager.initTransaction(transaction);
	}

	public void moneyTransfer(int sourceAccountNo, int targetAccountNo, double amount, String description)
			throws CustomException {

		Transaction primaryTransaction = new Transaction();
		primaryTransaction.setPrimaryAccount(sourceAccountNo);
		primaryTransaction.setTransactionalAccount(targetAccountNo);
		primaryTransaction.setAmount(amount);
		primaryTransaction.setType(TransactionType.DEBIT);
		primaryTransaction.setDescription(description);
		transactionManager.initTransaction(primaryTransaction);

		if (transactionManager.isSameBankTransaction(sourceAccountNo, targetAccountNo)) {
			Transaction secondaryTransaction = new Transaction();
			secondaryTransaction.setPrimaryAccount(targetAccountNo);
			secondaryTransaction.setTransactionalAccount(sourceAccountNo);
			secondaryTransaction.setAmount(amount);
			secondaryTransaction.setType(TransactionType.CREDIT);
			secondaryTransaction.setDescription(description);
			transactionManager.initTransaction(secondaryTransaction);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(primaryTransaction);
			transactions.add(secondaryTransaction);

			transactionManager.initTransaction(transactions);
		} else {
			transactionManager.initTransaction(primaryTransaction);
		}

	}

	public List<Transaction> getTransactions() {
		return null;
	}

}
