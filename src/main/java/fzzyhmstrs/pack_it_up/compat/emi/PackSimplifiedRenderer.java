package fzzyhmstrs.pack_it_up.compat.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.render.EmiRenderable;
import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PackSimplifiedRenderer implements EmiRenderable {

    private final Identifier SPRITE_SHEET = new Identifier(PIU.MOD_ID,"textures/gui/emi_recipe_texture.png");

    public PackSimplifiedRenderer(){
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, float delta) {
        RenderSystem.setShaderTexture(0, SPRITE_SHEET);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 16, 16, 16, 16);
    }
}
