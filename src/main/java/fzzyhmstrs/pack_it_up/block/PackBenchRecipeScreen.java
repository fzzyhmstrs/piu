package fzzyhmstrs.pack_it_up.block;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class PackBenchRecipeScreen extends Screen {

    protected PackBenchRecipeScreen(Screen oldScreen) {
        super(Text.translatable("pack_it_up.recipes"));
        this.oldScreen = oldScreen;
    }

    private final Screen oldScreen;

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(oldScreen);
        }
    }
}
