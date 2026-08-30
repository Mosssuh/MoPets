package mv.mossuh.mopets.MODEL.Pets.Actions.MoBoosters;

import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.UTILITIES.Enums.MultiplierType;

public class LocalBooster {
    private MultiplierType multiplierType = MultiplierType.NONE;
    private BoosterIdentifier identifier = new BoosterIdentifier();
    private double boost = 0;

    public LocalBooster(BoosterIdentifier identifier, MultiplierType multiplierType, Double boost) {
        if (multiplierType != null) { this.multiplierType = multiplierType; }
        if (identifier != null) { this.identifier = identifier; }
        if (boost != null) {
            if (boost < 0) {
                this.boost = 0;
            } else {
                this.boost = boost;
            }
        }
    }

    public MultiplierType getMultiplierType() { return multiplierType; }
    public BoosterIdentifier getIdentifier() { return identifier; }
    public double getBoost() { return boost; }
    public double getBoost(int multiplier) {
        if (multiplierType == MultiplierType.BOOST_PER_LEVEL) {
            return boost * multiplier;
        }
        return boost;
    }

    public boolean isValid() {
        return identifier.getBoosterType() != BoosterType.NONE
                && identifier.getApplicatorType() != ApplicatorType.NONE
                && !identifier.getBoosted().isEmpty()
                && multiplierType != MultiplierType.NONE;
    }
}
