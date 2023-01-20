package fzzyhmstrs.pack_it_up.mixins;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Redirect(method = "internalOnSlotClick", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerInventory.setStack (ILnet/minecraft/item/ItemStack;)V", ordinal = 0))
    private void pack_it_up_fixScreenHandlerSwapBehavior(PlayerInventory instance, int slot, ItemStack stack){
        if (stack.getCount() > instance.getMaxCountPerStack()){
            ItemStack stack1 = stack.split(instance.getMaxCountPerStack());
            instance.setStack(slot,stack1);
            while (stack.getCount() > instance.getMaxCountPerStack()){
                ItemStack stack2 = stack.split(instance.getMaxCountPerStack());
                instance.offerOrDrop(stack2);
            }
            instance.offerOrDrop(stack);
        } else {
            instance.setStack(slot, stack);
        }
    }

}
