import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3000);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        System.out.println(bufferedReader.readLine());
        System.out.println(bufferedReader.readLine());
        printWriter.println(scanner.nextLine());
        printWriter.flush();

        while (true) {
            System.out.println(bufferedReader.readLine());
            printWriter.println(scanner.nextLine());
            printWriter.flush();

        }
    }
}
