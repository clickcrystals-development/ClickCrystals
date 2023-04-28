package io.github.itzispyder.clickcrystals.scheduler;

import java.util.ArrayList;
import java.util.List;

public class ArrayQueue<E> implements Queue<E> {

    private final List<E> elements;
    private final int capacity;

    public ArrayQueue(int capacity) {
        this.elements = new ArrayList<>();
        this.capacity = capacity;
    }

    @Override
    public void enqueue(E element) {
        this.elements.add(0, element);

        if (size() > this.capacity) {
            this.pop();
        }
    }

    @Override
    public void dequeue(E element) {
        this.elements.remove(element);
    }

    @Override
    public void insert(int index, E element) {
        this.elements.add(index, element);
    }

    @Override
    public void pop(int index) {
        this.elements.remove(index);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public List<E> getElements() {
        return new ArrayList<>(elements);
    }

    @Override
    public int size() {
        return this.elements.size();
    }
}
