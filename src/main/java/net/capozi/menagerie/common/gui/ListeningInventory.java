package net.capozi.menagerie.common.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ListeningInventory extends PlayerInventory implements Inventory {
    private final Inventory delegate;
    private final Runnable onChange;

    public ListeningInventory(Inventory delegate, Runnable onChange) {
        super(null);
        this.delegate = delegate;
        this.onChange = onChange;
    }

    @Override
    public int size() { return delegate.size(); }

    @Override
    public boolean isEmpty() { return delegate.isEmpty(); }

    @Override
    public ItemStack getStack(int slot) { return delegate.getStack(slot); }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = delegate.removeStack(slot, amount);
        if (!result.isEmpty()) onChange.run();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = delegate.removeStack(slot);
        if (!result.isEmpty()) onChange.run();
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        delegate.setStack(slot, stack);
        onChange.run();
    }

    @Override
    public void markDirty() {
        delegate.markDirty();
        onChange.run(); // Trigger whenever the inventory is marked dirty
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) { return delegate.canPlayerUse(player); }

    @Override
    public void clear() {
        delegate.clear();
        onChange.run();
    }
}

