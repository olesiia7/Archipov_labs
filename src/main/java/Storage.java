public class Storage {
    private final long detailCapacity;
    private final long billetCapacity;
    private long detailCount;
    private long billetCount;

    /**
     * Агент Место хранения
     *
     * @param detailCapacity максимальная вместимость деталей
     * @param billetCapacity максимальная вместимость заготовок
     */
    protected Storage(long detailCapacity, long billetCapacity) {
        this.detailCapacity = detailCapacity;
        this.billetCapacity = billetCapacity;
        System.out.println(" ==== ");
        System.out.println("Агент Место хранения создан со следующими параметрами:\n" + getStatus());
        System.out.println(" ==== ");
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
    protected String getStatus() {
        return "Текущее состояние Места хранения {\n\tЗаготовок: " + billetCount + "/" + billetCapacity +
                "\n\tДеталей: " + detailCount + "/" + detailCapacity + "}";
    }

    /**
     * Получение Местом хранение новых заготовок
     *
     * @param billetCount кол-во полученных заготовок
     */
    protected void addBillet(long billetCount) {
        long availableBilletCapacity = getAvailableBilletCapacity();
        if (availableBilletCapacity == 0) {
            System.out.println(getAgentName() + "Не могу принять заготовки, т.к. нет свободного места");
        } else if (availableBilletCapacity < billetCount) {
            System.out.println(
                    getAgentName() + "Не могу принять " + billetCount + " заготовок, т.к. есть место только для " +
                            availableBilletCapacity);
        } else {
            this.billetCount += billetCount;
            System.out.println(getAgentName() + "Принято " + billetCount + " заготовок");
            System.out.println(
                    getAgentName() + "Отправляю сигнал стационарному роботу о готовности " + this.billetCount +
                            " заготовок к обработке");
            System.out.println(getStatus());
        }
        checkStatus();
    }

    /**
     * Получение Местом хранение новых деталей
     *
     * @param detailCount кол-во полученных деталей
     */
    protected void addDetail(long detailCount) {
        long availableDetailCapacity = getAvailableDetailCapacity();
        if (availableDetailCapacity == 0) {
            System.out.println(getAgentName() + "Не могу принять детали, т.к. нет свободного места");
        } else if (availableDetailCapacity < detailCount) {
            System.out.println(
                    getAgentName() + "Не могу принять " + detailCount + " деталей, т.к. есть место только для " +
                            availableDetailCapacity);
        } else {
            this.detailCount += detailCount;
            System.out.println(getAgentName() + "Принято " + detailCount + " деталей");
            System.out.println(getStatus());
        }
        checkStatus();
    }

    /**
     * Проверяет состояние места хранения, в зависимости от ситуацию отправляет сигналы другим участникам
     */
    private void checkStatus() {
        long availableBilletCapacity = getAvailableBilletCapacity();
        long availableDetailCapacity = getAvailableDetailCapacity();
        if (availableBilletCapacity == 0) {
            System.out.println(getAgentName() +
                    "Нет места для заготовок -> посылаю сигнал стационарному роботу, чтобы он срочно отправил заготовки на далнейшую обработку");
        } else if (availableBilletCapacity == billetCapacity) {
            System.out.println(getAgentName() +
                    "Нет заготовок -> посылаю сигнал мобильному роботу о том, что необходимо получить " +
                    billetCapacity + " заготовок");
        }
        if (availableDetailCapacity == 0) {
            System.out.println(getAgentName() +
                    "Нет места для деталей -> посылаю сигнал мобильному роботу, чтобы он отправил готовые детали на склад");
        } else if (availableDetailCapacity == detailCapacity) {
            System.out.println(getAgentName() +
                    "Нет готовых деталей -> посылаю сигнал мобильному роботу о том, есть место для складирования готовых деталей");
        }
    }

    /**
     * Осуществляет отгрузку заготовок
     *
     * @param billetCount кол-во заготовок для отгрузки
     */
    protected void getBillet(long billetCount) {
        if (billetCount > this.billetCount) {
            System.out.println(getAgentName() + "Не могу отдать " + billetCount +
                    " заготовок, т.к. имеется только " + this.billetCount);
        } else {
            this.billetCount -= billetCount;
            System.out.println(getAgentName() + "Отдано " + billetCount + " заготовок");
            System.out.println(getStatus());
        }
        checkStatus();
    }
    /**
     * Осуществляет отгрузку деталей
     *
     * @param detailCount кол-во деталей для отгрузки
     */
    protected void getDetail(long detailCount) {
        if (detailCount > this.detailCount) {
            System.out.println(getAgentName() + "Не могу отдать " + detailCount +
                    " деталей, т.к. имеется только " + this.detailCount);
        } else {
            this.detailCount -= detailCount;
            System.out.println(getAgentName() + "Отдано " + detailCount + " деталей");
            System.out.println(getStatus());
        }
        checkStatus();
    }

    private String getAgentName() {
        return "Место хранения: ";
    }
}
