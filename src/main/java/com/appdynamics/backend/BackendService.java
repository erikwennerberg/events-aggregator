package com.appdynamics.backend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class BackendService {

    private final String BASE_URL = "http://4517controllernoss-sneumannhny2020-imcoi9k6.srv.ravcloud.com:9080/events/query";


    private final String ACCOUNT_ID = "customer1_4057dd43-b852-4d2b-870c-fd0a03464d01";
    private final String API_KEY = "c17ee2d5-1047-4188-8958-9f465f54c564";
    private final String RESULT_SET_LIMIT = "20000";

    private static final Logger LOGGER= LoggerFactory.getLogger(BackendService.class);

    private final OkHttpClient httpClient = new OkHttpClient();

    // for some reason the events service expects a blank media type for the body, WTFF??
    public static final MediaType BLANK
            = MediaType.parse("");

    private final Gson gson = new Gson();


    public void doAggregation(String index1, String index2, String key1,String key2, String query){

        try {
            Response resultSet1 = runQuery(index1);
           // JsonObject resultSet1Json = new Gson().fromJson(resultSet1.body().string(), JsonObject.class);
            JsonArray resultSet1Json = gson.fromJson(resultSet1.body().string(), JsonArray.class);





            int keyFieldPosition1 = findKeyFieldPosition(key1, resultSet1Json);

            //loop over results and rebuild results with keys



            Response resultSet2 = runQuery(index2);
            JsonArray resultSet2Json = gson.fromJson(resultSet2.body().string(), JsonArray.class);

            int keyFieldPosition2 = findKeyFieldPosition(key2, resultSet2Json);





        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    Map<Object, Object> parseJsonObject(JsonArray keys, JsonArray values) {
//        Map result = new HashMap();
//        for (int i = 0; i < keys.size(); i++) {
//            result.put(parseJsonObject(keys.get(i)), parseJsonObject(values.get(i)));
//
//        }
//    }
//
//    Map<Object, Object> parseJsonObject(JsonElement element) {
//
//    }

    Response runQuery(String index) {


        LOGGER.info("running query");

        Date now = new Date();
        //Date datePastHour = new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
        Date datePastHour = new Date(System.currentTimeMillis() - (10 * 60 * 1000));
        //String datePastHourString = datePastHour.getTime().

        //append date
        String apiUrl = BASE_URL + "?start="+datePastHour.getTime()+"&end="+now.getTime()+"&limit=" + RESULT_SET_LIMIT;

        //ADQL
        RequestBody body = RequestBody.create(BLANK,"SELECT * FROM "+index);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("X-Events-API-AccountName", ACCOUNT_ID)
                .addHeader("X-Events-API-Key", API_KEY)
                .addHeader("Content-Type", "application/vnd.appd.events+json;v=2")
                .addHeader("Accept", "application/vnd.appd.events+json;v=2")
                .post(body)
                .build();

        LOGGER.info("request content header "+request.header("Content-Type"));

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            //LOGGER.info("Response: "+response.body().string());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Exception happened"+e);
            return null;
        }
    }

    private int findKeyFieldPosition(String key1, JsonArray array){
        //find key position- find transactionId for transactions under fields
        JsonArray fields = array.get(0).getAsJsonObject().get("fields").getAsJsonArray();

        //traverse results everywhere an id is found look for in result set 2 and append
        int keyPosition = 0;
        for(JsonElement element : fields) {
            keyPosition++;
            if(element.getAsJsonObject().get("field").getAsString().equals(key1)){
                break;
            }
        }
        return keyPosition;
    }


}
