import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dish {
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String image;


    public Dish(String title, BigDecimal price, int preparationTime, String image) throws DishOrderException {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.image = image;
        if(this.preparationTime<=0){
            throw new DishOrderException("Doba přípravy nesmí být záporné číslo či nula");
        } else if (this.price.compareTo(BigDecimal.ZERO)<0) {
            throw new DishOrderException("Cena nesmí být záporné číslo");
        }
    }

    public Dish(String title, BigDecimal price, int preparationTime) throws DishOrderException {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.image = "blank";
        if(this.preparationTime<=0){
            throw new DishOrderException("Doba přípravy nesmí být záporné číslo či nula");
        }else if (this.price.compareTo(BigDecimal.ZERO)<0) {
            throw new DishOrderException("Cena nesmí být záporné číslo");
        }
    }

    public Dish(){
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) throws DishOrderException {
        if(preparationTime<=0){
            throw new DishOrderException("Doba přípravy nesmí být záporné číslo či nula");
        }
        this.preparationTime = preparationTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return  "\nJídlo{" +
                "Název jídla: " + title +
                ", Cena: " + price +
                ", Čas přípravy: " + preparationTime +
                ", Obrázek: " + image +
                '}';
    }
}
