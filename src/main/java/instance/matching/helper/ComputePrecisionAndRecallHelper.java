package instance.matching.helper;

import instance.matching.unit.SubjectSimilarity;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ciferlv on 17-3-6.
 */
public class ComputePrecisionAndRecallHelper {

    Logger logger = LoggerFactory.getLogger(ComputePrecisionAndRecallHelper.class);

    private String standardFilePath;

    private Map<String, String> standardLink;

    private Map<String, SubjectSimilarity> myLink;

    private int correctNumber, wrongNumber;

    private double precision, recall;

    private List<String> correctResultList;

    private List<String> wrongResultList;

    public ComputePrecisionAndRecallHelper(String standardFilePath, Map<String, SubjectSimilarity> myLink) throws DocumentException, FileNotFoundException {

        this.standardFilePath = standardFilePath;

        this.myLink = myLink;

        ParseStandardFileHelper parseStandardFileHelper = new ParseStandardFileHelper(standardFilePath);

        standardLink = parseStandardFileHelper.parseXML();

        correctNumber = wrongNumber = 0;

        precision = recall = 0;

        correctResultList = new ArrayList<String>();
        wrongResultList = new ArrayList<String>();

        for (String key : myLink.keySet()) {
            if (standardLink.containsKey(key)) {

                standardLink.remove(key);
                standardLink.put(key, "1");
                correctResultList.add(key);
                correctNumber++;
            } else {

                wrongNumber++;
                wrongResultList.add(key);
            }
        }

        precision = (double) correctNumber / myLink.size();

        recall = (double) correctNumber / standardLink.size();
    }

    public void printCorrectResult(String correctResultFilePath) throws FileNotFoundException {

        PrintWriter correctStream = new PrintWriter(new FileOutputStream(correctResultFilePath));

        for (String str : correctResultList) {
            SubjectSimilarity temp = myLink.get(str);
            correctStream.println("CORRECT!");
            correctStream.println("subject1: " + temp.getSubject1());
            correctStream.println("subject2: " + temp.getSubject2());
            correctStream.println("similarity: " + temp.getSimilarity());
        }

        correctStream.close();
    }

    public void printWrongResult(String wrongResultFilePath) throws FileNotFoundException {

        PrintWriter wrongStream = new PrintWriter(new FileOutputStream(wrongResultFilePath));

        for (String str : wrongResultList) {
            SubjectSimilarity temp = myLink.get(str);
            wrongStream.println("WRONG!");
            wrongStream.println("subject1: " + temp.getSubject1());
            wrongStream.println("subject2: " + temp.getSubject2());
            wrongStream.println("similarity: " + temp.getSimilarity());
        }

        wrongStream.close();
    }

    public void printUnFoundResult(String unFoundResultFilePath) throws FileNotFoundException {

        PrintWriter unRecallStream = new PrintWriter(new FileOutputStream(unFoundResultFilePath));

        for (String key : standardLink.keySet()) {

            String temp = standardLink.get(key);

            if (temp.equals("0")) {

                String[] sub = key.split("@");
                unRecallStream.println("subject1: " + sub[0]);
                unRecallStream.println("subject2: " + sub[1]);
            }
        }

        unRecallStream.close();
    }

    public int getCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }

    public int getWrongNumber() {
        return wrongNumber;
    }

    public void setWrongNumber(int wrongNumber) {
        this.wrongNumber = wrongNumber;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }
}
