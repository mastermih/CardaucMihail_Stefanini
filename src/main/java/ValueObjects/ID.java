package ValueObjects;

public class ID
{
    private long product_id;

    public ID(long id) {
        this.product_id = id;
    }

    public long getId() {
        return product_id;
    }

    public void setId(long id) {
        this.product_id = id;
    }

    @Override
    public String toString() {
        return "ID{" +
                "product_id=" + product_id +
                '}';
    }
}
