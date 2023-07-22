import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader bfr;
    PrintWriter out;

    //declare compnents..
    private JLabel heading = new JLabel("Clienr Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messegeInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);




    public Client()
    {
        try{
            //System.out.println("sending request to Server..");
            socket = new Socket("127.0.0.1",6666);
            System.out.println("connection done....");


            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            //creating GUI...
            createGUI();
            handleEvent();
            StartReading();
            //StartWriting(); 

        
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void handleEvent(){

        messegeInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
              //  throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
              //  throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("key releasedd..."+ e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    //System.out.println("you have pressed enter button");
                    String contentTosend = messegeInput.getText();
                    messagArea.append("ME :"+contentTosend+"\n");
                    out.println(contentTosend);
                    out.flush();
                    messegeInput.setText("");
                    messegeInput.requestFocus();
                    messegeInput.isFocusable();

                }

               // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            }
            
        });
    }


    //GUI method code..
    private void createGUI()
    {
        this.setTitle("Client Messenger");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //coding for component...
        heading.setFont(font);
        messagArea.setFont(font);
        messegeInput.setFont(font);
        heading.setIcon(new ImageIcon("new_icon (4).jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messagArea.setEditable(false);
        
        //messegeInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        //adding component to frame...
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messegeInput,BorderLayout.SOUTH);

        this.setVisible(true);


    }



    //Start reading for the code....
    public void StartReading(){

         //for the readingg...
        Runnable run1=()->{
                System.out.println("Reader started...");

                try{
                while(true)
                {
                   
                    String messege = bfr.readLine();
                    if(messege.equals("exit"))
                    {
                        System.out.println("Server terminate the chat");
                        JOptionPane.showMessageDialog(this, "Server terminited..");
                        messegeInput.setEditable(false);
                        break;
                    }
                    System.out.println("Server : "+messege);
                    messagArea.append("server : "+ messege+"\n");
                }
            }
            catch(Exception e)
            {
                //e.printStackTrace();
                System.out.println("client closed the conversation.");
            }
        };

        new Thread(run1).start();

    }
    //Writing the code for client writer....
    public void StartWriting(){

        //thred for user to server client..
        System.out.println("Writer is started.....");
        Runnable run2=()->{

            try{

            while(!socket.isClosed())
            {
                

                    BufferedReader bfr1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = bfr1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

            }
            }
            
            catch(Exception e)
            {
               // e.printStackTrace();
               System.out.println("client closed the conversation.");
            }
            // if(bfr!=null)
            // {
            //     System.out.println("this is already closed ...");
            // }
            // else{
            //     return;
            // }
        };

        new Thread(run2).start();


    }

    public static void main(String[] args) {
        System.out.println("this is client....");
        new Client();
    }
}
