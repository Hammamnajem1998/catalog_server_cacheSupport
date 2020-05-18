import org.apache.log4j.BasicConfigurator;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static spark.Spark.*;

public class SparkAppMain {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        JSONObject book1Detailes = new JSONObject();
        book1Detailes.put("cost", "10");
        book1Detailes.put("number", "1");
        JSONObject book1 = new JSONObject();
        book1.put("topic", "distributed systems");
        book1.put("title", "RPCs for Dummies");
        book1.put("id", "1000");
        book1.put("details" , book1Detailes);



        JSONObject book2Detailes = new JSONObject();
        book2Detailes.put("cost", "20");
        book2Detailes.put("number", "2");
        JSONObject book2 = new JSONObject();
        book2.put("topic", "distributed systems");
        book2.put("title", "How to get a good grade in DOS in 20 minutes a day");
        book2.put("id", "2000");
        book2.put("details" , book2Detailes);



        JSONObject book3Detailes = new JSONObject();
        book3Detailes.put("cost", "30");
        book3Detailes.put("number", "3");
        JSONObject book3 = new JSONObject();
        book3.put("topic", "graduate school");
        book3.put("title", "Xen and the Art of Surviving Graduate School");
        book3.put("id", "3000");
        book3.put("details" , book3Detailes);



        JSONObject book4Detailes = new JSONObject();
        book4Detailes.put("cost", "40");
        book4Detailes.put("number", "4");
        JSONObject book4 = new JSONObject();
        book4.put("topic", "graduate school");
        book4.put("title", "Cooking for the Impatient Graduate Student");
        book4.put("id", "4000");
        book4.put("details" , book4Detailes);


        JSONObject book5Detailes = new JSONObject();
        book4Detailes.put("cost", "50");
        book4Detailes.put("number", "5");
        JSONObject book5 = new JSONObject();
        book4.put("topic", "distributed systems");
        book4.put("title", "How to finish project 3 on time");
        book4.put("id", "5000");
        book4.put("details" , book5Detailes);



        JSONObject book6Detailes = new JSONObject();
        book4Detailes.put("cost", "60");
        book4Detailes.put("number", "6");
        JSONObject book6 = new JSONObject();
        book4.put("topic", "distributed systems");
        book4.put("title", "Whu theory classes are so hard");
        book4.put("id", "6000");
        book4.put("details" , book6Detailes);



        JSONObject book7Detailes = new JSONObject();
        book4Detailes.put("cost", "70");
        book4Detailes.put("number", "7");
        JSONObject book7 = new JSONObject();
        book4.put("topic", "distributed systems");
        book4.put("title", "Sprint on the pioneer valley");
        book4.put("id", "7000");
        book4.put("details" , book7Detailes);

        JSONArray booksList = new JSONArray();
        booksList.put(book1);
        booksList.put(book2);
        booksList.put(book3);
        booksList.put(book4);


        try (FileWriter file = new FileWriter("books.json")) {

            file.write(booksList.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


        get("/search/:value", (request, response) -> {

            String value = request.params(":value");
            String foundBooks ="";
            //int index=1;
            JSONArray temp =new JSONArray();
            JSONObject temp1= new JSONObject();
            for (int i = 0; i < booksList.length(); i++)
                if (booksList.getJSONObject(i).get("topic").equals(value)) {
                    temp.put(booksList.getJSONObject(i));
                }
            temp1.put("contains",temp);
            foundBooks=temp1.toString();
            return foundBooks;

        });


        get("/lookup/:value", (request, response) -> {

            String value = request.params(":value");
            String foundBook ="";

            for (int i = 0; i < booksList.length(); i++)
                if (booksList.getJSONObject(i).get("id").equals(value))
                {

                   foundBook+=booksList.get(i).toString();
                   // foundBook += "<pre> topic : " + booksList.getJSONObject(i).get("topic")+".     Title: "+booksList.getJSONObject(i).get("title")+
                   //         ".      details : "+booksList.getJSONObject(i).get("details")+ ".</pre>";
                }
            return foundBook;
        });
        get("/buy/:value", (request, response) -> {
            String value = request.params(":value");

            for (int i = 0; i < booksList.length(); i++)
                if (booksList.getJSONObject(i).get("id").equals(value))
                {
                    int newNumber =  Integer.parseInt(booksList.getJSONObject(i).getJSONObject("details").get("number").toString())  ;
                    String id = booksList.getJSONObject(i).get("id").toString();
                    if (newNumber==0)
                    {
                        return "Failed ,The quantity is over !!";
                    }
                    newNumber--;
                    booksList.getJSONObject(i).getJSONObject("details").put("number", newNumber);

                    try (FileWriter file = new FileWriter("books.json")) {

                        file.write(booksList.toString());
                        file.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {

                        URL url = new URL("http://192.168.7.101:4567/invalidate/"+id.replaceAll(" ","%20"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
                        }
                        conn.disconnect();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    return "success!!";

                }
            return "Failed ,book not found !!";
        });
    }
}