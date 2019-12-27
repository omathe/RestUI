package fr.omathe.restui.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;

public interface AppVersion {

	public class DateVersion {

		public String version;
		public Long date;

		public DateVersion(final String version, final Long date) {
			super();
			this.version = version;
			this.date = date;
		}
	}

	static String date(final String timeZone, final Long date) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		return simpleDateFormat.format(date);
	}

	static DateVersion getDateVersion() {

		DateVersion dateVersion = new DateVersion("X.X.X", Instant.now().toEpochMilli());

		URL url = AppVersion.class.getResource("App2.class");
		if (url != null && url.toString().startsWith("jar")) {

			// retrieve version from the MANIFEST
			InputStream inputStream = null;
			try {
				inputStream = AppVersion.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
				Properties prop = new Properties();
				prop.load(inputStream);
				dateVersion.version = prop.getProperty("RestUI-Version");
				dateVersion.date = Long.valueOf(prop.getProperty("RestUI-Date"));
			} catch (IOException e) {
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}

		} else {
			// retrieve version from the file 'build.gradle'
			File file = new File("build.gradle");
			if (file.exists()) {
				Optional<String> optional;
				try {
					optional = Files.lines(file.toPath()).filter(line -> line != null && line.contains("project.version")).findFirst();
					if (optional.isPresent()) {
						dateVersion.version = optional.get().replaceAll("project.version", "");
						dateVersion.version = dateVersion.version.replaceAll(" ", "");
						dateVersion.version = dateVersion.version.replaceAll("'", "");
						dateVersion.version = dateVersion.version.replaceAll("\"", "");
					}
				} catch (IOException e) {
				}
			}
		}
		return dateVersion;
	}

}
