package fzzyhmstrs.pack_it_up.screen;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.PackInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class PackBenchScreenHandler extends ScreenHandler {


    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, Map<String,String> map, ItemStack stack, PackInventory inventory, ScreenHandlerContext context) {
        super(PIU.PACK_BENCH_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.map = map;
        this.stack = stack;
        this.inventory = inventory;
        this.context = context;
    }

    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(syncId,playerInventory,generateMap(buf),buf.readItemStack(),new PackInventory(buf.readByte()),ScreenHandlerContext.EMPTY);
    }

    private final PlayerInventory playerInventory;
    private final Map<String,String> map;
    private final ItemStack stack;
    private final PackInventory inventory;
    private final ScreenHandlerContext context;


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private static Map<String,String> generateMap(PacketByteBuf buf){
        Map<String,String> map = new HashMap<>();
        int size = buf.readByte();
        for (int i = 0; i < size; i++){
            String str1 = buf.readString();
            String str2 = buf.readString();
            map.put(str1,str2);
        }
        return map;
    }
}
