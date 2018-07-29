package entities;

import java.util.Date;

/**
 * Created by olga on 13.07.18.
 */
public class Query extends Input {

    private Date startDate;
    private Date endDate;


    public Query() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Query{" +
                "service =" + super.getService() +
                ", variation =" + super.getVariation() +
                ", question type=" + super.getQuestionType() +
                ", category=" + super.getCategory() +
                ", subcategory=" + super.getSubcategory() +
                ", answer=" + super.getAnswer() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Query query = (Query) o;

        if (!getStartDate().equals(query.getStartDate())) return false;
        return getEndDate().equals(query.getEndDate());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + getEndDate().hashCode();
        return result;
    }
}

