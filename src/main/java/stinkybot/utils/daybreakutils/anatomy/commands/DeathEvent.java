package stinkybot.utils.daybreakutils.anatomy.commands;

public class DeathEvent {
    private String attackerName;
    private String attackerClass;
    private String attackerVehicle;
    private String attackerWeapon;
    private String victimName;
    private String victimOutfit;
    private String victimClass;
    private String timestamp;
    private String vehicleName;
    private String worldName;
    private String zoneName;
    private String eventName;
    private boolean isHeadshot;
    private String attackerOutfit;
    private String victimFaction;
    private String attackerFaction;

    public String getAttackerName() {
        return attackerName;
    }

    public void setAttackerName(String attackerName) {
        this.attackerName = attackerName;
    }

    public String getAttackerClass() {
        return attackerClass;
    }

    public void setAttackerClass(String attackerClass) {
        this.attackerClass = attackerClass;
    }

    public String getAttackerVehicle() {
        return attackerVehicle;
    }

    public void setAttackerVehicle(String attacker_vehicle) {
        this.attackerVehicle = attacker_vehicle;
    }

    public String getAttackerWeapon() {
        return attackerWeapon;
    }

    public void setAttackerWeapon(String attackerWeapon) {
        this.attackerWeapon = attackerWeapon;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimOutfit() {
        return victimOutfit;
    }

    public void setVictimOutfit(String victimOutfit) {
        this.victimOutfit = victimOutfit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isHeadshot() {
        return isHeadshot;
    }

    public void setHeadshot(boolean headshot) {
        isHeadshot = headshot;
    }

    public String getAttackerOutfit() {
        return attackerOutfit;
    }

    public void setAttackerOutfit(String attackerOutfit) {
        this.attackerOutfit = attackerOutfit;
    }

    public String getVictimFaction() {
        return victimFaction;
    }

    public void setVictimFaction(String victimFaction) {
        this.victimFaction = victimFaction;
    }

    public String getAttackerFaction() {
        return attackerFaction;
    }

    public void setAttackerFaction(String attackerFaction) {
        this.attackerFaction = attackerFaction;
    }

    public String getVictimClass() {
        return victimClass;
    }

    public void setVictimClass(String victimClass) {
        this.victimClass = victimClass;
    }
}
