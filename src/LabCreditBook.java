import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabCreditBook {

    private ArrayList<CreditBook> mCreditBooks;

    public static class CreditBookWindow extends JFrame {
        private JLabel countLabel;
        private JButton printStudent;
        private JButton removeText;
        private JButton isPassedSession;
        private JButton printMarkBySubject;
        private JTextArea textArea = new JTextArea(20,20);

        public CreditBookWindow(CreditBook creditBook) {
            super("Calculator");
            setBounds(500, 200, 600, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Подготавливаем компоненты объекта
            countLabel = new JLabel("Credit book of student " + creditBook.getStudent());
            printStudent = new JButton("Print creditbook");
            removeText = new JButton("remove");
            isPassedSession = new JButton("is Passed Session");
            printMarkBySubject = new JButton("Mark by subject");

            //Подготавливаем временные компоненты
            JPanel buttonsPanel = new JPanel(new FlowLayout());
            //Расставляем компоненты по местам
            add(countLabel, BorderLayout.NORTH);
            add(textArea);
            textArea.setLineWrap(true);
            buttonsPanel.add(printStudent);
            buttonsPanel.add(removeText);
            buttonsPanel.add(isPassedSession);
            buttonsPanel.add(printMarkBySubject);

            add(buttonsPanel, BorderLayout.SOUTH);

            printStudent.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    textArea.setText(creditBook.toString());
                }
            });

            removeText.addActionListener((new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clearTextArea();
                }
            }));

            isPassedSession.addActionListener((new ActionListener() {
                @Override
                    public void actionPerformed (ActionEvent e) {
                    for (CreditBook.Session se : creditBook.getSessions()) {
                        boolean passed = se.isPassed();
                        textArea.append("Student " + creditBook.getStudent() + (passed ? " passed " : " not passed ")
                                + "session " + se.getNumberOfSession() + "\n");
                    }
                }
            }));
        }

        private void clearTextArea() {
            textArea.setText("");
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

    public void printStudentWithHighAverageMark(ArrayList<CreditBook> creditBooks) {
        for (CreditBook cr : creditBooks) {
            for (CreditBook.Session se : cr.getSessions()) {
                if(se.averageMarkPerSession() >= 9.0) {
                    System.out.format("Student %s get High mark per session %d\n", cr.getStudent(), se.getNumberOfSession());
                }
            }
        }
    }

    public void printMarkBySubject(ArrayList<CreditBook> creditBooks, String subject, int numberOfSession) {
        for (CreditBook cr : creditBooks) {
            for (CreditBook.Session se : cr.getSessions()) {
                if(se.getNumberOfSession() != numberOfSession) {
                    continue;
                }

                for (CreditBook.Session.Exam ex : se.getExams()) {
                    if (subject.equals(ex.mSubject)) {
                        System.out.format("Mark by %s by %s per session %d is: %d\n",
                                ex.mSubject, cr.getStudent(), se.getNumberOfSession(), ex.getMark());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        /*
        1. Input
        2. Output
        3. Output particular CreditBook
        4. Count scholarship, average mark per session, total average mark
        5. Edit
        6. Print students with avg. mark >= 9.0
         */
        BufferedReader bufferedReader = null;

        try {
            LabCreditBook lab = new LabCreditBook();
            bufferedReader = new BufferedReader(new FileReader("input.txt"));
            lab.mCreditBooks = CreditBook.Parser.parse(bufferedReader);

            String name;
//            Scanner in = new Scanner(System.in);
//            name = in.nextLine();
            name = JOptionPane.showInputDialog("Input the name of student");

            CreditBook creditBook = lab.findCreditBookByStudentName(name);
            System.out.println(creditBook.toString());

            for (CreditBook.Session se : creditBook.getSessions()) {
                boolean passed = se.isPassed();
                System.out.format("Student %s " + (passed ? "passed " : "not passed ")
                        + "session %d\n", name, se.getNumberOfSession());
            }

            for (CreditBook.Session session : creditBook.getSessions()) {
                System.out.format("Average mark per session %d: %.1f\n" , session.getNumberOfSession() ,
                        session.averageMarkPerSession());
            }

            System.out.format("Total average mark %.1f:\n",creditBook.totalAverageMark());
            lab.printStudentWithHighAverageMark(lab.mCreditBooks);

            lab.printMarkBySubject(lab.mCreditBooks, "Matan", 1);

            CreditBookWindow app = new CreditBookWindow(creditBook);
            app.setVisible(true);

            HashMap<String, ArrayList<CreditBook.Session.Exam>> map =
                    (HashMap<String, ArrayList<CreditBook.Session.Exam>>) CreditBook.Session.
                    Exam.Parser.parse(FileUtils.readFile("statement.txt"));
            for (Map.Entry<String, ArrayList<CreditBook.Session.Exam>> entry : map.entrySet()) {
                CreditBook cb = lab.findCreditBookByStudentName(entry.getKey());
                CreditBook.Session se = new CreditBook.Session();
                se.addExams(entry.getValue());
                cb.addSession(se);
            }

            System.out.println(lab.mCreditBooks);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } catch (Exception en) {
            System.out.println("Input variable name of student");
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
}


