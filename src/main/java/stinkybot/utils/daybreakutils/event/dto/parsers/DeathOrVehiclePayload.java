package stinkybot.utils.daybreakutils.event.dto.parsers;

public class DeathOrVehiclePayload {

    private DeathOrVehicleDestroy payload;

    public DeathOrVehiclePayload(){}

    public DeathOrVehicleDestroy getPayload() {
        return payload;
    }

    public void setPayload(DeathOrVehicleDestroy payload) {
        this.payload = payload;
    }
}
