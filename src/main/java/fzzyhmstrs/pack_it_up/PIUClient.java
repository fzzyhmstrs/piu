package fzzyhmstrs.pack_it_up;


import fzzyhmstrs.pack_it_up.block.PackBenchScreen;
import fzzyhmstrs.pack_it_up.item.PackScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class PIUClient implements ClientModInitializer {

    public static String MOD_ID = "pack_it_up";

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PIU.PACK_SCREEN_HANDLER, PackScreen::new);
        HandledScreens.register(PIU.PACK_BENCH_SCREEN_HANDLER, PackBenchScreen::new);
    }
}
