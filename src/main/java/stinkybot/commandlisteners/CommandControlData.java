package stinkybot.commandlisteners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.utilities.CommandAnnotation;
import stinkybot.commandlisteners.utilities.CommandInterface;
import stinkybot.commandlisteners.utilities.Utils;
import stinkybot.utils.SqlConnector;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.CharacterName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandAnnotation
public class CommandControlData implements CommandInterface {
    @Override
    public String getName() {
        return "track";
    }

    @Override
    public String getDescription() {
        return "Controlling the players that are being tracked by the bot";
    }

    @Override
    public void run(GuildMessageReceivedEvent event, String[] args) {
        try {
            Member member = event.getMember();
//            List<Role> roles;
            if (member == null) {
                return;
            }
//            roles = member.getRoles();
//            boolean access = roles.stream().anyMatch(role -> role.getName().equals("Bot Master"));
//            if (!access) {
//                event.getChannel().sendMessage("Only Bot Masters are allowed to run this command.").queue();
//                return;
//            }

            String nickName = member.getEffectiveName();
            if (!nickName.equals("StinkyBullet")) {
                event.getChannel().sendMessage("Still under construction").queue();
                return;
            }

            if (args.length < 2) {
                event.getChannel().sendMessage("usage: ~track [command] [characters]").queue();
                return;
            }

            switch (args[1]) {
                case "add":
                    String[] playersToAdd = Arrays.copyOfRange(args, 2, args.length);
                    String msg1 = addMembers(playersToAdd);
                    event.getChannel().sendMessage(msg1).queue();
                    break;
                case "remove":
                    String[] playersToRemove = Arrays.copyOfRange(args, 2, args.length);
                    String msg2 = removeMembers(playersToRemove);
                    event.getChannel().sendMessage(msg2).queue();
                    break;
                case "addOutfit":
                    String outfitNameAdd = args[2];
                    String msg3 = addOutfitMembers(outfitNameAdd);
                    event.getChannel().sendMessage(msg3).queue();
                    break;
                case "removeOutfit":
                    String outfitNameRmv = args[2];
                    String msg4 = removeOutfitMembers(outfitNameRmv);
                    event.getChannel().sendMessage(msg4).queue();
                    break;
                case "list":
                    List<String> trackedList = getList();
                    String filePath = Utils.writeListToFile(trackedList);
                    File file = new File(filePath);
                    event.getChannel().sendFile(file).queue();
                    Files.deleteIfExists(file.toPath());
                    break;
                case "clear":
                    String msg6 = clearList();
                    event.getChannel().sendMessage(msg6).queue();
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getList() {
        SqlConnector sql = SqlConnector.getInstance();
        return sql.selectAllNamesFromTrackedList();
    }

    private String removeOutfitMembers(String outfitNameRmv) throws IOException, CensusInvalidSearchTermException {
        List<CharacterName> characterNames = getOutfitMembers(outfitNameRmv);
        if (characterNames == null) {
            return "Error occurred, outfit not found";
        }
        List<String> list = characterNames.stream()
                .map(cn -> cn.getName().getFirst_lower())
                .collect(Collectors.toList());
        return convertDataAndRemoveFromTable(list);
    }

    private String removeMembers(String[] playersToRemove) {
        List<String> list = Arrays.stream(playersToRemove)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return convertDataAndRemoveFromTable(list);
    }

    private String convertDataAndRemoveFromTable(List<String> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String name : list) {
            sb.append("'").append(name).append("'").append(",");
        }
        String valuesQuery = sb.deleteCharAt(sb.length() - 1).append(")").toString();
        SqlConnector sql = SqlConnector.getInstance();

        return sql.updateQuery("DELETE FROM trackedlist WHERE name in " + valuesQuery);
    }

    private String addMembers(String[] playersToAdd) throws IOException, CensusInvalidSearchTermException {
        String[] list = Arrays.stream(playersToAdd)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        List<CharacterName> characterNames = DaybreakApiQuery.getCharacterNamesByName(list);
        if (characterNames == null) {
            return "Error occurred, Characters not found";
        }
        return convertDataAndInsertToTrackedList(characterNames);
    }

    private String addOutfitMembers(String outfitNameAdd) throws IOException, CensusInvalidSearchTermException {
        List<CharacterName> characterNames = getOutfitMembers(outfitNameAdd);
        if (characterNames == null) {
            return "Error occurred, outfit not found";
        }
        return convertDataAndInsertToTrackedList(characterNames);
    }

    private String clearList() {
        SqlConnector sql = SqlConnector.getInstance();
        return sql.updateQuery("delete from trackedlist");
    }


    @Nullable
    private List<CharacterName> getOutfitMembers(String outfitNameAdd) throws IOException, CensusInvalidSearchTermException {
        String[] allCharIds = DaybreakApiQuery.getCharacterIdsFromOutfitTag(outfitNameAdd);
        if (allCharIds == null) {
            return null;
        }
        return DaybreakApiQuery.getCharacterNamesByIds(allCharIds);
    }

    private String convertDataAndInsertToTrackedList(List<CharacterName> characterNames) {
        StringBuilder sb = new StringBuilder();
        for (CharacterName cn : characterNames) {
            sb.append("('").append(cn.getName().getFirst_lower()).append("','").append(cn.getCharacter_id()).append("')");
            sb.append(",");
        }
        String valuesQuery = sb.deleteCharAt(sb.length() - 1).toString();
        SqlConnector sql = SqlConnector.getInstance();
        return sql.updateQuery("INSERT INTO trackedlist values" + valuesQuery);
    }
}
