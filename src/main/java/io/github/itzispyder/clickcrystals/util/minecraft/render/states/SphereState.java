package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators.EntityIndicatorSimulation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;

public class SphereState {

    public Matrix3x2fStack pose;
    public Quaternionf rotation;
    public float x, y, radius, focalLen;
    public int deltaTheta, color;
    public ScreenRect bounds, scissor;
    public EntityIndicatorSimulation simulation;

    public SphereState(DrawContext context, Quaternionf rotation, float x, float y, float radius, float focalLen, int deltaTheta, int color, EntityIndicatorSimulation simulation) {
        this.pose = context.getMatrices();
        this.rotation = rotation;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.focalLen = focalLen;
        this.deltaTheta = deltaTheta;
        this.color = color;
        this.scissor = context.scissorStack.peekLast();

        ScreenRect bounds = new ScreenRect((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2)).transformEachVertex(pose);
        this.bounds = scissor == null ? bounds : scissor.intersection(bounds);

        this.simulation = simulation;
    }
}