package ru.ssau.tk.labwork.ooplabworks.exceptions;

public class ArrayIsNotSortedException extends RuntimeException{
    public ArrayIsNotSortedException() {
        super();
    }

    public ArrayIsNotSortedException(String err) {
        super(err);
    }
}
