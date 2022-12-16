package fzzyhmstrs.pack_it_up.item;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

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
        openPackScreenHandler(user,tier,stackPredicate,stack);
        return TypedActionResult.success(stack);
    }

    @Override
    public void openPackScreenHandler(PlayerEntity user, ItemStack stack) {
        openPackScreenHandler(user,tier, stackPredicate, stack);
    }

    @Override
    public void openPackScreenHandler(PlayerEntity user, PackItem.ModuleTier tier, PackItem.StackPredicate stackPredicate, ItemStack stack) {
        Inventory inventory;
        if (tier == PackItem.ModuleTier.ENDER){
            inventory = user.getEnderChestInventory();
        } else {
            inventory = Packable.getInventory(stack, tier.slots, stackPredicate);
        }
        int index = user.getInventory().indexOf(stack);
        user.openHandledScreen(new PackScreenHandlerFactory(inventory, tier, stack, index));
    }

    public enum Materials implements ArmorMaterial{
        IRON("iron",3, 9, 0.0f),
        GOLD("gold",4, 25, 0.0f),
        NETHERITE("netherite",5, 15, 1.0f);

        Materials(String name, int protection, int enchantability, float toughness){
            this.name = name;
            this.protection = protection;
            this.enchantability = enchantability;
            this.toughness = toughness;
        }

        private final int protection;
        private final int enchantability;
        private final String name;
        private final float toughness;

        @Override
        public int getDurability(EquipmentSlot slot) {
            return 100;
        }

        @Override
        public int getProtectionAmount(EquipmentSlot slot) {
            return protection;
        }

        @Override
        public int getEnchantability() {
            return enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public float getToughness() {
            return toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}
