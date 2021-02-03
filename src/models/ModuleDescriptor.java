package models;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

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
	public ArrayList<Double> getContinuousAssignmentWeights() {
		return continuousAssignmentWeights;
	}

	/**
	 * @return The code attribute
	 */
	public String getCode() {
		return code;
	}
}
