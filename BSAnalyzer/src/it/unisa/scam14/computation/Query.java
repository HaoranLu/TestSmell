package it.unisa.scam14.computation;

import it.unisa.scam14.beans.Blob;
import it.unisa.scam14.beans.FeatureEnvy;
import it.unisa.scam14.beans.LongMethod;
import it.unisa.scam14.beans.MispacedClass;
import it.unisa.scam14.utility.FileUtility;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

public class Query {

	public static void main(String args[]) {
		Query query=new Query();
		Vector<MispacedClass> blobs = query.getAllMispacedClasses("/Users/fabiopalomba/Desktop/mispacedClass.csv");

		for(MispacedClass b: blobs) {
			System.out.println(b.getName());
		}
	}

	public Vector<Blob> getAllBlobs(String pVersionPath) {
		Vector<Blob> blobs = new Vector<Blob>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				Blob blob = new Blob();
				blob.setName(fields[0]);
				blob.setBlob(Boolean.parseBoolean(fields[1]));
				blob.setLoc(Integer.parseInt(fields[2]));
				blob.setLcom(Integer.parseInt(fields[3]));
				blob.setWmc(Integer.parseInt(fields[4]));
				blob.setRfc(Integer.parseInt(fields[5]));
				blob.setCbo(Integer.parseInt(fields[6]));
				blob.setNom(Integer.parseInt(fields[7]));
				blob.setNoa(Integer.parseInt(fields[8]));
				blob.setDit(Integer.parseInt(fields[9]));
				blob.setNoc(Integer.parseInt(fields[10]));

				blobs.add(blob);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return blobs;
	}

	public Vector<Blob> getCandidateBlobs(String pVersionPath) {
		Vector<Blob> blobs = new Vector<Blob>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				if(fields[1].equals("true")) {
					Blob blob = new Blob();
					blob.setName(fields[0]);
					blob.setBlob(true);
					blob.setLoc(Integer.parseInt(fields[2]));
					blob.setLcom(Integer.parseInt(fields[3]));
					blob.setWmc(Integer.parseInt(fields[4]));
					blob.setRfc(Integer.parseInt(fields[5]));
					blob.setCbo(Integer.parseInt(fields[6]));
					blob.setNom(Integer.parseInt(fields[7]));
					blob.setNoa(Integer.parseInt(fields[8]));
					blob.setDit(Integer.parseInt(fields[9]));
					blob.setNoc(Integer.parseInt(fields[10]));

					blobs.add(blob);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return blobs;
	}

	public Vector<LongMethod> getAllLongMethods(String pVersionPath) {
		Vector<LongMethod> longMethods = new Vector<LongMethod>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				LongMethod longMethod = new LongMethod();
				longMethod.setName(fields[0]);
				longMethod.setLongMethod(Boolean.parseBoolean(fields[1]));
				longMethod.setLoc(Integer.parseInt(fields[2]));
				longMethod.setNop(Integer.parseInt(fields[3]));
				longMethod.setCc(Integer.parseInt(fields[4]));
				longMethod.setCbo(Integer.parseInt(fields[5]));

				longMethods.add(longMethod);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return longMethods;
	}

	public Vector<LongMethod> getCandidateLongMethods(String pVersionPath) {
		Vector<LongMethod> longMethods = new Vector<LongMethod>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				if(fields[1].equals("true")) {
					LongMethod longMethod = new LongMethod();
					longMethod.setName(fields[0]);
					longMethod.setLoc(Integer.parseInt(fields[2]));
					longMethod.setNop(Integer.parseInt(fields[3]));
					longMethod.setCc(Integer.parseInt(fields[4]));
					longMethod.setCbo(Integer.parseInt(fields[5]));

					longMethods.add(longMethod);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return longMethods;
	}

	public Vector<FeatureEnvy> getAllFeatureEnvys(String pVersionPath) {
		Vector<FeatureEnvy> featureEnvys = new Vector<FeatureEnvy>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				FeatureEnvy featureEnvy = new FeatureEnvy();
				featureEnvy.setName(fields[0]);
				featureEnvy.setTargetClassName(fields[1]);
				featureEnvy.setFeatureEnvy(Boolean.parseBoolean(fields[2]));
				featureEnvy.setMethodLoc(Integer.parseInt(fields[3]));
				featureEnvy.setMethodNop(Integer.parseInt(fields[4]));
				featureEnvy.setMethodCc(Integer.parseInt(fields[5]));
				featureEnvy.setMethodCbo(Integer.parseInt(fields[6]));

				featureEnvy.setClassLoc(Integer.parseInt(fields[7]));
				featureEnvy.setClassLcom(Integer.parseInt(fields[8]));
				featureEnvy.setClassWmc(Integer.parseInt(fields[9]));
				featureEnvy.setClassRfc(Integer.parseInt(fields[10]));
				featureEnvy.setClassCbo(Integer.parseInt(fields[11]));
				featureEnvy.setClassNom(Integer.parseInt(fields[12]));
				featureEnvy.setClassNoa(Integer.parseInt(fields[13]));
				featureEnvy.setClassDit(Integer.parseInt(fields[14]));
				featureEnvy.setClassNoc(Integer.parseInt(fields[15]));

				featureEnvy.setDependenciesWithBelongingClass(Integer.parseInt(fields[16]));
				featureEnvy.setDependenciesWithTargetClass(Integer.parseInt(fields[17]));
				featureEnvy.setSimilarityWithBelongingClass(Double.parseDouble(fields[18]));
				featureEnvy.setSimilarityWithTargetClass(Double.parseDouble(fields[18]));

				featureEnvys.add(featureEnvy);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return featureEnvys;
	}

	public Vector<FeatureEnvy> getCandidateFeatureEnvys(String pVersionPath) {
		Vector<FeatureEnvy> featureEnvys = new Vector<FeatureEnvy>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				if(fields[2].equals("true")) {
					FeatureEnvy featureEnvy = new FeatureEnvy();
					featureEnvy.setName(fields[0]);
					featureEnvy.setTargetClassName(fields[1]);

					featureEnvy.setMethodLoc(Integer.parseInt(fields[3]));
					featureEnvy.setMethodNop(Integer.parseInt(fields[4]));
					featureEnvy.setMethodCc(Integer.parseInt(fields[5]));
					featureEnvy.setMethodCbo(Integer.parseInt(fields[6]));

					featureEnvy.setClassLoc(Integer.parseInt(fields[7]));
					featureEnvy.setClassLcom(Integer.parseInt(fields[8]));
					featureEnvy.setClassWmc(Integer.parseInt(fields[9]));
					featureEnvy.setClassRfc(Integer.parseInt(fields[10]));
					featureEnvy.setClassCbo(Integer.parseInt(fields[11]));
					featureEnvy.setClassNom(Integer.parseInt(fields[12]));
					featureEnvy.setClassNoa(Integer.parseInt(fields[13]));
					featureEnvy.setClassDit(Integer.parseInt(fields[14]));
					featureEnvy.setClassNoc(Integer.parseInt(fields[15]));

					featureEnvy.setDependenciesWithBelongingClass(Integer.parseInt(fields[16]));
					featureEnvy.setDependenciesWithTargetClass(Integer.parseInt(fields[17]));
					featureEnvy.setSimilarityWithBelongingClass(Double.parseDouble(fields[18]));
					featureEnvy.setSimilarityWithTargetClass(Double.parseDouble(fields[18]));

					featureEnvys.add(featureEnvy);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return featureEnvys;
	}

	public Vector<MispacedClass> getAllMispacedClasses(String pVersionPath) {
		Vector<MispacedClass> mispacedClasses = new Vector<MispacedClass>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				MispacedClass mispacedClass = new MispacedClass();
				mispacedClass.setName(fields[0]);
				mispacedClass.setTargetPackageName(fields[1]);
				mispacedClass.setMisplacedClass(Boolean.parseBoolean(fields[2]));
				mispacedClass.setLoc(Integer.parseInt(fields[3]));
				mispacedClass.setLcom(Integer.parseInt(fields[4]));
				mispacedClass.setWmc(Integer.parseInt(fields[5]));
				mispacedClass.setRfc(Integer.parseInt(fields[6]));
				mispacedClass.setCbo(Integer.parseInt(fields[7]));
				mispacedClass.setNom(Integer.parseInt(fields[8]));
				mispacedClass.setNoa(Integer.parseInt(fields[9]));
				mispacedClass.setDit(Integer.parseInt(fields[10]));
				mispacedClass.setNoc(Integer.parseInt(fields[11]));
				mispacedClass.setIntraConnectivity(Double.parseDouble(fields[12]));
				mispacedClass.setInterConnectivity(Double.parseDouble(fields[13]));
				mispacedClass.setDependenciesWithBelongingPackage(Integer.parseInt(fields[14]));
				mispacedClass.setDependenciesWithTargetPackage(Integer.parseInt(fields[15]));
				mispacedClass.setSimilarityWithBelongingPackage(Double.parseDouble(fields[16]));
				mispacedClass.setSimilarityWithTargetPackage(Double.parseDouble(fields[17]));

				mispacedClasses.add(mispacedClass);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mispacedClasses;
	}

	public Vector<MispacedClass> getCandidateMispacedClasses(String pVersionPath) {
		Vector<MispacedClass> mispacedClasses = new Vector<MispacedClass>();
		Pattern comma = Pattern.compile(";");
		Pattern newLine = Pattern.compile("\n");

		try {
			String[] lines = newLine.split(FileUtility.readFile(pVersionPath));

			for(String line: lines) {
				String fields[] = comma.split(line);

				if(fields[2].equals("true")) {
					MispacedClass mispacedClass = new MispacedClass();
					mispacedClass.setName(fields[0]);
					mispacedClass.setTargetPackageName(fields[1]);
					mispacedClass.setLoc(Integer.parseInt(fields[3]));
					mispacedClass.setLcom(Integer.parseInt(fields[4]));
					mispacedClass.setWmc(Integer.parseInt(fields[5]));
					mispacedClass.setRfc(Integer.parseInt(fields[6]));
					mispacedClass.setCbo(Integer.parseInt(fields[7]));
					mispacedClass.setNom(Integer.parseInt(fields[8]));
					mispacedClass.setNoa(Integer.parseInt(fields[9]));
					mispacedClass.setDit(Integer.parseInt(fields[10]));
					mispacedClass.setNoc(Integer.parseInt(fields[11]));
					mispacedClass.setIntraConnectivity(Double.parseDouble(fields[12]));
					mispacedClass.setInterConnectivity(Double.parseDouble(fields[13]));
					mispacedClass.setDependenciesWithBelongingPackage(Integer.parseInt(fields[14]));
					mispacedClass.setDependenciesWithTargetPackage(Integer.parseInt(fields[15]));
					mispacedClass.setSimilarityWithBelongingPackage(Double.parseDouble(fields[16]));
					mispacedClass.setSimilarityWithTargetPackage(Double.parseDouble(fields[17]));

					mispacedClasses.add(mispacedClass);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return mispacedClasses;
	}

	// For each smell of a specific version, get metrics and filtered metrics


}