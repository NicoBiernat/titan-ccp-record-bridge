package titan.ccp.kiekerbridge.stages;

import java.util.Queue;

public interface QueueProvider<T> {

	public Queue<T> getQueue();

}
