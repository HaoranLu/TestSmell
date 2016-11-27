package testsmellplugin;

public class NullDeltaListener implements IDeltaListener{
	protected static NullDeltaListener soleInstance = new NullDeltaListener();
	public static NullDeltaListener getSoleInstance() {
		return soleInstance;
	}
	@Override
	public void add(DeltaEvent event) {
		
	}

	@Override
	public void remove(DeltaEvent event) {
		
	}

}
