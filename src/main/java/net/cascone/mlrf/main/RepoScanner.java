package net.cascone.mlrf.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cascone.mlrf.config.ConfigReader;
import static net.cascone.mlrf.main.FileDownloader.download;

public class RepoScanner extends SimpleFileVisitor<Path> {

	private int numFixedFiles = 0;
	private int notUpdated = 0;
	private PrintWriter fileWriter;

	private static String[] repos;

	private final String mavenRepositoryAbsolutePath;

	public RepoScanner(String mavenRepositoryAbsolutePath, String[] repoList) {
		super();
		this.mavenRepositoryAbsolutePath = mavenRepositoryAbsolutePath;
		repos = repoList;
		try {
			this.fileWriter = new PrintWriter(ConfigReader.LOG_FILENAME, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException ex) {
			Logger.getLogger(RepoScanner.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Print information about each type of file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

		if (attr.isSymbolicLink()) {
//            System.out.format("Symbolic link: %s ", file);
		} else if (attr.isRegularFile()) {
//            System.out.format("Regular file: %s ", file);
			String ext = getFileExtension(file);

			if (ext != null) {
				switch (ext) {
					case "jar":
					case "JAR":
						if (!net.cascone.mlrf.checker.JarChecker.check(file)) {
							System.out.println("Found corrupted jar: " + file);
							downloadFile(getPartialPath(file));
						}
						break;
					case "pom":
					case "POM":
						if (!net.cascone.mlrf.checker.PomChecker.check(file)) {
							System.out.println("Found corrupted pom: " + file);
							downloadFile(getPartialPath(file));
						}
						break;
					default:
						break;
				}
			}
		}

		// System.out.println("(" + attr.size() + "bytes)");
		return CONTINUE;
	}

	// Print each directory visited.
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		if (dir.equals(Paths.get(mavenRepositoryAbsolutePath))) {

			fileWriter.close();

			System.out.println("\n\n FINISHED");

			if (numFixedFiles > 0) {
				System.out.print("\n Fixed " + numFixedFiles + " files è.é9\n\n");
			}

			if (notUpdated > 0) {
				if (notUpdated > 1) {
					System.out.println(" " + notUpdated + " corrupted files cannot be fixed");
				} else {
					System.out.println(" " + notUpdated + " corrupted file cannot be fixed");
				}
				System.out.println(" (could not find in given repositories)\n"
						+ " >> Check the log: " + ConfigReader.LOG_FILENAME);
			}

			if (numFixedFiles <= 0 && notUpdated <= 0) {
				System.out.print("Everything looks good!\n");
			}
		}
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}

	private static String getFileExtension(Path path) {
		String s = path.toString();
		int i = s.lastIndexOf('.');
		if (i > 0) {
			return s.substring(i + 1);
		}
		return null;
	}

	private void downloadFile(String filePartialPath) {

		String localPath = mavenRepositoryAbsolutePath + filePartialPath;
		boolean found = false;
		for (String s : repos) {
			String remoteURL = s + filePartialPath;
			try {
				System.out.print("   Trying download from " + remoteURL + "...");
				download(remoteURL, localPath);
				found = true;
				System.out.print(" OK");
				numFixedFiles++;
				if (removeJunkFiles(localPath)) {
					System.out.print(" J");
				}
				System.out.print("\n");
				break;
			} catch (IOException fe) {
				System.out.print(" NOT FOUND\n");
			} catch (Exception e) {
				System.out.print("Exception: " + e.getMessage());
			}
		}
		if (!found) {
			notUpdated++;
			fileWriter.println("[NOT UPDATED] " + localPath.replaceAll("/", "\\\\"));
		}
	}

	private String getPartialPath(Path file) {
		return file.toString().substring(mavenRepositoryAbsolutePath.length()).replaceAll("\\\\", "/");
	}

	private boolean removeJunkFiles(String baseName) {
		File lastUpdated = new File(baseName + ".lastUpdated");
		return lastUpdated.delete();
	}

//    @Deprecated
//    private static String getPartialPath(Path file) {
//
//        String[] pathChunks = file.toString().split("\\\\");
//
//        String s = "";
//        boolean repositoryPatternFound = false;
//        for (String c : pathChunks) {
//            if (repositoryPatternFound) {
//                s += '/' + c;
//            }
//            if (c.equals("repository")) {
//                repositoryPatternFound = true;
//            }
//        }
//        return s;
//    }
}
