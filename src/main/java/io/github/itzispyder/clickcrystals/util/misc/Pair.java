package io.github.itzispyder.clickcrystals.util.misc;

public class Pair<L, R> {

    public L left;
    public R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public boolean hasRight() {
        return right != null;
    }
}
