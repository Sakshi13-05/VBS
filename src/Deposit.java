import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Deposit extends JFrame
{
    Deposit(String username) {

        Font f = new Font("Futura", Font.BOLD, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 22);

        JLabel title = new JLabel("Deposit Money", JLabel.CENTER);
        JLabel label = new JLabel("Enter Amount:");
        JTextField t1 = new JTextField(10);
        JButton b1 = new JButton("Deposit");
        JButton b2 = new JButton("Back");

        title.setFont(f);
        label.setFont(f2);
        t1.setFont(f2);
        b1.setFont(f2);
        b2.setFont(f2);

        Container c = getContentPane();
        c.setLayout(null);

        title.setBounds(200, 30, 400, 50);
        label.setBounds(250, 120, 300, 30);
        t1.setBounds(250, 160, 300, 30);
        b1.setBounds(300, 220, 200, 40);
        b2.setBounds(300, 280, 200, 40);

        c.add(title);
        c.add(label);
        c.add(t1);
        c.add(b1);
        c.add(b2);

        b2.addActionListener(
                a ->
                {
                    new Home(username);
                    dispose();

                }

        );

        //part 1 database mein ja kar existing balance ko haath mein lo
        b1.addActionListener(
                a ->
                {

                    double balance = 0.0;
                    double total = 0.0;
                    double amount = 0.0;
                    String url = "jdbc:mysql://localhost:3306/3dec";
                    try (Connection con = DriverManager.getConnection(url, "root", "Sak13@31")) {
                        String sql = "select balance from users where username=?";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            pst.setString(1, username);

                            ResultSet rs = pst.executeQuery();//no update only jo hai woh print karo
                            if (rs.next())
                            {
                                balance = rs.getDouble("balance");
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }

                    //part 2 existing balance ko update karo
                    String s1 = t1.getText();
                    //check kro amount dala hai ki nhi
                    if (s1.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "please enter amount");
                    } else {
                        amount = Double.parseDouble(s1);
                        total = balance + amount;

                        JOptionPane.showMessageDialog(null, "sucessfully deposited");

                        updatePassbook(username, "Deposited", amount, total);


                    }

                    //part 3
                    //update karo
                    try (Connection con = DriverManager.getConnection(url, "root", "Sak13@31")) {
                        String sql = "update users set balance=? where username=?";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            pst.setDouble(1, total);
                            pst.setString(2, username);
                            pst.executeUpdate();


                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }


                }
        );


        setVisible(true);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Deposit Money");
    }

        void updatePassbook(String username,String desc,double amount,double total)
        {
            String url = "jdbc:mysql://localhost:3306/3dec";
            try (Connection con = DriverManager.getConnection(url, "root", "Sak13@31")) {
                String sql = "insert into transactions(username,description,amount,balance) values(?,?,?,?)";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, username);
                    pst.setString(2, desc);
                    pst.setDouble(3, amount);
                    pst.setDouble(4, total);
                    pst.executeUpdate();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());

            }
        }





    public static void main(String[] args) {
        new Deposit("deepak");
    }
    }


