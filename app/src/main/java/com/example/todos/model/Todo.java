package com.example.todos.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.io.Serializable;

public class Todo implements Serializable {
    public final ObservableInt Id = new ObservableInt();
    public final ObservableField<String> text = new ObservableField<String>();
    public final ObservableField<String> created = new ObservableField<String>();
    public final ObservableField<String> expired = new ObservableField<String>();
    public final ObservableBoolean done = new ObservableBoolean();
    public final ObservableField<String> category = new ObservableField<String>();

    public Todo(int id, String text, String created, String expired, boolean done, String category) {
        this.Id.set(id);
        this.text.set(text);
        this.created.set(created);
        this.expired.set(expired);
        this.done.set(done);
        this.category.set(category);
    }

}

