package fzzyhmstrs.pack_it_up.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    private int pack_it_up_removeStackMaxCountFromMin(int a, int b){
        return b;
    }

}
