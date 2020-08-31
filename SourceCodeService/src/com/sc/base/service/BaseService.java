package com.sc.base.service;

import org.springframework.transaction.interceptor.TransactionAspectSupport;


public class BaseService{
	
//	private final String TOPIC = "/topics/";
	
	
	public void callRollBack(){
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}
	
	
}
