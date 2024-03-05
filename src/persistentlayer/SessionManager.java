package persistentlayer;

import customexceptions.CustomException;
import model.User;

public interface SessionManager {

	User authenticate(int userId, String password) throws CustomException;

}
