package com.example.lab3_20213849.Dtos;

import java.io.Serializable;

public class ToDoDto implements Serializable {
    private ToDo[] todos;
    private Integer total;
    private Integer skip;
    private Integer limit;

    public ToDo[] getTodos() {
        return todos;
    }

    public void setTodos(ToDo[] todos) {
        this.todos = todos;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
