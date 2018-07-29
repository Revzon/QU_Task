package entities;


abstract public class Input {

    private int service;
    private int variation;
    private int questionType;
    private int category;
    private int subcategory;
    private String answer;

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int subcategory) {
        this.subcategory = subcategory;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Input input = (Input) o;

        if (getService() != input.getService()) return false;
        if (getVariation() != input.getVariation()) return false;
        if (getQuestionType() != input.getQuestionType()) return false;
        if (getCategory() != input.getCategory()) return false;
        if (getSubcategory() != input.getSubcategory()) return false;
        return getAnswer().equals(input.getAnswer());
    }

    @Override
    public int hashCode() {
        int result = getService();
        result = 31 * result + getVariation();
        result = 31 * result + getQuestionType();
        result = 31 * result + getCategory();
        result = 31 * result + getSubcategory();
        result = 31 * result + getAnswer().hashCode();
        return result;
    }
}
