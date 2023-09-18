package chatting.app;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {
    JTextField tf;
    static JPanel text_panel;
    static JFrame f=new JFrame();
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;

    Client() {
        //panel
        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(128, 0, 128));
        panel1.setBounds(0, 0, 450, 70);
        panel1.setLayout(null);

        //adding images
        ImageIcon image1 = new ImageIcon(ClassLoader.getSystemResource("arrow.png"));
        Image image2 = image1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon image3 = new ImageIcon(image2);
        JLabel back_button = new JLabel(image3);
        back_button.setBounds(5, 20, 25, 25);
        panel1.add(back_button);

        ImageIcon image4=new ImageIcon(ClassLoader.getSystemResource("ClientPFP.png"));
        Image image5=image4.getImage().getScaledInstance(50,35,Image.SCALE_DEFAULT);
        ImageIcon image6=new ImageIcon(image5);
        JLabel client_pfp=new JLabel(image6);
        client_pfp.setBounds(40,20,50,35);
        panel1.add(client_pfp);

        ImageIcon image7=new ImageIcon(ClassLoader.getSystemResource("video.png"));
        Image image8=image7.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon image9=new ImageIcon(image8);
        JLabel video_call=new JLabel(image9);
        video_call.setBounds(280,20,30,30);
        panel1.add(video_call);

        ImageIcon image10=new ImageIcon(ClassLoader.getSystemResource("phone.png"));
        Image image11=image10.getImage().getScaledInstance(35,30,Image.SCALE_DEFAULT);
        ImageIcon image12=new ImageIcon(image11);
        JLabel phone_call=new JLabel(image12);
        phone_call.setBounds(340,20,35,30);
        panel1.add(phone_call);

        ImageIcon image13=new ImageIcon(ClassLoader.getSystemResource("3icon.png"));
        Image image14=image13.getImage().getScaledInstance(10,25,Image.SCALE_DEFAULT);
        ImageIcon image15=new ImageIcon(image14);
        JLabel dots=new JLabel(image15);
        dots.setBounds(400,22,10,25);
        panel1.add(dots);

        //back arrow button
        back_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        //Client's name
        JLabel name=new JLabel("Client");
        name.setBounds(110,17,100,18);
        name.setFont(new Font("Arial",Font.CENTER_BASELINE,18));
        name.setForeground(Color.white);
        panel1.add(name);
        //active status
        JLabel status=new JLabel("Active now");
        status.setForeground(Color.white);
        status.setBounds(110,39,100,18);
        status.setFont(new Font("Arial",Font.CENTER_BASELINE,13));
        panel1.add(status);

        text_panel=new JPanel();
        text_panel.setBounds(5,75,440,570);
        f.add(text_panel);

        tf=new JTextField();
        tf.setBounds(5,655,310,40);
        tf.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        f.add(tf);

        JButton send=new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(128,0,128));
        send.setForeground(Color.white);
        send.setFont(new Font("SAN_SERIF",Font.BOLD,16));
        send.addActionListener(this);
        f.add(send);

        //frame
        f.setSize(450,700);
        f.setLocation(800,50);
        f.setUndecorated(true);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.white);
        f.add(panel1);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String out=tf.getText();
        JPanel output=textBox(out);

        text_panel.setLayout(new BorderLayout());

        JPanel right=new JPanel(new BorderLayout());
        right.add(output,BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));
        text_panel.add(vertical,BorderLayout.PAGE_START);

        try {
            dout.writeUTF(out); //to send client's text to server
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        tf.setText("");

        f.repaint();
        f.invalidate();
        f.validate();
    }

    public static JPanel textBox(String out){
        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JLabel output=new JLabel(out);
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(221,160,221));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));
        panel.add(output);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        JLabel time=new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Client();
        try{
            Socket skt=new Socket("127.0.0.1",12345);
            DataInputStream din=new DataInputStream(skt.getInputStream());
            dout=new DataOutputStream(skt.getOutputStream());

            while(true){
                text_panel.setLayout(new BorderLayout()); //which layout should server's text follow
                String msg=din.readUTF();
                JPanel panel=textBox(msg);

                JPanel left=new JPanel(new BorderLayout());
                left.add(panel,BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                text_panel.add(vertical,BorderLayout.PAGE_START);
                f.validate();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

