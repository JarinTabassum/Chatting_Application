package chatting.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server implements ActionListener {
    JTextField tf; //global declaration so that we can use the actionPerformed of this TextField
    JPanel text_panel;
    static Box vertical=Box.createVerticalBox(); //so that the text boxes appear one down after the other
    static JFrame frame=new JFrame();
    static DataOutputStream dout;
    Server(){
        //panel
        JPanel panel1=new JPanel();
        panel1.setBackground(new Color(128,0,128));
        panel1.setBounds(0,0,450,70); //will start from top left
        panel1.setLayout(null);


        //image(back arrow image)
        ImageIcon image1=new ImageIcon(ClassLoader.getSystemResource("arrow.png"));
        Image image2= image1.getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT); //shaping the image appropriate to the size
        ImageIcon image3=new ImageIcon(image2); //have to make image2 an ImageIcon object before adding
        JLabel back_button=new JLabel(image3);
        back_button.setBounds(5,20,25,25); //x means how left from 0 from x-axis and y means how down is from 0 from y-axis and width and height is for the image
        panel1.add(back_button);
        //image(server pfp)
        ImageIcon image4=new ImageIcon(ClassLoader.getSystemResource("ServerPFP.png"));
        Image image5=image4.getImage().getScaledInstance(50,35,Image.SCALE_DEFAULT);
        ImageIcon image6=new ImageIcon(image5);
        JLabel server_pfp=new JLabel(image6);
        server_pfp.setBounds(40,20,50,35);
        panel1.add(server_pfp);
        //image(video call image)
        ImageIcon image7=new ImageIcon(ClassLoader.getSystemResource("video.png"));
        Image image8=image7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon image9=new ImageIcon(image8);
        JLabel video_call=new JLabel(image9);
        video_call.setBounds(280,20,30,30);
        panel1.add(video_call);
        //image(phone call image)
        ImageIcon image10=new ImageIcon(ClassLoader.getSystemResource("phone.png"));
        Image image11=image10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon image12=new ImageIcon(image11);
        JLabel phone_call=new JLabel(image12);
        phone_call.setBounds(340,20,35,30);
        panel1.add(phone_call);
        //image(3 dots)
        ImageIcon image13=new ImageIcon(ClassLoader.getSystemResource("3icon.png"));
        Image image14=image13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon image15=new ImageIcon(image14);
        JLabel dots=new JLabel(image15);
        dots.setBounds(400,22,10,25);
        panel1.add(dots);


        //implementing the back arrow button
        back_button.addMouseListener(new MouseAdapter(){ //this adapter will help us to use only the desired method we wanna use rather then implementing all methods of MouseListener
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); //the window will close if back arrow button is clicked
            }
        });

        //Server's name
        JLabel name=new JLabel("Server");
        name.setBounds(110,17,100,18);
        name.setForeground(Color.white);
        name.setFont(new Font("Arial",Font.CENTER_BASELINE,18));
        panel1.add(name);
        //Server's active status
        JLabel status=new JLabel("Active now");
        status.setBounds(110,39,100,18);
        status.setForeground(Color.white);
        status.setFont(new Font("Arial",Font.CENTER_BASELINE,13));
        panel1.add(status);

        //Text panel
        text_panel=new JPanel();
        text_panel.setBounds( 5,75,440,570);
        frame.add(text_panel);

        //Text field
        tf=new JTextField();
        tf.setBounds(5,655,310,40);
        tf.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        frame.add(tf);

        //Send button
        JButton send=new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(128,0,128));
        send.setFont(new Font("SAN_SERIF",Font.BOLD,16));
        send.setForeground(Color.white);
        send.addActionListener(this);
        frame.add(send);

        //frame
        frame.setSize(450,700);
        frame.setLocation(200,50);
        frame.setUndecorated(true); //will remove the upper default label
        frame.getContentPane().setBackground(Color.white);
        frame.setLayout(null); //will make customized layout
        frame.add(panel1);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //send buttons action perform
        String out=tf.getText();
        JPanel output=textBox(out);

        text_panel.setLayout(new BorderLayout());

        JPanel right=new JPanel(new BorderLayout());
        right.add(output,BorderLayout.EAST); //the text will appear in the right
        vertical.add(right); //if multiple texts then it will appear down right after the other
        vertical.add(Box.createVerticalStrut(15)); //the space between two text boxes
        text_panel.add(vertical,BorderLayout.PAGE_START); //page baad diye dkhte hbe

        try {
            dout.writeUTF(out); // to send the text to client
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        tf.setText(""); //to make the text go away from text field after sending

        frame.repaint();
        frame.invalidate(); //mark the panel as invalid
        frame.validate(); //trigger layout validation
    }

    //creating the text box
    public static JPanel textBox(String out){
        JPanel panel=new JPanel(); //as we also have to return a panel
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS)); //vertical layout(components will be laid vertically from top to bottom)

        JLabel output=new JLabel(out);
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(221,160,221));
        output.setOpaque(true); //it will make the background color visible,if false then background will be transparent
        output.setBorder(new EmptyBorder(15,15,15,50));
        panel.add(output);

        //to add time
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        JLabel time=new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }


    public static void main(String[] args){
        new Server();
        try{
            ServerSocket skt=new ServerSocket(12345); //there can be only one ServerSocket but multiple Socket(which Client uses) as one server can have many clients
            while(true){
                Socket s=skt.accept(); //to receive the texts
                DataInputStream din=new DataInputStream(s.getInputStream());
                dout=new DataOutputStream(s.getOutputStream()); //to send texts

                while(true){
                    String msg=din.readUTF();  //to read the texts(clients text will stay here)
                    JPanel p=textBox(msg);

                    JPanel left=new JPanel(new BorderLayout()); //received message will appear in the left
                    left.add(p,BorderLayout.LINE_START);
                    vertical.add(left);
                    frame.validate();


                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
