package ru.notebuns.farmcharmfix.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.farm_and_charm.core.block.entity.CookingPotBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Farm & Charm 1.1.22 bug: CookingPotBlockEntity.craft only shrinks the container slot
 * when the container item has a craftingRemainingItem. Empty glass bottles / bowls / cups
 * have none, so they are never consumed while water-bottle ingredients still return empties
 * and finished drinks return containers again → net dupe.
 *
 * Fix: after a successful craft, if a required empty container is still in slot 6, consume it.
 */
@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin {
    @Unique
    private static final int NOTEBUNS$CONTAINER_SLOT = 6;

    @Inject(method = "craft", at = @At("TAIL"))
    private void notebuns$consumeEmptyContainer(Recipe<?> recipe, RegistryAccess access, CallbackInfo ci) {
        if (!(recipe instanceof CookingPotRecipe cooking) || !cooking.isContainerRequired()) {
            return;
        }

        CookingPotBlockEntity self = (CookingPotBlockEntity) (Object) this;
        ItemStack container = self.getItem(NOTEBUNS$CONTAINER_SLOT);
        if (container.isEmpty()) {
            return;
        }

        // Only force-consume the required empty container that the buggy path left behind.
        if (!container.is(cooking.getContainerItem().getItem())) {
            return;
        }

        container.shrink(1);
        self.setChanged();
    }
}
