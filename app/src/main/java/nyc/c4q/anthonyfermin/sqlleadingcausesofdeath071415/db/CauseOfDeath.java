package nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415.db;

/**
 * Created by c4q-anthonyf on 7/14/15.
 */
public class CauseOfDeath {
    int id;
    int year;
    String ethnicity;
    String sex;
    String causeOfDeath;
    int count;
    int percent;

    public CauseOfDeath(int id, int year, String ethnicity, String sex, String causeOfDeath, int count, int percent) {
        this.id = id;
        this.year = year;
        this.ethnicity = ethnicity;
        this.sex = sex;
        this.causeOfDeath = causeOfDeath;
        this.count = count;
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Year: " + year + ",Ethnicity: " + ethnicity + ",Sex: " + sex + ",Cause: " +  causeOfDeath + ",Count: " +  count + ",Percent: " + percent;
    }
}
