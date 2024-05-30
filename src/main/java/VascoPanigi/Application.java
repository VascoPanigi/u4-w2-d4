package VascoPanigi;

import VascoPanigi.entities.Customer;
import VascoPanigi.entities.Order;
import VascoPanigi.entities.Product;
import VascoPanigi.enums.Categories;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

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

    }

    //        -------------------------------- methods---------------------------

    private static List<Product> getProducts() {
        Random random = new Random();

        Supplier<Double> randomPriceSupplier = () -> random.nextDouble(1, 300);

        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);


        List<Product> productList = new ArrayList<>();

        Supplier<Product> productSupplier = () -> new Product(randomIdSupplier.get(), "Hello, I'm a book :D", randomCategory(), randomPriceSupplier.get());

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

        List<Product> currentClientProductList = new ArrayList<>();

        for (int i = 0; i < randomLengthSupplier.get(); i++) {
            currentClientProductList.add(productList.get(randomProductIndexSupplier.get()));
        }

        List<Order> orderList = new ArrayList<>();
        for (Customer currentCustomer : customersList) {
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
