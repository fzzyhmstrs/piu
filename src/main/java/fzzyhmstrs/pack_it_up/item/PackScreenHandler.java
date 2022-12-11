package fzzyhmstrs.pack_it_up.item;

import fzzyhmstrs.pack_it_up.PIU;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
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
                this.addSlot(new PackSlot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new PackSlot(playerInventory, j, 8 + j * 18, 161 + i));
        }
    }

    public PackScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(
            syncId,
            playerInventory,buf.readByte() == PackItem.ModuleTier.ENDER.id ? playerInventory.player.getEnderChestInventory() : new PackInventory(buf.readByte(), PackItem.StackPredicate.ANY),
            buf.readByte(),
            Objects.equals(buf.readString(), Hand.MAIN_HAND.name()) ? playerInventory.player.getStackInHand(Hand.MAIN_HAND) : playerInventory.player.getStackInHand(Hand.OFF_HAND),
            ScreenHandlerContext.EMPTY
        );
    }

    public final int rows;
    private final ItemStack stack;
    private final Inventory inventory;

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (this.inventory instanceof PackInventory){
            PackItem.saveInventory(stack,(PackInventory) inventory);
        }
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
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private static class PackSlot extends Slot{

        public PackSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
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
                return stack.getMaxCount();
            }
            return getMaxItemCount();
        }
    }
}
