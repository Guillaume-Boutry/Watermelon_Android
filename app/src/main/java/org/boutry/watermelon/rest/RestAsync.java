package org.boutry.watermelon.rest;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.JsonReader;


import org.boutry.watermelon.data.Result;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class RestAsync<T> extends AsyncTask<Request, Void, List<Result<T>>> {

    private Handler handler;
    private Function<JsonReader, T> parseFunction;
    private Consumer<List<Result<T>>> resultFunction;

    public RestAsync(Handler handler, Function<JsonReader, T> parseFunction, Consumer<List<Result<T>>> resultFunc) {
        this.handler = handler;
        this.parseFunction = parseFunction;
        this.resultFunction = resultFunc;
    }

    @Override
    protected List<Result<T>> doInBackground(Request... requests) {
        List<Result<T>> results = new ArrayList<>();
        for (Request request : requests) {
            URL url;
            try {
                url = new URL(request.getEndPoint());

            } catch (MalformedURLException e) {
                results.add(new Result.Error(e));
                continue;
            }
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                results.add(new Result.Error(e));
                continue;
            }
            // IN ms
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            try {
                // Set method: GET, POST, PUT, DELETE
                connection.setRequestMethod(request.getMethod());
            } catch (ProtocolException e) {
                results.add(new Result.Error(e));
                continue;
            }

            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // Say we want to get the response
            connection.setDoInput(true);
            // If the request is a GET request, we don't send any data
            if (request.getMethod().equals(Request.Method.GET.getMethod())) {
                connection.setDoOutput(false);
            } else {
                // Else, we add the length of the payload, it's encoding and we write it in the connection
                connection.setRequestProperty("Content-Length", request.getBody().length() + "");
                connection.setRequestProperty("charset", "utf-8");
                try (DataOutputStream o = new DataOutputStream(connection.getOutputStream())) {
                    o.write(request.getBody().getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    results.add(new Result.Error(e));
                    continue;
                }
            }
            try {
                connection.connect();
            } catch (IOException e) {
                results.add(new Result.Error(e));
                continue;
            }

            try (InputStream inputStream = connection.getInputStream();
                 JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
                results.add(new Result.Success<>(this.parseFunction.apply(reader)));
            } catch (IOException e) {
                results.add(new Result.Error(e));
            }
        }
        return results;

    }

    @Override
    protected void onPostExecute(List<Result<T>> results) {
        super.onPostExecute(results);
        handler.post(() -> this.resultFunction.accept(results));
    }
}
