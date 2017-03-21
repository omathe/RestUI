package restui.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceHelper {

	/**
	 * Copy source to destination
	 * 
	 * @param source
	 *            - The source path
	 * @param destination
	 *            - The destination path directory
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void copyResource(final String source, final String destination) throws IOException, URISyntaxException {

		final File file = new File(ResourceHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		if (file.isFile()) {
			// this is a jar file : we copy all the files starting with source
			copyResourceFromJar(new JarFile(file), source, destination);
		} else {
			// application is lauching from an ide
			copyResourceFromIde(source, destination);
		}
	}

	private static void copyResourceFromJar(final JarFile jar, final String source, final String destination) throws IOException {

		final Enumeration<JarEntry> entries = jar.entries();
		final String jarPath = source.startsWith("/") ? source.substring(1) : source; // remove '/' at the beginning

		InputStream inputStream = null;
		try {
			while (entries.hasMoreElements()) {
				final JarEntry entry = entries.nextElement();
				final String entryName = entry.getName();

				if (entryName.startsWith(jarPath)) {
					final Path path = Paths.get(destination, File.separator, entryName);

					if (entry.isDirectory()) {
						path.toFile().mkdirs();
					} else {
						try {
							inputStream = jar.getInputStream(entry);
							if (!path.toFile().exists()) {
								Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
							}
						} finally {
							if (inputStream != null) {
								inputStream.close();
							}
						}
					}
				}
			}
		} finally {
			jar.close();
			System.out.println("jar closed");
		}
	}

	private static void copyResourceFromIde(final String source, final String destination) throws URISyntaxException, IOException {

		final URL url = ResourceHelper.class.getResource(source);
		final Path root = Paths.get(url.toURI());

		final Stream<Path> paths = Files.walk(root).filter(Files::isRegularFile);

		for (final Path sourcePath : paths.collect(Collectors.toList())) {
			final Path destinationPath = Paths.get(destination, File.separator, source, File.separator, root.relativize(sourcePath).toString());
			if (!destinationPath.getParent().toFile().exists()) {
				destinationPath.getParent().toFile().mkdirs();
			}
			if (!destinationPath.toFile().exists()) {
				final InputStream inputStream = Files.newInputStream(sourcePath);
				try {
					Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
		}
	}

}
