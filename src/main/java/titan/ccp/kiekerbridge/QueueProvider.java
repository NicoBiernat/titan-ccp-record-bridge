package titan.ccp.kiekerbridge;

import java.util.Queue;

public interface QueueProvider<T> {

	public Queue<T> getQueue();

}
