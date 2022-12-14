package fzzyhmstrs.pack_it_up.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArmoredPackItem extends ArmorItem implements Packable {

    public ArmoredPackItem(ArmorMaterial material, Settings settings, PackItem.ModuleTier tier, PackItem.StackPredicate stackPredicate) {
        super(material, EquipmentSlot.CHEST, settings);
        this.tier = tier;
        this.stackPredicate = stackPredicate;
    }

    private final PackItem.ModuleTier tier;
    private final PackItem.StackPredicate stackPredicate;

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(stackPredicate.translationKey).formatted(Formatting.ITALIC));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null){
            PackInventory inventory = Packable.getInventory(stack, tier.slots, stackPredicate);
            if (this.tier == PackItem.ModuleTier.ENDER){
                inventory.dump(player);
            } else {
                inventory.validate(player, stackPredicate);
            }
            Packable.saveInventory(stack, inventory, stackPredicate);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            world.playSound(user,user.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER,0.5f,1.0f);
            return TypedActionResult.fail(stack);
        }
        Inventory inventory;
        if (tier == PackItem.ModuleTier.ENDER){
            inventory = user.getEnderChestInventory();
        } else {
            inventory = Packable.getInventory(stack, tier.slots, stackPredicate);
        }

        user.openHandledScreen(new PackScreenHandlerFactory(inventory, tier, stack, hand));
        return TypedActionResult.success(stack);
    }

}
