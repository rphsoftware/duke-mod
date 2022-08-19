package fi.pfef.dukemod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;


public class DukeMod implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    public static ArrayList<Identifier> list = new ArrayList<>();
    @Override
    public void onInitialize() {
        LOGGER.info("Prepare for trouble...");
    }
}
