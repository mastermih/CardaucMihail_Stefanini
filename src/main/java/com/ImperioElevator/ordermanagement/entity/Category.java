package com.ImperioElevator.ordermanagement.entity;


import com.ImperioElevator.ordermanagement.valueobjects.Id;
import com.ImperioElevator.ordermanagement.valueobjects.Name;

public class Category {

    private Id id;
    private Name name;
    private Category parentId;

    public Category(Id id, Name name, Category parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Category getParentId() {
        return parentId;
    }

    public void setParentId(Category parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name=" + name +
                ", parentId=" + parentId +
                '}';
    }
}