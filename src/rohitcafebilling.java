import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterAbortException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class rohitcafebilling extends JFrame {

    // Declare all the required user interface variable

    JLabel customer,item,contactNo,price,label;
    JTextField tcustomer,titem,tconactNo,tprice;
    JButton add,total,print,reset;
    JTextArea res;
    int sum=0;
    int totalamt=0;
    List<String> listItem=new ArrayList<>();
    List<Integer> list= new ArrayList<>();
    static Connection con;

    rohitcafebilling(){
        customer= new JLabel("Customer Name : ");
        customer.setBounds(20,20,120,30);
        tcustomer = new JTextField();
        tcustomer.setBounds(200,20,200,30);

        item= new JLabel("Food item  : ");
        item.setBounds(20,70,120,30);
        titem = new JTextField();
        titem.setBounds(200,70,200,30);

        price= new JLabel("Enter the price : ");
        price.setBounds(20,120,120,30);
        tprice = new JTextField();
        tprice.setBounds(200,120,200,30);

        contactNo= new JLabel("Contact Number : ");
        contactNo.setBounds(20,270,120,30);
        tconactNo = new JTextField();
        tconactNo.setBounds(200,270,200,30);


        add= new JButton("Add");
        add.setBounds(50,200,100,30);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ans=tprice.getText();
                int value=Integer.parseInt(ans);
                totalamt+=value;
                list.add(value);
                listItem.add(titem.getText());
                tprice.setText("");
                titem.setText("");
                System.out.println(list);
                System.out.println(listItem+"   "+totalamt);
            }
        });

        total= new JButton("Get Total= ");
        total.setBounds(300,200,100,30);
        total.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str=new String();
                for(int i=0;i<list.size();i++){
                    str=str+listItem.get(i)+" :\t"+list.get(i)+"\n";
                }
                str=str+"------------------------------------------\n";
                str=str+"gst(18%) :\t"+totalamt*18/100+"\n";
                str=str+"------------------------------------------\n";
                str=str+"TOTAL =\t"+(totalamt+totalamt*18/100)+"\n";
                str=str+"======================================\n\n\n";
                str=str+"\t\t ThankYou\n\t\t Visit Again ";
                res.setText(str);
            }
        });

        print= new JButton("Print BILL");
        print.setBounds(50,350,100,30);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String str="insert into rohitbills(customerName,ContactNumber,Items,Total) values(?,?,?,?);";
                    PreparedStatement stmt=con.prepareStatement(str);
                    stmt.setString(1,tcustomer.getText());
                    stmt.setString(2,tconactNo.getText());
                    stmt.setString(3,listItem.toString()+list.toString());
                    stmt.setInt(4,(totalamt+totalamt*18/100));
                    stmt.executeUpdate();

                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }

//                clearForm();
            }

        });

        reset = new JButton("RESET");
        reset.setBounds(300,350,100,30);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                res.setText("");
            }
        });

        label = new JLabel("RECIPT");
        label.setBounds(675,20,100,30);

        res= new JTextArea();
        res.setBounds(500,50,400,400);
        res.setEditable(false);


        setLayout(new FlowLayout());
        setLayout(null);
        getContentPane().add(customer);
        getContentPane().add(tcustomer);
        getContentPane().add(item);
        getContentPane().add(titem);
        getContentPane().add(price);
        getContentPane().add(tprice);
        getContentPane().add(contactNo);
        getContentPane().add(tconactNo);
        getContentPane().add(add);
        getContentPane().add(total);
        getContentPane().add(print);
        getContentPane().add(reset);
        getContentPane().add(res);
        getContentPane().add(label);


        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,500);
    }

    void clearForm(){
        tconactNo.setText("");
        titem.setText("");
        tprice.setText("");
        tcustomer.setText("");
        res.setText("");
        list.clear();
        listItem.clear();
        sum=0;
        totalamt=0;
    }

    public static void main(String[] args) {
        //First Step Database Connectivity using JDBC
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/rohitbillingsystem";
            String user = "root";
            String pass = "Bestfriend@2129";
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection Establised successfully");

            String q = "create table if not exists rohitbills(BillID int(20)  primary key auto_increment,customerName varchar(50) not null, ContactNumber varchar(10) not null, Items varchar(100) not null,Total int(50) not null)";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(q);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        new rohitcafebilling();

    }
}