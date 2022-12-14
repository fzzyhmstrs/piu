package fzzyhmstrs.pack_it_up.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PackInventory extends SimpleInventory {

    public PackInventory(int size, PackItem.StackPredicate stackPredicate){
        super(size);
        this.maxStack = stackPredicate == PackItem.StackPredicate.ANY ? 64 : 256;
        this.stackPredicate = stackPredicate;
    }
    private final int maxStack;
    final PackItem.StackPredicate stackPredicate;
    private final List<ItemStack> stacksToDrop = new ArrayList<>();

    public void dump(PlayerEntity player){
        for (int i = 0; i < size(); i++){
            player.getInventory().offerOrDrop(getStack(i).copy());
            setStack(i,ItemStack.EMPTY);
        }
        for (ItemStack stack: stacksToDrop){
            player.getInventory().offerOrDrop(stack);
        }
        stacksToDrop.clear();
    }

    public void validate(PlayerEntity player, PackItem.StackPredicate newPredicate){
        for (int i = 0; i < size(); i++){
            ItemStack test = getStack(i).copy();
            if (!newPredicate.test(test) || test.getCount() > maxStack){
                player.getInventory().offerOrDrop(test);
                setStack(i,ItemStack.EMPTY);
            }
        }
        for (ItemStack stack: stacksToDrop){
            player.getInventory().offerOrDrop(stack);
        }
        stacksToDrop.clear();
    }

    public NbtCompound toNbt(NbtCompound nbt){
        return toNbt(nbt,stackPredicate);
    }

    public NbtCompound toNbt(NbtCompound nbt, PackItem.StackPredicate predicate){
        predicate.toNbt(nbt);
        NbtList list = this.toNbtList();
        nbt.put("stack_contents",list);
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
            Identifier identifier = Registries.ITEM.getId(stack.getItem());
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
            Item item = Registries.ITEM.get(new Identifier(compound.getString("id")));
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
            if (i < this.size()) {
                this.setStack(slot, stack);
            } else {
                stacksToDrop.add(stack);
            }
        }
    }
}
