import Agents.Billboard;
import Agents.MobileRobot;
import Agents.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

import static Agents.Billboard.Destination.STORAGE;
import static Agents.Billboard.Destination.STOREHOUSE;
import static Agents.Billboard.Type.BILLET;

public class Start_4_lab {
    public static void main(String[] args) {
        System.out.println("\nСоздание Агента Место хранение");
        System.out.println("Введите параметра для создания Агента Места хранения");
        Scanner scan = new Scanner(System.in, "utf-8");
        System.out.print("\tМаксимальная вместимость заготовок, шт: ");
        long billetCapacity = scan.nextLong();
        System.out.print("\tМаксимальная вместимость деталей, шт: ");
        long detailCapacity = scan.nextLong();
        Storage storage = new Storage(detailCapacity, billetCapacity);

        System.out.println("\nСоздание Агента Мобильный робот");
        System.out.println("Введите параметра для создания Агента Мобильный робот");
        System.out.print("\tМаксимальная вместимость заготовок, шт: ");
        billetCapacity = scan.nextLong();
        System.out.print("\tМаксимальная вместимость деталей, шт: ");
        detailCapacity = scan.nextLong();
        MobileRobot mobileRobot = new MobileRobot(0, detailCapacity, billetCapacity);

        Billboard board = new Billboard();

        StringJoiner jn = new StringJoiner("\n");
        getAvailableActions().forEach(jn::add);
        System.out.println("Выберите действие: \n" + jn.toString());
        while (true) {
            if (scan.hasNext()) {
                String action = scan.nextLine();
                // привезти заготовки на Место хранения в количестве X шт
                if (action.matches("[MМ]1 \\d+\n?")) {
                    int newBillet = Integer.parseInt(action.substring(3));
                    int orderId = board.placeOrder(newBillet, BILLET, STOREHOUSE, STORAGE);
                    mobileRobot.getOrder(orderId, storage, board);
                } else if (action.matches("[MМ]1 -\\d+")) {
                    int billets = Integer.parseInt(action.substring(4));
                    storage.getBillet(billets);
                } else if (action.matches("[MМ]2 \\d+")) {
                    int newDetail = Integer.parseInt(action.substring(3));
                    storage.addDetail(newDetail);
                } else if (action.matches("[MМ]2 -\\d+")) {
                    int details = Integer.parseInt(action.substring(4));
                    storage.getDetail(details);
                } else if (action.equals("0")) {
                    System.out.println("Программа завершена");
                    break;
                } else if (action.equals("Помощь") || action.equals("Help")) {
                    System.out.println("Выберите действие: \n" + jn.toString());
                } else {
                    if (!action.equals("")) {
                        System.out.println("Команда \"" + action + "\" не распознана");
                        System.out.println("Выберите действие: \n" + jn.toString());
                    }
                }
            }
        }
    }

    private static List<String> getAvailableActions() {
        List<String> actions = new ArrayList<>();
        actions.add("М1 X - привезти заготовки на Место хранения в количестве X шт");
        actions.add("М1 -X - забрать с Места хранения заготовки в количестве X шт");
        actions.add("M2 X - привезти детали на Место хранения в количестве X шт");
        actions.add("М2 -X - забрать с Места хранения на склад детали в количестве X шт");
        actions.add("0 - Завершение программы");
        actions.add("Help - вывести список доступных команд");
        return actions;
    }
}
