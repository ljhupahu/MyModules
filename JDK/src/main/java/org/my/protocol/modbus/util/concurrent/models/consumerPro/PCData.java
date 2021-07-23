package org.my.protocol.modbus.util.concurrent.models.consumerPro;

public class PCData {
     
	    private final int intData;
	    
	    public PCData(int d) {
	    	intData = d;
	    }
	    public PCData(String d) {
	    	intData = Integer.valueOf(d);
	    }
	    public int getData() {
	   	 return this.intData;
	    }
        public String toString() {
	    	return "data:" + intData;
	    }
}