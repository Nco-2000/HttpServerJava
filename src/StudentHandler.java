package src;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class StudentHandler {
    
    private List<Student> students = new LinkedList<Student>();

    private ClientHandler user = null;
    
    private synchronized void getAcess(ClientHandler user) throws InterruptedException {
        while (this.user != null) {
            wait();
        }
        this.user = user;
        return;
    }

    private synchronized void leftAcess() {
        this.user = null;
        notifyAll();
    }

    public synchronized int getLastStudentId() {
        int id = 1;
        
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(id <= student.getId()) {
                id = student.getId() + 1;
            }
        }
        return id;
    }

    public synchronized Student createStudent(ClientHandler user) throws InterruptedException {
        this.getAcess(user);
        int id = this.getLastStudentId();
        Student newStudent = new Student("STUDENT" + id, id);
        this.students.add(newStudent);
        this.leftAcess();
        //this.showAllStudents(user);
        return newStudent;
    }

    public synchronized void showAllStudents(ClientHandler user) throws InterruptedException {
        this.getAcess(user);
        Iterator<Student> studenIterator = this.students.iterator();
        while (studenIterator.hasNext()) {
            Student student = studenIterator.next();
            System.out.println(student.toString());
        }
        this.leftAcess();
    }

    public synchronized String getStudentToHTML(int studentId, ClientHandler user) throws InterruptedException {
        this.getAcess(user);
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(student.getId() == studentId) {
                return student.toHtml();
            }
        }
        this.leftAcess();
        return null;
    }
}
