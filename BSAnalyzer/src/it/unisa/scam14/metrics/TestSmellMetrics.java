package it.unisa.scam14.metrics;

import java.util.Collection;

import org.eclipse.jdt.core.dom.CompilationUnit;

import it.unisa.bsic.RQs.why.assignProjectStatus.CommitGoal;
import it.unisa.bsic.bean.Change;
import it.unisa.bsic.bean.Commit;
import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.MethodBean;
import it.unisa.scam14.parser.AssertVisitor;
import it.unisa.scam14.parser.CodeParser;

public class TestSmellMetrics {

	public static int getNumberOfAsserts(ClassBean cb) {
		CodeParser codeParser = new CodeParser();
		AssertVisitor assertVisitor = new AssertVisitor();
		
		CompilationUnit cu = codeParser.createParser(cb.getTextContent());
		cu.accept(assertVisitor);
		
		return assertVisitor.getAsserts().size();
	}
	
	public static int getSizeOfTextFixture(ClassBean cb) {
		int size = 0;
		
		for(MethodBean mb: cb.getMethods()) {
			if( (mb.getName().equals("setUp")) || (mb.getName().equals("tearDown"))) {
				size += mb.getTextContent().split("\n").length;
			}
		}
		
		return size;
	}
	
	public static int getNumberOfChanges(ClassBean pClassBean, long pCommitTime, Collection<Commit> pHistory) {
		int changes = 0;

		for(Commit commit: pHistory) {
			if(commit.getCommitterTime() <= pCommitTime) {
				for(Change change: commit.getChanges()) {
					if(change.getFile().getPath().contains(pClassBean.getName())) {
						changes++;
					}
				}
			}
		}

		return changes;
	}

	public static int getNumberOfRefactoring(ClassBean pClassBean, long pCommitTime, Collection<Commit> pHistory) {
		int refactorings = 0;

		for(Commit commit: pHistory) {
			if(commit.getCommitterTime() <= pCommitTime) {
				for(Change change: commit.getChanges()) {
					if(change.getFile().getPath().contains(pClassBean.getName())) {
						CommitGoal.tagCommit(commit);

						if(commit.getTags().get("COMMIT-GOAL").equals("REFACTORING")) {
							refactorings++;
						}
					}
				}
			}
		}

		return refactorings;
	}

	public static int getNumberOfBugFixing(ClassBean pClassBean, long pCommitTime, Collection<Commit> pHistory) {
		int bugFixing = 0;

		for(Commit commit: pHistory) {
			if(commit.getCommitterTime() <= pCommitTime) {
				for(Change change: commit.getChanges()) {
					if(change.getFile().getPath().contains(pClassBean.getName())) {
						CommitGoal.tagCommit(commit);

						if(commit.getTags().get("COMMIT-GOAL").equals("BUG-FIXING")) {
							bugFixing++;
						}
					}
				}
			}
		}

		return bugFixing;
	}	
	
}