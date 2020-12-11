package com.findthebusiness.backend.dto.search;

public class LeftRightPairForBS {

    private int left;
    private int right;


    public LeftRightPairForBS() {
    }

    public LeftRightPairForBS(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public LeftRightPairForBS setLeft(int left) {
        this.left = left;
        return this;
    }

    public int getRight() {
        return right;
    }

    public LeftRightPairForBS setRight(int right) {
        this.right = right;
        return this;
    }
}
