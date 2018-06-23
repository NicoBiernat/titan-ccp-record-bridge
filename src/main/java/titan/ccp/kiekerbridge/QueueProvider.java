package titan.ccp.kiekerbridge;

import java.util.Queue;

@Deprecated
public interface QueueProvider<T> {

	public Queue<T> getQueue();

}
