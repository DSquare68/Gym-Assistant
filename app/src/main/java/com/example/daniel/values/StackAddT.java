package com.example.daniel.values;
import android.widget.LinearLayout;

import java.util.ArrayList;
public class StackAddT {
    ArrayList<Training> sp = new ArrayList<>();
    private int maxSize;
    private int top;

    public StackAddT(int maxSize){
        top=-1;
    }
    public void push(Training training) {
        sp.set(++top,training);
    }
    public Training pop() {
        return sp.get(top--);
    }
    public Training peek() {
        return sp.get(top);
    }
    public boolean isEmpty() {
        return (top == -1);
    }
    public boolean isFull() {
        return (top == maxSize - 1);
    }
}
