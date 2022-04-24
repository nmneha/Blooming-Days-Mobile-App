public abstract class Person {
    String firstName;
    String lastName;

    public int getAge() {
        return age;
    }

    int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String toString() {
        String name = this.firstName + " " + this.lastName;
        String age = String.valueOf(this.age);
        String person = "Name: " + name + "\n" + "Age: " + age;
        return person;
    }
}
