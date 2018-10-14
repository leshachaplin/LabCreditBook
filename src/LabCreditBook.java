import javax.swing.*;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;


public class LabCreditBook {

    private ArrayList<CreditBook> mCreditBooks;

    public static void main(String[] args) {
        /*
        1. Input
        2. Output
        3. Output particular CreditBook
        4. Count scholarship, average mark per session, total average mark
        5. Edit
        6. Print students with avg. mark >= 9.0
         */
        String name;
        name = JOptionPane.showInputDialog("Input the name of student");

        LabCreditBook lab = new LabCreditBook();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("input.txt"));
            lab.mCreditBooks = CreditBook.Parser.parse(bufferedReader);
            CreditBook creditBook = lab.findCreditBookByStudentName(name);

            System.out.println(creditBook.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CreditBook findCreditBookByStudentName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        for (CreditBook cb : mCreditBooks) {
            if (name.equals(cb.getStudent())) {
                return cb;
            }
        }

        return null;
    }

    public void printStudentByName(String name) {
        CreditBook creditBook = findCreditBookByStudentName(name);

        System.out.println("Student name: " + creditBook.getStudent());
        System.out.println("Student Id: " + creditBook.getId());

        for (CreditBook.Session session : creditBook.getSessions()) {
            System.out.println("----------------EXAMS------------------");
            for (CreditBook.Session.Exam ex : session.getExams()) {
                System.out.println("Date: " + ex.mDate + " " + "Teacher: " +
                        ex.mTeacher + " " + "Subjeckt: " + ex.mSubject + " " + "Mark: " + ex.getMark());
            }
            System.out.println("---------------CREDITS----------------");
            for (CreditBook.Session.Credit cr : session.getCredits()) {
                System.out.println("Date: " + cr.mDate + " " + "Teacher: " +
                        cr.mTeacher + " " + "Subjeckt: " + cr.mSubject + " " + "Mark: " + cr.isPassed());
            }
            System.out.println("-------------------------------------------");
        }
    }
}

