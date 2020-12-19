package stinkybot.utils.daybreakutils.event.listener;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import stinkybot.utils.daybreakutils.event.dto.AchievementEarned;
import stinkybot.utils.daybreakutils.event.dto.BattleRankUp;
import stinkybot.utils.daybreakutils.event.dto.ContinentLock;
import stinkybot.utils.daybreakutils.event.dto.ContinentUnlock;
import stinkybot.utils.daybreakutils.event.dto.Death;
import stinkybot.utils.daybreakutils.event.dto.FacilityControl;
import stinkybot.utils.daybreakutils.event.dto.GainExperience;
import stinkybot.utils.daybreakutils.event.dto.ItemAdded;
import stinkybot.utils.daybreakutils.event.dto.MetagameEvent;
import stinkybot.utils.daybreakutils.event.dto.PlayerFacilityCapture;
import stinkybot.utils.daybreakutils.event.dto.PlayerFacilityDefend;
import stinkybot.utils.daybreakutils.event.dto.PlayerLogin;
import stinkybot.utils.daybreakutils.event.dto.PlayerLogout;
import stinkybot.utils.daybreakutils.event.dto.SkillAdded;
import stinkybot.utils.daybreakutils.event.dto.VehicleDestroy;
import okhttp3.Response;

public abstract class EventStreamListener {

	//WebSocket Events
	public void onClosed(int code, String reason) {}
	public void onClosing(int code, String reason) {}
	public void onOpen(Response response) {}
	
	public void onFailure(Throwable t, Response r) {}
	public void onException(Throwable t) {}
	
	//Server Response Events
	public void onMessage(JsonNode node) {}
	public void onSubscriptionResponse(JsonNode node) {}
	public void onRecentCharIdListOrCount(JsonNode node) {}
	
	//Character Events
	public void onAchievmentEarned(AchievementEarned event) {}
	public void onBattleRankUp(BattleRankUp event) {}
	public void onDeath(Death event) {}
	public void onItemAdded(ItemAdded event) {}
	public void onSkillAdded(SkillAdded event) {}
	public void onVehicleDestroy(VehicleDestroy event) {}
	public void onGainExperience(GainExperience event) {}
	public void onPlayerFacilityCapture(PlayerFacilityCapture event) {}
	public void onPlayerFacilityDefend(PlayerFacilityDefend event) {}
	
	//Character Status Events
	public void onPlayerLogin(PlayerLogin event) {}
	public void onPlayerLogout(PlayerLogout event) {}
	
	//World Events
	public void onContinentLock(ContinentLock event) {}
	public void onContinentUnlock(ContinentUnlock event) {}
	public void onFacilityControl(FacilityControl event) {}
	public void onMetagameEvent(MetagameEvent event) {}
	
	
	
	public final void propagateMessage(JsonNode node) throws JsonParseException, JsonMappingException, IOException {
		//TODO: convert node to java object, then call respective eventhandler
		onMessage(node);
		if (node.has("subscription")) {
			onSubscriptionResponse(node.path("subscription"));
		} else if (node.has("recent_character_id_count") || node.has("recent_character_id_list")) {
			onRecentCharIdListOrCount(node.path("payload"));
		} else if (node.has("payload")){
			JsonNode payload = node.path("payload");

			switch(payload.path("event_name").asText()) {
			
			case "AchievementEarned":
				onAchievmentEarned(new ObjectMapper().readValue(payload.toString(), AchievementEarned.class));
				break;
			case "BattleRankUp":
				onBattleRankUp(new ObjectMapper().readValue(payload.toString(), BattleRankUp.class));
				break;
			case "Death":
				onDeath(new ObjectMapper().readValue(payload.toString(), Death.class));
				break;
			case "ItemAdded":
				onItemAdded(new ObjectMapper().readValue(payload.toString(), ItemAdded.class));
				break;
			case "VehicleDestroy":
				onVehicleDestroy(new ObjectMapper().readValue(payload.toString(), VehicleDestroy.class));
				break;
			case "GainExperience":
				onGainExperience(new ObjectMapper().readValue(payload.toString(), GainExperience.class));
				break;
			case "PlayerFacilityCapture":
				onPlayerFacilityCapture(new ObjectMapper().readValue(payload.toString(), PlayerFacilityCapture.class));
				break;
			case "PlayerFacilityDefend":
				onPlayerFacilityDefend(new ObjectMapper().readValue(payload.toString(), PlayerFacilityDefend.class));
				break;
			case "PlayerLogin":
				onPlayerLogin(new ObjectMapper().readValue(payload.toString(), PlayerLogin.class));
				break;
			case "PlayerLogout":
				onPlayerLogout(new ObjectMapper().readValue(payload.toString(), PlayerLogout.class));
				break;
			case "ContinentLock":
				onContinentLock(new ObjectMapper().readValue(payload.toString(), ContinentLock.class));
				break;
			case "ContinentUnlock":
				onContinentUnlock(new ObjectMapper().readValue(payload.toString(), ContinentUnlock.class));
				break;
			case "FacilityControl":
				onFacilityControl(new ObjectMapper().readValue(payload.toString(), FacilityControl.class));
				break;
			case "MetagameEvent":
				onMetagameEvent(new ObjectMapper().readValue(payload.toString(), MetagameEvent.class));
				break;
			default:
				break;
			}
		}
	}
}
