package models;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@DatabaseTable()
public class Student extends BaseModel{
	@DatabaseField()
	private String name;
	@DatabaseField()
	private char gender;
	@DatabaseField()
	private double gpa;
	@ForeignCollectionField()
	private ForeignCollection<StudentRecord> records;
	@DatabaseField(foreign = true)
	private University university;

	public Student() {}

    /**
     * Calculate the GPA of a student, then write it to the object, and save to the DB.
     *
     * @throws SQLException : All DB queries have a risk of this occurring
     */
	private void calculateGpa() throws SQLException {
        if (this.records.size() > 0) {
            ArrayList<StudentRecord> finalScores = new ArrayList<>(this.records);
            double sum = 0.0;
            for (StudentRecord finalScore : finalScores) {
                double score = finalScore.getFinalScore();
                sum = sum + score;
            }
            this.gpa = (sum / this.records.size());
            this.update();
        }
    }

    /**
     * Splits the records up into years, then for each year, splits the records up into terms.
     *
     * @param records : ForeignCollection objects of records straight from the Student
     * @return Map in the structure {year: {term: [StudentRecord], ...}, ...}
     * @throws SQLException : All DB queries have a risk of this occurring
     */
	private Map<Integer, Map<Byte, ArrayList<StudentRecord>>> getYearMappedTermRecords(ForeignCollection<StudentRecord> records)
            throws SQLException {

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
     * @throws SQLException : All DB queries have a risk of this occurring
     */
	public String printTranscript() throws SQLException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    PrintStream printStream = new PrintStream(byteArrayOutputStream);

	    this.calculateGpa();
        printStream.printf("\nID: %d\nName: %s\nGPA: %s\n", this.pk, this.name, Math.round(this.gpa));

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
}
