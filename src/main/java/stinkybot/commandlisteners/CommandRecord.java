package stinkybot.commandlisteners;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import stinkybot.apiQuery.DaybreakApiEvents;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.utils.SqlConnector;
import stinkybot.utils.daybreakutils.event.dto.parsers.DeathOrVehiclePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.GainExperiencePayload;
import stinkybot.utils.daybreakutils.event.dto.parsers.LogInOrLogOutPayload;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandAnnotation
public class CommandRecord implements CommandInterface {

    @Override
    public String getName() {
        return "record";
    }

    @Override
    public String getDescription() {
        return "records events from PS2 for a certain timeframe, \n" +
                "usage: ~record [session name] [sleep for X minutes] [record for X minutes]";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            Member member = event.getMember();
            List<Role> roles;
            if (member == null) {
                return;
            }
            roles = member.getRoles();
            boolean access = roles.stream().anyMatch(role -> role.getName().equals("Bot Master"));
            if (!access) {
                event.getChannel().sendMessage("Only Bot Masters are allowed to run this command.").queue();
                return;
            }

            if (args.length < 2) {
                event.getChannel().sendMessage("usage: ~record [session name] [sleep for X minutes] [record for X minutes]").queue();
                return;
            }

            String session = args[1];
            long sleepFor = Long.parseLong(args[2]);
            long recordFor = Long.parseLong(args[3]);

            String killDeath = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
            String gainExp = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
            String playerLogger = session + "_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false);
            try (FileOutputStream fos = new FileOutputStream(killDeath);
                 OutputStreamWriter streamWriter = new OutputStreamWriter(fos);
                 BufferedWriter bw = new BufferedWriter(streamWriter);
                 FileOutputStream fos2 = new FileOutputStream(gainExp);
                 OutputStreamWriter streamWriter2 = new OutputStreamWriter(fos2);
                 BufferedWriter bw2 = new BufferedWriter(streamWriter2);
                 FileOutputStream fos3 = new FileOutputStream(playerLogger);
                 OutputStreamWriter streamWriter3 = new OutputStreamWriter(fos3);
                 BufferedWriter bw3 = new BufferedWriter(streamWriter3)
            ) {
                DaybreakApiEvents.streamAllEventsForStinkyBot(bw, bw2, bw3, sleepFor, recordFor, getList());
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            {"payload":{"amount":"250",
//            "character_id":"5429119940639123633",
//            "event_name":"GainExperience",
//            "experience_id":"291",
//            "loadout_id":"31",
//            "other_id":"0",
//            "timestamp":"1618049949",
//            "world_id":"17",
//            "zone_id":"6"},
//            "service":"event","type":"serviceMessage"}
            List<DeathOrVehiclePayload> deathOrVehiclePayloads = parseJsonFile(killDeath, DeathOrVehiclePayload.class);
            List<GainExperiencePayload> gainExperiencePayloads =  parseJsonFile(gainExp, GainExperiencePayload.class);
            List<LogInOrLogOutPayload> logInOrLogOutPayloads = parseJsonFile(playerLogger, LogInOrLogOutPayload.class);

        } catch (
                Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage(e.getMessage()).queue();
        }

    }

    private String[] getList() {
        SqlConnector sql = SqlConnector.getInstance();
        List<String> listOfChars = sql.selectAllCharacterIdsFromTrackedList();
        return listOfChars.toArray(new String[0]);
    }

    private <T> List<T> parseJsonFile(String filename, Class<T> classType) throws IOException {
        List<T> result = new LinkedList<>();
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonFactory factory = new JsonFactory(om);
        File file = new File(filename);
        if (file.exists()) {
            try (JsonParser parser = factory.createParser(file)) {
                Iterator<T> itr = parser.readValuesAs(classType);
                while (itr.hasNext()) {
                    T info = itr.next();
                    result.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtils.forceDelete(file);
            }
        }
        return result;
    }
}



