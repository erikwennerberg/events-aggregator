package com.appdynamics.backend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


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


    public void doAggregation(String index1, String index2, String key, String query){



        Response resultSet1 = runQuery(index1);
        //LOGGER.info("resultSet1: " + resultSet1.body().string());
        Response resultSet2 = runQuery(index2);
        //LOGGER.info("resultSet2: " + resultSet2.body().string());




    }

    Response runQuery(String index) {


        LOGGER.info("running query");

        Date now = new Date();
        Date datePastHour = new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000));

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

        LOGGER.info("sending out request "+request.toString());
        LOGGER.info("request content header "+request.header("Content-Type"));
        LOGGER.info("form body" + bodyToString(request));

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            LOGGER.info("Response: "+response.body().string());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Exception happened"+e);
            return null;
        }
    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
