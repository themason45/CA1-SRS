import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class ModuleDescriptor extends BaseModel {
	private String code;
	private String name;
	private ArrayList<Double> continuousAssignmentWeights;
	private University university;

	public ModuleDescriptor(int pk, String code, String name, ArrayList<Double> continuousAssignmentWeights, University university) throws Exception {
		if (code == null | name == null) {
			throw new NullPointerException("Name, or Code cannot be null for a Module Descriptor");
		}

		this.pk = pk;
		this.code = code;
		this.name = name;
		if (ModuleDescriptor.checkValidWeights(continuousAssignmentWeights)) {
			this.continuousAssignmentWeights = continuousAssignmentWeights;
		} else {
			throw new Exception("Weights must sum to 1, and be non null");
		}
		this.university = university;
	}

	/**
	 * @param weights Input ArrayList of Doubles containing the assessment weights
	 * @return Whether the inputted ArrayList is valid, otherwise, false
	 */
	public static Boolean checkValidWeights(ArrayList<Double> weights) {
		if (weights != null) {
			double sum = weights.stream().mapToDouble(x -> x).sum();
			return Math.abs(1 - sum) < 0.0001;
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return The continuousAssignmentWeights attribute
	 */
	public Double[] getContinuousAssignmentWeights() {
		return this.continuousAssignmentWeights.toArray(new Double[0]);
	}

	/**
	 * @return The code attribute
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	public void setContinuousAssignmentWeights(ArrayList<Double> continuousAssignmentWeights) {
		this.continuousAssignmentWeights = continuousAssignmentWeights;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}
}
