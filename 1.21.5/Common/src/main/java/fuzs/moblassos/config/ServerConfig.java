package fuzs.moblassos.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = {"Time in seconds for which a golden lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int goldenLassoTime = 120;
    @Config(description = {"Time in seconds for which an aqua lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int aquaLassoTime = 240;
    @Config(description = {"Time in seconds for which a diamond lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int diamondLassoTime = -1;
    @Config(description = {"Time in seconds for which an emerald lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int emeraldLassoTime = 300;
    @Config(description = {"Time in seconds for which a hostile lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int hostileLassoTime = 180;
    @Config(description = {"Time in seconds for which a creative lasso is able to hold a mob.", "Set to -1 to disable the timer."})
    @Config.IntRange(min = -1, max = 3600)
    public int creativeLassoTime = -1;
    @Config(description = "Percentage of its total health or less a hostile mob must have in order for a hostile lasso to be able to pick it up.")
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double hostileMobHealth = 0.5;
    @Config(description = {"Time interval in seconds after which the player is hurt by half a heart for carrying any hostile lasso containing a monster.", "Set to -1 to disable."})
    @Config.IntRange(min = -1, max = 3600)
    public int hostileDamageRate = 5;
    @Config(description = "Capturing a villager in an emerald lasso requires it to accept a contract.")
    public boolean villagersRequireContract = true;
    @Config(description = "The percentage each level of the holding enchantment will increase a lasso's holding time by.")
    @Config.DoubleRange(min = 0.0, max = 100.0)
    public double holdingMultiplier = 0.2;
}
