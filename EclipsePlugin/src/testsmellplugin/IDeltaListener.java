package testsmellplugin;
/** 
 * prepare for more elegant view refresh, Currently not needed. Just ignore
 * @author Haoran Lu
 *
 */
public interface IDeltaListener {
	public void add(DeltaEvent event);
	public void remove(DeltaEvent event);
}
