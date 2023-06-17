package fzzyhmstrs.pack_it_up.block;

import com.mojang.blaze3d.systems.RenderSystem;
import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.client.gui.DrawContext;
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.drawInvalidRecipeArrow(context, this.x, this.y);
    }


    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
        if (((this.handler).getSlot(0).hasStack() || (this.handler).getSlot(1).hasStack()) && !(this.handler).getSlot(2).hasStack()) {
            context.drawTexture(TEXTURE, x + 99, y + 45, this.backgroundWidth, 0, 28, 21);
        }
    }
}
