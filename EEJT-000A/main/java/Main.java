import task1.FileView;
import task2.InputValidation;
import task2.SearchingController;
import task2.Parameters;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        Scanner scanner1 = new Scanner(System.in);
        FileView fileView = new FileView("UTF8.txt");

        System.out.println("--------------------TASK1--------------------");
        System.out.println(fileView.readFile());
        System.out.println("---------------------------------------------");

        System.out.println("--------------------TASK2--------------------");
        Parameters parameters = new Parameters();
        System.out.println("> искать по имени файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите имя файла");
            String name = scanner1.nextLine();
            while (!InputValidation.isNameValid(name)){
                System.out.println("Не корректный ввод , еще раз");
                name = scanner1.nextLine();
            }
            parameters.setName(name);
        }
        System.out.println("> искать по расширению файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите расширение .abc");
            String ext = scanner1.nextLine();
            while (!InputValidation.isExtValid(ext)){
                System.out.println("Не корректный ввод , еще раз");
                ext = scanner1.nextLine();
            }
            parameters.setExt(ext);
        }
        System.out.println("> искать по диапазону дат изменения файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите диапозон , например 2012-11-16 20:48:16");
            String less = scanner1.nextLine();
            String more = scanner1.nextLine();
            parameters.setDateLess(less);
            parameters.setDateMore(more);
        }
        System.out.println("> искать по диапазону размеров файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите диапозон  , например 123");
            String less = scanner1.nextLine();
            String more = scanner1.nextLine();
            while (!InputValidation.isSizeValid(less,more)){
                System.out.println("Не корректный ввод , еще раз");
                less = scanner1.nextLine();
                more = scanner1.nextLine();
            }
            parameters.setSizeLess(less);
            parameters.setSizeMore(more);
        }
        SearchingController searchingController = new SearchingController(parameters);
        List<String> list = searchingController.doChain(new ArrayList<>());
        list.forEach(System.out::println);
        System.out.println("---------------------------------------------");
    }
}
