package fzzyhmstrs.pack_it_up;


import fzzyhmstrs.pack_it_up.block.PackBenchScreen;
import fzzyhmstrs.pack_it_up.item.PackScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class PIUClient implements ClientModInitializer {

    public static String MOD_ID = "pack_it_up";
    KeyBinding BACKPACK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.pack_it_up.backpack_key", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B,KeyBinding.GAMEPLAY_CATEGORY));

    @Override
    public void onInitializeClient() {
        HandledScreens.register(PIU.PACK_SCREEN_HANDLER, PackScreen::new);
        HandledScreens.register(PIU.PACK_BENCH_SCREEN_HANDLER, PackBenchScreen::new);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean wasPressed = false;
            while (BACKPACK_KEY.wasPressed()){
                wasPressed = true;
            }
            if (wasPressed){
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(true);
                ClientPlayNetworking.send(PIU.OPEN_BACKPACK,buf);
            }
        });
    }
}
