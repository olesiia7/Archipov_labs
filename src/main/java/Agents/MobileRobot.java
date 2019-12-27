package Agents;

import Agents.Billboard.Order;

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

    public boolean getOrder(int orderId, Storage storage, Billboard board) {
        System.out.println(getAgentName() + "получил заказ №" + orderId + " к исполнению");
        Order order = board.getOrderList().get(orderId);
        int count = order.getCount();
        boolean status = true;
        boolean done = false;
        switch (order.getType()) {
            case BILLET:
                status = checkBilletCapacity(count);
                if (!status) break;
                switch (order.getFrom()) {
                    case STOREHOUSE:
                    case MACHINE:
                        status = getBillet(count);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.getBillet(count);
                        if (!status) break;
                        status = getBillet(count);
                        if (!status) break;
                        break;
                }
                status = checkPutBilletAvailable(count);
                switch (order.getTo()) {
                    case STOREHOUSE:
                    case MACHINE:
                        status = putBillet(count);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.addBillet(count);
                        if (!status) break;
                        putBillet(count);
                        done = true;
                        break;
                }
                break;
            case DETAIL:
                status = checkDetailCapacity(count);
                if (!status) break;
                switch (order.getFrom()) {
                    case STOREHOUSE:
                    case MACHINE:
                        status = getDetail(count);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.getDetail(count);
                        if (!status) break;
                        getDetail(count);
                        break;
                }
                status = checkPutDetailAvailable(count);
                switch (order.getTo()) {
                    case STOREHOUSE:
                    case MACHINE:
                        status = putDetail(count);
                        if (!status) break;
                        break;
                    case STORAGE:
                        status = storage.addDetail(count);
                        if (!status) break;
                        putDetail(count);
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
        return "Текущее состояние Мобильный робт " + id +
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
     */
    protected boolean getDetail(int detailCount) {
        if (checkDetailCapacity(detailCount)) {
            this.detailCount += this.detailCount;
            System.out.println(getAgentName() + "Принято " + this.detailCount + " деталей");
            System.out.println(getStatus());
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
    protected boolean getBillet(int billetCount) {
        if (checkBilletCapacity(billetCount)) {
            this.billetCount += billetCount;
            System.out.println(getAgentName() + "Принято " + billetCount + " заготовок");
            System.out.println(getStatus());
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
     */
    protected boolean putBillet(int billetCount) {
        if (checkPutBilletAvailable(billetCount)) {
            this.billetCount -= billetCount;
            System.out.println(getAgentName() + "Отдано " + billetCount + " заготовок");
            System.out.println(getStatus());
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
     */
    protected boolean putDetail(int detailCount) {
        if (checkPutDetailAvailable(detailCount)) {
            this.detailCount -= detailCount;
            System.out.println(getAgentName() + "Отдано " + detailCount + " деталей");
            System.out.println(getStatus());
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
