import Agents.Billboard;
import Agents.Billboard.Destination;
import Agents.Item;
import Agents.MobileRobot;
import Agents.Storage;

import java.util.*;

import static Agents.Billboard.Destination.*;
import static Agents.Billboard.getDestinationString;
import static Agents.Item.Type.BILLET;
import static Agents.Item.Type.DETAIL;

public class Start_5_lab {
    private static int id = 0;
    private static TreeMap<Integer, Item> items = new TreeMap<>();
    private static List<MobileRobot> robots = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in, "utf-8");
        System.out.println("\nСоздание Агента Место хранение");
        System.out.println("Введите параметра для создания Агента Места хранения");
        System.out.print("\tМаксимальная вместимость заготовок, шт: ");
        long billetCapacity = scan.nextLong();
        System.out.print("\tМаксимальная вместимость деталей, шт: ");
        long detailCapacity = scan.nextLong();
        Storage storage = new Storage(detailCapacity, billetCapacity);

        System.out.println("\nСоздание Агентов Мобильных роботов");
        System.out.println("\tВведите количество Мобильных роботов");
        int robotCounts = scan.nextInt();
        System.out.println("Введите параметра для создания Агента Мобильный робот");
        System.out.print("\tМаксимальная вместимость заготовок, шт: ");
        billetCapacity = scan.nextLong();
        System.out.print("\tМаксимальная вместимость деталей, шт: ");
        detailCapacity = scan.nextLong();
        for (int i = 0; i < robotCounts; i++) {
            MobileRobot mobileRobot = new MobileRobot(i, billetCapacity, detailCapacity);
            robots.add(mobileRobot);
        }

        Billboard board = new Billboard();

        StringJoiner jn = new StringJoiner("\n");
        getAvailableActions().forEach(jn::add);
        System.out.println("Выберите действие: \n" + jn.toString());
        while (true) {
            if (scan.hasNext()) {
                String action = scan.nextLine();
                // создать Изделие
                if (action.matches("\\+")) {
                    createItem();
                    // показать доступные изделия
                } else if (action.equals("show_items")) {
                    showItems();
                    // X D - сделать изделие с id X деталью
                } else if (action.matches("\\d+ D")) {
                    int itemId = getItemId(action);
                    Item item = checkItemStates(itemId, null);
                    if (item == null) {
                        System.out.println("Выберите действие: \n" + jn.toString());
                        showItems();
                        break;
                    }
                    item.setType(DETAIL);
                    // X Z - сделать изделие с id X заготовкой
                } else if (action.matches("\\d+ Z")) {
                    int itemId = getItemId(action);
                    Item item = checkItemStates(itemId, null);
                    if (item == null) {
                        System.out.println("Выберите действие: \n" + jn.toString());
                        showItems();
                        break;
                    }
                    item.setType(BILLET);
                    // X M - привезти изделие с id X на Место хранения
                } else if (action.matches("\\d+ [MМ]")) {
                    int itemId = getItemId(action);
                    Item item = checkItemStates(itemId, STORAGE);
                    if (item == null) {
                        System.out.println("Выберите действие: \n" + jn.toString());
                        showItems();
                        break;
                    }
                    item.getGoal(board, STORAGE, robots, storage);
                    // X H - привезти изделие с id X на склад
                } else if (action.matches("\\d+ [НH]")) {
                    int itemId = getItemId(action);
                    Item item = checkItemStates(itemId, STOREHOUSE);
                    if (item == null) {
                        System.out.println("Выберите действие: \n" + jn.toString());
                        showItems();
                        break;
                    }
                    item.getGoal(board, STOREHOUSE, robots, storage);
                } else if (action.equals("0")) {
                    System.out.println("Программа завершена");
                    scan.close();
                    break;
                } else if (action.equals("Помощь") || action.equals("Help")) {
                    System.out.println("Выберите действие: \n" + jn.toString());
                    showItems();
                } else {
                    if (!action.equals("")) {
                        System.out.println("Команда \"" + action + "\" не распознана");
                        System.out.println("Выберите действие: \n" + jn.toString());
                    }
                }
            }
        }
    }

    private static Item checkItemStates(int itemId, Destination destination) {
        Item item = items.get(itemId);
        if (item == null) {
            System.out.println("Изделия №" + itemId + " не существует");
            return null;
        }
        if (destination != null) {
            if (item.getDestination() == destination) {
                System.out
                        .println("Изделие №" + item.getId() + " уже располагается на " +
                                getDestinationString(destination));
                return null;
            }
            if (item.getDestination() == MOBILE_ROBOT) {
                System.out
                        .println("Изделие №" + item.getId() + " уже располагается у робота " + item.getMobileRobotId());
                return null;
            }
        }
        return item;
    }

    private static int getItemId(String action) {
        return Integer.parseInt(action.substring(0, action.indexOf(" ")));
    }

    private static List<String> getAvailableActions() {
        List<String> actions = new ArrayList<>();
        actions.add("+ - создать изделие - заготовку");
        actions.add("X D - сделать изделие с id X деталью (переместится на склад)");
        actions.add("X Z - сделать изделие с id X заготовкой (переместится на склад)");
        actions.add("show_items - показать доступные изделия");
        actions.add("X M - привезти изделие с id X на Место хранения");
        actions.add("X H - привезти изделие с id X на склад");
        actions.add("0 - Завершение программы");
        actions.add("Help - вывести список доступных команд");
        return actions;
    }

    private static void createItem() {
        Item item = new Item(id, BILLET, STOREHOUSE);
        items.put(id, item);
        id++;
    }

    private static void showItems() {
        StringJoiner jn = new StringJoiner("\n");
        items.forEach((id, item) -> jn.add(item.getStatus()));
        System.out.println("Доступные изделия: \n" + jn.toString());
    }
}
