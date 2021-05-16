package stinkybot.utils.daybreakutils.anatomy.commands;

import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.query.dto.CensusCollectionImpl;
import stinkybot.utils.daybreakutils.query.dto.internal.Character;
import stinkybot.utils.daybreakutils.query.dto.internal.Loadout;
import stinkybot.utils.daybreakutils.query.dto.internal.Vehicle;
import stinkybot.utils.daybreakutils.query.dto.internal.Weapon;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeathVehicleKillMapper {
//    private String event_name;
//    private String faction_id;
//    private String is_headshot;
//    private String timestamp;
    Map<String, Character> characters;
    Map<String, Loadout> loadOuts;
    Map<String, String> weapons;
    Map<String, Vehicle> vehicles;

    public Map<String, Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Map<String, Character> characters) {
        this.characters = characters;
    }

    public Map<String, Loadout> getLoadOuts() {
        return loadOuts;
    }

    public void setLoadOuts(Map<String, Loadout> loadOuts) {
        this.loadOuts = loadOuts;
    }

    public Map<String, String> getWeapons() {
        return weapons;
    }

    public void setWeapons(Map<String, String> weapons) {
        this.weapons = weapons;
    }

    public Map<String, Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<String, Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
