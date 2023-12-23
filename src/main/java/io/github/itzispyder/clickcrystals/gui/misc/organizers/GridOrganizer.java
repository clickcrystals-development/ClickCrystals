package io.github.itzispyder.clickcrystals.gui.misc.organizers;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ScrollPanelElement;

import java.util.ArrayList;
import java.util.List;

public class GridOrganizer implements Organizer<GuiElement> {

    private ScrollPanelElement panel;
    private final List<GuiElement> entries;
    private int startX, startY, cellWidth, cellHeight, maxPerRow, gap;

    public GridOrganizer(int startX, int startY, int cellWidth, int cellHeight, int maxPerRow, int gap) {
        this.entries = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.maxPerRow = maxPerRow;
        this.gap = gap;
    }

    @Override
    public void organize() {
        int r, c;
        r = c = 0;

        for (GuiElement entry : entries) {
            entry.moveTo(startX + (c * (cellWidth + gap)), startY + (r * (cellHeight + gap)));

            if (c++ >= maxPerRow - 1) {
                c = 0;
                r++;
            }
        }
    }

    @Override
    public List<GuiElement> getEntries() {
        return entries;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getMaxPerRow() {
        return maxPerRow;
    }

    public void setMaxPerRow(int maxPerRow) {
        this.maxPerRow = maxPerRow;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void createPanel(GuiScreen screen, int height) {
        panel = new ScrollPanelElement(screen, startX, startY, (cellWidth + gap) * maxPerRow + gap, height);
    }

    public boolean hasPanel() {
        return panel != null;
    }

    public void destroyPanel() {
        panel = null;
    }

    public void clearPanel() {
        panel.clearChildren();
    }

    public void addAllToPanel() {
        clearPanel();
        if (hasPanel()) {
            entries.forEach(panel::addChild);
        }
    }

    public void setPanelParent(GuiElement parent) {
        if (hasPanel() && parent != null) {
            parent.addChild(panel);
        }
    }

    public ScrollPanelElement getPanel() {
        return panel;
    }
}
