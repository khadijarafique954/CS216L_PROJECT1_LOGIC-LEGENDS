package lab1assignment;
import java.util.Scanner;

class Student {
    int rollNo;
    String name;
    double gpa;
    Student next;

    Student(int rollNo, String name, double gpa) {
        this.rollNo = rollNo;
        this.name = name;
        this.gpa = gpa;
        this.next = null;
    }
}

class UndoStack {

    String[] stack;
    int top;

    UndoStack(int size) {
        stack = new String[size];
        top = -1;
    }

    void push(String action) {
        if (top == stack.length - 1) {
            System.out.println("Undo stack is full.");
        } else {
            top++;
            stack[top] = action;
        }
    }

    String pop() {
        if (top == -1) {
            return null;
        }

        String action = stack[top];
        top--;

        return action;
    }

    void display() {
        if (top == -1) {
            System.out.println("Undo stack is empty.");
            return;
        }

        System.out.println("\n--- Undo Stack ---");

        for (int i = top; i >= 0; i--) {
            System.out.println(stack[i]);
        }
    }
}

public class StudentRecordSystem {

    Student head;

    // Add Student
    void addStudent(int rollNo, String name, double gpa) {

        Student newStudent = new Student(rollNo, name, gpa);

        if (head == null) {
            head = newStudent;
        } else {

            Student current = head;

            while (current.next != null) {
                current = current.next;
            }

            current.next = newStudent;
        }

        System.out.println("Student added successfully!");
    }

    void displayStudents() {

        if (head == null) {
            System.out.println("No student records.");
            return;
        }

        Student current = head;

        System.out.println("\n----- Student Records -----");

        while (current != null) {

            System.out.println(
                "Roll No: " + current.rollNo +
                " | Name: " + current.name +
                " | GPA: " + current.gpa
            );

            current = current.next;
        }
    }

    Student searchStudent(int rollNo) {

        Student current = head;

        while (current != null) {

            if (current.rollNo == rollNo) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    Student deleteStudent(int rollNo) {

        if (head == null) {
            return null;
        }

        if (head.rollNo == rollNo) {

            Student deleted = head;
            head = head.next;

            return deleted;
        }

        Student current = head;

        while (current.next != null) {

            if (current.next.rollNo == rollNo) {

                Student deleted = current.next;

                // Remove node
                current.next = current.next.next;

                return deleted;
            }

            current = current.next;
        }

        return null;
    }
    void sortByGPA() {

        if (head == null || head.next == null) {
            System.out.println("Not enough students to sort.");
            return;
        }

        boolean swapped;

        do {

            swapped = false;

            Student current = head;

            while (current.next != null) {

                if (current.gpa > current.next.gpa) {

                    int tempRoll = current.rollNo;
                    current.rollNo = current.next.rollNo;
                    current.next.rollNo = tempRoll;

                    String tempName = current.name;
                    current.name = current.next.name;
                    current.next.name = tempName;

                    double tempGPA = current.gpa;
                    current.gpa = current.next.gpa;
                    current.next.gpa = tempGPA;

                    swapped = true;
                }

                current = current.next;
            }

        } while (swapped);

        System.out.println("Students sorted by GPA!");
    }

    boolean updateGPA(int rollNo, double newGPA) {

        Student student = searchStudent(rollNo);

        if (student != null) {

            student.gpa = newGPA;
            return true;
        }

        return false;
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        StudentRecordSystem system = new StudentRecordSystem();

        UndoStack undoStack = new UndoStack(10);

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("      STUDENT RECORD SYSTEM");
            System.out.println("================================");

            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display Students");
            System.out.println("5. Sort by GPA");
            System.out.println("6. Update GPA");
            System.out.println("7. Undo Last Action");
            System.out.println("8. Show Undo Stack");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            // Input validation
            while (!input.hasNextInt()) {
                System.out.println("Please enter a number.");
                input.next();
                System.out.print("Enter your choice: ");
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                // =========================
                // 1. ADD STUDENT
                // =========================
                case 1:

                    System.out.print("Enter Roll No: ");
                    int rollNo = input.nextInt();
                    input.nextLine();

                    // Check duplicate roll number
                    if (system.searchStudent(rollNo) != null) {
                        System.out.println("Roll number already exists!");
                        break;
                    }

                    System.out.print("Enter Name: ");
                    String name = input.nextLine();

                    if (name.trim().isEmpty()) {
                        System.out.println("Name cannot be empty!");
                        break;
                    }

                    System.out.print("Enter GPA: ");
                    double gpa = input.nextDouble();

                    if (gpa < 0 || gpa > 4) {
                        System.out.println("GPA must be between 0 and 4.");
                        break;
                    }

                    system.addStudent(rollNo, name, gpa);

                    break;

                // =========================
                // 2. DELETE STUDENT
                // =========================
                case 2:

                    System.out.print("Enter Roll No to delete: ");
                    int deleteRoll = input.nextInt();

                    Student deleted = system.deleteStudent(deleteRoll);

                    if (deleted != null) {

                        undoStack.push(
                            "Deleted Student: Roll No = "
                            + deleted.rollNo
                            + ", Name = "
                            + deleted.name
                            + ", GPA = "
                            + deleted.gpa
                        );

                        System.out.println("Student deleted successfully!");

                    } else {

                        System.out.println("Student not found!");
                    }

                    break;

                // =========================
                // 3. SEARCH STUDENT
                // =========================
                case 3:

                    System.out.print("Enter Roll No to search: ");
                    int searchRoll = input.nextInt();

                    Student found = system.searchStudent(searchRoll);

                    if (found != null) {

                        System.out.println("\nStudent Found!");
                        System.out.println("Roll No: " + found.rollNo);
                        System.out.println("Name: " + found.name);
                        System.out.println("GPA: " + found.gpa);

                    } else {

                        System.out.println("Student not found!");
                    }

                    break;

                // =========================
                // 4. DISPLAY STUDENTS
                // =========================
                case 4:

                    system.displayStudents();

                    break;

                // =========================
                // 5. SORT BY GPA
                // =========================
                case 5:

                    system.sortByGPA();

                    break;

                // =========================
                // 6. UPDATE GPA
                // =========================
                case 6:

                    System.out.print("Enter Roll No: ");
                    int updateRoll = input.nextInt();

                    Student student = system.searchStudent(updateRoll);

                    if (student != null) {

                        System.out.println("Old GPA: " + student.gpa);

                        System.out.print("Enter New GPA: ");
                        double newGPA = input.nextDouble();

                        if (newGPA < 0 || newGPA > 4) {

                            System.out.println(
                                "GPA must be between 0 and 4."
                            );

                        } else {

                            system.updateGPA(updateRoll, newGPA);

                            undoStack.push(
                                "Updated GPA of Roll No: "
                                + updateRoll
                            );

                            System.out.println(
                                "GPA updated successfully!"
                            );
                        }

                    } else {

                        System.out.println("Student not found!");
                    }

                    break;

                // =========================
                // 7. UNDO
                // =========================
                case 7:

                    String action = undoStack.pop();

                    if (action == null) {

                        System.out.println("Nothing to undo.");

                    } else {

                        System.out.println(
                            "Last action removed from stack:"
                        );

                        System.out.println(action);
                    }

                    break;

                // =========================
                // 8. SHOW STACK
                // =========================
                case 8:

                    undoStack.display();

                    break;

                // =========================
                // 9. EXIT
                // =========================
                case 9:

                    System.out.println(
                        "Thank you! Program ended."
                    );

                    break;

                default:

                    System.out.println(
                        "Invalid choice! Enter 1 to 9."
                    );
            }

        } while (choice != 9);

        input.close();

    }
}
