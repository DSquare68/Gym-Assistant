package com.example.daniel.values;
import android.widget.LinearLayout;

import java.util.ArrayList;
public class StackAddT {
    ArrayList<Training.Exercise> sp = new ArrayList<>();
    private int maxSize;
    private int top;

    public StackAddT(int maxSize){
        top=-1;
        this.maxSize=maxSize;
    }
    public void push(Training.Exercise exercise) {
        sp.add(++top,exercise);
    }
    public Training.Exercise pop() {
        return sp.get(top--);
    }
    public Training.Exercise peek() {
        return sp.get(top);
    }
    public boolean isEmpty() {
        return (top == -1);
    }
    public boolean isFull() {
        return (top == maxSize - 1);
    }
}
