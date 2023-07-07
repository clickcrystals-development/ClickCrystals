package io.github.itzispyder.clickcrystals.gui.screens;

public class CreditsScreen extends ClickCrystalsBase {

    public CreditsScreen() {
        super("ClickCrystals Credits Screen");
    }

    @Override
    protected void init() {

    }

    @Override
    public void close() {
        super.close();
        ClickCrystalsBase.setPrevOpened(this.getClass());
    }
}
