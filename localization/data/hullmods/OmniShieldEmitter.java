package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class OmniShieldEmitter extends BaseHullMod {

	public static final float ARC_PENALTY = 33.3333f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getShieldArcBonus().modifyMult(id, 1f - ARC_PENALTY * 0.01f);
	}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield != null) {
			shield.setType(ShieldType.OMNI);
		}
	}

	
	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) ARC_PENALTY;
		if (index == 0) return "" + (int) ARC_PENALTY + "%";
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSpec().getShieldType() == ShieldType.OMNI && 
				!ship.getVariant().hasHullMod("frontemitter")) return false;
		if (ship.getVariant().getHullMods().contains("frontemitter")) return false;
		if (ship.getVariant().hasHullMod("adaptiveshields") && ship.getShield() != null) return true;
		return ship != null && ship.getShield() != null && ship.getShield().getType() == ShieldType.FRONT;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship == null || ship.getShield() == null) return "该舰没有护盾";
		
		if (ship.getShield().getType() == ShieldType.OMNI) { 
			return "该舰已经拥有全角护盾";
		}
		
		if (ship.getVariant().getHullMods().contains("frontemitter")) {
			return "与固化护盾发生器冲突";
		}
		
		return null;
	}
	
}
