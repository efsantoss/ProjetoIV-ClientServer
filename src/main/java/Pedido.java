public class Pedido {
    private int quantity;
    private String address;

    public Pedido(int quantity, String address) {
        this.quantity = quantity;
        this.address = address;
    }

    public int getQuantidade() {
        return quantity;
    }

    public String getEndereco() {
        return address;
    }
}