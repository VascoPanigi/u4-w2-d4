package VascoPanigi;

import VascoPanigi.entities.Customer;
import VascoPanigi.entities.Order;
import VascoPanigi.entities.Product;
import VascoPanigi.enums.Categories;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {

//        -------------------------------- product creation---------------------------------------------------

        List<Product> productList = getProducts();

        System.out.println("Products: \n" + productList);
        System.out.println();


//        --------------------------------filter products by category and price -----------------------------
        List<Product> booksList = productList.stream()
                .filter(product -> product.getCategory().equals(Categories.BOOKS) && product.getPrice() > 100)
                .toList();
        System.out.println("Book list: \n" + booksList);
        System.out.println();

        //        ------------------------filter products by category and apply discount ---------------------------

        List<Product> boysProductList = productList.stream()
                .filter(product -> product.getCategory().equals(Categories.BOYS))
                .map(product -> {
                    product.setPrice(product.getPrice() * 0.9);
                    return product;
                }).
                toList();

        System.out.println("Boys products: \n" + boysProductList);
        System.out.println();


        //        ---------------create customers, create orders and filter orders by customer's tier---------------------------


        List<Customer> customersList = getCustomersList();

        System.out.println("Customers: \n" + customersList);
        System.out.println();


        List<Order> orderList = placeOrders(customersList, productList);
        System.out.println("Order: \n" + orderList);
        System.out.println();


        List<Product> productListFilteredByTierAndDate = getSpecificTierOrderList(orderList);
        System.out.println("Tier 2 orders: \n" + productListFilteredByTierAndDate);
        System.out.println();


//        TODO 1: Raggruppare gli ordini per cliente utilizzando Stream e Lambda Expressions.
//         Crea una mappa in cui la chiave è il cliente e il valore è una lista di ordini
//         effettuati da quel cliente

        //        ---------------filter orders by client---------------------------

        Map<Customer, List<Order>> ordersByCustomer = orderList.stream().collect(Collectors.groupingBy(Order::getCustomer));

        System.out.println("Orders filtered per customer: \n" + ordersByCustomer);
        System.out.println();

//        TODO 2: Dato un elenco di ordini, calcola il totale delle vendite per ogni cliente
//         utilizzando Stream e Lambda Expressions.
//         Crea una mappa in cui la chiave è il cliente e il valore è l'importo totale dei suoi acquisti

        Map<Customer, Double> totalOrderByCustomer = orderList.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(Order::getTotal)));

        System.out.println("Total price order per customer: \n" + totalOrderByCustomer);
        System.out.println();

//        TODO 3: Dato un elenco di prodotti, trova i prodotti più costosi utilizzando Stream e Lambda Expressions

        List<Product> mostExpensiveProducts = productList.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).limit(5).toList();
        System.out.println("Most 5 expensive products \n" + mostExpensiveProducts);
        System.out.println();

//        TODO 4: Dato un elenco di ordini, calcola la media degli importi degli ordini utilizzando Stream e Lambda Expressions
        
//        TODO 5: Dato un elenco di prodotti, raggruppa i prodotti per categoria e calcola la somma degli importi per ogni categoria
//         utilizzando Stream e Lambda Expressions.

//        TODO 6: Usando la classe Apache Commons IO FileUtils implementare un metodo salvaProdottiSuDisco che salvi su disco un
//         file contenente la lista dei prodotti. Utilizzare un formato simile al seguente per storicizzare i dati su file:
//         nomeprodotto1@categoriaprodotto1@prezzoprodotto1#nomeprodotto2@categoriaprodotto2@prezzoprodotto2

//        TODO 7: Sempre usando la classe Apache Commons IO FileUtils implementare un metodo leggiProdottiDaDisco che riempia un
//         ArrayList con il contenuto del file salvato al punto 6


    }

    //        -------------------------------- methods---------------------------

    private static List<Product> getProducts() {
        Random random = new Random();
        Faker faker = new Faker(Locale.ENGLISH);

        Supplier<Double> randomPriceSupplier = () -> random.nextDouble(1, 300);

        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);


        List<Product> productList = new ArrayList<>();

        Supplier<Product> productSupplier = () -> new Product(randomIdSupplier.get(), faker.book().title(), randomCategory(), randomPriceSupplier.get());

        for (int i = 0; i < 100; i++) {
            Product newProduct = productSupplier.get();
            productList.add(newProduct);
        }
        return productList;
    }


    private static Categories randomCategory() {
        Random random = new Random();
        int randomNum = random.nextInt(1, 4);
        Categories category = Categories.BOOKS;

        switch (randomNum) {
            case 2:
                category = Categories.BABY;
                break;
            case 3:
                category = Categories.BOYS;
                break;
            default:
                break;
        }

        return category;
    }

    public static List<Customer> getCustomersList() {
        Random random = new Random();
        Faker faker = new Faker(Locale.ENGLISH);


        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);
        Supplier<Integer> randomTierSupplier = () -> random.nextInt(1, 3);

        List<Customer> customersList = new ArrayList<>();

        Supplier<Customer> customerSupplier = () -> new Customer(randomIdSupplier.get(), randomTierSupplier.get(), faker.gameOfThrones().character());

        for (int i = 0; i < 100; i++) {
            Customer newCustomer = customerSupplier.get();

            customersList.add(newCustomer);

        }
        return customersList;
    }


    public static List<Order> placeOrders(List<Customer> customersList, List<Product> productList) {
        Random random = new Random();

        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);

        Supplier<Integer> randomLengthSupplier = () -> random.nextInt(1, 6);
        Supplier<Integer> randomProductIndexSupplier = () -> random.nextInt(0, 50);


        List<Order> orderList = new ArrayList<>();
        for (Customer currentCustomer : customersList) {
            List<Product> currentClientProductList = new ArrayList<>();
            for (int i = 0; i < randomLengthSupplier.get(); i++) {
                currentClientProductList.add(productList.get(randomProductIndexSupplier.get()));
            }
            Order currentOrder = new Order(randomIdSupplier.get(), currentClientProductList, currentCustomer);
            orderList.add(currentOrder);
        }
        return orderList;
    }

    public static List<Product> getSpecificTierOrderList(List<Order> orderList) {
        List<Order> filteredByDateAndTier = orderList.stream().filter(order -> order.getCustomer().getTier() == 2
                        && order.getOrderDate().isBefore(LocalDate.parse("2024-06-29"))
                        && order.getOrderDate().isAfter(LocalDate.parse("2024-05-29")))
                .toList();

        List<Product> products = new ArrayList<>();
        for (Order order : filteredByDateAndTier) {
            products.addAll(order.getProducts());
        }
        return products;
    }
}
