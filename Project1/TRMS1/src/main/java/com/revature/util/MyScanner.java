package com.revature.util;

import java.util.Scanner;

public class MyScanner {

	private static MyScanner instance;

	private Scanner scan;

	private MyScanner() {
		scan = new Scanner(System.in);
	}

	public static synchronized MyScanner getScanner() {
		if (instance == null) {
			instance = new MyScanner();
		}
		return instance;
	}

	public Scanner getScan() {
		return scan;
	}
}
