package org.boutry.watermelon.data.model;

import android.util.JsonReader;

import java.io.IOException;

public class Wallet {
    private String id;
    private String balance;

    public Wallet(String id, String balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public static Wallet parseJson(JsonReader jsonReader) {
        try {
            jsonReader.beginArray();
            jsonReader.beginObject();
            String id = null;
            String balance = null;
            try {
                while (jsonReader.hasNext()) {
                    final String key = jsonReader.nextName();
                    switch (key) {
                        case "id":
                            id = jsonReader.nextInt() + "";
                            break;
                        case "balance":
                            balance = (jsonReader.nextDouble() / 100) + "";
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
            } finally {
                jsonReader.endObject();
                jsonReader.endArray();
            }
            return new Wallet(id, balance);
        } catch(IOException e) {
            return null;
        }
    }
}
