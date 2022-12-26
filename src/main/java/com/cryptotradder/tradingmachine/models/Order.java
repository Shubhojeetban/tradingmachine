package com.cryptotradder.tradingmachine.models;

public class Order {
	public String id;
    public boolean buyOrSell;
    public double cryptos;
    //int limit;
    public int entryTime;
    public int eventTime;
    public Order nextOrder;
    public Order prevOrder;
    public Limit parentLimit;
}
