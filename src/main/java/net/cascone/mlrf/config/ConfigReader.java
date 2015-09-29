package net.cascone.mlrf.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigReader {

	/**
	 * The name of the file contaning the list of repositories
	 */
	private static final String CONFIG_FILENAME = "config.properties";

	private static String REPO_LIST_FILENAME;
	private static String REPO_LIST_LINE_COMMENT_CHAR;

	public static String LOG_FILENAME;
	
	public static boolean PROXY_ENABLED;
	public static String PROXY_SERVER = "172.26.0.133";
	public static String PROXY_PORT = "3128";

	static {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(CONFIG_FILENAME)) {

			prop.load(input);

			LOG_FILENAME = prop.getProperty("log_filename", "mlrf.log");
			
			REPO_LIST_FILENAME = prop.getProperty("repo_list_filename", "repoList");
			REPO_LIST_LINE_COMMENT_CHAR = prop.getProperty("repo_list_comment_char", "#");

			PROXY_ENABLED = Boolean.valueOf(prop.getProperty("proxy_enabled"));
			PROXY_SERVER = prop.getProperty("proxy_server");
			PROXY_PORT = prop.getProperty("proxy_port");

			System.out.println("Configuration file read successfully");

		} catch (IOException ex) {
			System.out.println("Please be sure a config.properties file exists.");
			System.err.println(ex.getMessage());
		}
	}

	public static String[] readRepoList() throws IOException {

		List<String> repoList = new ArrayList<>();

		File fin = new File(REPO_LIST_FILENAME);
		try (FileInputStream fis = new FileInputStream(fin);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {

			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith(REPO_LIST_LINE_COMMENT_CHAR)) {
					repoList.add(line);
				}
			}
		}

		return repoList.toArray(new String[repoList.size()]);
	}
}
