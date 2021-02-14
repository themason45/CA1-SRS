import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Student extends BaseModel{
	private String name;
	private char gender;
	private double gpa;
	private ArrayList<StudentRecord> records;
	private University university;

	public Student(int pk, String name, char gender, double gpa, University university) {
        if (name == null) {
            throw new NullPointerException("Name, or ID cannot be null");
        }
	    this.pk = pk;
	    this.name = name;
	    this.gender = gender;
	    this.gpa = gpa;
	    this.university = university;
	    this.records = new ArrayList<>();
    }

    /**
     * Calculate the GPA of a student, then write it to the object, and save to the DB.
     */
	private void calculateGpa() {
        if (this.records.size() > 0) {
            ArrayList<StudentRecord> finalScores = new ArrayList<>(this.records);
            double sum = 0.0;
            for (StudentRecord finalScore : finalScores) {
                double score = finalScore.getFinalScore();
                sum = sum + score;
            }
            this.gpa = (sum / this.records.size());
        }
    }

    /**
     * Splits the records up into years, then for each year, splits the records up into terms.
     *
     * @param records : ForeignCollection objects of records straight from the Student
     * @return Map in the structure {year: {term: [StudentRecord], ...}, ...}
     */
	private Map<Integer, Map<Byte, ArrayList<StudentRecord>>> getYearMappedTermRecords(ArrayList<StudentRecord> records) {

        Map<Integer, ArrayList<StudentRecord>> yearMappedRecords = new HashMap<>();
        Map<Integer, Map<Byte, ArrayList<StudentRecord>>> yearMappedTermRecords = new HashMap<>();

        for (StudentRecord record : records) {
            yearMappedRecords.computeIfAbsent(record.getYear(), k -> new ArrayList<>());  // Create empty list if needs be
            yearMappedRecords.get(record.getYear()).add(record);
        }

        for (ArrayList<StudentRecord> yearRecords : yearMappedRecords.values()) {
            Map<Byte, ArrayList<StudentRecord>> termMappedRecords = new HashMap<>();
            for (StudentRecord record : yearRecords) {

                yearMappedTermRecords.computeIfAbsent(record.getYear(), k -> new HashMap<>());
                termMappedRecords.computeIfAbsent(record.getTerm(), k -> new ArrayList<>());

                ArrayList<StudentRecord> termRecords = termMappedRecords.get(record.getTerm());
                termRecords.add(record);
                termMappedRecords.put(record.getTerm(), termRecords);
                yearMappedTermRecords.put(record.getYear(), termMappedRecords);
            }
        }
        return yearMappedTermRecords;
    }

    /**
     * This uses a PrintStream in order to be able to write to the string as if it were printing as normal
     *
     * @return A string that when printed, puts out the transcript in the required format
     */
	public String printTranscript() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    PrintStream printStream = new PrintStream(byteArrayOutputStream);

	    this.calculateGpa();
        printStream.printf("University of Knowledge - Official Transcript \n\n\nID: %d\nName: %s\nGPA: %s\n\n", this.pk, this.name, Math.round(this.gpa));

        Map<Integer, Map<Byte, ArrayList<StudentRecord>>> yearMappedTermRecords = this.getYearMappedTermRecords(this.records);
        for (Map<Byte, ArrayList<StudentRecord>> yearRecords: yearMappedTermRecords.values()) {
            for (ArrayList<StudentRecord> termRecords : yearRecords.values()) {
                termRecords.forEach(studentRecord -> {
                    try {
                        printStream.println(studentRecord.recordReport());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
                printStream.println();  // Print new line for new term
            }
        }
        return byteArrayOutputStream.toString();
    }

    public double getGpa() throws SQLException {
	    this.calculateGpa();
        return gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public ArrayList<StudentRecord> getRecordsReferences() {
        return records;
    }

    public StudentRecord[] getRecords() {
        return (StudentRecord[]) this.records.toArray();
    }

    public void setRecordsReference(ArrayList<StudentRecord> records) {
        this.records = records;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public void addRecord(StudentRecord studentRecord) {
        this.records.add(studentRecord);
    }
}
