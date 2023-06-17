package fzzyhmstrs.pack_it_up.compat.emi;

import dev.emi.emi.api.render.EmiRenderable;
import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class PackSimplifiedRenderer implements EmiRenderable {

    private final Identifier SPRITE_SHEET = new Identifier(PIU.MOD_ID,"textures/gui/emi_recipe_texture.png");

    public PackSimplifiedRenderer(){
    }

    @Override
    public void render(DrawContext matrices, int x, int y, float delta) {
        matrices.drawTexture(SPRITE_SHEET, x, y, 0, 0, 16, 16, 16, 16);
    }
}
