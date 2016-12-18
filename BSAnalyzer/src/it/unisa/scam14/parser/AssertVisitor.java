package it.unisa.scam14.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;

public class AssertVisitor extends ASTVisitor {

	private List<AssertStatement> asserts = new ArrayList<AssertStatement>();

	@Override
	public boolean visit(AssertStatement node) {
		asserts.add(node);

		return super.visit(node);
	}

	/**
	 * This method allows to get all the Blocks for the Class on which it is;
	 * 
	 * @return
	 * 				a List of all Blocks;
	 */
	public Collection<AssertStatement> getAsserts() {
		return asserts;			
	}
}