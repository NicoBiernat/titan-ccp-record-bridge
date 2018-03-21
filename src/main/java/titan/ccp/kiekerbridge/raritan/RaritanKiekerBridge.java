package titan.ccp.kiekerbridge.raritan;

import titan.ccp.kiekerbridge.KiekerBridge;

public class RaritanKiekerBridge extends KiekerBridge {

	public RaritanKiekerBridge() {
		super(c -> new RaritanRestListener(c));
	}

	public static void main(String[] args) {
		new RaritanKiekerBridge().start();
	}
	
}
