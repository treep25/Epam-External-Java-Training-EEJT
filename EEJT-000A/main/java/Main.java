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
        Scanner scanner = new Scanner(System.in);
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("--------------------TASK1--------------------");
        FileView fileView = new FileView("UTF8.txt");
        List<String> text = fileView.readFile();
        for (String a :
                text) {
            System.out.println(a);
        }
        System.out.println("---------------------------------------------");
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.out.println("--------------------TASK2--------------------");

        String name = null;
        String extension = null;
        String dateLess = null;
        String dateMore = null;
        String sizeLess = null;
        String sizeMore = null;

        System.out.println("> искать по имени файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите имя файла");
            name = scanner1.nextLine();
            while (!InputValidation.isNameValid(name)) {
                System.out.println("Не корректный ввод , еще раз");
                name = scanner1.nextLine();
            }
        }
        System.out.println("> искать по расширению файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите расширение .abc");
            extension = scanner1.nextLine();
            while (!InputValidation.isExtValid(extension)) {
                System.out.println("Не корректный ввод , еще раз");
                extension = scanner1.nextLine();
            }
        }
        System.out.println("> искать по диапазону дат изменения файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите диапозон , например 2012-11-16 20:48:16");
            dateLess = scanner1.nextLine();
            dateMore = scanner1.nextLine();
        }
        System.out.println("> искать по диапазону размеров файла ? (0/1)");
        if (scanner.nextInt() == 1) {
            System.out.println("Введите диапозон  , например 123");
            sizeLess = scanner1.nextLine();
            sizeMore = scanner1.nextLine();
            while (!InputValidation.isSizeValid(sizeLess, sizeMore)) {
                System.out.println("Не корректный ввод , еще раз");
                sizeLess = scanner1.nextLine();
                sizeMore = scanner1.nextLine();
            }
        }
        Parameters parameters = new Parameters(name, extension, dateLess, dateMore, sizeLess, sizeMore);
        SearchingController searchingController = new SearchingController(parameters);
        List<String> list = searchingController.doChain();
        list.forEach(System.out::println);
        System.out.println("---------------------------------------------");
    }
}
