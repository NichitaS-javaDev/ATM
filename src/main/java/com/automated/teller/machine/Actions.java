package com.automated.teller.machine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class Actions {
    CurrentClient currentClient = new CurrentClient();
    States current_state = States.LOGIN;
    private String password, entered_sum, executed_operation = "", insert_sum;

    void createCurrentClient(String password){
        try {
            ResultSet result = new ConnectionDB().createConnection().executeQuery(
                    "select * from clients_counts_info where Password = '" + password + "';"
            );

            if (result.next()){
                currentClient.setName(
                        result.getString("FirstName") + " " + result.getString("LastName")
                );
                currentClient.setCount_num(result.getString("CountNumber"));
                currentClient.setAmount(result.getInt("Amount"));
                currentClient.setCurrency(result.getString("Currency"));
                currentClient.setCard_num(result.getString("CardNumber"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void executeAmountUpdate(int new_amount){
        try {
            new ConnectionDB().createConnection().executeUpdate(
                    "update clients_counts_info set Amount = " + new_amount + " where CardNumber like '" + currentClient.getCard_num() + "';"
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    void returnToInfoPage(){
        current_state = States.LOGIN;
        GUI.small_screen.setForeground(new Color(33,33,33));
        GUI.small_screen.setText(password);
        GUI.enter_button.doClick();
        GUI.small_screen.setForeground(new Color(21,210,21));
    }

    void delayOnReturnToInfoPage(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                returnToInfoPage();
            }
        };
        new Timer().schedule(task,3500);
    }

    public ActionListener numButtonListener(JTextField small_screen){
        return e -> {
            if (small_screen.getText().length() < 4){
                JButton button = (JButton) e.getSource();
                small_screen.setText(small_screen.getText() + button.getText());
            }
        };
    }

    public ActionListener deleteButtonListener(JTextField small_screen){
        return e -> {
            if (small_screen.getText().length() > 0){
                small_screen.setText("");
            }
        };
    }

    public ActionListener enterButtonListener(JTextField small_screen, JEditorPane big_screen){
        return e -> {
            if (current_state.equals(States.LOGIN)){
                createCurrentClient(small_screen.getText());

                if (!currentClient.getName().isEmpty()){
                    big_screen.setText(
                            " \t*** INFO ***\n"
                                    + "\n Name : " + currentClient.getName()
                                    + "\n Count Number : " + currentClient.getCount_num()
                                    + "\n Amount : " + currentClient.getAmount() + " " + currentClient.getCurrency()
                                    + "\n Card Number : " + currentClient.getCard_num()
                                    + "\n\n\n\t *** What do you want ? ***\n"
                                    + "\n I. Withdraw money"
                                    + "\n II. Insert money"
                                    + "\n III. Generate Cash-by-Code"
                    );
                    GUI.scheduled_color_green.cancel(false);
                    GUI.scheduled_color_grey.cancel(false);
                    GUI.card_reader.setBackground(new Color(33,33,33));
                    current_state = States.INFO;
                    password = small_screen.getText();
                } else {
                    big_screen.setText("\n ...incorrect password");
                }
            } else
                if (current_state.equals(States.ENTER_THE_SUM_TO_WITHDRAW)){
                    GUI.animateTheObject(GUI.money_taker);
                    big_screen.setText(
                            " \n Do not go anyway"
                                    + "\n\n Wait a little bit and take the money..."
                                    + "\n\n *** Your current amount is : " + (currentClient.getAmount() - Integer.parseInt(small_screen.getText()))
                                            + " " + currentClient.getCurrency() + " ***"
                                    + "\n\n\t Do you want a receipt ?"
                                    + "\n I. Yes"
                                    + "\n II. No"
                    );
                    entered_sum = small_screen.getText();
                    current_state = States.TAKING_MONEY_ENTERED_SUM;
                    executeAmountUpdate(currentClient.getAmount() - Integer.parseInt(small_screen.getText()));
                }
            else
                if (current_state.equals(States.INSERT_MONEY)){
                    big_screen.setText(
                            "\n Do not go anyway"
                                    + "\n\n\t Take the rest (if you have)..."
                                    + "\n\n *** Your current amount is : " + (currentClient.getAmount()
                                    + Integer.parseInt(small_screen.getText())) + " " + currentClient.getCurrency() + " ***"
                                    + "\n\n\t Do you want a receipt ?"
                                    + "\n I. Yes"
                                    + "\n II. No"
                    );
                    executed_operation = "Insert";
                    insert_sum = small_screen.getText();
                    current_state = States.TAKING_THE_REST;
                    executeAmountUpdate(currentClient.getAmount() + Integer.parseInt(small_screen.getText()));
                }
            else
                if (current_state.equals(States.CASH_BY_CODE)){
                    big_screen.setText(
                            "\n\t *** CASH-BY-CODE :: " + String.valueOf(Math.random()).substring(3,7) + " ***"
                            + "\n\n ...do not share it with randome people"
                            + "\n\n *** Your current amount is : " + (currentClient.getAmount()
                                    - Integer.parseInt(small_screen.getText())) + " " + currentClient.getCurrency() + " ***"
                            + "\n\n ...click OK button to exit"
                            + "\n\n ...bye & have a wonderful day :)"
                    );
                    current_state = States.GENERATED_CASH_BY_CODE;
                    executeAmountUpdate(currentClient.getAmount() - Integer.parseInt(small_screen.getText()));
                }
            else
                if (current_state.equals(States.GENERATED_CASH_BY_CODE)){
                    delayOnReturnToInfoPage();
                }
            small_screen.setText("");
        };
    }

    ActionListener firstButtonListener(JEditorPane big_screen){
        return e -> {
            if (current_state.equals(States.INFO)){
                big_screen.setText(
                        "\t*** Your current amount : " + currentClient.getAmount() + " " + currentClient.getCurrency() + " ***"
                                + "\n\n Enter or select the sum you want to withdraw :\n"
                                + "\n I. 300 " + currentClient.getCurrency()
                                + "\n II. 750 " + currentClient.getCurrency()
                                + "\n III. Other sum"
                );
                executed_operation = "Withdraw";
                current_state = States.WITHDRAW;
            } else
                if (current_state.equals(States.WITHDRAW)){
                    GUI.animateTheObject(GUI.money_taker);
                    big_screen.setText(
                            " \n Do not go anyway"
                                    + "\n\n Wait a little bit and take the money..."
                                    + "\n\n *** Your current amount is : " + (currentClient.getAmount() - 300) + " " + currentClient.getCurrency() + " ***"
                                    + "\n\n\t Do you want a receipt ?"
                                    + "\n I. Yes"
                                    + "\n II. No"
                    );
                    executeAmountUpdate(currentClient.getAmount() - 300);
                    current_state = States.TAKING_MONEY_300;
            } else
                if (current_state.equals(States.TAKING_MONEY_300) || current_state.equals(States.TAKING_MONEY_750) ||
                        current_state.equals(States.TAKING_MONEY_ENTERED_SUM) || current_state.equals(States.TAKING_THE_REST)){
                    String sum;
                    if (current_state.equals(States.TAKING_MONEY_300)){
                        sum = "300";
                    } else
                        if (current_state.equals(States.TAKING_MONEY_750)){
                            sum = "750";
                        }
                    else
                        if (current_state.equals(States.TAKING_MONEY_ENTERED_SUM)){
                            sum = entered_sum;
                        }
                    else {
                        sum = insert_sum;
                    }
                    big_screen.setText(
                            "\n\t*** eBank ATM №.3575 ***"
                                    + "\n\n " + executed_operation + " sum : " + sum + " " + currentClient.getCurrency()
                                    + "\n Date : " + LocalDate.now()
                                    + "\n Time : " + LocalTime.now()
                                    + "\n Card number : **** **** " + currentClient.getCard_num().substring(currentClient.getCard_num().length()-4)
                                    + "\n\n ...take please the receipt and have a nice day :)"
                    );
                        GUI.scheduled_color_green.cancel(false);
                        GUI.scheduled_color_grey.cancel(false);
                        GUI.money_taker.setBackground(new Color(33,33,33));
                        delayOnReturnToInfoPage();
                }
        };
    }

    ActionListener secondButtonListener(JEditorPane big_screen){
        return e -> {
            if (current_state.equals(States.INFO)){
                big_screen.setText(
                        "\t*** Your current amount : " + currentClient.getAmount() + " " + currentClient.getCurrency() + " ***"
                                + "\n\nEnter the sum you want to insert on your count and then  insert the money..."
                );
                GUI.animateTheObject(GUI.money_taker);
                current_state = States.INSERT_MONEY;
            } else
                if (current_state.equals(States.WITHDRAW)){
                    GUI.animateTheObject(GUI.money_taker);
                    big_screen.setText(
                            " \n Do not go anyway"
                                    + "\n\n Wait a little bit and take the money..."
                                    + "\n\n *** Your current amount is : " + (currentClient.getAmount() - 750) + " " + currentClient.getCurrency() + " ***"
                                    + "\n\n\t Do you want a receipt ?"
                                    + "\n I. Yes"
                                    + "\n II. No"
                    );
                    executeAmountUpdate(currentClient.getAmount() - 750);
                    current_state = States.TAKING_MONEY_750;
            } else
                if (current_state.equals(States.TAKING_MONEY_750) || current_state.equals(States.TAKING_MONEY_300) ||
                        current_state.equals(States.TAKING_MONEY_ENTERED_SUM) || current_state.equals(States.TAKING_THE_REST)){
                    big_screen.setText(
                            "\n\t*** eBank ATM №.3575 ***"
                                    + "\n\n ...bye & have a wonderful day :)"
                    );
                    GUI.scheduled_color_grey.cancel(false);
                    GUI.scheduled_color_green.cancel(false);
                    GUI.money_taker.setBackground(new Color(33,33,33));
                    delayOnReturnToInfoPage();
            }
        };
    }

    ActionListener thirdButtonListener(JEditorPane big_screen){
        return e -> {
            if (current_state.equals(States.INFO)) {
                big_screen.setText(
                        "\t*** Your current amount : " + currentClient.getAmount() + " " + currentClient.getCurrency() + " ***"
                                + "\n\nEnter the sum for cash-by-code..."
                );
                current_state = States.CASH_BY_CODE;
            } else
                if (current_state.equals(States.WITHDRAW)){
                    big_screen.setText(
                            "\t*** Your current amount : " + currentClient.getAmount() + " " + currentClient.getCurrency() + " ***"
                                + "\n\n ...please enter the sum you want to withdraw"
                    );
                    current_state = States.ENTER_THE_SUM_TO_WITHDRAW;
                }
        };
    }
}
