package titan.ccp.kiekerbridge.raritan;

import java.util.Queue;

//TODO test if necessary
public interface QueueProvider<T> {

	public Queue<T> getQueue();
	
}
