import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Optional;

@DatabaseTable(tableName = "university")
public class University extends BaseModel {
    @ForeignCollectionField(eager = true, maxEagerLevel = 2)
    private ForeignCollection<ModuleDescriptor> moduleDescriptors;
    @ForeignCollectionField(eager = true, maxEagerLevel = 2)
    private ForeignCollection<Student> students;
    @ForeignCollectionField(eager = true, maxEagerLevel = 2)
    private ForeignCollection<Module> modules;

    public University() {
    }

    /**
     * Initialize the database tables, this only needs to be used once, the database should be bundled in with this
     * project. The reason I used a DB was for speed, and for further expansion down the line if I need it.
     */
    @SuppressWarnings("unused")
    private static void createTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, University.class);
        TableUtils.createTableIfNotExists(connectionSource, StudentRecord.class);
        TableUtils.createTableIfNotExists(connectionSource, Student.class);
        TableUtils.createTableIfNotExists(connectionSource, ModuleDescriptor.class);
        TableUtils.createTableIfNotExists(connectionSource, Module.class);
    }

    /**
     * @return The number of students registered in the system.
     */
    @SuppressWarnings("unused")
    public int getTotalNumberStudents() {
        return students.size();
    }

    /**
     * @return The student with the highest GPA.
     */
    @SuppressWarnings("unused")
    public Student getBestStudent() {
        Optional<Student> bestStudent = this.students.stream().max((o1, o2) -> {
            try {
                return (o1.getGpa() > o2.getGpa()) ? 1 : 0;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return 0;
        });
        return bestStudent.orElse(null);
    }

    /**
     * @return The module with the highest average score.
     */
    @SuppressWarnings("unused")
    public Module getBestModule() {
        Optional<Module> bestStudent = this.modules.stream().max((o1, o2) -> (o1.getFinalAverageGrade() > o2.getFinalAverageGrade()) ? 1 : 0);
        return bestStudent.orElse(null);
    }

    public static void main(String[] args) throws SQLException {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:db.sqlite");
        // StudentRecordSystem.createTables(connectionSource);  Only used once, to create all the tables.

        Dao<University, String> universityDao = DaoManager.createDao(connectionSource, University.class);
        University baseUni = universityDao.queryForId("1");

        // Do all the printing malarkey
        System.out.printf("The UoK has %d students.\n", baseUni.getTotalNumberStudents());
        System.out.printf("The best module is: \n%s\n", baseUni.getBestModule().getDescriptor().getName());
        System.out.printf("The best student is: \n%s", baseUni.getBestStudent().printTranscript());

//        baseUni.getStudents().forEach(student -> {
//            try {
//                System.out.print(student.printTranscript());
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//        });
        connectionSource.closeQuietly();
    }

    public ForeignCollection<Student> getStudents() {
        return students;
    }

    public ForeignCollection<Module> getModules() {
        return modules;
    }
}
