import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order{
    private int dishNumber;
    private int numberOfDishes;
    private int tableNumber;
    private LocalTime orderedTime;
    private LocalTime fulfilmentTime;
    private boolean payed;

    public Order(int dishNumber,int numberOfDishes, int tableNumber, LocalTime orderedTime, LocalTime fulfilmentTime, boolean payed) throws DishOrderException {
        this.dishNumber = dishNumber;
        this.numberOfDishes=numberOfDishes;
        this.tableNumber = tableNumber;
        this.orderedTime = orderedTime;
        this.fulfilmentTime = fulfilmentTime;
        this.payed = payed;
    }

    public Order(){
        super();
    }

    public int getDishNumber() {
        return dishNumber;
    }

    public void setDishNumber(int dishNumber) {
        this.dishNumber = dishNumber;
    }

    public int getNumberOfDishes() {
        return numberOfDishes;
    }

    public void setNumberOfDishes(int numberOfDishes) {
        this.numberOfDishes = numberOfDishes;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public LocalTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public void setFulfilmentTime(LocalTime fulfilmentTime) {
        this.fulfilmentTime = fulfilmentTime;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    @Override
    public String toString() {
        return  "\nObjednávka{" +
                "Číslo jídla: " + dishNumber +
                ", Počet kusů: " + numberOfDishes +
                ", Číslo stolu: " + tableNumber +
                ", Čas objednání:" + orderedTime +
                ", Čas dokončení: " + fulfilmentTime +
                ", Pladba: " + (payed?"zaplceno":"nezaplaceno") +
                '}';
    }
}
