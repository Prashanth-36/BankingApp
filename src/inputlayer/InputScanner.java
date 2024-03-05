package inputlayer;

import java.util.Scanner;

public class InputScanner {
	private InputScanner() {
	}

	private static class ScannerHelper {
		private static Scanner scanner = new Scanner(System.in);
	}

	public static Scanner getScanner() {
		return ScannerHelper.scanner;
	}

	public static void closeScanner() {
		if (ScannerHelper.scanner != null) {
			ScannerHelper.scanner.close();
		}
	}
}
