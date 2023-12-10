
import org.json.JSONObject;

public class Producto {

    private int id;
    private String name;
    private String desc;
    private int quant;
    private double price;
    private String img;

    public Producto() {
    }

    public Producto(JSONObject productoJSON) {
        this.id = productoJSON.getInt("id");
        this.name = productoJSON.getString("name");
        this.quant = productoJSON.getInt("quant");
        this.desc = productoJSON.getString("desc");
        this.price = productoJSON.getDouble("price");
        this.img = productoJSON.getString("img");
    }

    public int getId() {
        return id;
    }

    public void setId(int id_prod) {
        this.id = id_prod;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String descripcion) {
        this.desc = descripcion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int cantidad) {
        this.quant = cantidad;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double precio) {
        this.price = precio;
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + id + ", name=" + name + ", desc=" + desc + ", quant=" + quant + ", price=" + price + ", img=" + img + '}';
    }

}
