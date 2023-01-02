package fzzyhmstrs.pack_it_up.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PackScreenHandlerFactory implements ExtendedScreenHandlerFactory {

    public PackScreenHandlerFactory(Inventory inventory, PackItem.ModuleTier tier, ItemStack stack, int index){
        this.inventory = inventory;
        this.tier = tier;
        this.index = index;
        this.stack = stack;
    }

    private final Inventory inventory;
    private final PackItem.ModuleTier tier;
    private final int index;
    private final ItemStack stack;

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeByte(tier.id);
        if (tier != PackItem.ModuleTier.ENDER){
            buf.writeByte(tier.slots);
            ((PackInventory)inventory).stackPredicate.toBuf(buf);
        }
        buf.writeByte(tier.height);
        buf.writeByte(index);
        buf.writeByte(index);
        buf.writeByte(index);
    }

    @Override
    public Text getDisplayName() {
        return stack.getName();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PackScreenHandler(syncId,inv,inventory,tier.height,stack,index, ScreenHandlerContext.create(player.world, BlockPos.ORIGIN));
    }
}
