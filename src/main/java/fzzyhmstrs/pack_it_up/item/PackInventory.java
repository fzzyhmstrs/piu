package fzzyhmstrs.pack_it_up.item;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedList;
import java.util.List;

public class PackInventory extends SimpleInventory {

    public PackInventory(int size, PackItem.StackPredicate stackPredicate){
        super(size);
        this.maxStack = stackPredicate == PackItem.StackPredicate.ANY ? 64 : 256;
        this.stackPredicate = stackPredicate;
    }
    private final int maxStack;
    final PackItem.StackPredicate stackPredicate;

    public List<ItemStack> getStacks(){
        List<ItemStack> list = new LinkedList<>();
        for (int i = 0; i < size(); i++){
            list.add(getStack(i).copy());
        }
        return list;
    }

    public NbtCompound toNbt(NbtCompound nbt){
        System.out.println("A");
        stackPredicate.toNbt(nbt);
        System.out.println(nbt);
        System.out.println("B");
        NbtList list = this.toNbtList();
        System.out.println(list);
        System.out.println("C");
        nbt.put("stack_contents",list);
        System.out.println("D");
        return nbt;
    }

    public static PackInventory fromNbt(int size, NbtCompound nbt){
        PackItem.StackPredicate predicate = PackItem.StackPredicate.fromNbt(nbt);
        PackInventory inventory = new PackInventory(size,predicate);
        if (nbt.contains("stack_contents")) {
            NbtList list = nbt.getList("stack_contents", 10);
            inventory.readNbtList(list);
        }
        return inventory;
    }

    @Override
    public int getMaxCountPerStack() {
        return maxStack;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stackPredicate.test(stack) && super.canInsert(stack);
    }

    @Override
    public NbtList toNbtList() {
        NbtList list = new NbtList();
        for (int i = 0; i < this.size(); i++){
            NbtCompound compound = new NbtCompound();
            ItemStack stack = this.getStack(i);
            compound.putShort("slot", (short)i);
            compound.putShort("count",(short)stack.getCount());
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
            int count = compound.getShort("count");
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
            int slot = compound.getShort("slot");
            this.setStack(slot,stack);
        }
    }
}
