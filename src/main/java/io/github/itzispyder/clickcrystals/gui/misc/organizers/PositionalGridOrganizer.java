package io.github.itzispyder.clickcrystals.gui.misc.organizers;

import io.github.itzispyder.clickcrystals.gui.Positionable;

import java.util.ArrayList;
import java.util.List;

public class PositionalGridOrganizer implements Organizer<Positionable> {

    private final List<Positionable> entries;
    private int startX, startY, cellWidth, cellHeight, maxPerRow, gap;

    public PositionalGridOrganizer(int startX, int startY, int cellWidth, int cellHeight, int maxPerRow, int gap) {
        this.entries = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.maxPerRow = maxPerRow;
        this.gap = gap;
    }

    @Override
    public List<Positionable> getEntries() {
        return entries;
    }

    @Override
    public void organize() {
        int r, c;
        r = c = 0;

        for (Positionable entry : entries) {
            entry.setX(startX + (c * (cellWidth + gap)));
            entry.setY(startY + (r * (cellHeight + gap)));

            if (c++ >= maxPerRow - 1) {
                c = 0;
                r++;
            }
        }
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
}
