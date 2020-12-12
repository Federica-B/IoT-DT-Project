package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reading {

    /*try with two value
    * Create an array list that contain the destination of the document that i want read
    * */

    private static String[] locationData = {"TS1.txt", "TS2.txt"};

    public static void main(String[] args) {

        ArrayList<Double> x = readTwo();
        System.out.println(Arrays.deepToString(x.toArray()));


        /*List l = readListFileInList("C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Progetti\\database-reading\\data\\CE.txt");
        l.stream().spliterator();
        Iterator<String> itr = l.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());}*/
    }

    private static List readListFileInList(String fileName) {
        List<String> lines = Collections.emptyList();
        try{
            lines = Files.readAllLines(Paths.get(fileName));

        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    private static ArrayList<Double> readone(){
        try{

            BufferedReader cvsReader = new BufferedReader(new FileReader("C:\\Users\\User\\Desktop\\Uni\\Terzo anno\\IoT\\Progetti\\database-reading\\data\\CE.txt"));
            String row;
            ArrayList<Double> data = new ArrayList<Double>();
            while ((row = cvsReader.readLine()) != null){
                String [] line = row.split("\t");
                for (String s : line) {
                    data.add(Double.parseDouble(s));
                    //System.out.println(line[i]);
                }
            }

            return data;

            //System.out.println(Arrays.deepToString(data.toArray()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private static ArrayList<Double> readTwo(){
        try{

            ArrayList<Double> data = new ArrayList<Double>();

            for(String s : locationData){
                String location = "data/" + s.toString();
                BufferedReader cvsReader = new BufferedReader(new FileReader(location));
                String row;
                while ((row = cvsReader.readLine()) != null){
                    String [] line = row.split("\t");
                    for (String x : line) {
                        data.add(Double.parseDouble(x));
                        //System.out.println(line[i]);
                    }

            }
            }
            return data;

        }catch (Exception e){
            return null;
        }
    }
}
