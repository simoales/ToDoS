package com.example.todos;


import com.example.todos.data.TodosContract;

import org.junit.Test;

public class TodosContractTest {
    @Test
    public void testAuthority(){
        String authority = TodosContract.CONTENT_AUTHORITY;
        org.junit.Assert.assertEquals("Unexpected authority value",
                "com.example.todos.todosprovider", authority);

    }

    @Test
    public void testConcat(){
        TodosContract contract = new TodosContract();
        String content = contract.concatContent("com.example.todos");
        org.junit.Assert.assertEquals("content://com.example.todos", content);
    }
}
