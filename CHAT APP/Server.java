import java.net.*;

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
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
class Server extends JFrame
{

    ServerSocket server;
    Socket socket;
    BufferedReader bfr;
    PrintWriter out;



     //declare compnents..
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messegeInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);


    public Server ()
    {
        try
        {
        server = new ServerSocket(6666);
        //System.out.println("server is ready to acccept the connection...");
       // System.out.println("waiting for connecction");
        socket = server.accept();

        bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());


        //Creating GUI..
        createGUI();
         handleEvent();
         StartReading();
        // StartWriting();

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
                //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
               // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
               // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");

                if(e.getKeyCode()==10)
                {
                    //System.out.println("you have pressed enter button");
                    String contentTosend = messegeInput.getText();
                    messagArea.append("YOU :"+contentTosend+"\n");
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
        this.setTitle("Server Messenger");
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



    public void StartReading()
    {
        //for the readingg...
        Runnable run1=()->{
                System.out.println("Reader started...");
            try{
                while(true)
                {
                    
                    String messege = bfr.readLine();
                    if(messege.equals("exit"))
                    {
                        System.out.println("client terminate the chat");
                        JOptionPane.showMessageDialog(this, "Client terminated");
                        messegeInput.setEnabled(false);
                        //socket.close();
                        break;
                    }
                    System.out.println("Client : "+messege);
                    messagArea.append("Client :"+messege+"\n");

                }
            } 
            catch(Exception e)
            {
               // e.printStackTrace();
               System.out.println("Server closed the conversation.");
            }
        };

        new Thread(run1).start();
    }

    public void StartWriting()
    {
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
           System.out.println("server closed the conversation.");
        }
        // if(bfr!=null)
        //     {
        //         System.out.println("this is already closed ...");
        //     }
        //     else{
        //         return;
        //     }

        };

        new Thread(run2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is serverr going to start ...");
        new Server();
    }
}
