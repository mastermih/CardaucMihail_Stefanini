package ValueObjects;

public class ID
{
    private int product_id;

    public ID(int id) {
        this.product_id = id;
    }

    public int getId() {
        return product_id;
    }

    public void setId(int id) {
        this.product_id = id;
    }

    @Override
    public String toString() {
        return "ID{" +
                "product_id=" + product_id +
                '}';
    }
}
