import java.math.BigDecimal;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) throws DishOrderException {

        RestaurantManager restaurantManager = new RestaurantManager();

        Dish kure = new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(130),20,"kure_rizek_obal");
        Dish hranolky = new Dish("Hranolky 150 g", BigDecimal.valueOf(60),10,"hranolky");
        Dish pstruh = new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(140),25);
        Dish kofola = new Dish("Kofola 0,5 l", BigDecimal.valueOf(25),3,"kofola");
        Dish hoveziSteak = new Dish("Hovězí steak 200 g", BigDecimal.valueOf(200),30,"hovezi_steak");
        Dish slepiceNaPaprice = new Dish("Slepice na paprice 150 g", BigDecimal.valueOf(150),20,"slepice_na_paprice");
        Dish zmrzlina = new Dish("Zmrzlinový pohár 100g", BigDecimal.valueOf(55),10,"zmrzlinovy_pohar");



        restaurantManager.addDishesToCookBook(kure,hranolky,pstruh,kofola,hoveziSteak);

        Order order1 = new Order(0,2,15,LocalTime.of(12,33),null,false);
        Order order2 = new Order(1,2,15,LocalTime.of(12,33),null,false);
        Order order3 = new Order(3,2,15,LocalTime.of(12,30),LocalTime.of(12,33),false);
        Order order4 = new Order(2,3,2,LocalTime.of(11,24),LocalTime.of(11,50),true);
        Order order5 = new Order(3,4,2,LocalTime.of(11,24),LocalTime.of(11,26),true);

        restaurantManager.addOrdersToOrderList(order1,order2,order3,order4,order5);


        restaurantManager.saveCookBookToFile();
        restaurantManager.saveOrderListToFile();


        restaurantManager.loadCookBookFromFile();
        restaurantManager.loadOrderListFromFile();

        System.out.println("\nStůl číslo 15 utratil " + restaurantManager.getTotalPricePerTable(15) + " Kč");
        System.out.println("\nPočet nedokončených objednávek: " + restaurantManager.unfinishedOrders());
        restaurantManager.sortOrdersByOrderTime();
        System.out.println("\nSeřazené objednávky podle času zadání: " + restaurantManager.getOrderList());
        System.out.println("\nPrůměrná doba zpracování objednávek: "+ restaurantManager.getAveragePrepareTime() + " min");
        System.out.println("\nSeznam jídel, která byla dnes objednána: "+restaurantManager.getDailyDishList());
        System.out.println("\n");
        System.out.println(restaurantManager.getTableInfo(15));

        restaurantManager.addDishesToCookBook(slepiceNaPaprice,zmrzlina);

        restaurantManager.saveCookBookToFile();
        restaurantManager.saveOrderListToFile();

        restaurantManager.removeDish("Kuřecí řízek obalovaný 150 g");

        restaurantManager.saveCookBookToFile();
        restaurantManager.saveOrderListToFile();

    }

    public static void handleFileError(String fileName, Exception e){
        System.err.println("Soubor "+fileName+" je poškozen: " + e.getMessage());
    }

}
