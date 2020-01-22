package fr.omathe.restui.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.omathe.restui.controller.ControllerManager;
import javafx.scene.paint.Color;

public interface Tools {

	/**
	 * Get byte[] from an InputStream
	 */
	static byte[] getBytes(final InputStream inputStream) {

		byte[] bytes = null;

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		try {
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			bytes = buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ControllerManager.getMainController().getBottomController().setNotification(e.getMessage(), Color.RED);
			}
		}
		return bytes;
	}

	static void writeBytesToFile(final File file, final byte[] bytes) {

		if (file != null && bytes.length > 0) {
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
				ControllerManager.getMainController().getBottomController().setNotification(e.getMessage(), Color.RED);
			}
		}
	}

	static String findFileName(final String contentDisposition) {

		String fileName = null;

		System.out.println(contentDisposition);
		int index = contentDisposition.indexOf("filename");
		if (index != -1) {
			String s2 = contentDisposition.substring(index, contentDisposition.length());
			String[] split = s2.split("=");
			if (split.length == 2) {
				fileName = split[1].replaceAll("\"", "");
			}
		}
		return fileName;
	}
}
