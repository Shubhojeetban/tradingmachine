package com.cryptotradder.tradingmachine.models;

import com.cryptotradder.tradingmachine.datastructure.OrderList;

public class Limit {
	public double limitPrice;
    public int size;
    public double totalVolume;
    public Limit parent ;
    public Limit leftChild ;
    public Limit rightChild ;
    public OrderList order ;
}
