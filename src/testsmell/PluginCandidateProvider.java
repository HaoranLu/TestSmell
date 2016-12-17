package testsmell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import testsmell.relatedCodeSmell.codeSmellResult;
import testsmellplugin.IDeltaListener;
import testsmellplugin.NullDeltaListener;

public enum PluginCandidateProvider {
    INSTANCE;

    private List<PluginCandidate> candidates;
    private List<codeSmellResult> codeSmellResults;
    protected IDeltaListener listener = NullDeltaListener.getSoleInstance();//listener method to refresh view is not implemented
    private PluginCandidateProvider() {
    	candidates = new ArrayList<PluginCandidate>();
    	codeSmellResults = new ArrayList<codeSmellResult>();
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
    }
    public List<codeSmellResult> getCodeSmellResults(){
		return codeSmellResults;
    	
    }
    public void setCodeSmellResult(List<codeSmellResult> csResults){
    	if (!this.codeSmellResults.isEmpty()) {
			this.codeSmellResults.clear();
		}
    	this.codeSmellResults.addAll(csResults);
    }
    public void addCodeSmellResult(List<codeSmellResult> csResults){
    	this.codeSmellResults.addAll(csResults);
    	
    }
    public void removeCodeSmellResult(Collection<codeSmellResult> csCodeSmellResults){
    	this.codeSmellResults.removeAll(csCodeSmellResults);
    }
}