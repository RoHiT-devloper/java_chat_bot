import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Client2 extends JFrame{
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Declare components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);
    
    
    
    //constructor
    public Client2() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvent();

            startReading();
            //startWriting();

        } catch (Exception e) {
            // TODO: handling exception
        }
    }
    private void handleEvent(){
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e){

            }
            @Override
            public void keyPressed(KeyEvent e){

            }
            @Override
            public void keyReleased(KeyEvent e){
                //System.out.println("key released " +e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("You have clicked enter");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }
     

    private void createGUI(){
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        //frame 
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    //method of start reading
    public void startReading() {
        // thread-read karke deta rahega
        Runnable r1 = () -> {

            System.out.println("reader started...");
            try {
                while(true)
                {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server :" + msg);
                    messageArea.append("Server: "+msg+"\n");
                }
            } catch(Exception e) {
                //e.printStackTrace();
                System.out.println("Conncetion Close");
            }
        };
        new Thread(r1).start();
    }
    // start writing method
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("writer started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                    
                }
                System.out.println("Conncetion Close");
            } catch (Exception e) {
                //System.out.println("Conncetion Close");
                e.printStackTrace();
            }
            
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) 
    {
        System.out.println("this is client...");
        new Client2();
    }

}
