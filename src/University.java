import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class University extends BaseModel {
    private ModuleDescriptor[] moduleDescriptors;
    private Student[] students;
    private Module[] modules;

    public University() {
        this.moduleDescriptors = new ModuleDescriptor[]{};
        this.students = new Student[]{};
        this.modules = new Module[]{};
    }

    /**
     * @return The number of students registered in the system.
     */
    @SuppressWarnings("unused")
    public int getTotalNumberStudents() {
        return students.length;
    }

    /**
     * @return The student with the highest GPA.
     */
    public Student getBestStudent() {
        Optional<Student> bestStudent = Arrays.stream(this.getStudents()).max((o1, o2) -> {
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
        Optional<Module> bestStudent = Arrays.stream(this.modules).max((o1, o2) -> (o1.getFinalAverageGrade() > o2.getFinalAverageGrade()) ? 1 : 0);
        return bestStudent.orElse(null);
    }

    public String[][] csvParse(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String[][] output = new String[][]{};

        scanner.nextLine();  // Skip header
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[][] newArr = Arrays.copyOf(output, output.length + 1);
            newArr[output.length] = (line.split(","));
            output = newArr;
        }
        scanner.close();
        return output;
    }

    public void populate() throws IOException, ClassNotFoundException {
        // Load module descriptors
        File md_file = new File("/Users/mason/dev/IDEAProjects/CA1-SRS/csv/main_moduledescriptor.csv");
        for (String[] row : this.csvParse(md_file)) {
            ArrayList<Double> continuousAssignmentWeights = new ArrayList<>();
            for (String s : Support.parseSublist(row[2])) {
                continuousAssignmentWeights.add(Double.valueOf(s));
            }

            ModuleDescriptor[] newArr = Arrays.copyOf(this.moduleDescriptors, this.moduleDescriptors.length + 1);
            newArr[this.moduleDescriptors.length] = new ModuleDescriptor(Integer.parseInt(row[4]), row[0], row[1],
                    continuousAssignmentWeights, this);
            this.moduleDescriptors = newArr;
        }

        File s_file = new File("/Users/mason/dev/IDEAProjects/CA1-SRS/csv/main_student.csv");
        for (String[] row : this.csvParse(s_file)) {
            Student[] newArr = Arrays.copyOf(this.students, this.students.length + 1);
            newArr[this.students.length] = new Student(Integer.parseInt(row[4]), row[0], row[1].charAt(0),
                    Double.parseDouble(row[2]), this);
            this.students = newArr;
        }

        File m_file = new File("/Users/mason/dev/IDEAProjects/CA1-SRS/csv/main_module.csv");
        for (String[] row : this.csvParse(m_file)) {
            Support<ModuleDescriptor> moduleDescriptorSupport = new Support<>();
            ModuleDescriptor md = moduleDescriptorSupport.getObjectByPk(this.moduleDescriptors, Integer.parseInt(row[2]));

            Module[] newArr = Arrays.copyOf(this.modules, this.modules.length + 1);
            newArr[this.modules.length] = new Module(Integer.parseInt(row[4]), Integer.parseInt(row[0]),
                    Byte.parseByte(row[1]), md, this);
            this.modules = newArr;
        }

        File sr_file = new File("/Users/mason/dev/IDEAProjects/CA1-SRS/csv/main_student_record.csv");
        for (String[] row : this.csvParse(sr_file)) {
            Support<Student> studentSupport = new Support<>();
            Student student = studentSupport.getObjectByPk(this.students, Integer.parseInt(row[0]));

            Support<Module> moduleSupport = new Support<>();
            Module module = moduleSupport.getObjectByPk(this.modules, Integer.parseInt(row[1]));

            StudentRecord studentRecord = new StudentRecord(Integer.parseInt(row[4]),
                    student, module, Support.parseSublistAsDoubles(row[2]), Double.parseDouble(row[3]));

            student.addRecord(studentRecord);
            studentSupport.updateArr(this.students, student);

            module.addRecord(studentRecord);
            moduleSupport.updateArr(this.modules, module);

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");

        University baseUni = new University();
        baseUni.populate();

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
    }

    public Student[] getStudents() {
        return students;
    }

    public Module[] getModules() {
        return modules;
    }

    public ModuleDescriptor[] getModuleDescriptors() {
        return moduleDescriptors;
    }

    public void setModuleDescriptors(ModuleDescriptor[] moduleDescriptors) {
        this.moduleDescriptors = moduleDescriptors;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public void setModules(Module[] modules) {
        this.modules = modules;
    }
}
