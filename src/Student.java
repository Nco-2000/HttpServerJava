package src;

public class Student {
    
    private int id;
    private String name;
    //private int age;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    /*
    public int getAge() {
        return this.age;
    }
    */

    public String getName() {
        return this.name;
    }

    public String toHtml() {
        return "<html><body><h1>Aluno: " + name + " (ID: " + id + ")</h1></body></html>";
    }

    public String toString() {
        return"Aluno: " + name + " (ID: " + id + ").";
    }
}
