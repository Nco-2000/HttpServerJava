package src;

public class Student {
    
    private int id;
    private int age;
    private String name;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.age = 9 + (int)(Math.random() * ((22 - 9) + 1));
    }

    public Student(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String toHtml() {
        return "<html><body><h1>Aluno: " + name + ", " + age + " anos. (ID: " + id + ").</h1></body></html>";
    }

    public String toString() {
        return "Aluno: " + name + ", " + age + " anos. (ID: " + id + ").";
    }
}
