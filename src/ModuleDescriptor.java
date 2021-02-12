import java.util.ArrayList;

public class ModuleDescriptor extends BaseModel {
	private String code;
	private String name;
	private ArrayList<Double> continuousAssignmentWeights;
	private University university;

	public ModuleDescriptor(int pk, String code, String name, ArrayList<Double> continuousAssignmentWeights, University university) {
		this.pk = pk;
		this.code = code;
		this.name = name;
		this.continuousAssignmentWeights = continuousAssignmentWeights;
		this.university = university;
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
