package testsmell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import testsmell.relatedCodeSmell.codeSmellResult;
import testsmellplugin.IDeltaListener;
import testsmellplugin.NullDeltaListener;
/**
 * A singleton perform as a database service of this Plugin
 * <p>
 * Every time want to get the testSmellresult or codeSmellresult,use PluginCandidateProvider.INSTANCE.getXXX
 * 
 * @author Haoran Lu
 *
 */
public enum PluginCandidateProvider {
    INSTANCE;
/**
 * the list of PluginCandidate contaion TestSmellResults
 */
    private List<PluginCandidate> candidates;
    /**
     * the list of codeSmell
     */
    private List<codeSmellResult> codeSmellResults;
    /**
     * currently, this plugin close and reopen view to refresh. But a more elegant method is to use Listener
     */
    protected IDeltaListener listener = NullDeltaListener.getSoleInstance();//listener method to refresh view is not implemented
    private PluginCandidateProvider() {
    	candidates = new ArrayList<PluginCandidate>();
    	codeSmellResults = new ArrayList<codeSmellResult>();
    }

    
    public List<PluginCandidate> getPluginCandidates() {
            return candidates;
    }
    /**
     * truncate or clear the current results and insert new
     * @param pcList
     */
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