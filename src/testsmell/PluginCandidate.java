package testsmell;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Position;

public class PluginCandidate {
	
	public int id = 0;
	public String testSmellType;
	public String testSmellSourceEntity;
	public String testSmellTargetClass;
	public PluginCandidate(){
		this.testSmellType = "NotKnown";
		this.testSmellSourceEntity = "NotKnown";
		this.testSmellTargetClass = "NotKnown";
				
	}
	/*public  double getEntityPlacement();
	public  String getSourceEntity();
	public  String getSource();
	public  String getTarget();
	public  Set<String> getEntitySet();
	public  TypeDeclaration getSourceClassTypeDeclaration();
	public  TypeDeclaration getTargetClassTypeDeclaration();
	public  IFile getSourceIFile();
	public  IFile getTargetIFile();
	public  List<Position> getPositions();*/
	public PluginCandidate(String testSmellType, String testSmellSourceEntity, String testSmellTargetClass) {
		super();
		this.testSmellType = testSmellType;
		this.testSmellSourceEntity = testSmellSourceEntity;
		this.testSmellTargetClass = testSmellTargetClass;
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
	
	/*public String getAnnotationText() {
		Map<String, ArrayList<String>> accessMap = new LinkedHashMap<String, ArrayList<String>>();
		for(String entity : getEntitySet()) {
			String[] tokens = entity.split("::");
			String classOrigin = tokens[0];
			String entityName = tokens[1];
			if(accessMap.containsKey(classOrigin)) {
				ArrayList<String> list = accessMap.get(classOrigin);
				list.add(entityName);
			}
			else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(entityName);
				accessMap.put(classOrigin, list);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = accessMap.keySet();
		int i = 0;
		for(String key : keySet) {
			ArrayList<String> entities = accessMap.get(key);
			sb.append(key + ": " + entities.size());
			if(i < keySet.size()-1)
				sb.append(" | ");
			i++;
		}
		return sb.toString();
	}*/

}
