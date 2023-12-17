import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantManager {
    private List<Dish> cookBook;
    private List<Order> orderList;

    public int unfinishedOrders(){
        int unfinishedOrders =0;
        for (Order order: getOrderList()) {
            if(Objects.isNull(order.getFulfilmentTime())){
                unfinishedOrders++;
            }
        }
        return unfinishedOrders;
    }

    public void sortOrdersByOrderTime(){
        Collections.sort(getOrderList(), new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2){return o1.getOrderedTime().compareTo(o2.getOrderedTime());
            }
        });
    }

    public int getAveragePrepareTime(){
        int totalPrepareTime = 0;
        int finishedOrders = 0;
        for (Order order:getOrderList()) {
            if(Objects.nonNull(order.getFulfilmentTime())){
                finishedOrders++;
                totalPrepareTime += Duration.between(order.getOrderedTime(), order.getFulfilmentTime()).toMinutes();
            }
        }
        return totalPrepareTime/finishedOrders;
    }

    public List<String> getDailyDishList(){
        LinkedHashSet<String> dailyDishHashSet = new LinkedHashSet<>();
        for (int i = 0; i < getOrderList().size(); i++) {
            dailyDishHashSet.add(getCookBook().get(getOrderList().get(i).getDishNumber()).getTitle());
        }
        return new ArrayList<>(dailyDishHashSet);
    }

    public String getTableInfo(int table){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        List <Order> foundOrdersList = getOrderList().stream()
                .filter(order -> order.getTableNumber()==table)
                .collect(Collectors.toList());
        if(!foundOrdersList.isEmpty()){
        StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < foundOrdersList.size(); i++) {
                Dish dishInfo =getCookBook().get(foundOrdersList.get(i).getDishNumber());
                Order orderInfo = foundOrdersList.get(i);
                stringBuilder.append("\n"+(i+1)+". "+ dishInfo.getTitle()+
                        " " +(Objects.equals(orderInfo.getNumberOfDishes(),1)
                        ?"":orderInfo.getNumberOfDishes()+"x ")+
                        "(" +dishInfo.getPrice().multiply(BigDecimal.valueOf(orderInfo.getNumberOfDishes()))+
                        "Kč):\t"+orderInfo.getOrderedTime().format(formatter)+
                        "-"+(Objects.nonNull(orderInfo.getFulfilmentTime())
                        ?orderInfo.getFulfilmentTime().format(formatter)
                        :"nedodáno")+" "+(orderInfo.isPayed()?"zaplaceno":""));
            }
            return "\n** Objednávky pro stůl č. " + table + " **"+
                    "\n****"+
                    stringBuilder+
                    "\n******";
        }
return null;
    }

    public BigDecimal getTotalPricePerTable(int table){
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Order order : getOrderList()) {
            if (table == order.getTableNumber()) {
                totalPrice = totalPrice.add(getCookBook().get(order.getDishNumber()).getPrice().multiply(BigDecimal.valueOf(order.getNumberOfDishes())));
            }
        }
        return totalPrice;
    }

    public List<Dish> loadCookBookFromFile() throws DishOrderException{
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("cookBook.txt")))) {
            List<Dish> tempCookBook = new ArrayList<>(getCookBook());
            getCookBook().clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    getCookBook().add(parseDishLine(line));
                }catch (Exception e){
                    Main.handleFileError("cookBook.txt",e);
                    setCookBook(tempCookBook);
                    break;
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Nepodařilo se nalézt soubor cookBook.txt: " + e.getLocalizedMessage());
            return null;
        }
        return getCookBook();
    }

    public List<Order> loadOrderListFromFile(){
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("orderList.txt")))) {
            List<Order> tempOrderList = new ArrayList<>(getOrderList());
            getOrderList().clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    getOrderList().add(parseOrderLine(line));
                }catch (Exception e){
                    Main.handleFileError("orderList.txt",e);
                    setOrderList(tempOrderList);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Nepodařilo se nalézt soubor orderList.txt: "+e.getLocalizedMessage());
            return null;
        }
        return getOrderList();
    }



    public void saveCookBookToFile() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("cookBook.txt"))))) {
            if (getCookBook().size() != 0) {
                for (Dish dish :  getCookBook()) {
                    String line = dish.getTitle() +
                            "\t" + dish.getPrice() +
                            "\t" + dish.getPreparationTime() +
                            "\t" + dish.getImage()+"\n";
                    writer.write(line);
                }
            } else {
                writer.write("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveOrderListToFile(){
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get("orderList.txt"))))) {
            if(getOrderList().size()!=0){
                for (Order order : getOrderList()) {
                    writer.println(order.getDishNumber()+
                            "\t"+order.getNumberOfDishes()+
                            "\t"+order.getTableNumber()+
                            "\t"+order.getOrderedTime()+
                            "\t"+order.getFulfilmentTime()+
                            "\t"+order.getFulfilmentTime());
                }
            }else{
                writer.println("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish parseDishLine(String line) throws DishOrderException {
        String[] parameterArray = line.split("\t");
        return  new Dish(parameterArray[0],
                BigDecimal.valueOf(Integer.parseInt(parameterArray[1])),
                Integer.parseInt(parameterArray[2]),
                parameterArray[3]);
    }

    public Order parseOrderLine(String line) throws DishOrderException {
        String[] parameterArray = line.split("\t");
        return new Order(Integer.parseInt(parameterArray[0]),
                Integer.parseInt(parameterArray[1]),
                Integer.parseInt(parameterArray[2]),
                LocalTime.parse(parameterArray[3]),
                (parameterArray[4].equals("null"))
                        ? null
                        : LocalTime.parse(parameterArray[4]),
                Boolean.parseBoolean(parameterArray[5]));
    }

    public void addOrdersToOrderList(Order... newOrders){
        for (Order newOrder:newOrders) {
            getOrderList().add(newOrder);
        }
    }
    public void removeDish(String title){
        for (int i = 0; i < getCookBook().size(); i++) {
            if(getCookBook().get(i).getTitle().equals(title)){
                getCookBook().remove(i);
                break;
            }
        }
    }
    public void addDishesToCookBook(Dish... newDishes){
        for (Dish newDish:newDishes) {
            if (!isDishTitleDuplicate(newDish.getTitle())) {
                getCookBook().add(newDish);
            }
        }
    }

    private boolean isDishTitleDuplicate(String title) {
        for (Dish dish : getCookBook()) {
            if (dish.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public List<Dish> getCookBook() {
        if(Objects.isNull(cookBook)){
            cookBook = new ArrayList<>();
        }
        return cookBook;
    }

    public void setCookBook(List<Dish> cookBook) {
        this.cookBook = cookBook;
    }

    public List<Order> getOrderList() {
        if(Objects.isNull(orderList)){
            orderList = new ArrayList<>();
        }
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
