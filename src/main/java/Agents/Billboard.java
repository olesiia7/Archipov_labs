package Agents;

import Agents.Item.Type;

import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;

import static Agents.Item.getTypeString;

public class Billboard extends Agent {
    private static TreeMap<Integer, Order> orderList = new TreeMap<>();
    private static int orderId = 0;

    public MobileRobot auction(List<MobileRobot> robots) {
        int a = 0; // Начальное значение диапазона - "от"
        int b = 100; // Конечное значение диапазона - "до"
        int maxPrice = 0;
        MobileRobot winner = null;
        for (MobileRobot robot : robots) {
            int price = a + (int) (Math.random() * b);
            System.out.println(robot.getId() + " - " + price + " у.е.");
            if (price > maxPrice) {
                maxPrice = price;
                winner = robot;
            }
        }
        System.out.println("Победил робот №" + winner.getId());
        return winner;
    }

    public enum Destination {
        STORAGE,
        STOREHOUSE,
        MOBILE_ROBOT
    }

    public Billboard() {
        System.out.println(" ==== ");
        System.out.println("Агент Доска объявлений был создан");
        System.out.println(" ==== ");
    }

    public TreeMap<Integer, Order> getOrderList() {
        return orderList;
    }

    /**
     * Размещение заказа
     *
     * @param to до
     * @return id заказа
     */
    public int placeOrder(Destination to, Item item) {
        return placeOrder(1, item.getType(), item.getDestination(), to, item);
    }

    public int placeOrder(int count, Type type, Destination from, Destination to, Item item) {
        Order order = new Order(count, to, from, type, item);
        orderList.put(orderId, order);
        System.out.println(getAgentName() + "Заказ №" + orderId + " размещен");
        System.out.println(getStatus());
        return orderId++;
    }

    public void deleteOrder(int id) {
        orderList.remove(id);
        System.out.println(getAgentName() + "Заказ №" + id + " выполнен");
        System.out.println(getStatus());
    }

    @Override
    protected String getStatus() {
        StringJoiner jn = new StringJoiner("\n");
        orderList.forEach((id, order) -> jn.add(id + " - " + order.toString()));
        return getAgentName() + "доступные объявления: {" + jn + "}";
    }

    @Override
    protected String getAgentName() {
        return "Доска объявлений: ";
    }

    public static String getDestinationString(Destination destination) {
        String destinationName = "";
        switch (destination) {
            case STORAGE:
                destinationName = "Место хранения";
                break;
            case STOREHOUSE:
                destinationName = "склад";
                break;
            case MOBILE_ROBOT:
                destinationName = "мобильный робот";
        }
        return destinationName;
    }

    static class Order {
        private final int count;
        private final Type type;
        private final Destination from;
        private final Destination to;
        private Item item;


        public Order(int count, Destination to, Destination from, Type type, Item item) {
            this.count = count;
            this.type = type;
            this.from = from;
            this.to = to;
            this.item = item;
        }

        public int getCount() {
            return count;
        }

        public Type getType() {
            return type;
        }

        public Destination getFrom() {
            return from;
        }

        public Destination getTo() {
            return to;
        }

        public Item getItem() {
            return item;
        }

        @Override
        public String toString() {
            return "Заказ{" +
                    "тип = " + getTypeString(type) +
                    ", количество = " + count +
                    ", " + getDestinationString(from) + " -> " + getDestinationString(to) +
                    '}';
        }
    }
}
