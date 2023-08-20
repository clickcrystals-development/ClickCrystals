package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.GuiElement;

public class EnumSetting<E extends Enum<?>> extends ModuleSetting<E> {

    private E[] values;
    private int reading;

    public EnumSetting(String name, String description, E def, E val) {
        super(name, description, def, val);

        try {
            values = (E[])def.getClass().getDeclaredMethod("values").invoke(null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public E[] values() {
        return values;
    }

    public void next() {
        reading++;

        if (reading >= values.length) {
            reading = 0;
        }

        setVal(values[reading]);
    }

    @Override
    public <E extends GuiElement> E toGuiElement(int x, int y, int width, int height) {
        return null;
    }

    public static <E extends Enum<?>> Builder<E> create(Class<E> type) {
        return new Builder<>();
    }

    public static class Builder<E extends Enum<?>> extends SettingBuilder<E, Builder<E>, EnumSetting<E>> {
        @Override
        public EnumSetting<E> build() {
            return new EnumSetting<>(name, description, def, val);
        }
    }
}
