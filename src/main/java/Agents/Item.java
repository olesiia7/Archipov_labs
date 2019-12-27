package Agents;

import Agents.Billboard.Destination;

import java.util.List;

import static Agents.Billboard.Destination.STOREHOUSE;
import static Agents.Billboard.getDestinationString;

public class Item extends Agent {
    private Type type;
    private final int id;
    private Destination destination;
    private int mobileRobotId;

    public enum Type {
        BILLET,
        DETAIL
    }

    public Item(int id, Type type, Destination destination) {
        this.id = id;
        this.type = type;
        this.destination = destination;
        System.out.println(" ==== ");
        System.out.println("Агент Издение №" + id + " создан");
        System.out.println(getStatus());
        System.out.println(" ==== ");
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        this.destination = STOREHOUSE;
        System.out.println(getAgentName() + " теперь " + getTypeString(type));
        System.out.println(getAgentName() + "перемещена на склад");
    }

    public Destination getDestination() {
        return destination;
    }

    public int getMobileRobotId() {
        return mobileRobotId;
    }

    public void setMobileRobotId(int mobileRobotId) {
        this.mobileRobotId = mobileRobotId;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
        System.out.println(getAgentName() + "прибыла на " + getDestinationString(getDestination()));
    }

    public int getId() {
        return id;
    }

    /**
     * Организация транспортировки
     *
     * @param board   доска объявлений
     * @param to      место назначения
     * @param storage место хранения
     */
    public void getGoal(Billboard board, Destination to, List<MobileRobot> robots, Storage storage) {
        int orderId = board.placeOrder(to, this);
        MobileRobot executor = board.auction(robots);
        executor.getOrder(orderId, storage, board);
    }

    public static String getTypeString(Type type) {
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

    @Override
    public String getStatus() {
        return "Изделие-" + getTypeString(getType()) + " №" + id +
                ", местонахождение: " + getDestinationString(getDestination());
    }

    @Override
    protected String getAgentName() {
        return "Изделие " + id + ": ";
    }
}
