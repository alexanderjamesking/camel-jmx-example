package com.alexanderjamesking;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class CamelMonitoring implements Processor {

	private MBeanServer server;
	
	@Override
	public void process(Exchange exchange) throws Exception {

		getMBeanServer(exchange);

		List<ObjectName> routeList = getRouteList();
		
		for (Iterator<ObjectName> iter = routeList.iterator(); iter.hasNext();)
		{
		    ObjectName objName = iter.next();
		    ObjectName objectName = new ObjectName("org.apache.camel:" + objName.getCanonicalKeyPropertyListString());

		    if ("helloRoute".equals(server.getAttribute(objectName, "RouteId").toString())) {
				System.out.println("-------hello route stats--------");		

				getValue(objectName, "getExchangesTotal");
				getValue(objectName, "getExchangesCompleted");
				getValue(objectName, "getExchangesFailed");
				getValue(objectName, "getRedeliveries");
				getValue(objectName, "getMeanProcessingTime");
		    }
		}
	}

	private void getValue(ObjectName objectName, String methodName)
			throws ReflectionException, InstanceNotFoundException,
			MBeanException {
		System.out.println(methodName + ": " + server.invoke(objectName, methodName, null, null).toString());
	}

	private List<ObjectName> getRouteList() throws MalformedObjectNameException {
		ObjectName objectName = new ObjectName("org.apache.camel:type=routes,*");
		List<ObjectName> routeList = new LinkedList<ObjectName>(server.queryNames(objectName, null));
		return routeList;
	}

	private void getMBeanServer(Exchange exchange) {
		if (server == null) {
			server = exchange.getContext().getManagementStrategy().getManagementAgent().getMBeanServer();
		}
	}
}