package stinkybot.apiQuery;

import stinkybot.utils.SettingsReader;
import stinkybot.utils.daybreakutils.Join;
import stinkybot.utils.daybreakutils.Query;
import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.anatomy.SearchModifier;
import stinkybot.utils.daybreakutils.exception.CensusInvalidSearchTermException;
import stinkybot.utils.daybreakutils.query.dto.CensusCollectionImpl;
import stinkybot.utils.daybreakutils.query.dto.ICensusCollection;
import stinkybot.utils.daybreakutils.query.dto.internal.Character;
import stinkybot.utils.daybreakutils.query.dto.internal.*;
import stinkybot.utils.daybreakutils.tree.Pair;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DaybreakApiQuery {

    public static final String serviceId = SettingsReader.getInstance().getSettings().getDaybreakServiceId();

    public static int getRegionCount(String worldId, String zoneId) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.MAP, serviceId).filter("world_id", worldId).filter("zone_ids", zoneId);
        Map col = (Map) q.getAndParse().get(0);
        return col.getRegions().getRow().size();
    }

    public static Character getPlayerByName(String name) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("name.first_lower", name.toLowerCase());
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return (Character) list.get(0);
    }

	public static CharactersWeaponStat getPlayerTopWeaponKillsByName(String name) throws IOException, CensusInvalidSearchTermException {
        String characterId = getPlayerIdByName(name);
        if (characterId == null) {
            return null;
        }
        List<ICensusCollection> list = new Query(Collection.CHARACTERS_WEAPON_STAT, serviceId)
                .filter("character_id", characterId)
                .filter("stat_name", "weapon_score")
                .filter("item_id", SearchModifier.NOT,"0","650","432","44605","429","800623","1095"
                		,"881","6008686","50560","34002","85","16031","804652","804179")
                .filter("vehicle_id", "0")
                .sort(new Pair<>("value", -1))
                .join(new Join(Collection.ITEM).on("item_id"))
                .getAndParse();
        if (list == null || list.isEmpty() || !(list.get(0) instanceof CharactersWeaponStat)) {
            return null;
        }
        CharactersWeaponStat charactersWeaponStat = (CharactersWeaponStat) list.get(0);
        List<ICensusCollection> nested = charactersWeaponStat.getNested();
        if (nested == null || nested.isEmpty() || !(nested.get(0) instanceof Item)) {
            return null;
        }
        return (CharactersWeaponStat) list.get(0);
    }

    public static CharactersWeaponStat getPlayerTopVehicleWeaponKillsByName(String name) throws IOException, CensusInvalidSearchTermException {
        List<ICensusCollection> list = new Query(Collection.CHARACTERS_WEAPON_STAT, serviceId)
                .filter("character_id", getPlayerIdByName(name))
                .filter("stat_name", "weapon_score")
                .filter("item_id",SearchModifier.NOT,"0")
                .filter("vehicle_id", SearchModifier.NOT, "0")
                .sort(new Pair<>("value", -1))
                .join(new Join(Collection.VEHICLE).on("vehicle_id"))
                .join(new Join(Collection.ITEM).on("item_id"))
                .getAndParse();
        if (list == null || list.isEmpty() || !(list.get(0) instanceof CharactersWeaponStat)) {
            return null;
        }
        CharactersWeaponStat charactersWeaponStat = (CharactersWeaponStat) list.get(0);
        List<ICensusCollection> nested = charactersWeaponStat.getNested();
        if (nested == null || nested.isEmpty() || !(nested.get(0) instanceof Vehicle)|| !(nested.get(1) instanceof Item)) {
            return null;
        }
        return (CharactersWeaponStat) list.get(0);
    }

    public static List<ICensusCollection> getPLayerWeaponStats(String characterId, String weaponId) throws IOException, CensusInvalidSearchTermException {
        List<ICensusCollection> list2 = new Query(Collection.CHARACTERS_WEAPON_STAT_BY_FACTION, serviceId)
                .filter("character_id", characterId)
                .filter("item_id", weaponId)
                .filter("vehicle_id", "0").limit(10).getAndParse();
        if (list2 == null || list2.isEmpty() || !(list2.get(0) instanceof CharactersWeaponStatByFaction)) {
            return null;
        }
        return list2;
    }
    public static List<ICensusCollection> getPLayerVehicleWeaponStats(String characterId, String weaponId, String vehicle_id) throws IOException, CensusInvalidSearchTermException {
        List<ICensusCollection> list2 = new Query(Collection.CHARACTERS_WEAPON_STAT_BY_FACTION, serviceId)
                .filter("character_id", characterId)
                .filter("item_id", weaponId)
                .filter("vehicle_id",vehicle_id).limit(10).getAndParse();
        if (list2 == null || list2.isEmpty() || !(list2.get(0) instanceof CharactersWeaponStatByFaction)) {
            return null;
        }
        return list2;
    }

    public static CharactersWeaponStat getPLayerWeaponDeaths(String characterId, String weaponId) throws IOException, CensusInvalidSearchTermException {
        List<ICensusCollection> list = new Query(Collection.CHARACTERS_WEAPON_STAT, serviceId)
                .filter("character_id", characterId)
                .filter("stat_name", "weapon_deaths")
                .filter("item_id", weaponId)
                .filter("vehicle_id", "0").getAndParse();
        if (list == null || list.isEmpty() || !(list.get(0) instanceof CharactersWeaponStat)) {
            return null;
        }

        return (CharactersWeaponStat) list.get(0);
    }

    public static CharactersWeaponStat getPLayerVehicleWeaponDeaths(String characterId, String weaponId, String vehicleId) throws IOException, CensusInvalidSearchTermException {
        List<ICensusCollection> list = new Query(Collection.CHARACTERS_WEAPON_STAT, serviceId)
                .filter("character_id", characterId)
                .filter("stat_name", "weapon_deaths")
                .filter("item_id", weaponId)
                .filter("vehicle_id", vehicleId).getAndParse();
        if (list == null || list.isEmpty() || !(list.get(0) instanceof CharactersWeaponStat)) {
            return null;
        }
        return (CharactersWeaponStat) list.get(0);
    }
    public static Character getPlayerById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("character_id", id);
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return (Character) list.get(0);
    }

    public static List<Character> getPlayersById(String... ids) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("character_id", ids).limit(ids.length);
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().map(m -> (Character) m).collect(Collectors.toList());
    }

    public static List<OutfitMemberExtended> getMemberDataById(String... ids)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER_EXTENDED, serviceId).filter("character_id", ids).limit(ids.length);
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().map(m -> (OutfitMemberExtended) m).collect(Collectors.toList());
    }

    public static String getPlayerNameById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("character_id", id).show("name.first");
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return ((Character) list.get(0)).getName().getFirst();
    }

    public static List<String> getPlayerIdsByNames(String... names)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).show("character_id").limit(names.length);
        for (String name : names) {
            q.filter("name.first_lower", name.toLowerCase());
        }
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().map(m -> ((Character) m).getCharacter_id()).collect(Collectors.toList());
    }

    public static String getPlayerIdByName(String name) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("name.first_lower", name.toLowerCase())
                .show("character_id");
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return ((Character) list.get(0)).getCharacter_id();
    }

    public static Outfit getOutfitByTag(String tag) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT, serviceId).filter("alias_lower", tag.toLowerCase());
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return (Outfit) list.get(0);
    }

    public static Outfit getOutfitById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT, serviceId).filter("outfit_id", id);
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return (Outfit) list.get(0);
    }

    public static String getOutfitIdByTag(String tag) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT, serviceId).filter("alias_lower", tag.toLowerCase()).show("outfit_id");
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return ((Outfit) list.get(0)).getOutfit_id();
    }

    public static long getOutfitMemberCountByTag(String tag) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER_EXTENDED, serviceId).filter("alias_lower", tag.toLowerCase());
        return q.count();
    }

    public static long getOutfitMemberCountById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER, serviceId).filter("outfit_id", id);
        return q.count();
    }

    public static String getOutfitMemberRankById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER, serviceId).filter("character_id", id).show("rank");
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return ((OutfitMember) list.get(0)).getRank();
    }

    public static String getOutfitMemberRankByName(String name) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("name.first_lower", name)
                .join(new Join(Collection.OUTFIT_MEMBER).show("rank"));
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return ((OutfitMember) list.get(0)).getRank();
    }

    public static String getOutfitTagOfMember(String name) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("name.first_lower", name.toLowerCase())
                .show("character_id")
                .join(new Join(Collection.OUTFIT_MEMBER_EXTENDED).on("character_id").show("alias"));
        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return ((OutfitMemberExtended) list.get(0)).getAlias();
    }

    public static List<OutfitMember> getOutfitMembersByTag(String outfit_tag, String outfit_rank)
            throws CensusInvalidSearchTermException, IOException {
        Join outfit_members = new Join(Collection.OUTFIT_MEMBER).on("outfit_id").list(1);
        if (outfit_rank != null && !outfit_rank.isEmpty()) {
            outfit_members.terms(new Pair<String, String>("rank", outfit_rank));
        }
        Query q = new Query(Collection.OUTFIT, serviceId).filter("alias_lower", outfit_tag).show("outfit_id")
                .join(outfit_members);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.stream().map(m -> ((OutfitMember) m)).collect(Collectors.toList());
    }

    public static List<OutfitMember> getOutfitMembersById(String outfit_id, String outfit_rank)
            throws CensusInvalidSearchTermException, IOException {
        Join outfit_members = new Join(Collection.OUTFIT_MEMBER).on("outfit_id").list(1);
        if (outfit_rank != null && !outfit_rank.isEmpty()) {
            outfit_members.terms(new Pair<String, String>("rank", outfit_rank));
        }
        Query q = new Query(Collection.OUTFIT, serviceId).filter("outfit_id", outfit_id).show("outfit_id")
                .join(outfit_members);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.stream().map(m -> ((OutfitMember) m)).collect(Collectors.toList());
    }

    public static List<String> getMemberIdsByOutfitId(String outfit_id, String outfit_rank)
            throws CensusInvalidSearchTermException, IOException {
        Join outfit_members = new Join(Collection.OUTFIT_MEMBER).on("outfit_id").list(1).show("character_id");
        if (outfit_rank != null && !outfit_rank.isEmpty()) {
            outfit_members.terms(new Pair<String, String>("rank", outfit_rank));
        }
        Query q = new Query(Collection.OUTFIT, serviceId).filter("outfit_id", outfit_id).show("outfit_id")
                .join(outfit_members);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.stream().map(m -> ((OutfitMember) m).getCharacter_id()).collect(Collectors.toList());
    }

    public static stinkybot.utils.daybreakutils.enums.World getOutfitWorldById(String id)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER, serviceId).filter("outfit_id", id).show("character_id")
                .join(new Join(Collection.CHARACTERS_WORLD).on("character_id").show("world_id"));

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return stinkybot.utils.daybreakutils.enums.World.findWorld(Integer.parseInt(((CharactersWorld) list.get(0)).getWorld_id()));
    }

    public static stinkybot.utils.daybreakutils.enums.Faction getCharacterFactionByName(String name)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("name.first_lower", name.toLowerCase())
                .show("faction_id");

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        return stinkybot.utils.daybreakutils.enums.Faction.findFaction(Integer.parseInt(((Character) list.get(0)).getFaction_id()));
    }

    public static stinkybot.utils.daybreakutils.enums.Faction getCharacterFactionById(String id)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTER, serviceId).filter("character_id", id).show("faction_id");

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return stinkybot.utils.daybreakutils.enums.Faction.findFaction(Integer.parseInt(((Character) list.get(0)).getFaction_id()));
    }

    public static stinkybot.utils.daybreakutils.enums.Faction getOutfitFactionByTag(String tag)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER_EXTENDED, serviceId).filter("alias_lower", tag.toLowerCase())
                .show("character_id").join(new Join(Collection.CHARACTER).on("character_id").show("faction_id"));

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return stinkybot.utils.daybreakutils.enums.Faction.findFaction(Integer.parseInt(((Character) list.get(0)).getFaction_id()));
    }

    public static stinkybot.utils.daybreakutils.enums.Faction getOutfitFactionById(String id)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER_EXTENDED, serviceId).filter("outfit_id", id).show("character_id")
                .join(new Join(Collection.CHARACTER).on("character_id").show("faction_id"));

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }
        list = ((CensusCollectionImpl) list.get(0)).getNested();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return stinkybot.utils.daybreakutils.enums.Faction.findFaction(Integer.parseInt(((Character) list.get(0)).getFaction_id()));
    }

    public static OutfitMemberExtended getOutfitMemberById(String id)
            throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.OUTFIT_MEMBER_EXTENDED, serviceId).filter("character_id", id);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return (OutfitMemberExtended) list.get(0);
    }

    public static MapRegion getRegionByFacilityId(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.MAP_REGION, serviceId).filter("facility_id", id);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return (MapRegion) list.get(0);
    }

    public static MetagameEvent getAlertById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.METAGAME_EVENT, serviceId).filter("metagame_event_id", id);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return (MetagameEvent) list.get(0);
    }

    public static MetagameEventState getAlertStateById(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.METAGAME_EVENT_STATE, serviceId).filter("metagame_event_state_id", id);

        List<ICensusCollection> list = q.getAndParse();

        if (list == null || list.isEmpty()) {
            return null;
        }

        return (MetagameEventState) list.get(0);
    }

    public static void getWeaponsForCharacter(String id) throws CensusInvalidSearchTermException, IOException {
        Query q = new Query(Collection.CHARACTERS_WEAPON_STAT, serviceId).filter("character_id", "5428021759087055329");

        List<ICensusCollection> list = q.getAndParse();

//        if (list == null || list.isEmpty()) {
//            return null;
//        }

//        return (MetagameEventState) list.get(0);
    }

}

/*
 "character" +
`?character_id=${character_ids_partition.join( "," )}` +
"&c:show=character_id,times.last_save" +
//join characters_stat
"&c:join=characters_stat^inject_at:stat^list:1^show:stat_name'profile_id'value_forever" +
"&c:tree=start:stat^field:stat_name" +
"&c:tree=profile_id" +
"&c:tree=value_forever" +
//join characters_stat_by_faction
"&c:join=characters_stat_by_faction^inject_at:stat_by_faction^list:1^show:stat_name'profile_id  'value_forever_vs'value_forever_nc'value_forever_tr" +
"&c:tree=start:stat_by_faction^field:stat_name" +
"&c:tree=profile_id" +
//join characters_directive_tree
"&c:join=characters_directive_tree^inject_at:directive_tree^list:1^show:directive_tree_id       'current_directive_tier_id" +
//join characters_weapon_stat
"&c:join=characters_weapon_stat^inject_at:weapon_stat^list:1^show:item_id'stat_name'value^terms   :item_id=!0" +
"&c:tree=start:weapon_stat^field:item_id" +
"&c:tree=stat_name" +
"&c:tree=value" +
//join characters_weapon_stat_by_faction
"&c:join=characters_weapon_stat_by_faction^inject_at:weapon_stat_by_faction^list:1^show:item_id 'stat_name'value_vs'value_nc'value_tr^terms:item_id=!0" +
"&c:tree=start:weapon_stat_by_faction^field:item_id" +
"&c:tree=stat_name",

* */