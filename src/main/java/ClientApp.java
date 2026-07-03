

import javax.swing.*;
import javax.swing.table.*;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.result.Field;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@SuppressWarnings({"serial", "rawtypes"})

public class ClientApp extends JPanel
{
    private JButton connectButton, disconnectButton, clearCommandButton, executeCommandButton, clearResultWindowButton, closeAppButton;
    private JLabel sqlCommandLabel, dbInfoLabel, dbPropertiesLabel, userPropertiesLabel, usernameInputLabel, userPasswordInputLabel;
    private JTextArea textCommand;
    private JComboBox<String> dbPropertiesComboBox, userPropertiesComboBox;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JLabel connectionStatusLabel, connectionLabel, resultWindowLabel;
    // private ResultSetTableModel tableModel;
    private Connection connect;
    private TableModel Empty;
    private JTable resultTable;

    private JSeparator lineOne, lineTwo;

    Properties userProperties = new Properties();
    Properties dbProperties = new Properties();
    FileInputStream dbFileIn = null;
    FileInputStream userFileIn = null;
    MysqlDataSource dataSource = null;


    public ClientApp()
    {
        // dropdown menus
        String[] dbPropertiesItems = {"project2.properties", "bikedb.properties"};
        String[] userPropertiesItems = {"root.properties", "client1.properties", "client2.properties"};

        setPreferredSize (new Dimension (900, 980));
        setLayout(null);
        setBackground(Color.lightGray);

        // separators
        lineOne = new JSeparator();
        lineOne.setOrientation(SwingConstants.HORIZONTAL);
        lineOne.setForeground(Color.BLACK);
        lineOne.setBackground(Color.BLACK);

        lineTwo = new JSeparator();
        lineTwo.setOrientation(SwingConstants.HORIZONTAL);
        lineTwo.setForeground(Color.BLACK);
        lineTwo.setBackground(Color.BLACK);



        //Buttons
        connectButton = new JButton ("Connect to Database");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(Color.BLUE);
        connectButton.setForeground(Color.WHITE);
        connectButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        connectButton.setBorderPainted(true);
        connectButton.setOpaque(true);

        disconnectButton = new JButton ("Disconnect From Database");
        disconnectButton.setFont(new Font("Arial", Font.BOLD, 14));
        disconnectButton.setBackground(Color.RED);
        disconnectButton.setForeground(Color.BLACK);
        disconnectButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        disconnectButton.setBorderPainted(true);
        disconnectButton.setOpaque(true);

        clearCommandButton = new JButton ("Clear SQL Command");
        clearCommandButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearCommandButton.setBackground(Color.YELLOW);
        clearCommandButton.setForeground(Color.BLACK);
        clearCommandButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        clearCommandButton.setBorderPainted(true);
        clearCommandButton.setOpaque(true);

        executeCommandButton = new JButton ("Execute SQL Command");
        executeCommandButton.setFont(new Font("Arial", Font.BOLD, 14));
        executeCommandButton.setBackground(Color.GREEN);
        executeCommandButton.setForeground(Color.BLACK);
        executeCommandButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        executeCommandButton.setBorderPainted(true);
        executeCommandButton.setOpaque(true);

        clearResultWindowButton = new JButton ("Clear Result Window");
        clearResultWindowButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearResultWindowButton.setBackground(Color.YELLOW);
        clearResultWindowButton.setForeground(Color.BLACK);
        clearResultWindowButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        clearResultWindowButton.setBorderPainted(true);
        clearResultWindowButton.setOpaque(true);

        closeAppButton = new JButton ("CLOSE APPLICATION");
        closeAppButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeAppButton.setBackground(Color.RED);
        closeAppButton.setForeground(Color.BLACK);
        closeAppButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        closeAppButton.setBorderPainted(true);
        closeAppButton.setOpaque(true);



        // Labels
        sqlCommandLabel = new JLabel();
        sqlCommandLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sqlCommandLabel.setForeground(Color.BLUE);
        sqlCommandLabel.setText("SQL Command Input Window");

        dbInfoLabel = new JLabel();
        dbInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dbInfoLabel.setForeground(Color.BLUE);
        dbInfoLabel.setText("Connection Details");

        dbPropertiesLabel = new JLabel();
        dbPropertiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dbPropertiesLabel.setForeground(Color.BLACK);
        dbPropertiesLabel.setBackground(Color.GRAY);
        dbPropertiesLabel.setText("DB URL Properties");

        userPropertiesLabel = new JLabel();
        userPropertiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userPropertiesLabel.setForeground(Color.BLACK);
        userPropertiesLabel.setBackground(Color.GRAY);
        userPropertiesLabel.setText("User Properties");

        usernameInputLabel = new JLabel();
        usernameInputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameInputLabel.setForeground(Color.BLACK);
        usernameInputLabel.setText("Username");

        userPasswordInputLabel = new JLabel();
        userPasswordInputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userPasswordInputLabel.setForeground(Color.BLACK);
        userPasswordInputLabel.setText("Password");

        connectionStatusLabel = new JLabel();
        connectionStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        connectionStatusLabel.setForeground(Color.BLACK);
        connectionStatusLabel.setText("Connection Status");

        connectionLabel = new JLabel();
        connectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        connectionLabel.setForeground(Color.BLACK);
        connectionLabel.setText("NO CONNECTION ESTABLISHED");

        resultWindowLabel = new JLabel();
        resultWindowLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultWindowLabel.setForeground(Color.BLUE);
        resultWindowLabel.setText("SQL Execution Result Window");


        //input and output area
        textCommand = new JTextArea(5,5);
        textCommand.setFont(new Font("Arial", Font.BOLD, 18));


        // Combobox
        dbPropertiesComboBox = new JComboBox<>(dbPropertiesItems);
        userPropertiesComboBox = new JComboBox<>(userPropertiesItems);

        // username field
        usernameText = new JTextField ("", 10);
        passwordText = new JPasswordField("", 10);


        resultTable = new JTable();
        Empty = new DefaultTableModel();



        final Box square = Box.createHorizontalBox();
        square.add( new JScrollPane(resultTable));
        final Box sqlSquare = Box.createHorizontalBox();
        sqlSquare.add(new JScrollPane( textCommand ));
        resultTable.setEnabled (false);




        // connection section
        connectButton.setBounds(575, 50, 250, 25);
        disconnectButton.setBounds(575, 100, 250, 25);
        executeCommandButton.setBounds(160, 450, 250, 25);
        clearCommandButton.setBounds(490, 450, 250, 25);
        clearResultWindowButton.setBounds(75, 930, 250, 25);
        closeAppButton.setBounds(560, 930, 250, 25);

        dbPropertiesComboBox.setBounds(200, 35, 250, 25);
        userPropertiesComboBox.setBounds(200, 75, 250, 25);

        usernameText.setBounds(200, 115, 250, 25);
        passwordText.setBounds(200, 155, 250, 25);

        connectionStatusLabel.setBounds(630, 150, 200, 20);
        connectionLabel.setBounds(585, 175, 250, 25);
        dbInfoLabel.setBounds(400, 0, 300, 25);
        sqlCommandLabel.setBounds(365, 230, 900, 20);
        resultWindowLabel.setBounds(365, 505, 900, 20);

        dbPropertiesLabel.setBounds(50, 35, 200, 25);
        userPropertiesLabel.setBounds(50, 75, 200, 25);
        usernameInputLabel.setBounds(50, 115, 200, 25);
        userPasswordInputLabel.setBounds(50, 155, 200, 25);


        lineOne.setBounds(0, 225, 900, 2);
        lineTwo.setBounds(0, 500, 900, 2);

        textCommand.setBounds(100, 260, 700, 175);

        resultTable.setBounds(15, 535, 870, 375);


        // add the buttons
        add(connectButton);
        add(disconnectButton);
        add(clearCommandButton);
        add(executeCommandButton);
        add(clearResultWindowButton);
        add(closeAppButton);

        // add labels
        add(connectionLabel);
        add(connectionStatusLabel);
        add(dbInfoLabel);
        add(dbPropertiesLabel);
        add(sqlCommandLabel);
        add(resultWindowLabel);
        add(usernameInputLabel);
        add(userPasswordInputLabel);
        add(userPropertiesLabel);

        // add combobox
        add(dbPropertiesComboBox);
        add(userPropertiesComboBox);

        // add in/out
        add(textCommand);

        // add
        add(usernameText);
        add(passwordText);

        // table
        add(resultTable);

        // lines
        add(lineOne);
        add(lineTwo);




        // actions and event listeners
        // connect button
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // if no connection, make connection
                // update label
                // read properties file dictated by combobox
                try{
                    if (connect != null) {
                        connect.close();
                        connectionLabel.setText("No Connection Now");
                    }
                    dbFileIn = new FileInputStream("/home/christopheralbear/Projects/enterprise-proj-2/src/main/java/" + dbPropertiesComboBox.getSelectedItem().toString());
                    userFileIn = new FileInputStream("/home/christopheralbear/Projects/enterprise-proj-2/src/main/java/" + userPropertiesComboBox.getSelectedItem().toString());

                    dbProperties.load(dbFileIn);
                    userProperties.load(userFileIn);

                    dataSource = new MysqlDataSource();
                    dataSource.setURL(dbProperties.getProperty("MYSQL_DB_URL"));
                    dataSource.setUser(userProperties.getProperty("MYSQL_DB_USERNAME"));
                    dataSource.setPassword(userProperties.getProperty("MYSQL_DB_PASSWORD"));

                    // compare the properties file info to the user and pass input on gui
                    String userName = usernameText.getText();
                    String password = String.valueOf(passwordText.getPassword());

                    if (!userName.equals(dataSource.getUser()))
                    {
                        throw new MatchException("No Match on User Name", null);
                    }

                    if (!password.equals(dataSource.getPassword()))
                    {
                        throw new MatchException("No Match on Password", null);
                    }

                    connect = dataSource.getConnection();
                    connectionLabel.setText(dataSource.getURL());

                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // disconnect button
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // clear sql command button
        clearCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textCommand.setText("");
            }
        });

        // execute sql command button
        executeCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // clear result window button
        clearResultWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // close application button
        closeAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("SQL Client Application");
        frame.setPreferredSize (new Dimension (900, 980));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ClientApp());

        frame.setResizable(false);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

    }
}
