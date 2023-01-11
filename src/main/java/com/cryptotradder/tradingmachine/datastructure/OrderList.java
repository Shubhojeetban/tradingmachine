package com.cryptotradder.tradingmachine.datastructure;

import java.util.ArrayList;
import java.util.List;

import com.cryptotradder.tradingmachine.models.Order;

public class OrderList {
	private Order headOrder;
    private Order tailOrder;

    public OrderList() {
        headOrder = null;
        tailOrder = null;
    }

    public void insertOrder(Order order) {
        if (headOrder == null) {
            headOrder = order;
            tailOrder = order;
        } else {
            tailOrder.nextOrder = order;
            order.prevOrder = tailOrder;
            tailOrder = order;
        }
        order.parentLimit.totalVolume += order.cryptos;
        order.parentLimit.size += 1;
    }

    // return 0 when the list is empty -> so that the limit could be deleted
    // return 1 when there is still element in the list
    public int deleteOrder(Order order) {
        if (order == headOrder && order == tailOrder) {
            headOrder = null;
            tailOrder = null;
            return 0;
        }
        if (order == headOrder) {
            headOrder = headOrder.nextOrder;
            headOrder.prevOrder = null;
            //headOrder.parentLimit.headOrder = headOrder;
            return 1;
        }
        if (order == tailOrder) {
            tailOrder = tailOrder.prevOrder;
            tailOrder.nextOrder = null;
            //tailOrder.parentLimit.tailOrder = tailOrder;
            return 1;
        }
        order.prevOrder.nextOrder = order.nextOrder;
        order.nextOrder.prevOrder = order.prevOrder;
        order.parentLimit.totalVolume -= order.cryptos;
        order.parentLimit.size -= 1;
        return 1;
    }
    
    public Order getHead() {
        return headOrder;
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Order temp = headOrder;
        while(temp != null) {
            orders.add(temp);
            temp = temp.nextOrder;
        }
        return orders;
    }
    
    public int deleteByOrderId(String id) {
        Order temp = headOrder;
        while(temp != null) {
            if(temp.id.equals(id)) {
                break;
            }
        }
        if(temp != null) {
            return deleteOrder(temp);
        }
        return 1;
    }
}
