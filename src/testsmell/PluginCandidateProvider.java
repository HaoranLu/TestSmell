package testsmell;

import java.util.ArrayList;
import java.util.List;

import testsmellplugin.IDeltaListener;
import testsmellplugin.NullDeltaListener;

public enum PluginCandidateProvider {
    INSTANCE;

    private List<PluginCandidate> candidates;
    protected IDeltaListener listener = NullDeltaListener.getSoleInstance();//listener method to refresh view is not implemented
    private PluginCandidateProvider() {
    	candidates = new ArrayList<PluginCandidate>();
            // Image here some fancy database access to read the persons and to
            // put them into the model
    	/*candidates.add(new PluginCandidate("zhan", "Zufall", "male"));
    	candidates.add(new PluginCandidate("Reiner", "Babbel", "male"));
    	candidates.add(new PluginCandidate("Marie", "Dortmund", "female"));
    	candidates.add(new PluginCandidate("Holger", "Adams", "male"));
    	candidates.add(new PluginCandidate("Juliane", "Adams", "female"));*/
    }

    
    public List<PluginCandidate> getPluginCandidates() {
            return candidates;
    }
    public void setPluginCandidates(List<PluginCandidate> pcList) {
    	if(!this.candidates.isEmpty()){
    		this.candidates.clear();
    	}
    	this.candidates.addAll(pcList);
    }
    public void addPluginCandidates(List<PluginCandidate> pcList) {
    	this.candidates.addAll(pcList);
    }
    public void removePluginCandidates(List<PluginCandidate> pcList){
    	this.candidates.removeAll(pcList);
    	this.candidates.remove(0);
    	System.out.println(this.candidates);
    }
}