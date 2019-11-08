package DBStock;

public class DBStock {
    public static class Builder {
        private int id;
        private double price;
        private String name;
        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        public Builder setPrice(Double price) {
            this.price = price;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public DBStock build() {
            return new DBStock(id, name, price);
        }
    }
    private int id;
    private double price;
    private String name;
    public DBStock(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format(this.getId() + " " + this.getName() + " " + this.getPrice());
    }
}
