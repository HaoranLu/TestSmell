/**
 * 
 */
package testsmellplugin.preferences;


import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import testsmellplugin.Activator;


/**
 * Preference page of Test Smell Settings.
 * <p>
 * the radio button are slaves of the check button. Go to Preference~Test Smell Settings to see the GUI
 * @author Haoran Lu
 *
 */
public class PreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {

	private Button enableAutoDetect;
	private Button onlyNewRadio, allcommitRadio;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		enableAutoDetect = new Button(container, SWT.CHECK);
		enableAutoDetect.setText("Enable automatic Test Smell detection at Commit-Time");
		
		
		onlyNewRadio = new Button(container, SWT.RADIO);
		onlyNewRadio.setText("Only newly commited test classes");
		onlyNewRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pageChanged();
			}
		});
		indent(onlyNewRadio);
		
		allcommitRadio = new Button(container, SWT.RADIO);
		allcommitRadio.setText("All commited test classes");
		allcommitRadio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pageChanged();
			}
		});
		indent(allcommitRadio);
		initialize();
		enableAutoDetect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				pageChanged();
			}
		});

		Dialog.applyDialogFont(container);
		return container;
	}
	
	private void initialize() {
		IPreferenceStore pref = Activator.getDefault().getPreferenceStore();
		enableAutoDetect.setSelection(pref.getBoolean(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL));
		
		setRange(pref.getString(PreferenceConstants.PREF_AUTO_DETECT_RANGE));

		pageChanged();
	}

	private void setRange(String value) {
		if (value.equals(PreferenceConstants.PREF_AUTO_DETECT_ONLYNEW))
			onlyNewRadio.setSelection(true);
		else
			allcommitRadio.setSelection(true);
	}
	void pageChanged() {
		boolean master = enableAutoDetect.getSelection();
		onlyNewRadio.setEnabled(master);
		allcommitRadio.setEnabled(master);
	}
	
	protected void performDefaults() {
		super.performDefaults();
		IPreferenceStore pref = Activator.getDefault().getPreferenceStore();
		enableAutoDetect.setSelection(pref.getDefaultBoolean(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL));
		
		setRange(pref.getDefaultString(PreferenceConstants.PREF_AUTO_DETECT_RANGE));
		pageChanged();
	}
	
	public boolean performOk() {
		IPreferenceStore pref = Activator.getDefault().getPreferenceStore();
		pref.setValue(PreferenceConstants.PREF_AUTO_DETECT_TESTSMELL, enableAutoDetect.getSelection());
		if (onlyNewRadio.getSelection()) {
			pref.setValue(PreferenceConstants.PREF_AUTO_DETECT_RANGE, PreferenceConstants.PREF_AUTO_DETECT_ONLYNEW);
			
		}else{
			pref.setValue(PreferenceConstants.PREF_AUTO_DETECT_RANGE,  PreferenceConstants.PREF_AUTO_DETECT_ALLTEST);
		}
		return true;
	}
	
	private static void indent(Control control) {
		GridData gridData= new GridData();
		gridData.horizontalIndent= 20;
		control.setLayoutData(gridData);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}
	


}
