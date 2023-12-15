
import java.util.Map;
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

    public static void crearProductosIniciales(Map<Integer, Producto> productosAlmacen) {
        Producto p1 = new Producto();
        p1.setId(999999);
        p1.setName("Televisor Samsung 98\"");
        p1.setDesc("Televisor Samsung Smart TV 98\" QLED 4K QN98Q80CAGXPE (2023)");
        p1.setPrice(19999.00);
        p1.setQuant(15);
        p1.setImg("https://hiraoka.com.pe/media/catalog/product/1/3/130880_0.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:");

        productosAlmacen.put(p1.getId(), p1);

        Producto p2 = new Producto();
        p2.setId(999998);
        p2.setName("Parlante Harman");
        p2.setDesc("Parlante Harman Kardon Onyx Studio 8 Negro");
        p2.setPrice(849.00);
        p2.setQuant(27);
        p2.setImg("https://hiraoka.com.pe/media/catalog/product/1/3/130284-1.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:");

        productosAlmacen.put(p2.getId(), p2);

        Producto p3 = new Producto();
        p3.setId(999997);
        p3.setName("Apple Watch");
        p3.setDesc("Apple Watch Series 9 GPS 45mm Medianoche Talla M/L");
        p3.setPrice(2249.00);
        p3.setQuant(19);
        p3.setImg("https://hiraoka.com.pe/media/catalog/product/1/3/131291_1.jpg?quality=80&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:");

        productosAlmacen.put(p3.getId(), p3);

    }

}
