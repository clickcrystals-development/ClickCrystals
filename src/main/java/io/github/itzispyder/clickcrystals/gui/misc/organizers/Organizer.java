package io.github.itzispyder.clickcrystals.gui.misc.organizers;

import java.util.List;
import java.util.function.Consumer;

public interface Organizer<T> {

    List<T> getEntries();

    void organize();

    default void addEntry(T entry) {
        notNull(list -> list.add(entry));
    }

    default void removeEntry(T entry) {
        notNull(list -> list.remove(entry));
    }

    default void clear() {
        notNull(List::clear);
    }

    default int count() {
        return getEntries() == null ? 0 : getEntries().size();
    }

    private void notNull(Consumer<List<T>> action) {
        if (getEntries() != null) {
            action.accept(getEntries());
        }
    }
}
