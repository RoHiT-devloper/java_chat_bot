import java.io.*;
import java.net.*;

public class Client {
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending reequest to sercer");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // TODO: handling exception
        }
    }

    public void startReading() {
        // thread-read karke deta rahega
        Runnable r1 = () -> {

            System.out.println("reader started...");
            while (true)
                try {
                    {
                        String msg = br.readLine();
                        if (msg.equals("exit")) {
                            System.out.println("Server terminated the chat");
                            socket.close();
                            break;
                        }
                        System.out.println("Server :" + msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("writer started...");

            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
    }

}
