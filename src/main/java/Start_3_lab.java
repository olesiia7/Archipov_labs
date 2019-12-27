import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class Start_3_lab {
    public static void main(String[] args) {
        System.out.println("\nВведите параметра для создания Агента Места хранения");
        Scanner scan = new Scanner(System.in,"utf-8");
        System.setProperty("console.encoding","utf-8");
        System.out.print("Максимальная вместимость заготовок, шт: ");
        long billetCapacity = scan.nextLong();
        System.out.print("Максимальная вместимость деталей, шт: ");
        long detailCapacity = scan.nextLong();
        Storage storage = new Storage(detailCapacity, billetCapacity);
        StringJoiner jn = new StringJoiner("\n");
        getAvailableActions().forEach(jn::add);
        System.out.println("Выберите действие: \n" + jn.toString());
        while (true) {
            if (scan.hasNext()) {
                String action = scan.nextLine();
                if (action.matches("[MМ]1 \\d+\n?")) {
                    long newBillet = Long.parseLong(action.substring(3));
                    storage.addBillet(newBillet);
                } else if (action.matches("[MМ]1 -\\d+")) {
                    long billets = Long.parseLong(action.substring(4));
                    storage.getBillet(billets);
                } else if (action.matches("[MМ]2 \\d+")) {
                    long newDetail = Long.parseLong(action.substring(3));
                    storage.addDetail(newDetail);
                } else if (action.matches("[MМ]2 -\\d+")) {
                    long details = Long.parseLong(action.substring(4));
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
        actions.add("М1 X - Мобильный робот привез заготовки в количестве X шт");
        actions.add("М1 -X - Мобильный робот забрал с Места хранения заготовки в количестве X шт");
        actions.add("M2 X - Мобильный робот привез детали в количестве X шт");
        actions.add("М2 -X - Мобильный робот забрал с Места хранения детали в количестве X шт");
        actions.add("0 - Завершение программы");
        actions.add("Help - вывести список доступных команд");
        return actions;
    }
}
