package fzzyhmstrs.pack_it_up.item;

import fzzyhmstrs.pack_it_up.registry.RegisterItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Predicate;

public class PackInventory implements Inventory {

    public PackInventory(){
    }

    public PackInventory(Map<String, PackModuleItem> map, PlayerEntity player){
        for (Map.Entry<String, PackModuleItem> entry : map.entrySet()){
            addModule(entry.getKey(), entry.getValue(), player);
        }
    }

    private final Map<String,Inventory> modules = new HashMap<>(4,0.75f);
    private final Map<String, IntRange> moduleSlots = new HashMap<>(4,0.75f);
    private int size = 0;
    private final Inventory fallback = new SimpleInventory(0);

    public SimpleInventory addModule(String id, PackModuleItem newModule, PlayerEntity player){
        int newCapacity = newModule.getModuleCapacity(Objects.equals(id, PackItem.SIDE_MODULE_ID));
        size += newCapacity;
        SimpleInventory inv;
        if (newModule.getTier() == PackModuleItem.ModuleTier.ENDER){
            inv = player.getEnderChestInventory();
        } else {
            inv = new ModuleInventory(newCapacity, newModule.getMaxStackCount(), newModule.getPredicate(), newModule);
        }
        modules.put(id, inv);
        updateModuleSlots();
        return inv;
    }

    public List<ItemStack> removeModule(String id, PackModuleItem oldModule){
        int oldCapacity = oldModule.getModuleCapacity(Objects.equals(id, PackItem.SIDE_MODULE_ID));
        size -= oldCapacity;
        List<ItemStack> stacks = new LinkedList<>();
        Inventory inv = modules.get(id);
        if (inv instanceof ModuleInventory mInv) {
            stacks = mInv.getStacks();

        }
        if (inv != null) {
            modules.remove(id);
            updateModuleSlots();
        }
        return stacks;
    }

    private void updateModuleSlots(){
        int mainModule2Start = 0;
        if (modules.containsKey(PackItem.MAIN_MODULE_ID)){
            int size = modules.getOrDefault(PackItem.MAIN_MODULE_ID,fallback).size();
            moduleSlots.put(PackItem.MAIN_MODULE_ID, new IntRange(0, size - 1));
            mainModule2Start = size;
        } else {
            moduleSlots.remove(PackItem.MAIN_MODULE_ID);
        }
        int sideModuleStart = mainModule2Start;
        if (modules.containsKey(PackItem.MAIN_MODULE_2_ID)){
            int size = modules.getOrDefault(PackItem.MAIN_MODULE_2_ID,fallback).size();
            moduleSlots.put(PackItem.MAIN_MODULE_2_ID, new IntRange(mainModule2Start, mainModule2Start + size - 1));
            sideModuleStart = mainModule2Start + size;
        } else {
            moduleSlots.remove(PackItem.MAIN_MODULE_2_ID);
        }
        if (modules.containsKey(PackItem.SIDE_MODULE_ID)){
            int size = modules.getOrDefault(PackItem.SIDE_MODULE_ID,fallback).size();
            moduleSlots.put(PackItem.SIDE_MODULE_ID, new IntRange(sideModuleStart, sideModuleStart + size - 1));
        } else {
            moduleSlots.remove(PackItem.SIDE_MODULE_ID);
        }
    }


    public static boolean hasPackNbt(NbtCompound nbtCompound){
        return nbtCompound.contains("pack_inventory_list");
    }

    public static PackInventory fromNbt(NbtCompound nbtCompound, PlayerEntity player){
        NbtList list = nbtCompound.getList("pack_inventory_list",10);
        PackInventory inventory = new PackInventory();
        for (NbtElement nbt : list){
            if (nbt instanceof NbtCompound compound){
                String id = compound.getString("module_slot_id");
                String itemId = compound.getString("module_item_id");
                Item item = Registry.ITEM.get(new Identifier(itemId));
                if (item instanceof PackModuleItem){
                    SimpleInventory inv = inventory.addModule(id,(PackModuleItem) item, player);
                    inv.readNbtList(compound.getList("module_contents",10));
                }
            }
        }
        return inventory;
    }

    public NbtCompound toNbt(NbtCompound nbtCompound){
        NbtList list = new NbtList();
        for (Map.Entry<String,Inventory> entry : modules.entrySet()){
            NbtCompound compound = new NbtCompound();
            compound.putString("module_slot_id", entry.getKey());
            SimpleInventory inv = (SimpleInventory) entry.getValue();
            if (inv instanceof ModuleInventory mInv){
                compound.putString("module_item_id",Registry.ITEM.getId(mInv.item).toString());
            } else {
                compound.putString("module_item_id", Registry.ITEM.getId(RegisterItem.ENDER_BAG).toString());
            }
            NbtList contents = inv.toNbtList();
            compound.put("module_contents",contents);
            list.add(compound);
        }
        nbtCompound.put("pack_inventory_list",list);
        return nbtCompound;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        boolean bl1 = modules.isEmpty();
        if (bl1) return true;
        for (Inventory inv : modules.values()){
            if (!inv.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        for (Map.Entry<String,IntRange> entry: moduleSlots.entrySet()){
            if (entry.getValue().test(slot)){
                return modules.getOrDefault(entry.getKey(), fallback).getStack(slot);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        for (Map.Entry<String,IntRange> entry: moduleSlots.entrySet()){
            if (entry.getValue().test(slot)){
                return modules.getOrDefault(entry.getKey(), fallback).removeStack(slot,amount);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        for (Map.Entry<String,IntRange> entry: moduleSlots.entrySet()){
            if (entry.getValue().test(slot)){
                return modules.getOrDefault(entry.getKey(), fallback).removeStack(slot);
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean canInsert(ItemStack stack) {
        Inventory inv;
        inv = modules.get(PackItem.MAIN_MODULE_ID);
        if (((SimpleInventory) inv).canInsert(stack)) return true;
        inv = modules.get(PackItem.MAIN_MODULE_2_ID);
        if (((SimpleInventory) inv).canInsert(stack)) return true;
        inv = modules.get(PackItem.SIDE_MODULE_ID);
        return ((SimpleInventory) inv).canInsert(stack);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        for (Map.Entry<String,IntRange> entry: moduleSlots.entrySet()){
            if (entry.getValue().test(slot)){
                modules.getOrDefault(entry.getKey(), fallback).setStack(slot, stack);
            }
        }
    }

    @Override
    public int getMaxCountPerStack() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        modules.clear();
        moduleSlots.clear();
    }

    private static class ModuleInventory extends SimpleInventory{

        public ModuleInventory(int size, int maxStack, Predicate<ItemStack> stackPredicate, PackModuleItem item){
            super(size);
            this.maxStack = maxStack;
            this.stackPredicate = stackPredicate;
            this.item = item;
        }
        private final int maxStack;
        private final Predicate<ItemStack> stackPredicate;
        private final Item item;

        public List<ItemStack> getStacks(){
            List<ItemStack> list = new LinkedList<>();
            for (int i = 0; i < size(); i++){
                list.add(getStack(i).copy());
            }
            return list;
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

    private record IntRange(int min, int max) {

        public boolean test(int test) {
            return test >= min || test <= max;
        }

    }
}
