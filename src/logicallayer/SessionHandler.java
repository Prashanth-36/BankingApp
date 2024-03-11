package logicallayer;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.User;
import persistentdao.SessionDao;
import persistentlayer.SessionManager;
import utility.Utils;

public class SessionHandler {

	public User authenticate(int userId, String password) throws CustomException, InvalidValueException {
		Utils.checkNull(password);
		SessionManager sessionManager = new SessionDao();
		return sessionManager.authenticate(userId, password);
	}

}
