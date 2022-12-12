package fzzyhmstrs.pack_it_up.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fzzyhmstrs.pack_it_up.recipe.PackBenchRecipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PackBenchEmiRecipe implements EmiRecipe {

    public PackBenchEmiRecipe(PackBenchRecipe recipe){
        this.recipe = recipe;
        this.base = EmiIngredient.of(recipe.base);
        this.addition = EmiIngredient.of(recipe.addition);
        this.output = EmiStack.of(recipe.getOutput());
        this.inputs = List.of(base,addition);
    }

    private final PackBenchRecipe recipe;
    private final EmiIngredient base;
    private final EmiIngredient addition;
    private final EmiStack output;
    private final List<EmiIngredient> inputs;

    @Override
    public EmiRecipeCategory getCategory() {
        return EmiClientPlugin.PACK_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 125;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.PLUS, 27, 3);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        widgets.addSlot(base, 0, 0);
        widgets.addSlot(addition, 49, 0);
        widgets.addSlot(output, 107, 0).recipeContext(this);
    }
}
