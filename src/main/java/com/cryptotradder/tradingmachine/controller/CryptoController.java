package com.cryptotradder.tradingmachine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotradder.tradingmachine.businesslogic.TradingMachine;
import com.cryptotradder.tradingmachine.models.Request;
import com.cryptotradder.tradingmachine.models.Response;

@RestController
@RequestMapping("/javaapi")	
public class CryptoController {
	
	@Autowired
	private TradingMachine tradingMachine;
	
	@PostMapping("/")
	public ResponseEntity<List<Response>> postRequest(@RequestBody Request request) {
		return ResponseEntity.status(HttpStatus.OK).body(tradingMachine.addRequest(request));
	}
}
