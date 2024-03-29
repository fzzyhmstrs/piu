package fzzyhmstrs.pack_it_up.item;

import com.mojang.blaze3d.systems.RenderSystem;
import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PackScreen extends HandledScreen<PackScreenHandler> {

    public PackScreen(PackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.rows = handler.rows;
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    private static final Identifier TEXTURE = new Identifier(PIU.MOD_ID,"textures/gui/pack_screen.png");
    private final int rows;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        context.drawTexture(TEXTURE, i, j + this.rows * 18 + 17, 0, 144, this.backgroundWidth, 96);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.1f,0.5f));
        }
        super.close();
    }
}
