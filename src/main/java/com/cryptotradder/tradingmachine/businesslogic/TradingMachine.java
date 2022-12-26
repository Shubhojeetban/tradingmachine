package com.cryptotradder.tradingmachine.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cryptotradder.tradingmachine.datastructure.LimitTree;
import com.cryptotradder.tradingmachine.datastructure.OrderList;
import com.cryptotradder.tradingmachine.models.Book;
import com.cryptotradder.tradingmachine.models.Limit;
import com.cryptotradder.tradingmachine.models.Order;
import com.cryptotradder.tradingmachine.models.Request;
import com.cryptotradder.tradingmachine.models.Response;

@Service
public class TradingMachine {
	HashMap<String, Book> bookMap;

    TradingMachine() {
        bookMap = new HashMap<>();
    }

    public void addNewBook(String pairSymbol) {
        Book book = new Book();
        book.buyTree = new LimitTree();
        book.sellTree = new LimitTree();
        book.buyTreeMap = new HashMap<>();
        book.sellTreeMap = new HashMap<>();
        bookMap.put(pairSymbol, book);
    }

    // recieve a request for buy or sell
    public List<Response> addRequest(Request request) {
        List<Response> responses = new ArrayList<Response>();
        if (!bookMap.containsKey(request.pairSymbol)) {
            addNewBook(request.pairSymbol);
        }

        Book book = bookMap.get(request.pairSymbol);

        if (request.buyOrSell) {
            Limit lowestSell = book.sellTree.getSmallestLimit();
            LimitTree limitTree = book.sellTree;
            if (lowestSell != null && request.limitPrice >= lowestSell.limitPrice) {
                // start trading
                while (request.Cryptos != 0 && lowestSell != null && request.limitPrice >= lowestSell.limitPrice) {
                    if (lowestSell.totalVolume <= request.Cryptos) {
                        limitTree.deleteLimit(lowestSell);
                        // request.Cryptos = request.Cryptos - lowestSell.totalVolume;
//                        System.out.println("**** Sell *****");
//                        System.out.println("Price: " + request.limitPrice);
//                        System.out.println("Order Price: " + lowestSell.limitPrice);
//                        System.out.println("Cryptos: " + lowestSell.totalVolume);
                        for(Order o : lowestSell.order.getAllOrders()){
                            Response response = new Response();
                            response.buyId = request.RequestId;
                            response.sellId = o.id;
                            response.buyPrice = request.limitPrice;
                            response.sellPrice = lowestSell.limitPrice;
                            response.buyCryptoRemaining = request.Cryptos - o.cryptos;
                            response.sellCryptoRemaing = 0;
                            
                            responses.add(response);
                        }
                        lowestSell = limitTree.getSmallestLimit();
                    } else {
                        OrderList orderList = lowestSell.order;
                        int isOrderEmpty = 1;
                        while (request.Cryptos > 0) {
                            Order order = orderList.getHead();
                            if (order.cryptos <= request.Cryptos) {
                                isOrderEmpty = orderList.deleteOrder(order);
                                request.Cryptos = request.Cryptos - order.cryptos;
                                lowestSell.totalVolume = lowestSell.totalVolume - order.cryptos;
                            } else {
                                order.cryptos = order.cryptos - request.Cryptos;
                                lowestSell.totalVolume = lowestSell.totalVolume - request.Cryptos;
                                request.Cryptos = 0;
                            }
//                            System.out.println("**** Sell *****");
//                            System.out.println("Request Price: " + request.limitPrice);
//                            System.out.println("Order Price: " + lowestSell.limitPrice);
//                            System.out.println("Cryptos: " + order.cryptos);
                            Response response = new Response();
                            response.buyId = request.RequestId;
                            response.sellId = order.id;
                            response.buyPrice = request.limitPrice;
                            response.sellPrice = lowestSell.limitPrice;
                            response.buyCryptoRemaining = request.Cryptos;
                            response.sellCryptoRemaing = 0;
                            responses.add(response);
                            if(isOrderEmpty == 0) {
                                limitTree.deleteLimit(lowestSell);
                            }
                        }
                    }
                }
            } else {
                // add to queue
                addToQueue(request, book);
            }
        } else {
            Limit highestBuy = book.buyTree.getHighestLimit();
            LimitTree limitTree = book.buyTree;
            if (highestBuy != null && request.limitPrice <= highestBuy.limitPrice) {
                // start trading
                while (request.Cryptos != 0 && highestBuy != null && request.limitPrice <= highestBuy.limitPrice) {
                    if (highestBuy.totalVolume <= request.Cryptos) {
                        limitTree.deleteLimit(highestBuy);
                        // request.Cryptos = request.Cryptos - lowestSell.totalVolume;
//                        System.out.println("**** Buy *****");
//                        System.out.println("Price: " + request.limitPrice);
//                        System.out.println("Order Price: " + highestBuy.limitPrice);
//                        System.out.println("Cryptos: " + highestBuy.totalVolume);
                          for(Order o : highestBuy.order.getAllOrders()){
                            Response response = new Response();
                            response.buyId = o.id;
                            response.sellId = request.RequestId;
                            response.buyPrice = highestBuy.limitPrice;
                            response.sellPrice = request.limitPrice;
                            response.buyCryptoRemaining = 0;
                            response.sellCryptoRemaing = request.Cryptos - o.cryptos;
                            
                            responses.add(response);
                        }
                        highestBuy = limitTree.getHighestLimit();
                    } else {
                        OrderList orderList = highestBuy.order;
                        while (request.Cryptos > 0) {
                            Order order = orderList.getHead();
                            int isOrderEmpty = 1;
                            if (order.cryptos <= request.Cryptos) {
                                isOrderEmpty = orderList.deleteOrder(order);
                                request.Cryptos = request.Cryptos - order.cryptos;
                                highestBuy.totalVolume = highestBuy.totalVolume - order.cryptos;
                            } else {
                                order.cryptos = order.cryptos - request.Cryptos;
                                highestBuy.totalVolume = highestBuy.totalVolume - request.Cryptos;
                                request.Cryptos = 0;
                            }
//                            System.out.println("**** Buy *****");
//                            System.out.println("Price: " + request.limitPrice);
//                            System.out.println("Order Price: " + highestBuy.limitPrice);
//                            System.out.println("Cryptos: " + order.cryptos);
                            Response response = new Response();
                            response.buyId = order.id;
                            response.sellId = request.RequestId;
                            response.buyPrice = highestBuy.limitPrice;
                            response.sellPrice = request.limitPrice;
                            response.buyCryptoRemaining = 0;
                            response.sellCryptoRemaing = request.Cryptos;
                            responses.add(response);
                            if(isOrderEmpty == 0) {
                                limitTree.deleteLimit(highestBuy);
                            }
                        }
                    }
                }
            } else {
                // add to queue
                addToQueue(request, book);
            }
        }
        return responses;
    }

    public void addToQueue(Request request, Book book) {
        Limit limit;
        if (request.buyOrSell) {
            boolean isLimitExist = book.buyTreeMap.containsKey(request.limitPrice);

            if (isLimitExist) {
                // Create a order and add it to the orderlist
                limit = book.buyTreeMap.get(request.limitPrice);

            } else {
                // Create a limit add to limit tree and also create hte order and add to its order list 
                limit = new Limit();
                limit.limitPrice = request.limitPrice;
                limit.totalVolume = 0;
                limit.size = 0;
                limit.order = new OrderList();
                book.buyTree.insert(limit);
            }

        } else {
            boolean isLimitExist = book.sellTreeMap.containsKey(request.limitPrice);

            if (isLimitExist) {
                // Create a order and add it to the orderlist
                limit = book.sellTreeMap.get(request.limitPrice);

            } else {
                // Create a limit add to limit tree and also create hte order and add to its order list 
                limit = new Limit();
                limit.limitPrice = request.limitPrice;
                limit.totalVolume = 0;
                limit.size = 0;
                limit.order = new OrderList();
                book.sellTree.insert(limit);
            }
        }
        Order order = new Order();
        order.buyOrSell = request.buyOrSell;
        order.cryptos = request.Cryptos;
        // TODO: Enter the entrytime and eventtime
        order.id = request.RequestId; // TODO: Change the id accordingly
        order.parentLimit = limit;
        limit.order.insertOrder(order);
    }
}