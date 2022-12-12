package fzzyhmstrs.pack_it_up.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import fzzyhmstrs.pack_it_up.PIU;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import fzzyhmstrs.pack_it_up.registry.RegisterBlock;
import net.minecraft.util.Identifier;

public class EmiClientPlugin implements EmiPlugin {

    private static final Identifier PACK_ID = new Identifier(PIU.MOD_ID,"pack_bench");
    public static final EmiRecipeCategory PACK_CATEGORY = new EmiRecipeCategory(PACK_ID, EmiStack.of(RegisterBlock.PACK_BENCH.asItem()), new PackSimplifiedRenderer());
    private static final EmiStack PACK_WORKSTATION = EmiStack.of(RegisterBlock.PACK_BENCH.asItem());


    @Override
    public void register(EmiRegistry registry) {

        registry.addCategory(PACK_CATEGORY);
        registry.addWorkstation(PACK_CATEGORY, PACK_WORKSTATION);

        for (PackBenchRecipe recipe: registry.getRecipeManager().listAllOfType(PackBenchRecipe.TYPE)){
            registry.addRecipe(new PackBenchEmiRecipe(recipe));
        }
    }
}
