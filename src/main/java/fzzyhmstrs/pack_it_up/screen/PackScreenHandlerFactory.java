package fzzyhmstrs.pack_it_up.screen;

import fzzyhmstrs.pack_it_up.item.PackInventory;
import fzzyhmstrs.pack_it_up.item.PackItem;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class PackScreenHandlerFactory implements ExtendedScreenHandlerFactory {

    public PackScreenHandlerFactory(Map<String,String> map, ItemStack stack, PackInventory inventory){
        this.map = map;
        this.stack = stack;
        this.size = inventory.size();
        this.inventory = inventory;
    }
    Map<String,String> map;
    ItemStack stack;
    int size;
    PackInventory inventory;

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeByte(map.size());
        for (Map.Entry<String,String> entry : map.entrySet()){
            buf.writeString(entry.getKey());
            buf.writeString(entry.getValue());
            buf.writeBoolean(!Objects.equals(entry.getKey(), PackItem.FRAME_ID));
        }
        buf.writeItemStack(stack);
        buf.writeByte(size);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("pack_it_up.pack_bench_handler");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PackScreenHandler(syncId,inv,map,stack,inventory, ScreenHandlerContext.create(player.world,player.getBlockPos()));
    }
}
