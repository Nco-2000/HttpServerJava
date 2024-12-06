package src;

import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class StudentHandler {
    
    private List<Student> students = new LinkedList<Student>();
    private List<Integer> usedIds = new LinkedList<Integer>();

    private ClientProcessor user = null;
    
    private synchronized void getAcess(ClientProcessor user) throws InterruptedException {
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

    private void checkIfNotBeingAltered() throws InterruptedException {
        while (this.user != null) {
            wait();
        }
    }

    public Student getStudent(int studentId, ClientProcessor user) throws InterruptedException {
        this.checkIfNotBeingAltered();
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(student.getId() == studentId) {
                this.leftAcess();
                return student;
            }
        }
        this.leftAcess();
        return null;
    }

    public int getLastStudentId() {
        int nextId = 1;
        
        Iterator<Integer> idIterator = this.usedIds.iterator();
        while (idIterator.hasNext()) {
            Integer id = idIterator.next();
            if(nextId <= id) {
                nextId = id + 1;
            }
        }
        return nextId;
    }

    public synchronized Student createStudent(ClientProcessor user) throws InterruptedException {
        this.getAcess(user);
        int id = this.getLastStudentId();
        Student newStudent = new Student("STUDENT_" + id, id);
        this.usedIds.add(id);
        this.students.add(newStudent);
        this.leftAcess();
        return newStudent;
    }

    public void showAllStudents(ClientProcessor user) throws InterruptedException {
        this.checkIfNotBeingAltered();
        Iterator<Student> studenIterator = this.students.iterator();
        while (studenIterator.hasNext()) {
            Student student = studenIterator.next();
            System.out.println(student.toString());
        }
        this.leftAcess();
    }

    public String getStudentToHTML(int studentId, ClientProcessor user) throws InterruptedException {
        this.checkIfNotBeingAltered();
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(student.getId() == studentId) {
                this.leftAcess();
                return student.toHtml();
            }
        }
        this.leftAcess();
        return null;
    }

    public Boolean studentExists(int studentId, ClientProcessor user) throws InterruptedException {
        this.checkIfNotBeingAltered();
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(student.getId() == studentId) {
                this.leftAcess();
                return true;
            }
        }
        this.leftAcess();
        return false;
    }

    public synchronized void deleteStudent(int studentId, ClientProcessor user) throws InterruptedException {
        this.getAcess(user);
        Iterator<Student> studentIterator = this.students.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if(student.getId() == studentId) {
                studentIterator.remove();
            }
        }
        this.leftAcess();
    }
}
