package fzzyhmstrs.pack_it_up.screen;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.PackFrameItem;
import fzzyhmstrs.pack_it_up.item.PackInventory;
import fzzyhmstrs.pack_it_up.item.PackItem;
import fzzyhmstrs.pack_it_up.item.PackModuleItem;
import fzzyhmstrs.pack_it_up.registry.RegisterItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class PackBenchScreenHandler extends ScreenHandler {


    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PIU.PACK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.context = context;

        this.addSlot(new PackBenchBagSlot(inputs, 0, 47, 16, false));
        this.addSlot(new PackBenchBagSlot(inputs, 1, 47, 34, false));
        this.addSlot(new PackBenchBagSlot(inputs, 2, 27, 54, false));
        this.addSlot(new PackBenchFrameSlot(inputs, 3, 47, 54));

        this.addSlot(new PackBenchSlot(output, 3, 124, 35));

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        packInLimbo.set(0);
    }
    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId,playerInventory,ScreenHandlerContext.EMPTY);
    }

    private final PlayerInventory playerInventory;
    private final ScreenHandlerContext context;
    private final Property packInLimbo = Property.create();
    private final List<ItemStack> stacksInLimbo = new LinkedList<>();
    private ItemStack packStackInLimbo = ItemStack.EMPTY;
    private int lastSlotUpdated = -1;
    private final PackBenchInventory inputs = new PackBenchInputInventory(){
        @Override
        public void markDirty() {
            super.markDirty();
            PackBenchScreenHandler.this.onContentChanged(this);
        }
    };
    private final PackBenchInventory output = new PackBenchOutputInventory(){
        @Override
        public void markDirty() {
            super.markDirty();
            PackBenchScreenHandler.this.onContentChanged(this);
        }
    };


    @Override
    public void onContentChanged(Inventory inventory) {
        PackBenchSlot slot0 = (PackBenchSlot) this.slots.get(0);
        PackBenchSlot slot1 = (PackBenchSlot) this.slots.get(1);
        PackBenchSlot slot2 = (PackBenchSlot) this.slots.get(2);
        PackBenchSlot slot3 = (PackBenchSlot) this.slots.get(3);
        ItemStack packStack = output.getStack(0);
        Map<String,String> map = PackItem.getModules(packStack);
        String module1 = map.getOrDefault(PackItem.MAIN_MODULE_ID,"air");
        String module2 = map.getOrDefault(PackItem.MAIN_MODULE_2_ID,"air");
        String sideModule = map.getOrDefault(PackItem.SIDE_MODULE_ID,"air");
        String frame = map.getOrDefault(PackItem.FRAME_ID,"air");
        Item frameItem = Registry.ITEM.get(new Identifier(frame));
        Item module1Item = Registry.ITEM.get(new Identifier(module1));
        Item module2Item = Registry.ITEM.get(new Identifier(module2));
        Item sideModuleItem = Registry.ITEM.get(new Identifier(sideModule));

        if(inventory == this.output){
            ItemStack stack = inventory.getStack(0);
            if (stack.isEmpty()) {
                inputs.clear();
                super.onContentChanged(inventory);
                return;
            }
            if (frameItem instanceof PackFrameItem pfi){
                boolean sideSlot = pfi.getMaxTier().suitableForSideSlot();
                int mainModules = pfi.getMaxMainModules();
                slot2.setEnabled(sideSlot);
                slot0.setEnabled(mainModules > 1);
                if (slot3.hasStack()){
                    ItemStack oldFrame = slot3.getStack().copy();
                    slot3.voidStack();
                    playerInventory.offerOrDrop(oldFrame);
                }
                slot3.addStack(new ItemStack(frameItem));
            } else{
                slot2.setEnabled(true);
                slot0.setEnabled(true);
            }
            if (module1Item instanceof PackModuleItem){
                if (slot1.hasStack()){
                    ItemStack oldModule1 = slot1.getStack().copy();
                    slot1.voidStack();
                    playerInventory.offerOrDrop(oldModule1);
                }
                slot1.addStack(new ItemStack(module1Item));
            }
            if (module2Item instanceof PackModuleItem){
                if (slot0.hasStack()){
                    ItemStack oldModule2 = slot0.getStack().copy();
                    slot0.voidStack();
                    playerInventory.offerOrDrop(oldModule2);
                }
                slot0.addStack(new ItemStack(module2Item));
            }
            if (sideModuleItem instanceof PackModuleItem){
                if (slot2.hasStack()){
                    ItemStack oldSideModule = slot2.getStack().copy();
                    slot2.voidStack();
                    playerInventory.offerOrDrop(oldSideModule);
                }
                slot2.addStack(new ItemStack(sideModuleItem));
            }

        } else if (inventory == this.inputs){
            ItemStack frameStack = inventory.getStack(3);
            ItemStack module1Stack = inventory.getStack(1);
            ItemStack module2Stack = inventory.getStack(0);
            ItemStack sideModuleStack = inventory.getStack(2);
            if (!frameStack.isEmpty()){
                PackFrameItem pfi = (PackFrameItem) frameStack.getItem();
                boolean sideSlot = pfi.getMaxTier().suitableForSideSlot();
                int mainModules = pfi.getMaxMainModules();
                slot2.setEnabled(sideSlot);
                slot0.setEnabled(mainModules > 1);
            } else {
                slot2.setEnabled(true);
                slot0.setEnabled(true);
            }


        }
        super.onContentChanged(inventory);
    }


    private Pair<Boolean,ItemStack> updatePackItem(
            ItemStack packItem,
            Item inputFItem, Item inputM1Item, Item inputM2Item, Item inputSMItem
            , Item packFItem, Item packM1Item, Item packM2Item, Item packSMItem){
        if (packItem.isEmpty() && packStackInLimbo.isEmpty()){
            Optional<ItemStack> opt = composePackStack(inputFItem,inputM1Item,inputM2Item,inputSMItem);
            return opt.map(stack -> new Pair<>(false, stack)).orElseGet(() -> new Pair<>(false, ItemStack.EMPTY));

        } else if (packItem.isEmpty()){
            return updateLimboStack(packStackInLimbo,inputFItem,inputM1Item,inputM2Item,inputSMItem);
        } else if (packStackInLimbo.isEmpty()) {
            return updateLimboStack(packItem,inputFItem,inputM1Item,inputM2Item,inputSMItem);
        } else {
            throw new UnsupportedOperationException("pack in both limbo and on the table! This isn't supposed to happen!");
        }
    }

    private Optional<ItemStack> composePackStack(Item inputFItem, Item inputM1Item, Item inputM2Item, Item inputSMItem){
        if (inputFItem instanceof PackFrameItem pfi){
            if (inputM1Item instanceof PackModuleItem pmi1) {
                PackModuleItem.ModuleTier maxTier = pfi.getMaxTier();
                if (!(maxTier.checkTier(pmi1.getTier()))) return Optional.empty();
                PackInventory inv = new PackInventory();
                inv.addModule(PackItem.MAIN_MODULE_ID,pmi1,playerInventory.player);
                Item m2ToCompose;
                if (inputM2Item instanceof PackModuleItem pmi2){
                    if (pfi.getMaxMainModules() == 1){
                        ItemStack oldModule2 = slots.get(0).getStack().copy();
                        ((PackBenchSlot)slots.get(0)).voidStack();
                        playerInventory.offerOrDrop(oldModule2);
                        m2ToCompose = Items.AIR;
                    } else {
                        m2ToCompose = pmi2;
                        inv.addModule(PackItem.MAIN_MODULE_2_ID,pmi2,playerInventory.player);
                    }
                } else {
                    m2ToCompose = Items.AIR;
                }
                Item smToCompose;
                if (inputSMItem instanceof PackModuleItem smi){
                    if (!maxTier.suitableForSideSlot()){
                        ItemStack oldSideModule = slots.get(2).getStack().copy();
                        ((PackBenchSlot)slots.get(2)).voidStack();
                        playerInventory.offerOrDrop(oldSideModule);
                        smToCompose = Items.AIR;
                    } else {
                        smToCompose = smi;
                        inv.addModule(PackItem.SIDE_MODULE_ID,smi,playerInventory.player);
                    }
                } else {
                    smToCompose = Items.AIR;
                }
                ItemStack newPack = new ItemStack(RegisterItem.PACK);
                PackItem.writeModules(pfi,pmi1,m2ToCompose,smToCompose,newPack);
                inv.toNbt(newPack.getNbt());
                return Optional.of(newPack);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private Pair<Boolean,ItemStack> updateLimboStack(
            ItemStack original,
            Item inputFItem, Item inputM1Item, Item inputM2Item, Item inputSMItem){
        Map<String,String> map = PackItem.getModules(original);
        String module1 = map.getOrDefault(PackItem.MAIN_MODULE_ID,"air");
        String module2 = map.getOrDefault(PackItem.MAIN_MODULE_2_ID,"air");
        String sideModule = map.getOrDefault(PackItem.SIDE_MODULE_ID,"air");
        String frame = map.getOrDefault(PackItem.FRAME_ID,"air");
        Item frameItem = Registry.ITEM.get(new Identifier(frame));
        Item module1Item = Registry.ITEM.get(new Identifier(module1));
        Item module2Item = Registry.ITEM.get(new Identifier(module2));
        Item sideModuleItem = Registry.ITEM.get(new Identifier(sideModule));


        return new Pair<>(false, original);

    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


    //////////////////////////////////////////////////////////////////////////////////////


    private static class PackBenchSlot extends Slot{

        public PackBenchSlot(PackBenchInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        private boolean enabled = true;

        @Override
        public int getMaxItemCount() {
            return 1;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return enabled;
        }

        protected boolean isOfType(ItemStack stack){
            return stack.getItem() instanceof PackItem;
        }

        protected void voidStack(){
            PackBenchInventory inv = (PackBenchInventory) this.inventory;
            inv.voidStack(getIndex());
        }

        protected void addStack(ItemStack stack){
            PackBenchInventory inv = (PackBenchInventory) this.inventory;
            inv.addStack(stack,getIndex());
        }

        public void setEnabled(boolean enabled){
            this.enabled = enabled;
        }

    }

    private static class PackBenchBagSlot extends PackBenchSlot{

        public PackBenchBagSlot(PackBenchInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }
        public PackBenchBagSlot(PackBenchInventory inventory, int index, int x, int y, boolean enabled){
            this(inventory, index, x, y);
            this.setEnabled(enabled);
        }

        @Override
        protected boolean isOfType(ItemStack stack) {
            return stack.getItem() instanceof PackModuleItem;
        }
    }

    private static class PackBenchFrameSlot extends PackBenchSlot{

        public PackBenchFrameSlot(PackBenchInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }
        public PackBenchFrameSlot(PackBenchInventory inventory, int index, int x, int y, boolean enabled) {
            this(inventory, index, x, y);
            this.setEnabled(enabled);
        }

        @Override
        protected boolean isOfType(ItemStack stack) {
            return stack.getItem() instanceof PackFrameItem;
        }
    }

    private static class PackBenchOutputInventory implements PackBenchInventory{

        private ItemStack stack = ItemStack.EMPTY;

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return stack.isEmpty();
        }

        @Override
        public ItemStack getStack(int slot) {
            return stack;
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            ItemStack itemStack = stack.split(amount);
            if (!stack.isEmpty()) {
                this.markDirty();
            }
            return itemStack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack itemStack = stack.copy();
            stack = ItemStack.EMPTY;
            this.markDirty();
            return itemStack;
        }

        @Override
        public void voidStack(int slot){
            stack = ItemStack.EMPTY;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            addStack(stack, slot);
            this.markDirty();
        }

        @Override
        public void addStack(ItemStack stack, int slot){
            this.stack = stack;
            if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
                stack.setCount(this.getMaxCountPerStack());
            }
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
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
            this.stack = ItemStack.EMPTY;
            this.markDirty();
        }
    }

    private static class PackBenchInputInventory implements PackBenchInventory{


        private final ItemStack[] defaultStacks = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
        private final List<ItemStack> stacks = Arrays.stream(defaultStacks).toList();

        @Override
        public int size() {
            return 4;
        }

        @Override
        public boolean isEmpty() {
            return stacks.isEmpty();
        }

        @Override
        public ItemStack getStack(int slot) {
            return stacks.get(slot);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            ItemStack itemStack = stacks.get(slot).split(amount);
            if (!stacks.get(slot).isEmpty()) {
                this.markDirty();
            }
            return itemStack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack itemStack = stacks.get(slot).copy();
            stacks.set(slot,ItemStack.EMPTY);
            this.markDirty();
            return itemStack;
        }

        @Override
        public void voidStack(int slot){
            stacks.set(slot, ItemStack.EMPTY);
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            addStack(stack,slot);
            this.markDirty();
        }

        @Override
        public void addStack(ItemStack stack, int slot){
            stacks.set(slot,stack);
            ItemStack stack2 = stacks.get(slot);
            if (!stack2.isEmpty() && stack2.getCount() > this.getMaxCountPerStack()) {
                stack2.setCount(this.getMaxCountPerStack());
            }
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
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
            this.stacks.clear();
            stacks.addAll(Arrays.stream(defaultStacks).toList());
            this.markDirty();
        }
    }

    private interface PackBenchInventory extends Inventory {
        void voidStack(int slot);
        void addStack(ItemStack stack, int slot);
    }

}
