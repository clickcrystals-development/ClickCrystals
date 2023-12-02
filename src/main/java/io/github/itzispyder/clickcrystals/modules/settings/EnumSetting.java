package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.client.module.EnumSettingElement;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

public class EnumSetting<E extends Enum<?>> extends ModuleSetting<E> {

    private static long cooldown;
    private E[] values;
    private int reading;

    public EnumSetting(String name, String description, E def, E val) {
        super(name, description, def, val);
        this.values = (E[])def.getClass().getEnumConstants();
    }

    public E[] values() {
        return values;
    }

    public void next() {
        if (cooldown > System.currentTimeMillis()) {
            return;
        }

        cooldown = System.currentTimeMillis() + 50;
        reading++;

        if (reading >= values.length) {
            reading = 0;
        }

        setVal(values[reading]);
    }

    @Override
    public EnumSettingElement toGuiElement(int x, int y) {
        return new EnumSettingElement(this, x, y);
    }

    public static <E extends Enum<?>> Builder<E> create(Class<E> type) {
        return new Builder<>();
    }

    public static class Builder<E extends Enum<?>> extends SettingBuilder<E, Builder<E>, EnumSetting<E>> {
        @Override
        public EnumSetting<E> build() {
            return new EnumSetting<>(name, description, def, getOrDef(val, def));
        }
    }
}
