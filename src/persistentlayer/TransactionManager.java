package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import model.Transaction;

public interface TransactionManager {

	void initTransaction(Transaction transaction) throws CustomException;

	void initTransaction(List<Transaction> transactions) throws CustomException;

	List<Transaction> getTransactions(int accountNo);

	boolean isSameBankTransaction(int sourceAccountNo, int targetAccountNo) throws CustomException;

}
