import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentRecord extends BaseModel {
    private Student student;
    private Module module;
    private ArrayList<Double> marks;
    private double finalScore;

    public StudentRecord(int pk, Student student, Module module, ArrayList<Double> marks, double finalScore) {
        this.pk = pk;
        this.student = student;
        this.module = module;
        this.marks = marks;
        this.finalScore = finalScore;
    }

    /**
     * Calculates the final score for the Student Record, which is then stored in the instance
     */
    public void calculateFinalScore() {
        ArrayList<Double> weightedMarks = new ArrayList<>();
        Double[] weights = this.getModule().getDescriptor().getContinuousAssignmentWeights();

        // For each mark we want to retrieve the corresponding weighting from the module descriptor
        for (int i = 0; i < this.marks.size(); i++) {
            Double weighting = weights[i];
            weightedMarks.add(weighting * this.marks.get(i));
        }

        // Then sum them
        double markSum = weightedMarks.stream().reduce(0.0, Double::sum);

        // Then divide by the sum of the weightings
        double smallAvg = markSum / Arrays.stream(weights).reduce(0.0, Double::sum);
        this.finalScore = (smallAvg * 10);
    }

    /**
     * @return A Module object with all necessary fields filled out
     */
    public Module getModule() {
        return this.module;
    }

    /**
     * Calculates the final score, and returns it
     *
     * @return The final score
     */
    public double getFinalScore() {
        this.calculateFinalScore();
        return finalScore;
    }

    /**
     * @return The year attribute of the module for this record
     */
    public int getYear()  {
        return this.getModule().getYear();
    }

    /**
     * @return The term attribute of the module for this record
     */
    public byte getTerm()  {
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
     * @return True if the score if this record is higher than the average for the module
     */
    @SuppressWarnings("unused")
    private Boolean isAboveAverage() {
        return (this.finalScore > this.getModule().getFinalAverageGrade());
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @SuppressWarnings("unused")
    public ArrayList<Double> getMarks() {
        return marks;
    }

    @SuppressWarnings("unused")
    public void setMarks(Double[] marks) {
        this.marks = (ArrayList<Double>) Arrays.asList(marks);
    }

    @SuppressWarnings("unused")
    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }
}
