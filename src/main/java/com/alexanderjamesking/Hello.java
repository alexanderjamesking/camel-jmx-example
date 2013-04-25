package com.alexanderjamesking;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class Hello implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("hello");
	}

}
