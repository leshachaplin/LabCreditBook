import java.util.Date;

public class AcademicTest {

    protected Date mDate;
    protected String mTeacher;
    protected String mSubject;
    protected int mHours;

    public AcademicTest(Date date, String teacher, String subject, int hours) {
        this.mDate = date;
        this.mTeacher = teacher;
        this.mSubject = subject;
        this.mHours = hours;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public void setTeacher(String mTeacher) {
        this.mTeacher = mTeacher;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public int getHours() {
        return mHours;
    }

    public void setHours(int mHours) {
        this.mHours = mHours;
    }
}
