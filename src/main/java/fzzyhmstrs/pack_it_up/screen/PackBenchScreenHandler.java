package fzzyhmstrs.pack_it_up.screen;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.item.PackFrameItem;
import fzzyhmstrs.pack_it_up.item.PackInventory;
import fzzyhmstrs.pack_it_up.item.PackItem;
import fzzyhmstrs.pack_it_up.item.PackModuleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PackBenchScreenHandler extends ScreenHandler {


    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PIU.PACK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.context = context;

        this.addSlot(new PackBenchBagSlot(inputs,0,47,16));
        this.addSlot(new PackBenchBagSlot(inputs,1,47,34));
        this.addSlot(new PackBenchBagSlot(inputs,2,27,54));
        this.addSlot(new PackBenchFrameSlot(inputs,3,47,54));

        this.addSlot(new PackBenchSlot(output,3,124,35));

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId,playerInventory,ScreenHandlerContext.EMPTY);
    }

    private final PlayerInventory playerInventory;
    private final ScreenHandlerContext context;
    private final Inventory inputs = new SimpleInventory(4){
        @Override
        public void markDirty() {
            super.markDirty();
            PackBenchScreenHandler.this.onContentChanged(this);
        }
    };
    private final Inventory output = new PackBenchOutputInventory(){
        @Override
        public void markDirty() {
            super.markDirty();
            PackBenchScreenHandler.this.onContentChanged(this);
        }
    };

    @Override
    public void onContentChanged(Inventory inventory) {
        if(inventory == this.output){
            ItemStack stack = inventory.getStack(0);
            if (stack.isEmpty()) {
                inputs.clear();
                super.onContentChanged(inventory);
                return;
            }
            Map<String,String> map = PackItem.getModules(stack);
            String module1 = map.getOrDefault(PackItem.MAIN_MODULE_ID,"");
            String module2 = map.getOrDefault(PackItem.MAIN_MODULE_2_ID,"");
            String sideModule = map.getOrDefault(PackItem.SIDE_MODULE_ID,"");
            String frame = map.getOrDefault(PackItem.FRAME_ID,"");
            if (!Objects.equals(module1, "")){
                Item module1Item = Registry.ITEM.get(new Identifier(module1));
                
            }


        } else if (inventory == this.inputs){



        }
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private class PackBenchSlot extends Slot{

        public PackBenchSlot(Inventory inventory, int index, int x, int y) {
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

        public void setEnabled(boolean enabled){
            this.enabled = enabled;
        }

    }

    private class PackBenchBagSlot extends PackBenchSlot{

        public PackBenchBagSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        protected boolean isOfType(ItemStack stack) {
            return stack.getItem() instanceof PackModuleItem;
        }
    }

    private class PackBenchFrameSlot extends PackBenchSlot{

        public PackBenchFrameSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        protected boolean isOfType(ItemStack stack) {
            return stack.getItem() instanceof PackFrameItem;
        }
    }

    private class PackBenchOutputInventory implements Inventory{

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
            ItemStack itemStack = stack.copy();
            stack = ItemStack.EMPTY;
            this.markDirty();
            return itemStack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            ItemStack itemStack = stack.copy();
            stack = ItemStack.EMPTY;
            this.markDirty();
            return itemStack;
        }

        public void voidStack(){
            stack = ItemStack.EMPTY;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            this.stack = stack;
            if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
                stack.setCount(this.getMaxCountPerStack());
            }
            this.markDirty();
        }

        public void addStack(ItemStack stack){
            this.stack = stack;
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

}
