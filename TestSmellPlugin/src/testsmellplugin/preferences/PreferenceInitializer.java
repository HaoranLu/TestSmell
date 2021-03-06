package testsmellplugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import testsmellplugin.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL, true);
		store.setDefault(PreferenceConstants.PREF_AUTO_DETECT_RANGE, "onlyNewlyCreated");
		store.setDefault(PreferenceConstants.PREF_AUTO_DETECT_ONLYNEW, "onlyNewlyCreated");
		store.setDefault(PreferenceConstants.PREF_AUTO_DETECT_ALLTEST, "allCommitedTestClasses");
		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		//store.setDefault(PreferenceConstants.P_STRING,"Default value");
	}

}
