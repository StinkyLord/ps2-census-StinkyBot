package stinkybot.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {

	@JsonProperty("BotToken")
	private String botToken;
	
	@JsonProperty("DaybreakServiceId")
	private String daybreakServiceId;

	@JsonProperty("sqlUser")
	private String sqlUser;

	@JsonProperty("sqlPass")
	private String sqlPass;


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

	public String getSqlUser() {
		return sqlUser;
	}

	public void setSqlUser(String sqlUser) {
		this.sqlUser = sqlUser;
	}

	public String getSqlPass() {
		return sqlPass;
	}

	public void setSqlPass(String sqlPass) {
		this.sqlPass = sqlPass;
	}
}
