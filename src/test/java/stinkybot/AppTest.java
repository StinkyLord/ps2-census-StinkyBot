package stinkybot;

import static org.junit.Assert.assertTrue;

import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.Test;
import stinkybot.apiQuery.DaybreakApiQuery;
import stinkybot.commandlisteners.CommandTopVehicleWeapon;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.internal.CharactersWeaponStat;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws IOException, CensusInvalidSearchTermException {
        EmbedBuilder daybreakInfo = new CommandTopVehicleWeapon().getDaybreakInfo(new String[]{"topv", "StinkyBullet"});
    }
}
