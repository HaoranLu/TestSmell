package testsmellplugin.views;

import org.eclipse.jface.wizard.Wizard;

import testsmell.PluginCandidate;

public class DetailWizard extends Wizard {
	private PluginCandidate pluginCandidate;
	private DetailWizardPage detailWizardPage;
	public DetailWizard() {
		// TODO Auto-generated constructor stub
		//this.setHelpAvailable(true);
		this.detailWizardPage = new DetailWizardPage("detail information");
	}
	@Override
	public void addPages() {
		addPage(this.detailWizardPage);
	}
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
