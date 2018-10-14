import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreditBook {

    private final int mId;
    private final String mStudent;
    private final ArrayList<CreditBook.Session> mSessions = new ArrayList<>();

    public CreditBook(int id, String student) {
        this.mId = id;
        this.mStudent = student;
    }

    public String getStudent() {
        return mStudent;
    }

    public void addSessions(List<Session> sessions) {
        mSessions.addAll(sessions);
    }

    public void addSession(Session session) {
        mSessions.add(session);
    }

    public void clear() {
        mSessions.clear();
    }

    public int getId() {
        return mId;
    }

    public ArrayList<Session> getSessions() {
        return mSessions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format(Locale.US, "Id: %d, Student: %s\n", mId, mStudent));

        sb.append("Sessions:\n");
        for (Session session : mSessions) {
            sb.append(session.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static class Session {

        private final ArrayList<Session.Exam> mExams = new ArrayList<>();
        private final ArrayList<Session.Credit> mCredits = new ArrayList<>();

        public void addExam(Session.Exam exam) {
            mExams.add(exam);
        }

        public void addExams(List<Session.Exam> exams) {
            mExams.addAll(exams);
        }

        public void addCredit(Session.Credit credit) {
            mCredits.add(credit);
        }

        public void addCredits(List<Session.Credit> credits) {
            mCredits.addAll(credits);
        }

        public void clearExams() {
            mExams.clear();
        }

        public void clearCredits() {
            mCredits.clear();
        }

        public void clear() {
            clearCredits();
            clearExams();
        }

        public ArrayList<Exam> getExams() {
            return mExams;
        }

        public ArrayList<Credit> getCredits() {
            return mCredits;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Exams:\n");
            for (Exam exam : mExams) {
                sb.append(exam.toString());
                sb.append("\n");
            }

            sb.append("Credits:\n");
            for (Credit credit : mCredits) {
                sb.append(credit.toString());
                sb.append("\n");
            }

            return sb.toString();
        }

        public int averageMarkPerSession() {
            int result = 0;
            for(Session.Exam ex : getExams()) {
                result += ex.mMark;
            }
            
            return result;
        }

        public static class Credit extends AcademicTest {

            private boolean mPassed;

            public Credit(Date date, String teacher, String subject, int hours, boolean passed) {
                super(date, teacher, subject, hours);
                this.mPassed = passed;
            }

            public boolean isPassed() {
                return mPassed;
            }

            public void setPassed(boolean mPassed) {
                this.mPassed = mPassed;
            }

            @Override
            public String toString() {
                return String.format("Date: %s, Teacher: %s, Subject: %s, Hours: %d, Passed: %s",
                        DateUtils.formatDate(mDate), mTeacher, mSubject, mHours, String.valueOf(mPassed));
            }
        }

        public static class Exam extends AcademicTest {

            private int mMark;

            public Exam(Date date, String teacher, String subject, int hours, int mark) {
                super(date, teacher, subject, hours);
                this.mMark = mark;
            }

            public int getMark() {
                return mMark;
            }

            public void setMark(int mMark) {
                this.mMark = mMark;
            }

            @Override
            public String toString() {
                return String.format("Date: %s, Teacher: %s, Subject: %s, Hours: %d, Mark: %d",
                        DateUtils.formatDate(mDate), mTeacher, mSubject, mHours, mMark);
            }
        }
    }

    public static class Parser {
        private static final String CB_START = "CB_START";
        private static final String CB_END = "CB_END";
        private static final String SESSION_START = "SESSION_START";
        private static final String SESSION_END = "SESSION_END";
        private static final String CREDIT = "CREDIT";
        private static final String EXAM = "EXAM";
        private static final String DELIMETER = "\\|";

        private static SimpleDateFormat sDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

        public static ArrayList<CreditBook> parse(BufferedReader reader) throws IOException, ParseException {
            ArrayList<CreditBook> result = new ArrayList<>();

            CreditBook creditBook = null;

            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case CB_START:
                        creditBook = parseCreditBook(reader);
                        break;
                    case CB_END:
                        if (creditBook != null) {
                            result.add(creditBook);
                        }
                        break;
                    case SESSION_START:
                        if (creditBook != null) {
                            creditBook.addSession(parseSession(reader));
                        }
                        break;
                }
            }

            return result;
        }

        private static CreditBook parseCreditBook(BufferedReader reader) throws IOException {
            String line = reader.readLine();
            String[] args = line.split(DELIMETER);

            int id = Integer.parseInt(args[0]);
            String name = args[1];

            return new CreditBook(id, name);
        }

        private static Session parseSession(BufferedReader reader) throws IOException, ParseException {
            Session session = new Session();

            String line = reader.readLine();
            while (!SESSION_END.equals(line)) {
                if (line == null) {
                    throw new IOException("Unexpected end of file");
                }

                // EXAM|01.01.2018|Petrov-Vodkin|Matan|68|9
                // CREDIT|03.01.2018|Pupkin|GiA|106|true

                String[] args = line.split(DELIMETER);

                Date date = sDateFormatter.parse(args[1]);
                String teacher = args[2];
                String subject = args[3];
                int hours = Integer.parseInt(args[4]);

                switch (args[0]) {
                    case EXAM:
                        int mark = Integer.parseInt(args[5]);
                        CreditBook.Session.Exam exam = new CreditBook.Session.Exam(date, teacher, subject, hours, mark);
                        session.addExam(exam);
                        break;
                    case CREDIT:
                        boolean passed = Boolean.parseBoolean(args[5]);
                        CreditBook.Session.Credit credit = new CreditBook.Session.Credit(date, teacher, subject, hours, passed);
                        session.addCredit(credit);
                        break;
                }

                line = reader.readLine();
            }

            return session;
        }
    }
}



