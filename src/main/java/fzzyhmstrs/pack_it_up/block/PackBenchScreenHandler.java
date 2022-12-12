package fzzyhmstrs.pack_it_up.block;

import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class PackBenchScreenHandler extends ForgingScreenHandler {
    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PIU.PACK_BENCH_SCREEN_HANDLER, syncId, playerInventory, context);
        this.world = playerInventory.player.world;
        this.recipes = this.world.getRecipeManager().listAllOfType(PIU.PACK_BENCH_RECIPE);
    }

    public PackBenchScreenHandler(int syncId, PlayerInventory playerInventory){
        this(syncId,playerInventory,ScreenHandlerContext.EMPTY);
    }

    private final World world;
    private final List<PackBenchRecipe> recipes;
    private PackBenchRecipe currentRecipe;

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world);
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.world, player, stack.getCount());
        ItemStack remainder = new ItemStack(this.input.getStack(0).getItem().getRecipeRemainder());
        this.output.unlockLastRecipe(player);
        this.decrementStack(0);
        this.decrementStack(1);
        this.input.setStack(1, remainder);
        context.run((world,pos) -> {
            world.playSound(null,pos, SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS,0.4f,1.1f);
            world.playSound(null,pos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS,0.6f,1.0f);
        });
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    @Override
    protected boolean canUse(BlockState state) {
        return true;
    }

    @Override
    public void updateResult() {
        List<PackBenchRecipe> list = this.world.getRecipeManager().getAllMatches(PIU.PACK_BENCH_RECIPE, this.input, this.world);
        if (list.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
        } else {
            this.currentRecipe = list.get(0);
            ItemStack itemStack = this.currentRecipe.craft(this.input);
            this.output.setLastRecipe(this.currentRecipe);
            this.output.setStack(0, itemStack);
        }
    }

    @Override
    protected boolean isUsableAsAddition(ItemStack stack) {
        return this.recipes.stream().anyMatch(recipe -> recipe.testAddition(stack));
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }
}
