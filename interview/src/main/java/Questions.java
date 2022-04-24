import java.util.*;

public class Questions {


    //shortcut to crete main method: psvm
    public static void main(String[] args) {
        Person teacher1 = new Teacher("John", "Doe", 28); //polymorhpism
        Person teacher2 = new Teacher("Jane", "Dough", 35);
        Person student1 = new Student("Steve", "Rogers", 9, 5);
        Person student2 = new Student("Tony", "Stark", 9, 5);
        Person student3 = new Student("Kyle", "Bond", 15, 10);
        Person student4 = new Student("Madison", "Beer", 15, 10);

        List<Person> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        teachers.add(teacher1);
        teachers.add(teacher2);

        students.add((Student) student1);
        students.add((Student) student2);
        students.add((Student) student3);
        students.add((Student) student4);

        Map<Person, List<Student>> classRoom = classRooms(teachers, students);
        classPrint(classRoom);
    }

    public static Map<Person, List<Student>> classRooms(List<Person> teachers, List<Student> students) {
        Map<Person, List<Student>> classroom = new HashMap<>();
        List<Student> grade5 = new ArrayList<>();
        List<Student> grade10 = new ArrayList<>();
        for(Student s : students) {
            if (s.getGrade() == 5) {
                grade5.add(s);
            } else {
                grade10.add(s);
            }
        }
        for (Person t : teachers) {
            if (t.getAge() == 28) {
                classroom.put(t, grade5);
            } else {
                classroom.put(t, grade10);
            }
        }
        return classroom;
    }

    public static void classPrint(Map<Person, List<Student>> classroom) {
        for (Person t: classroom.keySet()) {
            List<Student> students = classroom.get(t);
            System.out.println("Teacher:" + t.toString());
            for (Student s: students) {
                System.out.println("Student: " + s.toString());
            }
            System.out.println("-------------");
        }
    }
}
