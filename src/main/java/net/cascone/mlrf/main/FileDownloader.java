package net.cascone.mlrf.main;

import java.io.File;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.cascone.mlrf.config.ConfigReader;

public class FileDownloader {

	public static void download(String src, String dest) throws Exception {

		if (ConfigReader.PROXY_ENABLED) {
			System.setProperty("http.proxyHost", ConfigReader.PROXY_SERVER);
			System.setProperty("http.proxyPort", ConfigReader.PROXY_PORT);
		}

		// Create a new trust manager that trusts all certificates
		TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
			}
		};

		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			System.err.println("Exception e: " + e.getLocalizedMessage());
		}

		org.apache.commons.io.FileUtils.copyURLToFile(new URL(src), new File(dest));

	}
}
