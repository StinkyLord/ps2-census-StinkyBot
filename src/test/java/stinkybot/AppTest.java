package stinkybot;

import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.Ignore;
import org.junit.Test;
import stinkybot.commandlisteners.CommandTopVehicleWeapon;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Ignore
    @Test
    public void shouldAnswerWithTrue() throws IOException, CensusInvalidSearchTermException {
        EmbedBuilder daybreakInfo = new CommandTopVehicleWeapon().getDaybreakInfo(new String[]{"topv", "samreeves"});
    }
}
