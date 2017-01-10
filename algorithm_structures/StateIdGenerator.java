package algorithm_structures;
import java.util.LinkedList;
import java.util.List;


public class StateIdGenerator {

	//private List<Long> generated_ids;
	private long last_id;
	
	public StateIdGenerator() {
		last_id = -1;
	}
	
	public long generateNewId() {
		return ++last_id;
	}
}
