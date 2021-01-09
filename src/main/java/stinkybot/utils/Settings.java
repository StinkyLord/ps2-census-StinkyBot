package stinkybot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {

	@JsonProperty("BotToken")
	private String botToken;
	
	@JsonProperty("DaybreakServiceId")
	private String daybreakServiceId;

	public String getBotToken() {
		return botToken;
	}

	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}

	public String getDaybreakServiceId() {
		return daybreakServiceId;
	}

	public void setDaybreakServiceId(String daybreakServiceId) {
		this.daybreakServiceId = daybreakServiceId;
	}
	
}
