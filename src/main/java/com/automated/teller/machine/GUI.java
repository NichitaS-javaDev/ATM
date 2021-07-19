package com.automated.teller.machine;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

public class GUI {
    protected static JButton enter_button;
    protected static JTextField card_reader, money_taker;
    private final JEditorPane big_screen = new JEditorPane();
    Actions actions = new Actions();
    protected static JTextField small_screen;
    protected static ScheduledFuture<?> scheduled_color_green, scheduled_color_grey;
    Font button_font = new Font(Font.MONOSPACED,Font.BOLD,25);
    Color num_button_color = new Color(112,112,105);

    public void createGUI(){
        JFrame frame = new JFrame();
        frame.setSize(1200,700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setTitle("e-Bank ATM");
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);

        frame.add(createNumPanel());
        frame.add(createWinPanel());
        frame.setVisible(true);
        frame.pack();
    }

    static void animateTheObject(JTextField object_to_animate){

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        scheduled_color_green = scheduledExecutorService.scheduleAtFixedRate(
                () -> object_to_animate.setBackground(new Color(0,153,0)),0,400, TimeUnit.MILLISECONDS
        );

        scheduled_color_grey = scheduledExecutorService.scheduleAtFixedRate(
                () -> object_to_animate.setBackground(new Color(33,33,33)),0,800, TimeUnit.MILLISECONDS
        );

    }

    private JPanel createNumPanel(){
        JPanel num_panel = new JPanel();
        num_panel.setBackground(new Color(210,210,210));
        num_panel.setPreferredSize(new Dimension(450,700));
        num_panel.setLayout(new FlowLayout(FlowLayout.CENTER,50,50));

        small_screen = new JTextField();
        small_screen.setPreferredSize(new Dimension(400,50));
        small_screen.setBackground(new Color(33,33,33));
        small_screen.setFont(new Font(Font.MONOSPACED,Font.BOLD,25));
        small_screen.setForeground(new Color(21,210,21));
        small_screen.setHorizontalAlignment(0);
        small_screen.setCaretColor(new Color(33,33,33));
        small_screen.setEditable(false);
        num_panel.add(small_screen);

        JButton num_button;
        for (int i = 1; i<10; i++){
            num_button = new JButton(String.valueOf(i));
            num_button.setPreferredSize(new Dimension(75,50));
            num_button.setBackground(num_button_color);
            num_button.setFont(button_font);
            num_button.addActionListener(actions.numButtonListener(small_screen));
            num_panel.add(num_button);
        }

        enter_button = new JButton("O");
        enter_button.setPreferredSize(new Dimension(75,50));
        enter_button.setBackground(new Color(21,208,15));
        enter_button.setFont(button_font);
        enter_button.addActionListener(actions.enterButtonListener(small_screen, big_screen));
        num_panel.add(enter_button);

        num_button = new JButton("0");
        num_button.setPreferredSize(new Dimension(75,50));
        num_button.setBackground(num_button_color);
        num_button.setFont(button_font);
        num_button.addActionListener(actions.numButtonListener(small_screen));
        num_panel.add(num_button);

        JButton delete_button = new JButton("X");
        delete_button.setPreferredSize(new Dimension(75,50));
        delete_button.setBackground(new Color(209,10,10));
        delete_button.setFont(button_font);
        delete_button.addActionListener(actions.deleteButtonListener(small_screen));
        num_panel.add(delete_button);

        card_reader = new JTextField();
        card_reader.setBackground(new Color(33,33,33));
        animateTheObject(card_reader);
        card_reader.setPreferredSize(new Dimension(250,15));
        card_reader.setEditable(false);
        num_panel.add(card_reader);

        JLabel label = new JLabel("▲▲ Enter card here ▲▲");
        label.setForeground(Color.red);
        label.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        num_panel.add(label);
        return num_panel;
    }

    private JPanel createWinPanel(){
        JPanel win_panel = new JPanel();
        win_panel.setPreferredSize(new Dimension(750,700));
        win_panel.setBackground(new Color(210,210,210));
        win_panel.setLayout(new FlowLayout(FlowLayout.CENTER,75,30));

        big_screen.setPreferredSize(new Dimension(700,500));
        big_screen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()
        ));
        big_screen.setEditable(false);
        big_screen.setForeground(new Color(171,233,175));
        big_screen.setText("\n *** WELCOME TO e-BANK ATM ***\n" +
                "\n\t...please enter your card and password");
        big_screen.setBackground(new Color(27,38,188));
        big_screen.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        win_panel.add(big_screen);

        JButton first_button = new JButton("I");
        first_button.setPreferredSize(new Dimension(85,50));
        first_button.setBackground(num_button_color);
        first_button.setFont(button_font);
        first_button.addActionListener(actions.firstButtonListener(big_screen));
        win_panel.add(first_button);

        JButton second_button = new JButton("II");
        second_button.setPreferredSize(new Dimension(85,50));
        second_button.setBackground(num_button_color);
        second_button.setFont(button_font);
        second_button.addActionListener(actions.secondButtonListener(big_screen));
        win_panel.add(second_button);

        JButton third_button = new JButton("III");
        third_button.setPreferredSize(new Dimension(85,50));
        third_button.setBackground(num_button_color);
        third_button.setFont(button_font);
        third_button.addActionListener(actions.thirdButtonListener(big_screen));
        win_panel.add(third_button);

        money_taker = new JTextField();
        money_taker.setBackground(new Color(33,33,33));
        money_taker.setPreferredSize(new Dimension(450,15));
        money_taker.setEditable(false);
        win_panel.add(money_taker);

        return win_panel;
    }

}
