package ValueObjects;

import java.util.ArrayList;
import java.util.List;

public class Category
{
    private int id;
    private String name;
    private Category parent;
    private List<Category> subCategories;

    public Category(int id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.subCategories = new ArrayList<>();
        if (parent != null) {
            parent.addSubCategory(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(Category subCategory) {
        this.subCategories.add(subCategory);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + (parent != null ? parent.getName() : "None") +
                ", subCategories=" + subCategories +
                '}';
    }
}