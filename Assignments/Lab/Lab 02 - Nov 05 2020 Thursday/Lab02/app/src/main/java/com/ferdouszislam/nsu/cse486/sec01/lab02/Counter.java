package com.ferdouszislam.nsu.cse486.sec01.lab02;

public class Counter {
// model class for MainActivity controller

    private int count;

    public static String COUNT_INSTANCE_ID = "int-counter";

    public Counter() {
        this.count = 0;
    }

    public Counter(int count) {
        this.count = count;
    }

    public void incrementCount(){
        this.count++;
    }

    public void decrementCount() { this.count--; }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
