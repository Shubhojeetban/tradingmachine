package com.cryptotradder.tradingmachine.models;

import java.util.HashMap;

import com.cryptotradder.tradingmachine.datastructure.LimitTree;

public class Book {
	public LimitTree buyTree ;
    public LimitTree sellTree ;
    public HashMap<Double, Limit> buyTreeMap;
    public HashMap<Double, Limit> sellTreeMap;
}
