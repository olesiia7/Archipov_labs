package Agents;

import Agents.Billboard.Destination;
import Agents.Billboard.Order;

import static Agents.Billboard.Destination.*;

public class MobileRobot extends Agent {
    private final long detailCapacity;
    private final long billetCapacity;
    private final int id;
    private long detailCount;
    private long billetCount;

    /**
     * Агент Мобиьный робот
     *
     * @param id             id робота
     * @param billetCapacity максимальная вместимость заготовок
     * @param detailCapacity максимальная вместимость деталей
     */
    public MobileRobot(int id, long billetCapacity, long detailCapacity) {
        this.id = id;
        this.detailCapacity = detailCapacity;
        this.billetCapacity = billetCapacity;
        System.out.println(" ==== ");
        System.out.println("Агент Мобильный робот " + id + " создан со следующими параметрами:\n" + getStatus());
        System.out.println(" ==== ");
    }

    public int getId() {
        return id;
    }

    public boolean getOrder(int orderId, Storage storage, Billboard board) {
        System.out.println(getAgentName() + "получил заказ №" + orderId + " к исполнению");
        Order order = board.getOrderList().get(orderId);
        int count = order.getCount();
        Item item = order.getItem();
        boolean status;
        boolean done = false;
        switch (order.getType()) {
            case BILLET:
                status = checkBilletCapacity(count);
                if (!status) break;
                switch (order.getFrom()) {
                    case STOREHOUSE:
                        status = getBillet(count, item);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.getBillet(count);
                        if (!status) break;
                        status = getBillet(count, item);
                        if (!status) break;
                        break;
                }
                status = checkPutBilletAvailable(count);
                if (!status) break;
                switch (order.getTo()) {
                    case STOREHOUSE:
                        status = putBillet(count, item, STOREHOUSE);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.addBillet(count);
                        if (!status) break;
                        putBillet(count, item, STORAGE);
                        done = true;
                        break;
                }
                break;
            case DETAIL:
                status = checkDetailCapacity(count);
                if (!status) break;
                switch (order.getFrom()) {
                    case STOREHOUSE:
                        status = getDetail(count, item);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.getDetail(count);
                        if (!status) break;
                        getDetail(count, item);
                        break;
                }
                status = checkPutDetailAvailable(count);
                if (!status) break;
                switch (order.getTo()) {
                    case STOREHOUSE:
                        status = putDetail(count, item, STOREHOUSE);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.addDetail(count);
                        if (!status) break;
                        putDetail(count, item, STORAGE);
                        done = true;
                        break;
                }
                break;
        }
        if (done) {
            board.deleteOrder(orderId);
        } else {
            System.out.println(getAgentName() + "не удалось выполнить заказ");
        }
        return done;
    }

    /**
     * @return кол-во деталей, которые могут еще поместиться
     */
    private long getAvailableDetailCapacity() {
        return detailCapacity - detailCount;
    }

    /**
     * @return кол-во заготовок, которые могут еще поместиться
     */
    private long getAvailableBilletCapacity() {
        return billetCapacity - billetCount;
    }

    /**
     * @return текущей статус заполненности заготовками и деталями
     */
    @Override
    protected String getStatus() {
        return "Текущее состояние Мобильный робот " + id +
                " {\n\tЗаготовок: " + billetCount + "/" + billetCapacity +
                "\n\tДеталей: " + detailCount + "/" + detailCapacity + "}";
    }

    @Override
    protected String getAgentName() {
        return "Мобильный робот " + id + ": ";
    }

    /**
     * Получение Мобильным роботом новых деталей
     *
     * @param detailCount кол-во полученных деталей
     * @param item
     */
    protected boolean getDetail(int detailCount, Item item) {
        if (checkDetailCapacity(detailCount)) {
            this.detailCount += detailCount;
            System.out.println(getAgentName() + "Принято " + this.detailCount + " деталей");
            System.out.println(getStatus());
            if (item != null) {
                item.setMobileRobotId(getId());
                item.setDestination(MOBILE_ROBOT);
            }
            return true;
        }
        return false;
    }

    private boolean checkDetailCapacity(int detailCount) {
        long availableDetailCapacity = getAvailableDetailCapacity();
        if (availableDetailCapacity == 0) {
            System.out.println(getAgentName() + "Не могу принять детали, т.к. нет свободного места");
            return false;
        } else if (availableDetailCapacity < detailCount) {
            System.out.println(
                    getAgentName() + "Не могу принять " + detailCount + " деталей, т.к. есть место только для " +
                            availableDetailCapacity);
            return false;
        }
        return true;
    }

    /**
     * Получение Мобильным роботом новых заготовок
     *
     * @param billetCount кол-во полученных заготовок
     */
    protected boolean getBillet(int billetCount,  Item item) {
        if (checkBilletCapacity(billetCount)) {
            this.billetCount += billetCount;
            System.out.println(getAgentName() + "Принято " + billetCount + " заготовок");
            System.out.println(getStatus());
            if (item != null) {
                item.setMobileRobotId(getId());
                item.setDestination(MOBILE_ROBOT);
            }
            return true;
        }
        return false;
    }

    private boolean checkBilletCapacity(int billetCount) {
        long availableBilletCapacity = getAvailableBilletCapacity();
        if (availableBilletCapacity == 0) {
            System.out.println(getAgentName() + "Не могу принять заготовки, т.к. нет свободного места");
            return false;
        } else if (availableBilletCapacity < billetCount) {
            System.out.println(
                    getAgentName() + "Не могу принять " + billetCount + " заготовок, т.к. есть место только для " +
                            availableBilletCapacity);
            return false;
        }
        return true;
    }

    /**
     * Осуществляет отгрузку заготовок
     *
     * @param billetCount кол-во заготовок для отгрузки
     * @param item
     * @param to
     */
    protected boolean putBillet(int billetCount,  Item item, Destination to) {
        if (checkPutBilletAvailable(billetCount)) {
            this.billetCount -= billetCount;
            System.out.println(getAgentName() + "Отдано " + billetCount + " заготовок");
            System.out.println(getStatus());
            if (item != null) {
                item.setMobileRobotId(getId());
                item.setDestination(to);
            }
            return true;
        }
        return false;
    }

    private boolean checkPutBilletAvailable(int billetCount) {
        if (billetCount > this.billetCount) {
            System.out.println(getAgentName() + "Не могу отдать " + billetCount +
                    " заготовок, т.к. имеется только " + this.billetCount);
            return false;
        }
        return true;
    }

    /**
     * Осуществляет отгрузку деталей
     *
     * @param detailCount кол-во деталей для отгрузки
     * @param item
     * @param to
     */
    protected boolean putDetail(int detailCount,  Item item, Destination to) {
        if (checkPutDetailAvailable(detailCount)) {
            this.detailCount -= detailCount;
            System.out.println(getAgentName() + "Отдано " + detailCount + " деталей");
            System.out.println(getStatus());
            if (item != null) {
                item.setMobileRobotId(getId());
                item.setDestination(to);
            }
            return true;
        }
        return false;
    }

    private boolean checkPutDetailAvailable(int detailCount) {
        if (detailCount > this.detailCount) {
            System.out.println(getAgentName() + "Не могу отдать " + detailCount +
                    " деталей, т.к. имеется только " + this.detailCount);
            return false;
        }
        return true;
    }
}
