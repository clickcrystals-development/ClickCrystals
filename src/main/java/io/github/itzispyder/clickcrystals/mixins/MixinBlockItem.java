package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

@Mixin(BlockItem.class)
public class MixinBlockItem {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"))
    private void onPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> info) {
        BlockPlaceEvent event = new BlockPlaceEvent(state, context.getBlockPos());
        system.eventBus.pass(event);
    }
}