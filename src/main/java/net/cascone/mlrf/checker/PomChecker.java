package net.cascone.mlrf.checker;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class PomChecker {

	public static boolean check(Path pathname) {
		FileReader file = null;
		try {
			file = new FileReader(pathname.toFile());

			file.read();
			if (file.read() == '!' && file.read() != '-') {
				file.close();
				return false;
			}

			file.close();
			return true;

		} catch (Exception e) {
			if (file != null) {
				try {
					file.close();
				} catch (IOException ex) {
				}
			}
			return false;
		}
	}
}
