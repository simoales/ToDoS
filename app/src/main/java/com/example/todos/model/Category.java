package com.example.todos.model;

import android.databinding.ObservableField;

import java.io.Serializable;

public class Category implements Serializable {
    public final ObservableField<Integer> catId = new ObservableField<Integer>();
    public final ObservableField<String>  description = new ObservableField<String>();

    public Category() {
    }

    public Category(int i, String d) {
        catId.set(i);
        description.set(d);
    }


}
