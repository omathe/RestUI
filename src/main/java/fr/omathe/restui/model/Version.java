package fr.omathe.restui.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Optional;

import fr.omathe.restui.service.Logger;
import fr.omathe.restui.service.Notifier;
import fr.omathe.restui.service.tools.DateFormater;

public class Version {

	private static String name = "1.3.3";
	private static Long date = 1580726614578L;
	private static boolean loaded = false;

	public static String getName() {
		
		load();
		return name;
	}

	public static String getDate(final String timeZone) {

		load();
		return DateFormater.iso(date, timeZone);
	}

	private static void load() {

		if (!loaded) {
			File file = new File("build.gradle");
			if (file.exists()) {
				try {
					Optional<String> line = Files.lines(file.toPath())
							.filter(l -> l != null && l.contains("project.version"))
							.findFirst();
					if (line.isPresent()) {
						name = line.get().replaceAll("project.version", "");
						name = name.replaceAll(" ", "");
						name = name.replaceAll("'", "");
						name = name.replaceAll("\"", "");
					}
				} catch (IOException e) {
					Logger.error(e);
					Notifier.notifyError(e.getMessage());
				}
				date = Instant.now().toEpochMilli();
			}
			loaded = true;
		}
	}

}
