package fzzyhmstrs.pack_it_up.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BagItem extends Item {

    public BagItem(Settings settings) {
        super(settings);
    }

    private static final Text tooltipText = Text.translatable("pack_it_up.bag_item_tooltip").formatted(Formatting.ITALIC);

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(tooltipText);
    }
}
