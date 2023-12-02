package io.github.itzispyder.clickcrystals.gui.misc.organizers;

import io.github.itzispyder.clickcrystals.gui.GuiElement;

import java.util.List;
import java.util.Objects;

public class AdaptiveGridOrganizer extends GridOrganizer {

    public AdaptiveGridOrganizer(int startX, int startY, int maxPerRow, int gap) {
        super(startX, startY, 0, 0, maxPerRow, gap);
        updateBounds();
    }

    public void updateBounds() {
        setCellWidth(getWidest());
        setCellHeight(getTallest());
    }

    @Override
    public void organize() {
        updateBounds();
        super.organize();
    }

    public int getWidest() {
        List<Integer> i = getEntries().stream().filter(Objects::nonNull).map(GuiElement::getWidth).sorted().toList();
        return i.isEmpty() ? 0 : i.get(i.size() - 1);
    }

    public int getTallest() {
        List<Integer> i = getEntries().stream().filter(Objects::nonNull).map(GuiElement::getHeight).sorted().toList();
        return i.isEmpty() ? 0 : i.get(i.size() - 1);
    }
}
