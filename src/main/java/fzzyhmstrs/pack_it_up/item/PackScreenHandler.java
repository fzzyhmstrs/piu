package fzzyhmstrs.pack_it_up.item;

import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

import java.util.Objects;

public class PackScreenHandler extends ScreenHandler {

    public PackScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, int height, ItemStack stack, ScreenHandlerContext context) {
        super(PIU.PACK_SCREEN_HANDLER, syncId);
        this.rows = height;
        this.stack = stack;
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        int i = (this.rows - 4) * 18;
        int j;
        int k;
        for (j = 0; j < this.rows; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new PackSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new PackLockedSlot(playerInventory,stack, j, 8 + j * 18, 161 + i));
        }
    }

    public PackScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(
            syncId,
            playerInventory,buf.readByte() == PackItem.ModuleTier.ENDER.id ? playerInventory.player.getEnderChestInventory() : new PackInventory(buf.readByte(), PackItem.StackPredicate.fromBuf(buf)),
            buf.readByte(),
            playerInventory.getStack(buf.readByte()),
            ScreenHandlerContext.EMPTY
        );
    }

    public final int rows;
    private final ItemStack stack;
    private final Inventory inventory;

    @Override
    public void close(PlayerEntity player) {
        if (this.inventory instanceof PackInventory && !player.world.isClient){
            Packable.saveInventory(stack,(PackInventory) inventory);
        }
        super.close(player);
        this.inventory.onClose(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.rows * 9 ? !this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true) : !this.insertItem(itemStack2, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        ItemStack itemStack;
        Slot slot;
        boolean bl = false;
        int i = startIndex;
        int max = inventory.getMaxCountPerStack();
        if (fromLast) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty() && (fromLast ? i >= startIndex : i < endIndex)) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
                    int j = itemStack.getCount() + stack.getCount();
                    if (j <= max) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot.markDirty();
                        bl = true;
                    } else if (itemStack.getCount() < max) {
                        stack.decrement(max - itemStack.getCount());
                        itemStack.setCount(max);
                        slot.markDirty();
                        bl = true;
                    }
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        if (!stack.isEmpty()) {
            i = fromLast ? endIndex - 1 : startIndex;
            while (fromLast ? i >= startIndex : i < endIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }
                    slot.markDirty();
                    bl = true;
                    break;
                }
                if (fromLast) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return bl;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private static class PackSlot extends Slot{

        public PackSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public ItemStack takeStack(int amount) {
            int amount2 = Math.min(64,amount);
            return super.takeStack(amount2);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            if (inventory instanceof PackInventory packInventory) {
                return packInventory.canInsert(stack);
            } else {
                return super.canInsert(stack);
            }
        }

        @Override
        public int getMaxItemCount(ItemStack stack) {
            if (stack.getMaxCount() < 64){
                if (stack.isDamageable()) {
                    return stack.getMaxCount();
                } else {
                    return stack.getMaxCount() * 4;
                }
            }
            return getMaxItemCount();
        }
    }

    private static class PackLockedSlot extends Slot{

        public PackLockedSlot(Inventory inventory,ItemStack packStack, int index, int x, int y) {
            super(inventory, index, x, y);
            this.packStack = packStack;
        }

        private final ItemStack packStack;

        @Override
        public boolean canInsert(ItemStack stack) {
            return stackMovementIsAllowed(stack);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return stackMovementIsAllowed(getStack());
        }

        private boolean stackMovementIsAllowed(ItemStack stack) {
            return !(stack.getItem() instanceof Packable) && stack != packStack;
        }
    }
}
