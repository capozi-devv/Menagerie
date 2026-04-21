package net.capozi.menagerie.common.entity;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrinketsHelper {

    public static List<ItemStack> findAllEquippedBy(LivingEntity entity) {

        List<ItemStack> stacks = new ArrayList<>();

        List<TrinketComponent> list = TrinketsApi.getTrinketComponent(entity).stream().toList();

        list.forEach(comp -> {
            comp.getAllEquipped().forEach(pair -> {
                stacks.add(pair.getRight().copy());
            });
        });

        return stacks;
    }

    public static void clearAllEquippedTrinkets(LivingEntity entity) {
        TrinketsApi.getTrinketComponent(entity).stream().forEach(comp -> {
            comp.getAllEquipped().forEach(pair -> {
                pair.getRight().setCount(0);
            });
        });
    }
}