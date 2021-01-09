package stinkybot.utils;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SettingsReader {

	private static final Logger logger = LoggerFactory.getLogger(SettingsReader.class);

	private Settings settings;
	private static SettingsReader instance = null;
	private static final Object lock = new Object();

	private SettingsReader() {
		settings = loadConfigFile("bot.config");
	}

	public static SettingsReader getInstance() {
		if (instance == null)
			synchronized (lock) {
				if (instance == null) {
					instance = new SettingsReader();
				}
			}
		return instance;
	}

	public Settings getSettings() {
		return this.settings;
	}

	private Settings loadConfigFile(String filePath) {
		Settings result = null;

		if (filePath != null) {
			File file = new File(filePath);
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				result = objectMapper.readValue(file, Settings.class);

			} catch (IOException e) {
				logger.warn("loadConfigFile", e);
			}
		}
		return result;
	}

}
