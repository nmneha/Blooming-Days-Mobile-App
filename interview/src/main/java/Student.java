public class Student extends Person{
    public int grade;


    public Student(String firstName, String lastName, int age, int grade) {
        super(firstName, lastName, age);
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }


}
