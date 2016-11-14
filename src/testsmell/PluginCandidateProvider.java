package testsmell;

import java.util.ArrayList;
import java.util.List;

public enum PluginCandidateProvider {
    INSTANCE;

    private List<PluginCandidate> candidates;

    private PluginCandidateProvider() {
    	candidates = new ArrayList<PluginCandidate>();
            // Image here some fancy database access to read the persons and to
            // put them into the model
    	candidates.add(new PluginCandidate("zhan", "Zufall", "male"));
    	candidates.add(new PluginCandidate("Reiner", "Babbel", "male"));
    	candidates.add(new PluginCandidate("Marie", "Dortmund", "female"));
    	candidates.add(new PluginCandidate("Holger", "Adams", "male"));
    	candidates.add(new PluginCandidate("Juliane", "Adams", "female"));
    }

    public List<PluginCandidate> getPluginCandidates() {
            return candidates;
    }

}