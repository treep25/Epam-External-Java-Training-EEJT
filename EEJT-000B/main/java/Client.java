import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000)) {
            Scanner scanner = new Scanner(System.in);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(bufferedReader.readLine());
            System.out.println(bufferedReader.readLine());
            while (true) {
                String request = scanner.nextLine();
                if (request.equals("get count")) {
                    printWriter.println(request);
                    System.out.println("Amount of products is - " + bufferedReader.readLine());

                } else if (Pattern.matches("get item = \\d", request)) {
                    printWriter.println(request);
                    System.out.println(bufferedReader.readLine());
                } else if (request.equals("")) {
                    printWriter.println(request);
                    break;
                } else {
                    System.out.println("Incorrect input <<");
                    printWriter.println(request);
                }
            }
        } catch (IOException ex) {
            System.out.println("Client exception " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

}
