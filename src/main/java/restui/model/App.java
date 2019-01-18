package restui.model;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.TimeZone;

public interface App {

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

		File file = null;
		try {
			// search for version generated by the build tool 
			URL url = App.class.getResource("/version/version.txt");
			if (url != null) {
				file = new File(url.toURI());
				if (file.exists()) {
					// version
					Optional<String> optional = Files.lines(file.toPath()).filter(line -> line != null && line.contains("version")).findFirst();
					if (optional.isPresent()) {
						String[] split = optional.get().split("=");
						if (split != null && split.length == 2) {
							dateVersion.version = split[1].trim();
						}
					}
					// date
					optional = Files.lines(file.toPath()).filter(line -> line != null && line.contains("date")).findFirst();
					if (optional.isPresent()) {
						String[] split = optional.get().split("=");
						if (split != null && split.length == 2) {
							dateVersion.date = Long.valueOf(split[1].trim());
						}
					}
				} else {
					// search for version defined in build tool script
					file = new File("build.gradle");
					if (file.exists()) {
						Optional<String> optional = Files.lines(file.toPath()).filter(line -> line != null && line.contains("project.version")).findFirst();
						if (optional.isPresent()) {
							dateVersion.version = optional.get().replaceAll("project.version", "");
							dateVersion.version = dateVersion.version.replaceAll(" ", "");
							dateVersion.version = dateVersion.version.replaceAll("'", "");
							dateVersion.version = dateVersion.version.replaceAll("\"", "");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateVersion;
	}

}
