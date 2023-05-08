package io.github.itzispyder.clickcrystals.modules.settings;

import java.io.Serializable;

public interface Setting<T> extends Serializable {

    default void init(String name, T def) {
        this.setName(name);
        this.setDef(def);
        this.setVal(getDef());
    }

    T getVal();

    T getDef();

    String getName();

    void setName(String name);

    void setDef(T def);

    void setVal(T value);
}
