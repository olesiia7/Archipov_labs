package Agents;

import java.util.StringJoiner;
import java.util.TreeMap;

public class Billboard extends Agent {
    private static TreeMap<Integer, Order> orderList = new TreeMap<>();
    private static int orderId = 0;

    public enum Destination {
        STORAGE,
        MACHINE,
        STOREHOUSE
    }

    public enum Type {
        BILLET,
        DETAIL
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
     * @param count кол-во
     * @param type  тип
     * @param from  от
     * @param to    до
     * @return id заказа
     */
    public int placeOrder(int count, Type type, Destination from, Destination to) {
        Order order = new Order(count, type, from, to);
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

    class Order {
        private final int count;
        private final Type type;
        private final Destination from;
        private final Destination to;

        /**
         * Заказ на перевозку
         *
         * @param count кол-во
         * @param type  тип (деталь или заготовка)
         * @param from  откуда
         * @param to    куда
         */
        public Order(int count, Type type, Destination from, Destination to) {
            this.count = count;
            this.type = type;
            this.from = from;
            this.to = to;
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

        @Override
        public String toString() {
            return "Заказ{" +
                    "тип = " + getTypeString() +
                    ", количество = " + count +
                    ", " + getDestinationString(from) + " -> " + getDestinationString(to) +
                    '}';
        }

        private String getTypeString() {
            String typeName = "";
            switch (type) {
                case BILLET:
                    typeName = "заготовка";
                    break;
                case DETAIL:
                    typeName = "деталь";
                    break;
            }
            return typeName;
        }

        private String getDestinationString(Destination destination) {
            String destinationName = "";
            switch (destination) {
                case STORAGE:
                    destinationName = "Место хранения";
                    break;
                case MACHINE:
                    destinationName = "станок";
                    break;
                case STOREHOUSE:
                    destinationName = "склад";
                    break;
            }
            return destinationName;
        }
    }
}
