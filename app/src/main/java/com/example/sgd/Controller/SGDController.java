package com.example.sgd.Controller;

import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.sgd.Entity.Amenities;
import com.example.sgd.Entity.Carpark;
import com.example.sgd.Entity.DataStoreFactory;
import com.example.sgd.Entity.DataStoreInterface;
import com.example.sgd.Entity.HDBCarpark;
import com.example.sgd.Entity.LTACarpark;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class SGDController {
    String token;
    BufferedReader br;
    Request request;
    String oneMapToken;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList amenList = new ArrayList();
    ArrayList<Carpark> carparkList = new ArrayList<Carpark>();
    ArrayList<HDBCarpark> hdbCarparkList = new ArrayList<HDBCarpark>();
    ArrayList<LTACarpark> ltaCarparkList = new ArrayList<LTACarpark>();
    ArrayList<String> CarparkNoList = new ArrayList<String>();
    List<String> URACarparks = new ArrayList<String>();
    Location curLocation;
    OkHttpClient httpClient = new OkHttpClient();
    String debugTag = "dbug:Controller";
    DataStoreInterface datastore;

    //instance creation
    private static SGDController instance = new SGDController();

    //Singleton Constructor
    private SGDController(){}

    public static SGDController getInstance(){
        return instance;
    }

    public String getToken(){
        return oneMapToken;
    }

    public ArrayList<LTACarpark> getLTACarparkList(){ return ltaCarparkList; }

    public ArrayList getAmenList() {
        return amenList;
    }

    public ArrayList<HDBCarpark> getHDBCarparkList(){
        return hdbCarparkList;
    }

    public ArrayList<String> getURACarparks(){
        return CarparkNoList;
    }

    public Location getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(Location curLocation) {
        this.curLocation = curLocation;
    }

    public String getOMToken(){
        return oneMapToken;
    }

    public void setHdbCarparkList(ArrayList<HDBCarpark> hdbCarparkList){
        this.hdbCarparkList = hdbCarparkList;
    }

    public void setLtaCarparkList(ArrayList<LTACarpark> ltaCarparkList){
        this.ltaCarparkList = ltaCarparkList;
    }

    //===============DataStore Factory Start===================================
    public void RetrieveTheme(String themeName) {
        amenList = new ArrayList<Amenities>();
        //Log.v(debugTag, themeName);
        datastore = DataStoreFactory.getDatastore(themeName);
        amenList = datastore.retrieveData(this,themeName);
    }
    public ArrayList nearestAmen(Location currentLoc, ArrayList amenList){
        ArrayList sortedAmenList = datastore.sortByDistance(currentLoc, amenList);
        return sortedAmenList;
    }
    //===============DataStore Factory End===================================

    //OneMap Retrieve Token
    public void GetOneMapToken(){
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("email", "C200174@e.ntu.edu.sg")
                .add("password", "P@ssword123")
                .build();

        request = new Request.Builder()
                .url("https://developers.onemap.sg/privateapi/auth/post/getToken")
                .post(formBody)
                .build();
        httpClient = new OkHttpClient();
        //synchronus call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                jsonObject = new JSONObject(response.body().string());
                oneMapToken = jsonObject.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getToken(String apiKey) throws IOException, Exception {
        URL url = new URL("https://www.ura.gov.sg/uraDataService/insertNewToken.action");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("AccessKey", apiKey);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36");
        int responseCode = connection.getResponseCode(); //GET RESPONSE <200>

        if(responseCode != 200) {
            throw new RuntimeException("HTTP RESPONSE CODE: " +responseCode);
        }
        else {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String readLine = null;
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in.close();
            JSONObject jobject = new JSONObject(response.toString());
            String token = (String) jobject.get("Result");
            System.out.println(token);
            return token;
        }
    }

    public ArrayList<String> carparkNo() throws IOException, Exception{
        String token = getToken("566492d4-a351-4aee-8560-247d125645ff");
        String readLine1=null;
        String carparkNo;
        ArrayList<String> listOfCarparks= new ArrayList<String>();
        URL url1 = new URL("https://www.ura.gov.sg/uraDataService/invokeUraDS?service=Car_Park_Availability");
                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
        connection1.setRequestMethod("GET");
        connection1.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
        connection1.addRequestProperty("Token", token);
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
            StringBuffer response1 = new StringBuffer();
            while ((readLine1 = in1 .readLine()) != null) {
                response1.append(readLine1);
            } in1.close();
            JSONObject jobjectOfCarparks = new JSONObject(response1.toString());

            JSONArray jsonarrOfCarparks =  jobjectOfCarparks.getJSONArray("Result");

            for(int i=0; i<jsonarrOfCarparks.length(); i++) {
                carparkNo = (String) ((JSONObject)jsonarrOfCarparks.get(i)).get("carparkNo");
                listOfCarparks.add(carparkNo);
            }
            Set<String> listWithoutDuplicates = new LinkedHashSet<String>(listOfCarparks);
            listOfCarparks.clear();
            listOfCarparks.addAll(listWithoutDuplicates);
        } catch(JSONException e)
        {
            Log.v(debugTag, "testa" + String.valueOf(e));
        }

        if(listOfCarparks.size() == 0)
        {
            Log.v(debugTag, "testa" + "Nothing is in list of carparks");
        }
        for(int i=0; i< listOfCarparks.size();i++)
        {
            //Log.v(debugTag,"testa" + listOfCarparks.get(i));
        }
        return listOfCarparks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List CrossCheck(String carparkId) throws IOException, Exception {
        String token = getToken("566492d4-a351-4aee-8560-247d125645ff");
        List<String> rates = new ArrayList<String>();

        String readLine2 = null;
        URL url2 = new URL("https://www.ura.gov.sg/uraDataService/invokeUraDS?service=Car_Park_Details");
        HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
        connection2.setRequestMethod("GET");
        connection2.setRequestProperty("AccessKey", "566492d4-a351-4aee-8560-247d125645ff");
        connection2.addRequestProperty("Token", token);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
        StringBuffer response2 = new StringBuffer();
        while ((readLine2 = in2 .readLine()) != null) {
            response2.append(readLine2);
        } in2.close();


        JSONObject jobject2 = new JSONObject(response2.toString());

        JSONArray jsonarr_2 =  jobject2.getJSONArray("Result");
        String carparkNo;

        String ppName = "";
        int carCount = 0, motorcycleCount = 0, heavyVehicleCount = 0;
        List<String[]> list = new ArrayList<>();
        String[] header = {"Carpark No", "Start Time", "End Time", "Parking Lot Availability","Parking Lot Capacity", "Vehicle Type",
                "Weekday Rate", "Weekday Min", "Saturday Rate", "Saturday Min","Sunday/PH Rate","Sunday/PH Min"};
        list.add(header);
        carparkNo = "";
        JSONObject jsonobj_2;
        int count = 0;
        JSONArray json_arr;
        jsonobj_2 = (JSONObject)jsonarr_2.get(count);
        carCount = 0;
        motorcycleCount = 0;
        heavyVehicleCount = 0;
        carparkNo = carparkId;
        ppName = (String) jsonobj_2.get("ppCode");
        System.out.println("carparkNo: " + carparkNo);
        //For loop to find where the rates and details of the carpark that we found
        //from the carpark lot availability in the carpark details json array
        for (int z = 0; z < jsonarr_2.length(); z++) {
            jsonobj_2 = (JSONObject)jsonarr_2.get(z);
            ppName = (String)jsonobj_2.get("ppCode");
            if( ppName.equals(carparkNo)){
                count = z;
                break;
            }
        }
        json_arr = new JSONArray();
        jsonobj_2 = (JSONObject)jsonarr_2.get(count);
        try {
            while(ppName.equals(carparkNo))
            {
                String vehicleType =(String) jsonobj_2.get("vehCat");
                if(vehicleType.equals("Car"))
                {
                    carCount++;
                } else if(vehicleType.equals("Motorcycle"))
                {
                    motorcycleCount++;
                } else if(vehicleType.equals("Heavy Vehicle"))
                {
                    heavyVehicleCount++;
                }
                json_arr.put(jsonobj_2);
                count++;
                if(count >= jsonarr_2.length()) break;
                jsonobj_2 = (JSONObject)jsonarr_2.get(count);
                ppName = (String) jsonobj_2.get("ppCode");

            }
            Log.v(debugTag, "tdtaaa");

            rates = CarparkRatesParsing(json_arr, carCount, heavyVehicleCount, motorcycleCount);
            for(int z = 0; z< rates.size(); z++)
            {
                System.out.println(rates.get(z));
            }

        }
        catch(JSONException e)
        {
            System.out.println("JSONException from getCarparkRatesForCarparkNo" + e);
        }
        return rates;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> CarparkRatesParsing(JSONArray data, int carCount, int heavyVehicleCount, int motorcycleCount) {
        String startTime, endTime, weekdayRate, weekdayMin, satdayRate, satdayMin, sundayPH, sundayPHMin, lotCapacity;
        int i, diff;
        String line;
        List<String> carparkRates = new ArrayList<String>();
        if(data.length() > 0)
        {
            try {
                JSONObject jsonobj_2 = (JSONObject)data.get(0);
                lotCapacity = jsonobj_2.get("parkCapacity").toString();
                line = "**Mon-Sat**" + "\nCar " + "Time Period\n";
                for( i=0; i<carCount; i++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff = checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        line = "**Mon - Sun**\n" + "Max Rate for Car from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        //System.out.println("**Mon - Sun**\n");
                        //carparkRates.put("Line", startTime);
                        carparkRates.add(line);
                        line = "";
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                    carparkRates.add(line);
                    line = "";
                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                }
                line = "**Mon-Sat**" + "\nHeavy Vehicle " + "Time Period\n";
                for(int j=0; j<heavyVehicleCount; j++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff=checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        i++;
                        line = "**Mon - Sun**\n" + "Max Rate for Heavy Vehicle from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        carparkRates.add(line);
                        line = "";
                        //System.out.println("**Mon - Sun**\n");
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                    carparkRates.add(line);
                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n");
                    i++;
                    line = "";
                }
                line = "**Mon-Sat**" + "\nMotorcycle" + "Time Period\n";
                for(int z = 0; z<motorcycleCount; z++)
                {
                    diff = 0;
                    jsonobj_2 = (JSONObject)data.get(i);
                    startTime = (String) jsonobj_2.get("startTime");
                    endTime = (String) jsonobj_2.get("endTime");
                    weekdayMin = (String) jsonobj_2.get("weekdayMin");
                    weekdayRate = (String) jsonobj_2.get("weekdayRate");
                    diff =checkDifferenceInTime(startTime, endTime, weekdayMin);
                    if(diff == 1)
                    {
                        i++;
                        line = "**Mon - Sun**\n" + "Max Rate for Motorcycle from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";
                        //System.out.println("**Mon - Sun**\n");
                        //System.out.println("Max Rate from \n" + startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + " \n");
                        carparkRates.add(line);
                        line = "";
                        continue;
                    }
                    satdayRate = (String) jsonobj_2.get("satdayRate");
                    satdayMin = (String) jsonobj_2.get("satdayMin");

                    sundayPH = (String) jsonobj_2.get("sunPHRate");
                    sundayPHMin = (String) jsonobj_2.get("sunPHMin");
                    line += startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + "\n";

                    //System.out.println(startTime + " - " + endTime + " : " + weekdayRate + " per " + weekdayMin + " \n");
                    i++;
                    line = "";
                }

                System.out.println("");
            }
            catch(JSONException e)
            {
                System.out.println("JSON exception error" + e);
            }
            //Do lots later after I settle the carpark details
            //String carparkNo = (String) jsonobj_1.get("carparkNo");
        }
        else
        {
            carparkRates.add("data json array is length 0");

        }
        return carparkRates;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int checkDifferenceInTime(String startTime, String endTime, String weekdayMin)
    {

        Log.v(debugTag, "tdtaaa");

        LocalDate endDate = LocalDate.now(), startDate = LocalDate.now();
        long diffMinutes;
        int count = 0;
        if(LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ).compareTo(
                LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH))) > 0)
            endDate = LocalDate.now().plusDays(1);

        LocalDateTime s    = LocalDateTime.of(
                startDate,
                LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ));
        LocalDateTime d = LocalDateTime.of(
                endDate,
                LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh.mm a", Locale.ENGLISH) ));

        diffMinutes =  Duration.between(s, d).toMinutes();
        String substr = "";

        for(int i = 0; i < weekdayMin.length(); i++)
        {
            if(Character.isDigit(weekdayMin.charAt(i)))
            {
                substr += weekdayMin.charAt(i);
            }
            else break;
        }
        if(diffMinutes == Long.parseLong(substr))
        {
            count++;
        }
        return count;
    }



}
