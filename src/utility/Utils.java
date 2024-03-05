package utility;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import customexceptions.InvalidValueException;

public class Utils {

	public static void checkNull(Object obj) throws InvalidValueException {
		if (obj == null) {
			throw new InvalidValueException("Input cannot be null.");
		}
	}

	public static void checkRange(int min, int value, int max, String message) throws InvalidValueException {
		if (value < min || value > max) {
			throw new InvalidValueException(message);
		}
	}

	public static void checkRange(int min, int value, int max) throws InvalidValueException {
		checkRange(min, value, max, "Invalid input given value is not within acceptable range");
	}

	public static Logger customLogger(Logger logger, String fileName) {
		try {
			FileHandler handler = new FileHandler(fileName);
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
			ConsoleHandler handler2 = new ConsoleHandler();
			handler2.setFormatter(new SimpleFormatter() {
//				@Override
//				public String format(LogRecord record) {
//					return record.getMessage() + System.lineSeparator();
//				}
				@Override
				public String format(LogRecord record) {
					StringBuilder message = new StringBuilder();
					String colorCode;
					if (record.getLevel() == Level.INFO) {
						colorCode = "\u001B[32m"; // Green
					} else if (record.getLevel() == Level.WARNING) {
						colorCode = "\u001B[33m"; // Yellow
					} else {
						colorCode = "\u001B[31m"; // Red
					}

					message.append(colorCode).append(record.getMessage()).append("\u001B[0m")
							.append(System.lineSeparator()); // Reset color

					Throwable thrown = record.getThrown();
					if (thrown != null) {
						message.append(System.lineSeparator()).append("Exception: ");
						message.append(thrown.toString());
						for (StackTraceElement element : thrown.getStackTrace()) {
							message.append(System.lineSeparator()).append("\t").append(element.toString());
						}
						thrown = thrown.getCause();
					}
					while (thrown != null) {
						message.append(System.lineSeparator()).append("Caused by: ");
						message.append(thrown.toString());
						for (StackTraceElement element : thrown.getStackTrace()) {
							message.append(System.lineSeparator()).append("\t").append(element.toString());
						}
						thrown = thrown.getCause();
					}

					return message.toString();
				}

			});
			logger.addHandler(handler2);
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, "File Handler exception", e);
		}
		logger.setUseParentHandlers(false);
		return logger;
	}

}
