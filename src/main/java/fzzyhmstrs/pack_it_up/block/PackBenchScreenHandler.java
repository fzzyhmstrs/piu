package fzzyhmstrs.pack_it_up.block;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PackBenchScreenHandler extends ScreenHandler {
    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PIU.PACK_BENCH_SCREEN_HANDLER, syncId);
        this.world = playerInventory.player.getWorld();
        this.recipes = this.world.getRecipeManager().listAllOfType(PIU.PACK_BENCH_RECIPE);
        this.context = context;
        this.player = playerInventory.player;
        int i;
        this.addSlot(new Slot(this.input, 0, 27, 47));
        this.addSlot(new Slot(this.input, 1, 76, 47));
        this.addSlot(new Slot(this.output, 0, 134, 47){

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return PackBenchScreenHandler.this.canTakeOutput(playerEntity, this.hasStack());
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                PackBenchScreenHandler.this.onTakeOutput(player, stack);
            }
        });
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

    private final World world;
    private final List<PackBenchRecipe> recipes;
    private PackBenchRecipe currentRecipe;
    protected final Inventory input = new SimpleInventory(2){

        @Override
        public void markDirty() {
            super.markDirty();
            PackBenchScreenHandler.this.onContentChanged(this);
        }
    };
    protected final CraftingResultInventory output = new CraftingResultInventory();
    protected final ScreenHandlerContext context;
    protected final PlayerEntity player;

    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world);
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.getWorld(), player, stack.getCount());
        Item item = this.input.getStack(0).getItem().getRecipeRemainder();
        ItemStack remainder;
        if (item == null){
            remainder = ItemStack.EMPTY;
        } else {
            remainder = new ItemStack(item);
        }
        this.output.unlockLastRecipe(player,List.of(input.getStack(0),input.getStack(1)));
        this.decrementStack(0);
        this.decrementStack(1);
        this.input.setStack(1, remainder);
        context.run((world,pos) -> {
            world.playSound(null,pos, SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS,0.4f,1.1f);
            world.playSound(null,pos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS,0.6f,1.0f);
        });
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (inventory == this.input) {
            this.updateResult();
        }
        super.onContentChanged(inventory);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) ->
            player.squaredDistanceTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5) <= 64.0
        , true);
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    public void updateResult() {
        List<PackBenchRecipe> list = this.world.getRecipeManager().getAllMatches(PIU.PACK_BENCH_RECIPE, this.input, this.world);
        if (list.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
        } else {
            this.currentRecipe = list.get(0);
            ItemStack itemStack = this.currentRecipe.craft(this.input,this.world.getRegistryManager());
            this.output.setLastRecipe(this.currentRecipe);
            this.output.setStack(0, itemStack);
        }
    }

    protected boolean isUsableAsAddition(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testAddition(stack));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot == 0 || slot == 1) {
                if (!this.insertItem(itemStack2, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot >= 3 && slot < 39) {
                int i = this.isUsableAsAddition(itemStack) ? 1 : 0;
                if (!this.insertItem(itemStack2, i, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }
}
