package stinkybot.utils.discordutils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class JDAConnector {


    private JDA jda;
    private static JDAConnector instance = null;
    private static final Logger logger = LoggerFactory.getLogger(JDAConnector.class);
    private static final Object lock = new Object();

    private JDAConnector() {
        String token = System.getenv().get("STINKY_BOT_TOKEN");
        try {
            jda = JDABuilder.createDefault(token).build();
        } catch (LoginException e) {
            logger.error("Unable to create jda", e);
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
