package models;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;

@DatabaseTable()
public class ModuleDescriptor extends BaseModel {
	@DatabaseField(unique = true)
	private String code;
	@DatabaseField()
	private String name;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Double> continuousAssignmentWeights;
	@DatabaseField(foreign = true)
	private University university;

	public ModuleDescriptor() {}

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
