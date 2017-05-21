package restui.commons;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public interface Strings {

	static boolean isNullOrEmpty(final String string) {

		return string == null || string.isEmpty();
	}
	
	/**
	 * Build a base 64 string from an UUID
	 * @return A String
	 */
	static String generate() {

		final UUID uuid2 = UUID.randomUUID();
		final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid2.getMostSignificantBits());
		byteBuffer.putLong(uuid2.getLeastSignificantBits());
		final String uuid64 = Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());

		return uuid64;
	}

}
