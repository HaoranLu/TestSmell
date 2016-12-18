package testsmellplugin;
/** 
 * prepare for more elegant view refresh, Currently not needed. Just ignore
 * @author Haoran Lu
 *
 */
public class DeltaEvent {

	protected Object actedUpon;

	public DeltaEvent(Object receiver) {
		actedUpon = receiver;
	}

	public Object receiver() {
		return actedUpon;
	}

}
