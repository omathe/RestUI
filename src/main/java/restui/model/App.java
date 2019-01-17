package restui.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

		//File file = new File("version.txt");
		File file = null;
		try {
			URL url = App.class.getResource("/version/version.txt");
			if (url != null) {
				System.out.println("url = " + url);
				file = new File(url.toURI());
			}
			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (file.exists()) {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			file = new File("build.gradle");
			if (file.exists()) {
				try {
					Optional<String> optional = Files.lines(file.toPath()).filter(line -> line != null && line.contains("project.version")).findFirst();
					if (optional.isPresent()) {
						dateVersion.version = optional.get().replaceAll("project.version", "");
						dateVersion.version = dateVersion.version.replaceAll(" ", "");
						dateVersion.version = dateVersion.version.replaceAll("'", "");
						dateVersion.version = dateVersion.version.replaceAll("\"", "");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		return dateVersion;
	}

}
