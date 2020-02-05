package com.appdynamics.backend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import okhttp3.*;
import okio.Buffer;
import org.springframework.stereotype.Service;


@Service
public class BackendService {

    //private List<Employee> employees;
    private final String BASE_URL = "http://4517controllernoss-sneumannhny2020-imcoi9k6.srv.ravcloud.com:9080/events/query";

    private final OkHttpClient httpClient = new OkHttpClient();


    public static final MediaType JSON
            = MediaType.parse("");

    public void runQuery(String index1, String index2, String key, String query) {

        //OkHttpClient client = new OkHttpClient();

        System.out.println("running query");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");



        String dateNowString = format.format( new Date()   );

        Date datePastHour = new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000));

        String datePastHourString = format.format( datePastHour  );

//        RequestBody formBody = new FormBody.Builder()
//                //.add("label", "combo_query")
//                //.add("SELECT * FROM transactions")
//                .addEncoded("query", "SELECT * FROM transactions")
//                //.add("limit", "100")
//                //.add("start", datePastHourString)
//                //.add("end", dateNowString)
//                .build();
        RequestBody body = RequestBody.create(JSON,"SELECT * FROM transactions");

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("X-Events-API-AccountName", "customer1_4057dd43-b852-4d2b-870c-fd0a03464d01")
                .addHeader("X-Events-API-Key", "c17ee2d5-1047-4188-8958-9f465f54c564")
                .addHeader("Content-Type", "application/vnd.appd.events+json;v=2")
                .addHeader("Accept", "application/vnd.appd.events+json;v=2")
                .post(body)
                .build();
        System.out.println("sending out request "+request.toString());
        System.out.println("request content header "+request.header("Content-Type"));

        System.out.println("form body" + bodyToString(request));

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println("Response"+response);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception happened"+e);
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
