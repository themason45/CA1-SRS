package models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

@DatabaseTable(tableName = "student_record")
public class StudentRecord extends BaseModel {
    @DatabaseField(foreign = true)
    private Student student;
    @DatabaseField(foreign = true)
    private Module module;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Double> marks;
    @DatabaseField()
    private double finalScore;

    public StudentRecord() {}

    /**
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public void calculateFinalScore() throws SQLException {
        ArrayList<Double> weightedMarks = new ArrayList<>();
        ArrayList<Double> weights = this.getModule().getDescriptor().getContinuousAssignmentWeights();

        // For each mark we want to retrieve the corresponding weighting from the module descriptor
        for (int i=0; i < this.marks.size(); i++) {
            Double weighting = weights.get(i);
            weightedMarks.add(weighting * this.marks.get(i));
        }

        // Then sum them
        double markSum = weightedMarks.stream().reduce(0.0, Double::sum);

        // Then divide by the sum of the weightings
        double smallAvg = markSum / weights.stream().reduce(0.0, Double::sum);
        this.finalScore = (smallAvg * 10);
    }

    /**
     * @return A Module object with all necessary fields filled out
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public Module getModule() throws SQLException {
        return (Module) this.module.getDao().queryForId(String.valueOf(this.module.pk));
    }

    /**
     * Calculates the final score, and returns it
     * @return The final score
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public double getFinalScore() throws SQLException {
        this.calculateFinalScore();
        return finalScore;
    }

    /**
     * @return The year attribute of the module for this record
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public int getYear() throws SQLException {
        return this.getModule().getYear();
    }

    /**
     *
     * @return The term attribute of the module for this record
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public byte getTerm() throws SQLException {
        return this.getModule().getTerm();
    }

    /**
     * @return A string breaking down the aspects of this record entry
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    public String recordReport() throws SQLException {
        return String.format("%s | %s | %s | %s", this.getModule().getYear(), this.getModule().getTerm(),
                this.getModule().getDescriptor().getCode(), Math.round(this.getFinalScore()));
    }

    /**
     *
     * @return True if the score if this record is higher than the average for the module
     * @throws SQLException : All DB queries have a risk of this occurring
     */
    private Boolean isAboveAverage() throws SQLException {
        return (this.finalScore > this.getModule().getFinalAverageGrade());
    }

}
