package com.asuka.game.net.client;

/**
 * Created by Asuka on 10.08.2016.
 */
import com.asuka.game.net.NetReader;
import com.asuka.game.net.netIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientMain extends NetReader{
    BufferedReader keyboard;
    netIO io;
    JFrame mainFrame = new JFrame("Крестики-нолики");
    JButton[] buttons = new JButton[9];
    JButton findOpponentButton = new JButton("Найти соперника");
    JButton findAIButton = new JButton("Играть против ИИ");
    JButton exitButton = new JButton("Выйти");
    JPanel panel = new JPanel();
    JTextField textField = new JTextField();
    JButton sendButton = new JButton("Send");

    class Listener implements ActionListener{
        int index;

        public Listener(int index){
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            io.send("CLCK","" + index);
        }
    }

    ClientMain(){
        int serverPort = 8954;
        String address = "localhost";

        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
            io = new netIO(this,new Socket(InetAddress.getByName(address),serverPort));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("You successfully connected to server.");
        initMainFrame();
    }
    private void initMainFrame(){
        mainFrame.setPreferredSize(new Dimension(300,350));
        mainFrame.setBackground(Color.gray);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setResizable(false);

        mainFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Да","Нет"};
                int n = JOptionPane.showOptionDialog(mainFrame,"Закрыть окно?","Подтверждение",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,options,options[0]);
                if (n == 0){
                    io.send("EXIT","");
                    io.setQuit(true);
                    e.getWindow().setVisible(false);
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        initPanel();
        initButtons();

        String name = "";
        while ((name == null) || ("".equals(name))){
            name = JOptionPane.showInputDialog(mainFrame, "Введите имя:", "Player");

            if ((name == null) || ("".equals(name))){
                JOptionPane.showMessageDialog(mainFrame,"Некорректное имя!");
            }
        }

        if (name.length() > 16) name = name.substring(0,16);
        io.send("NAME",name);
        mainFrame.setTitle(mainFrame.getTitle() + " (" + name + ")");

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    private void initPanel(){
        panel.setLayout(null);

        panel.setPreferredSize(new Dimension(300,350));
        panel.setVisible(true);

        mainFrame.getContentPane().add(panel);
    }
    private void initButtons(){
        findOpponentButton.setBounds(10,10,270,30);
        findOpponentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                io.send("FIND","");
            }
        });
        findOpponentButton.setVisible(true);
        panel.add(findOpponentButton);

        findAIButton.setBounds(10,45,270,30);
        findAIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                io.send("F_AI","");
            }
        });
        findAIButton.setVisible(true);
        panel.add(findAIButton);

        exitButton.setBounds(10,280,270,30);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                io.send("EXIT","");
                io.setQuit(true);
                mainFrame.setVisible(false);
                System.exit(0);
            }
        });
        exitButton.setVisible(true);

        panel.add(exitButton);

        for (int i = 0; i < 9; i++){
            buttons[i] = new JButton("");
            buttons[i].setBounds(10+((i%3)*90),10+((i/3)*90),90,90);
            buttons[i].addActionListener(new Listener(i));
            buttons[i].setVisible(false);
            panel.add(buttons[i]);
        }

        textField.setBounds(10,285,200,25);
        textField.setVisible(false);
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        panel.add(textField);

        sendButton.setBounds(215,285,65,25);
        sendButton.setVisible(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")){
                    io.send("TEXT", textField.getText());
                    textField.setText("");
                }
            }
        });

        panel.add(sendButton);
    }

    @Override
    public void read(String command, String text){
        switch (command){
            case "TEXT":{
                JOptionPane.showMessageDialog(mainFrame,text);
            }
            break;
            case "JOIN":{
                for (int i = 0; i < 9; i++){
                    buttons[i].setVisible(true);
                }
                findOpponentButton.setVisible(false);
                findAIButton.setVisible(false);
                exitButton.setVisible(false);

                textField.setVisible(true);
                sendButton.setVisible(true);
                textField.setEnabled(true);
                sendButton.setEnabled(true);
            }
            break;
            case "J_AI":{
                for (int i = 0; i < 9; i++){
                    buttons[i].setVisible(true);
                }
                findOpponentButton.setVisible(false);
                findAIButton.setVisible(false);
                exitButton.setVisible(false);

                textField.setVisible(true);
                sendButton.setVisible(true);
                textField.setEnabled(false);
                sendButton.setEnabled(false);
            }
            break;
            case "DATA":{
                for (int i = 0; i < 9; i++){
                    buttons[i].setText("" + text.charAt(i));
                }
            }
            break;
            case "CLBB":{ // Lobby was closed
                for (int i = 0; i < 9; i++){
                    buttons[i].setVisible(false);
                }
                findOpponentButton.setVisible(true);
                findAIButton.setVisible(true);
                exitButton.setVisible(true);

                textField.setVisible(false);
                sendButton.setVisible(false);
            }
            break;
        }
        //System.out.println(command + " - " + text);
    }

    @Override
    public void send(String command, String text) {

    }

    @Override
    public void close() {
        io.setQuit(true);
        //System.out.println("Exiting...");
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
                new ClientMain();
            }
        });
    }
}
