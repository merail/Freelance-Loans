package com.onlinecash.loanswithout;

public class DatabaseJson {

    public AppConfig app_config;
    public Loan[] loans;
    public Cards[] cards;

    public static class AppConfig
    {
        public int show_news_item;
        public int show_calculator_time;
        public int show_chat;
        public int show_phone_authentication;
        public int show_privacy_police;
        public String user_term_html;
        public String privacy_policy_html;
    }

    public static class Loan
    {
        public int id;
        public String itemId;
        public String name;
        public String description;
        public String order;
        public String orderButtonText;
        public String percent;
        public String percentPostfix;
        public String percentPrefix;
        public double score;
        public String screen;
        public int show_mastercard;
        public int show_mir;
        public int show_visa;
        public int show_qiwi;
        public int show_yandex;
        public int show_cash;
        public String termPostfix;
        public String termMax;
        public String termMid;
        public String termMin;
        public String termPrefix;
        public String summPostfix;
        public String summPrefix;
        public String summMin;
        public String summMid;
        public String summMax;
        public int hide_TermFields;
        public int hide_PercentFields;
    }

    public static class Cards
    {
        public Loan[] cards_credit;
        public Loan[] cards_debit;
        public Loan[] cards_installment;
    }
}