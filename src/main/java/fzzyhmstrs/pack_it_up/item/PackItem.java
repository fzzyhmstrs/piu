package fzzyhmstrs.pack_it_up.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class PackItem extends Item {

    public PackItem(Settings settings) {
        super(settings);
    }

    public static final String FRAME_ID = "frame_id";
    public static final String MAIN_MODULE_ID = "main_module_id";
    public static final String MAIN_MODULE_2_ID = "main_module_2_id";
    public static final String SIDE_MODULE_ID = "side_module_id";
    private static final String INVENTORY = "pack_inventory";


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.fail(stack);
        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            if (PackInventory.hasPackNbt(nbt)) {
                PackInventory inventory = PackInventory.fromNbt(nbt, user);
                Map<String,String> map = getModules(stack);
                //open screen with inventory, stack, and map
                return TypedActionResult.success(stack);
            }
        }
        return super.use(world, user, hand);
    }

    public static Map<String,String> getModules(ItemStack stack){
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return Map.of();
        Map<String,String> map = new HashMap<>(6,0.66f);
        if (nbt.contains(FRAME_ID)){
            map.put(FRAME_ID,nbt.getString(FRAME_ID));
        }
        if (nbt.contains(MAIN_MODULE_ID)){
            map.put(MAIN_MODULE_ID,nbt.getString(MAIN_MODULE_ID));
        }
        if (nbt.contains(MAIN_MODULE_2_ID)){
            map.put(MAIN_MODULE_2_ID,nbt.getString(MAIN_MODULE_2_ID));
        }
        if (nbt.contains(SIDE_MODULE_ID)){
            map.put(SIDE_MODULE_ID,nbt.getString(SIDE_MODULE_ID));
        }
        return map;
    }

    public static void writeModules(Item f, Item m1, Item m2, Item sm, ItemStack pack){
        NbtCompound nbt = pack.getOrCreateNbt();
        if (f instanceof PackFrameItem) {
            nbt.putString(FRAME_ID, Registry.ITEM.getId(f).toString());
        }
        if (m1 instanceof PackModuleItem) {
            nbt.putString(MAIN_MODULE_ID, Registry.ITEM.getId(m1).toString());
        }
        if (m2 instanceof PackModuleItem) {
            nbt.putString(MAIN_MODULE_2_ID, Registry.ITEM.getId(m2).toString());
        }
        if (sm instanceof PackModuleItem) {
            nbt.putString(SIDE_MODULE_ID, Registry.ITEM.getId(sm).toString());
        }
    }

}
