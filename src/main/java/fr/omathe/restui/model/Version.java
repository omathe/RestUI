package fr.omathe.restui.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;
import java.util.TimeZone;

import fr.omathe.restui.controller.ControllerManager;
import javafx.scene.paint.Color;

public class Version {

	private static String name = "1.3.2";
	private static Long date = 1579617730666L;

	public static String getName() {
		return name;
	}

	public static String getDate(final String timeZone) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		return simpleDateFormat.format(date);
	}

	public static void load() {

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
				e.printStackTrace();
				ControllerManager.getMainController().getBottomController().setNotification(e.getMessage(), Color.RED);
			}
			date = Instant.now().toEpochMilli();
		}
	}

}
