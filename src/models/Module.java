package models;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

@DatabaseTable()
public class Module extends BaseModel{

	@DatabaseField()
	private int year;
	@DatabaseField()
	private byte term;
	@DatabaseField(foreign = true)
	private ModuleDescriptor descriptor;
	@ForeignCollectionField()
	private ForeignCollection<StudentRecord> records;
	@DatabaseField(foreign = true)
	private University university;

	public Module() {}

	/**
	 * @return The year attribute
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return The term attribute
	 */
	public byte getTerm() {
		return term;
	}


	/**
	 * @return The average final grade for each student on the module
	 */
	public double getFinalAverageGrade() {
		Double sum = this.records.stream().map(studentRecord -> {
			try {
				return studentRecord.getFinalScore();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return 0.0;
		}).reduce(0.0, Double::sum);

		return (sum / this.records.size());
	}


	/**
	 * @return The ModuleDescriptor object with all fields filled out
	 * @throws SQLException : All DB queries have a risk of this occurring
	 */
	public ModuleDescriptor getDescriptor() throws SQLException {
		return (ModuleDescriptor) this.descriptor.getDao().queryForId(String.valueOf(this.descriptor.pk));
	}
}
