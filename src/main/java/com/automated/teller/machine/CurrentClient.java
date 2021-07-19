package com.automated.teller.machine;

public class CurrentClient {
    private String name = "", card_num, count_num, currency;
    private int amount;

    public void setName(String name) {
        this.name = name;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public void setCount_num(String count_num) {
        this.count_num = count_num;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getCard_num() {
        return card_num;
    }

    public String getCount_num() {
        return count_num;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAmount() {
        return amount;
    }
}
