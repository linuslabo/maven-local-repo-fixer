package net.cascone.mlrf.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import net.cascone.mlrf.config.ConfigReader;

public class Launch {

	public static void main(String[] args) {

		String mavenRepositoryAbsolutePath;
		if (args.length == 0) {
			mavenRepositoryAbsolutePath = System.getProperty("user.dir");
			System.out.println("Calling with no arguments uses execution folder.");
			System.out.println("Alternative use: java -jar MavenConsistencyChecker absolutePathOfTheDirectoryToCheck\n"
					+ "e.g. java -jar MavenConsistencyChecker C:\\Users\\User\\.m2\\repository\n");
		} else {
			mavenRepositoryAbsolutePath = args[0];
		}

		try {
			
			Path rootDir = Paths.get(mavenRepositoryAbsolutePath);
			System.out.println("Using repository: " + rootDir);

			File file = rootDir.toFile();
			if (!file.isDirectory()) {
				throw new IOException(mavenRepositoryAbsolutePath + " is not a directory");
			}
			if (!file.exists()) {
				throw new IOException(mavenRepositoryAbsolutePath + " does not exist");
			}

			String[] repoList;

			try {
				repoList = ConfigReader.readRepoList();
			} catch (Exception e) {
				System.out.println("A file 'repoList' must exist, containing the list of repositories to use (one per line).");
				System.out.println(e.getMessage());
				return;
			}

			SimpleFileVisitor sfv = new RepoScanner(mavenRepositoryAbsolutePath, repoList);
			Files.walkFileTree(rootDir, sfv);

		} catch (Exception e) {
			System.out.println("Repository path may be not valid: " + mavenRepositoryAbsolutePath);
			System.out.println(e.getMessage());
		}
	}
}
