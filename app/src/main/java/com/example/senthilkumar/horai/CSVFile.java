package com.example.senthilkumar.horai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.senthilkumar.horai.Constant.FIRST_COLUMN;
import static com.example.senthilkumar.horai.Constant.FOURTH_COLUMN;
import static com.example.senthilkumar.horai.Constant.SECOND_COLUMN;
import static com.example.senthilkumar.horai.Constant.THIRD_COLUMN;


public class CSVFile {

    private ArrayList<HashMap> resultList;
    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ArrayList<HashMap> read() {
        resultList = new ArrayList<HashMap>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");

                HashMap temp = new HashMap();
                temp.put(FIRST_COLUMN, row[1]);
                temp.put(SECOND_COLUMN, row[2]);
                temp.put(THIRD_COLUMN, row[3]);
                temp.put(FOURTH_COLUMN, row[4]);
                resultList.add(temp);

            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
}
