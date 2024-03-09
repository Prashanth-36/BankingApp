package persistentlayer;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.User;

public interface SessionManager {

	User authenticate(int userId, String password) throws CustomException, InvalidValueException;

}
