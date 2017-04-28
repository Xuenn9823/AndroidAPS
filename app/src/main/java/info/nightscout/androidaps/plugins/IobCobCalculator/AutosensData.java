package info.nightscout.androidaps.plugins.IobCobCalculator;

import java.util.Date;

/**
 * Created by mike on 25.04.2017.
 */

public class AutosensData {
    String pastSensitivity = "";
    double deviation = 0d;
    double absorbed = 0d;
    double carbsFromBolus = 0d;
    public double cob = 0;

    public String log(long time) {
        return "AutosensData: " + new Date(time).toLocaleString() + " " + pastSensitivity + " Deviation=" + deviation + " Absorbed=" + absorbed + " CarbsFromBolus=" + carbsFromBolus + " COB=" + cob;
    }

}