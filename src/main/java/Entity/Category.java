package Entity;

public class Category
{
    private long id;
    private String name;
    private Category parent_id;

    public Category(long id, String name, Category parent_id) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent_id() {
        return parent_id;
    }

    public void setParent_id(Category parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }
}