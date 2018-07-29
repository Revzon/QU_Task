package entities;

import java.util.Date;

/**
 * Created by olga on 13.07.18.
 */
public class DataLine extends Input {

    private int time;
    private Date date;


    public DataLine() {
    }


    public int getTime() {
        return time;
    }


    public void setTime(int time) {
        this.time = time;
    }


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public int getTimeByQuery(Query query) {
        if ( (query.getService() != 0) && (getService() != query.getService()) ) {
            return 0;
        }

        if ( (query.getVariation() != 0) && (getVariation() != query.getVariation()) ) {
            return 0;
        }

        if ( (query.getQuestionType() != 0) && (getQuestionType() != query.getQuestionType()) ) {
            return 0;
        }

        if ( (query.getCategory() != 0) && (getCategory() != query.getCategory()) ) {
            return 0;
        }

        if ( (query.getSubcategory() != 0) && (getSubcategory() != query.getSubcategory()) ) {
            return 0;
        }

        if (! getAnswer().equals(query.getAnswer())) {
            return 0;
        }

        if (getDate().before(query.getEndDate()) && getDate().after(query.getStartDate())) {
            return getTime();
        }

        return 0;
    }


    @Override
    public String toString() {
        return "DataLine{" +
                "service_id=" + super.getService() +
                ", variation_id=" + super.getVariation() +
                ", question_type_id=" + super.getQuestionType() +
                ", category_id=" + super.getCategory() +
                ", subcategory_id=" + super.getSubcategory() +
                ", answer=" + super.getAnswer() +
                ", date=" + date +
                ", time=" + time +'}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DataLine dataLine = (DataLine) o;

        if (getTime() != dataLine.getTime()) return false;
        return getDate().equals(dataLine.getDate());
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTime();
        result = 31 * result + getDate().hashCode();
        return result;
    }

}

