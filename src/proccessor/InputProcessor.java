package proccessor;

import entities.DataLine;
import entities.Input;
import entities.Query;
import exceptions.OutOfLimitException;
import exceptions.WrongFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static proccessor.Constants.*;

public class InputProcessor {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private static InputProcessor instance;

    private InputProcessor() {
    }


    public static synchronized InputProcessor getInstance() {
        if (instance == null) {
            instance = new InputProcessor();
        }
        return instance;
    }


    public void startProccessing() {
        getData();
    }


    private void getData() {
        int numberOfLines = getLineNumber();
        if (numberOfLines > ZERO_VALUE) {
            ArrayList<Query> queries = new ArrayList<>(numberOfLines);
            ArrayList<DataLine> dataLines = new ArrayList<>(numberOfLines);

            for (int i = 0; i < numberOfLines; i++) {
                try {
                    String command = br.readLine();
                    fillInByLine(command, queries, dataLines);
                } catch (WrongFormatException e) {
                    System.out.println("Not correspondant to the format. Line #" + (i + 1));
                    return;
                } catch (OutOfLimitException e) {
                    System.out.println("One or more values are out of limits. Line #" + (i + 1));
                    return;
                }catch (NumberFormatException e){
                    System.out.println("Not correspondant to the format. Line #" + (i + 1));
                    return;
                } catch (ParseException e) {
                    System.out.println("Unable to read the date. Line #" + (i + 1));
                    return;
                } catch (IOException e) {
                    System.out.println("Something went wrong.");
                    return;
                }
            }
            queries.trimToSize();
            dataLines.trimToSize();

            if (queries.size() > EMPTY_ARRAY_LENGTH) {
                List<String> results = findResultsForQueries(queries, dataLines);
                System.out.println(results);
            }
        }
    }


    private void fillInByLine(String command, List<Query> queries, List<DataLine> dataLines) throws WrongFormatException, OutOfLimitException, ParseException {
        String[] commandElements = command.split(" ");
        String lineType = commandElements[LINETYPE_INDICATOR_INDEX];
        switch (lineType) {
            case (QUERY_INDICATOR):
                if (checkParamLength(commandElements.length, QUERY_STRING_PARAMS_NUMBER)) {
                    Query query = new Query();
                    parseString(commandElements, query);
                    queries.add(query);
                }
                else {
                    throw new WrongFormatException();
                }
                break;
            case (TIMELINE_INDICATOR):
                if (checkParamLength(commandElements.length, TIMELINE_STRING_PARAMS_NUMBER)) {
                    DataLine dataLine = new DataLine();
                    parseString(commandElements, dataLine);
                    dataLines.add(dataLine);
                }
                else {
                    throw new WrongFormatException();
                }
                break;

            default:
                throw new WrongFormatException();
       }

    }

    private boolean checkParamLength(int length, int paramsNumber) {
        return length == paramsNumber;
    }


    private void parseString(String[] commandElements, Input input) throws OutOfLimitException, WrongFormatException, ParseException {
        if (commandElements.length > EMPTY_ARRAY_LENGTH) {
            input = fillInServiceInfo(commandElements[SERVICE_DATA_INDEX], input);
            input = fillInQuestionAddress(commandElements[QUESTION_DATA_INDEX], input);
            input = fillAnswerOption(commandElements[ANSWER_TYPE_INDEX], input);
            fillSpecificFields(input, commandElements);
        }
    }

    private Input fillAnswerOption(String element, Input input) throws WrongFormatException {
        if ((FIRST_ANSWER_INDICATOR.equals(element)) || (NEXT_ANSWER_INDICATOR.equals(element))){
            input.setAnswer(element);
        } else {
            throw new WrongFormatException();
        }
        return input;
    }


    private Input fillInQuestionAddress(String element, Input input) throws OutOfLimitException {
        String[] questionAddress = element.split("\\.");
        if (checkTheBorder(questionAddress.length, MAX_QUESTION_DEEP)) {
            for (int i = 0; i < questionAddress.length; i++) {
                int numValue;
                if (!questionAddress[i].equals(ALL_DATA_INDICATOR)) {
                    numValue = Integer.parseInt(questionAddress[i]);
                    if ((i == QUESTION_TYPE_ID_INDEX) && (checkTheBorder(numValue, MAX_QUESTION_TYPE))) {
                        input.setQuestionType(numValue);
                    } else if ((i == QUESTION_CATEGORY_ID_INDEX) && (checkTheBorder(numValue, MAX_QUESTION_CATEGORY))) {
                        input.setCategory(numValue);
                    } else if ((i == QUESTION_SUBCATEGORY_ID_INDEX) && (checkTheBorder(numValue, MAX_QUESTION_SUBCATEGORY))) {
                        input.setSubcategory(numValue);
                    }
                }
            }
        }
        return input;
    }


    private Input fillInServiceInfo(String element, Input input) throws OutOfLimitException {
        String[] serviceAddress = element.split("\\.");
        if (checkTheBorder(serviceAddress.length, MAX_SERVICE_DEEP)) {
            for (int i = 0; i < serviceAddress.length; i++) {
                int numValue;
                if (!serviceAddress[i].equals(ALL_DATA_INDICATOR)) {
                    numValue = Integer.parseInt(serviceAddress[i]);
                    if ((i == SERVICE_ID_INDEX) && (checkTheBorder(numValue, MAX_SERVICE))) {
                        input.setService(numValue);
                    } else if ((i == SERVICE_VARIATION_ID_INDEX) && (checkTheBorder(numValue, MAX_SERVICE_VARIATION))) {
                        input.setVariation(numValue);
                    }
                }
            }
        }
        return input;
    }


    private void fillSpecificFields(Input input, String[] commandElements) throws OutOfLimitException, ParseException{
            if (input instanceof Query) {
                Query query = (Query) input;
                query = fillInQueryfields(query, commandElements);
            } else {
                DataLine dataLine = (DataLine) input;
                dataLine = fillInTimeLinefields(dataLine, commandElements);
            }
    }


    private DataLine fillInTimeLinefields(DataLine dataLine, String[] commandElements) throws ParseException {
        dataLine.setDate(formatter.parse(commandElements[DATE_GENERAL_INDEX]));
        dataLine.setTime(Integer.parseInt(commandElements[TIME_INDEX]));
        return dataLine;
    }


    private Query fillInQueryfields(Query query, String[] commandElements) throws ParseException, OutOfLimitException {
        String[] dateInterval = commandElements[DATE_GENERAL_INDEX].split("-");
        if (checkTheBorder(dateInterval.length, MAX_DATE_PARAMS_NUMBER)) {
            query.setStartDate(formatter.parse(dateInterval[DATE_START_INDEX]));
            if (dateInterval.length == MAX_DATE_PARAMS_NUMBER) {
                query.setEndDate(formatter.parse(dateInterval[DATE_END_INDEX]));
            } else {
                query.setEndDate(formatter.parse(dateInterval[DATE_START_INDEX]));
            }
        }
        return query;
    }


    public int getLineNumber() {
        System.out.println("Please enter number of rows: ");
        int numberOfLines;
        String data = "";
        try {
            data = br.readLine();
            numberOfLines = Integer.parseInt(data);
            if (checkTheBorder(numberOfLines, MAX_LINE_AMOUNT)){
                return numberOfLines;
            }
        }catch (OutOfLimitException e){
            System.out.println("Number of lines should be between [1, 10000]");
            return ZERO_VALUE;
        } catch (IOException e) {
            System.out.println(data + "is not a number!");
            return ZERO_VALUE;
        }
        return numberOfLines;
    }


    private boolean checkTheBorder(int number, int limit) throws OutOfLimitException{
        if ((number > limit) || (number < MIN_VALUE)) {
           throw new OutOfLimitException();
        }
        return true;
    }


    private List<String> findResultsForQueries(List<Query> queries, List<DataLine> dataLines) {
        List<String> results = new ArrayList<>();
        for (Query q : queries) {
            int time = ZERO_VALUE;
            int timeLineCounts = ZERO_VALUE;
            for (DataLine dataLine : dataLines) {
                int newTime = dataLine.getTimeByQuery(q);
                if (newTime > ZERO_VALUE) {
                    time += newTime;
                    timeLineCounts++;
                }
            }
            if (timeLineCounts == ZERO_VALUE) {
                results.add(NO_MATCHES_FOUND);
            } else {
                results.add(String.valueOf(time / timeLineCounts));
            }
        }
        return results;
    }


}
