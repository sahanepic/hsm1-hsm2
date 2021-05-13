package testhsm1hsm2;

import org.tanukisoftware.wrapper.WrapperSimpleApp;

public class HsmMonitor extends WrapperSimpleApp {

	protected HsmMonitor(String[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	public static void main(String[] args) {
		
		HsmMonitorChangeHsm hh = new HsmMonitorChangeHsm();
		Thread tt = new Thread(hh);
		System.out.println("Starting .......");
		tt.start();
		
	}
	
}
