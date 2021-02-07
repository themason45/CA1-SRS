package models;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable()
public class Module extends BaseModel {
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

    public Module() {
    }

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

    public void setYear(int year) {
        this.year = year;
    }

    public void setTerm(byte term) {
        this.term = term;
    }

    public void setDescriptor(ModuleDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public ForeignCollection<StudentRecord> getRecordsReferences() {
        return records;
    }

    public StudentRecord[] getRecords() {
        return (StudentRecord[]) this.records.toArray();
    }

    public void setRecordsReferences(ForeignCollection<StudentRecord> records) {
        this.records = records;
    }

    public void addRecord(StudentRecord record) {
        this.records.add(record);
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
