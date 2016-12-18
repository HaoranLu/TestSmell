package testsmell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import it.unisa.scam14.beans.ClassBean;
import it.unisa.scam14.beans.TestClassBean;
/**
 * This class is used for feeding the View of TestSmellPlugin.
 * <p>
 * What we should focus is the TestClassBean testClass and HashMap(String, Object) testsmell.
 * The String field of this class is not needed.
 * @author Haoran Lu
 *
 */
public class PluginCandidate {
	/**
	 * not needed
	 */
	public int id = 0;
	/**
	 * not needed
	 */
	public String testSmellType;
	/**
	 * not needed
	 */
	public String testSmellSourceEntity;
	/**
	 * not needed
	 */
	public String testSmellTargetClass;
	public TestClassBean testClass;
	public HashMap<String, Object> testSmell;
	public PluginCandidate(){
		this.testSmellType = "NotKnown";
		this.testSmellSourceEntity = "NotKnown";
		this.testSmellTargetClass = "NotKnown";
		this.testClass = new TestClassBean();
		this.testSmell = new HashMap<>();
				
	}
	/**
	 * This constructor is not useful
	 * @param testSmellType
	 * @param testSmellSourceEntity
	 * @param testSmellTargetClass
	 */
	public PluginCandidate(String testSmellType, String testSmellSourceEntity, String testSmellTargetClass) {
		super();
		this.testSmellType = testSmellType;
		this.testSmellSourceEntity = testSmellSourceEntity;
		this.testSmellTargetClass = testSmellTargetClass;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((testClass == null) ? 0 : testClass.getTestCase().getName().hashCode());
		result = prime * result + ((testSmell == null) ? 0 : testSmell.hashCode());
		result = prime * result + ((testSmellSourceEntity == null) ? 0 : testSmellSourceEntity.hashCode());
		result = prime * result + ((testSmellTargetClass == null) ? 0 : testSmellTargetClass.hashCode());
		result = prime * result + ((testSmellType == null) ? 0 : testSmellType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PluginCandidate other = (PluginCandidate) obj;
		if (id != other.id)
			return false;
		if (testClass == null) {
			if (other.testClass != null)
				return false;
		} else if (!testClass.equals(other.testClass))
			return false;
		if (testSmell == null) {
			if (other.testSmell != null)
				return false;
		} else if (!testSmell.equals(other.testSmell))
			return false;
		if (testSmellSourceEntity == null) {
			if (other.testSmellSourceEntity != null)
				return false;
		} else if (!testSmellSourceEntity.equals(other.testSmellSourceEntity))
			return false;
		if (testSmellTargetClass == null) {
			if (other.testSmellTargetClass != null)
				return false;
		} else if (!testSmellTargetClass.equals(other.testSmellTargetClass))
			return false;
		if (testSmellType == null) {
			if (other.testSmellType != null)
				return false;
		} else if (!testSmellType.equals(other.testSmellType))
			return false;
		return true;
	}

	/**
	 * 
	 * @param id what ever you want
	 * @param testSmellType Any String is ok
	 * @param testSmellSourceEntity Any String is ok
	 * @param testSmellTargetClass Any String is ok
	 * @param testClass Be careful, this is TestClassBean, not ClassBean
	 * @param testSmell key is the string indicates smell type. Object see BSAnalyzer TestSmellRules.
	 */
	public PluginCandidate(int id, String testSmellType, String testSmellSourceEntity, String testSmellTargetClass,
			TestClassBean testClass, HashMap<String, Object> testSmell) {
		super();
		this.id = id;
		this.testSmellType = testSmellType;
		this.testSmellSourceEntity = testSmellSourceEntity;
		this.testSmellTargetClass = testSmellTargetClass;
		this.testClass = testClass;
		this.testSmell = testSmell;
	}

	public TestClassBean getTestClass() {
		return testClass;
	}

	public void setTestClass(TestClassBean testClass) {
		this.testClass = testClass;
	}

	public HashMap<String, Object> getTestSmell() {
		return testSmell;
	}

	public void setTestSmell(HashMap<String, Object> testSmell) {
		this.testSmell = testSmell;
	}

	public String getTestSmellType() {
		return this.testSmellType;
	}
	public void setTestSmellType(String testSmellType) {
		this.testSmellType = testSmellType;
	}
	public String getTestSmellSourceEntity() {
		return this.testSmellSourceEntity;
	}
	@Override
	public String toString() {
		return "PluginCandidate [id=" + id + ", testSmellType=" + testSmellType + ", testSmellSourceEntity="
				+ testSmellSourceEntity + ", testSmellTargetClass=" + testSmellTargetClass + "]";
	}
	public void setTestSmellSourceEntity(String testSmellSourceEntity) {
		this.testSmellSourceEntity = testSmellSourceEntity;
	}
	public String getTestSmellTargetClass() {
		return this.testSmellTargetClass;
	}
	public void setTestSmellTargetClass(String testSmellTargetClass) {
		this.testSmellTargetClass = testSmellTargetClass;
	}
}
