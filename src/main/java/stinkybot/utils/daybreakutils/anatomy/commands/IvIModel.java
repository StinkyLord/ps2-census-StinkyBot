package stinkybot.utils.daybreakutils.anatomy.commands;

import stinkybot.utils.daybreakutils.query.dto.internal.Item;

public class IvIModel implements Comparable<IvIModel> {
    String itemId;
    float headshots;
    float kills;
    float fireCount;
    float hitCount;
    Item item;
    float iviScore;

    String weaponName;
    String weaponDesc;

    public IvIModel(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getHeadshots() {
        return headshots;
    }

    public void setHeadshots(float headshots) {
        this.headshots = headshots;
    }

    public float getKills() {
        return kills;
    }

    public void setKills(float kills) {
        this.kills = kills;
    }

    public float getFireCount() {
        return fireCount;
    }

    public void setFireCount(float fireCount) {
        this.fireCount = fireCount;
    }

    public float getHitCount() {
        return hitCount;
    }

    public void setHitCount(float hitCount) {
        this.hitCount = hitCount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setIvIScore(float iviScore) {
        this.iviScore = iviScore;
    }

    public float getIvIScore() {
        return iviScore;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public String getWeaponDesc() {
        return weaponDesc;
    }

    public void setWeaponDesc(String weaponDesc) {
        this.weaponDesc = weaponDesc;
    }

    @Override
    public int compareTo(IvIModel other) {
        return Float.compare(other.getIvIScore(), getIvIScore());
    }
}
