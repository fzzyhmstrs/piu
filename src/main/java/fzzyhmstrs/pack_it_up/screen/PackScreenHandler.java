package fzzyhmstrs.pack_it_up.screen;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.PackInventory;
import fzzyhmstrs.pack_it_up.item.PackModuleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class PackScreenHandler extends ScreenHandler {


    public PackScreenHandler(int syncId, PlayerInventory playerInventory, Map<String,String> map, ItemStack stack, PackInventory inventory, ScreenHandlerContext context) {
        super(PIU.PACK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.player = playerInventory.player;
        this.map = map;
        this.stack = stack;
        this.inventory = inventory;
        this.context = context;

    }

    public PackScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(syncId,playerInventory, generateModuleMaps(buf),buf.readItemStack(),new PackInventory(packInvMap, playerInventory.player),ScreenHandlerContext.EMPTY);
    }

    private final PlayerInventory playerInventory;
    private final PlayerEntity player;
    private final Map<String,String> map;
    private final ItemStack stack;
    private final PackInventory inventory;
    private final ScreenHandlerContext context;
    private static Map<String, PackModuleItem> packInvMap = new HashMap<>();


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private static Map<String,String> generateModuleMaps(PacketByteBuf buf){
        Map<String,String> map = new HashMap<>();
        Map<String, PackModuleItem> map2 = new HashMap<>();
        int size = buf.readByte();
        for (int i = 0; i < size; i++){
            String str1 = buf.readString();
            String str2 = buf.readString();
            map.put(str1,str2);
            boolean bl1 = buf.readBoolean();
            if (bl1){
                Item item = Registry.ITEM.get(new Identifier(str2));
                if (item instanceof PackModuleItem){
                    map2.put(str1,(PackModuleItem) item);
                }
            }
        }
        packInvMap = map2;
        return map;
    }

}
