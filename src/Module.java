import java.sql.SQLException;
import java.util.ArrayList;

public class Module extends BaseModel {
    private int year;
    private byte term;
    private ModuleDescriptor descriptor;
    private ArrayList<StudentRecord> records;
    private University university;

    public Module(int pk, int year, byte term, ModuleDescriptor descriptor, University university) {
        this.pk = pk;
        this.year = year;
        this.term = term;
        this.descriptor = descriptor;
        this.university = university;
        this.records = new ArrayList<>();
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
        Double sum = this.records.stream().map(StudentRecord::getFinalScore).reduce(0.0, Double::sum);

        return (sum / this.records.size());
    }

    /**
     * @return The ModuleDescriptor object with all fields filled out
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public ModuleDescriptor getDescriptor() {
        return this.descriptor;
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

    public ArrayList<StudentRecord> getRecordsReferences() {
        return records;
    }

    public StudentRecord[] getRecords() {
        return (StudentRecord[]) this.records.toArray();
    }

    public void setRecordsReferences(ArrayList<StudentRecord> records) {
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
