package VascoPanigi;

import VascoPanigi.entities.Customer;
import VascoPanigi.entities.Order;
import VascoPanigi.entities.Product;
import VascoPanigi.enums.Categories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Application {

    public static void main(String[] args) {

//        --------------------------------ex 1---------------------------

        List<Product> productList = getProducts();

        System.out.println(productList);


//        --------------------------------ex 2---------------------------
        List<Product> booksList = productList.stream()
                .filter(product -> product.getCategory().equals(Categories.BOOKS))
                .toList();
        System.out.println(booksList);

        //        --------------------------------ex 3---------------------------

        List<Product> boysProductList = productList.stream()
                .filter(product -> product.getCategory().equals(Categories.BOYS))
                .map(product -> {
                    product.setPrice(product.getPrice() * 0.9);
                    return product;
                }).
                toList();

        System.out.println(boysProductList);

        //        --------------------------------ex 4---------------------------


        List<Customer> customersList = getCustomersList();

        System.out.println(customersList);

        List<Order> orderList = placeOrders(customersList, productList);
        System.out.println("ORDERS: " + orderList);


        List<Product> productListFilteredByTierAndDate = getSpecificTierOrderList(orderList);
        System.out.println("TIER 2 ORDERS: " + productListFilteredByTierAndDate);

    }

    //        --------------------------------ex 1 method---------------------------

    private static List<Product> getProducts() {
        Random random = new Random();

        Supplier<Double> randomPriceSupplier = () -> random.nextDouble(1, 300);

        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);


        List<Product> productList = new ArrayList<>();

        Supplier<Product> productSupplier = () -> new Product(randomIdSupplier.get(), "Hello, I'm a book :D", randomCategory(), randomPriceSupplier.get());

        for (int i = 0; i < 100; i++) {
            Product newProduct = productSupplier.get();
            if (newProduct.getPrice() > 100) {
                productList.add(newProduct);
            }
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

        Supplier<Long> randomIdSupplier = () -> random.nextLong(10000, 20000);
        Supplier<Integer> randomTierSupplier = () -> random.nextInt(1, 3);

        List<Customer> customersList = new ArrayList<>();

        Supplier<Customer> customerSupplier = () -> new Customer(randomIdSupplier.get(), randomTierSupplier.get(), "pippo");

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
