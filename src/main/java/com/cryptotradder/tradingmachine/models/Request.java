package com.cryptotradder.tradingmachine.models;

public class Request {
	public String RequestId;
	public double limitPrice;
	public String pairSymbol;
	public double Cryptos;
	public boolean buyOrSell; // true -> buy, false -> sell
}
