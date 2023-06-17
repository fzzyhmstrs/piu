package fzzyhmstrs.pack_it_up.block;

import com.mojang.blaze3d.systems.RenderSystem;
import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PackBenchScreen extends HandledScreen<PackBenchScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(PIU.MOD_ID,"textures/gui/pack_bench.png");

    public PackBenchScreen(PackBenchScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.titleX = 60;
        this.titleY = 18;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        ForgingScreen.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.drawInvalidRecipeArrow(matrices, this.x, this.y);
    }


    protected void drawInvalidRecipeArrow(MatrixStack matrices, int x, int y) {
        if (((this.handler).getSlot(0).hasStack() || (this.handler).getSlot(1).hasStack()) && !(this.handler).getSlot(2).hasStack()) {
            drawTexture(matrices, x + 99, y + 45, this.backgroundWidth, 0, 28, 21);
        }
    }
}
