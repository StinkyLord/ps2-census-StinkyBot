package stinkybot.utils.discordutils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import stinkybot.utils.SettingsReader;

import javax.security.auth.login.LoginException;

public class JDAConnector {


    private JDA jda;
    private static JDAConnector instance = null;
    private static final Object lock = new Object();

    private JDAConnector() {
    	SettingsReader sr = SettingsReader.getInstance();
        String token = sr.getSettings().getBotToken();
        try {
            jda = JDABuilder.createDefault(token).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static JDAConnector getInstance() {
        if (instance == null)
            synchronized (lock) {
                if (instance == null) {
                    instance = new JDAConnector();
                }
            }
        return instance;
    }

    public JDA getJda() {
        return this.jda;
    }
}
