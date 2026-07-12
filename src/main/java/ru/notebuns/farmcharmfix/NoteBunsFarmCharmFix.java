package ru.notebuns.farmcharmfix;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(NoteBunsFarmCharmFix.MOD_ID)
public final class NoteBunsFarmCharmFix {
    public static final String MOD_ID = "notebuns_farmcharm_fix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public NoteBunsFarmCharmFix(IEventBus modBus) {
        LOGGER.info("NoteBuns FarmCharm Fix loaded — Cooking Pot empty-container consume patch active");
    }
}
