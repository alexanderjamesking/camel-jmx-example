package com.alexanderjamesking;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CamelMonitoring implements Processor {

	private MBeanServer mBeanServer;
	
	@Override
	public void process(Exchange exchange) throws Exception {

		if (mBeanServer == null)
			mBeanServer = exchange.getContext().getManagementStrategy().getManagementAgent().getMBeanServer();
		
		ObjectName objName = new ObjectName("org.apache.camel:type=routes,*");
		List<ObjectName> routeList = new LinkedList<ObjectName>(mBeanServer.queryNames(objName, null));
		Iterator<ObjectName> routeIterator = routeList.iterator();

		while (routeIterator.hasNext()) {
			System.out.println("-------------------------------------------------------\n");

			ObjectName routeRootNode = routeIterator.next();

			String keyProps = routeRootNode.getCanonicalKeyPropertyListString();
			
			System.out.println(keyProps);
			
			ObjectName routeInfo = new ObjectName("org.apache.camel:" + keyProps);

			System.out.println(routeInfo);
			
			String routeId = (String) mBeanServer.getAttribute(routeInfo, "RouteId");
			System.out.println("Stats for route: " + routeId);

			System.out.println("invoked: " + mBeanServer.invoke(routeInfo, "getExchangesTotal", null, null).toString());
		}
	}
}