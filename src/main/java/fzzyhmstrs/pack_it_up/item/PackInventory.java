package fzzyhmstrs.pack_it_up.item;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PackInventory extends SimpleInventory {

    public PackInventory(int size){
        super(size);
    }

    public static boolean hasPackNbt(NbtCompound nbtCompound){
        return nbtCompound.contains("size") && nbtCompound.contains("list");
    }

    public static PackInventory fromNbt(NbtCompound nbtCompound){
        int size = nbtCompound.getShort("size");
        NbtList list = nbtCompound.getList("list",10);
        PackInventory inventory = new PackInventory(size);
        inventory.readNbtList(list);
        return inventory;
    }

    public NbtCompound toNbt(){
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putShort("size",(short)this.size());
        nbtCompound.put("list",toNbtList());
        return nbtCompound;
    }

    @Override
    public NbtList toNbtList() {
        NbtList list = new NbtList();
        for (int i = 0; i < this.size(); i++){
            NbtCompound compound = new NbtCompound();
            ItemStack stack = this.getStack(i);
            compound.putInt("slot", i);
            compound.putInt("count",stack.getCount());
            Identifier identifier = Registry.ITEM.getId(stack.getItem());
            compound.putString("id",identifier.toString());
            if (stack.getNbt() != null){
                compound.put("tag",stack.getNbt().copy());
            }
            list.add(compound);
        }
        return list;
    }

    @Override
    public void readNbtList(NbtList nbtList) {
        for (int i = 0; i < nbtList.size(); i++){
            NbtCompound compound = nbtList.getCompound(i);
            int count = compound.getInt("count");
            Item item = Registry.ITEM.get(new Identifier(compound.getString("id")));
            ItemStack stack = new ItemStack(item,count);
            if (compound.contains("tag", NbtElement.COMPOUND_TYPE)){
                NbtCompound itemNbt = compound.getCompound("tag");
                item.postProcessNbt(itemNbt);
                stack.setNbt(itemNbt);
            }
            if (item.isDamageable()){
                stack.setDamage(stack.getDamage());
            }
            int slot = compound.getInt("slot");
            this.setStack(slot,stack);
        }
    }
}
