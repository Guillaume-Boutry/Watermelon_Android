package org.boutry.watermelon.data.model;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PayIn {
    private String id;
    private String walletId;
    private String amount;

    public PayIn(String id, String walletId, String amount) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static PayIn parseJson(JsonReader jsonReader) {
        try {
            jsonReader.beginObject();
            String id = null;
            String walletId = null;
            String amount = null;
            try {
                while (jsonReader.hasNext()) {
                    final String key = jsonReader.nextName();
                    switch (key) {
                        case "id":
                            id = jsonReader.nextInt() + "";
                            break;
                        case "wallet_id":
                            walletId = jsonReader.nextInt() + "";
                            break;
                        case "amount":
                            amount = (jsonReader.nextDouble() / 100) + "";
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
            } finally {
                jsonReader.endObject();
            }
            return new PayIn(id, walletId, amount);
        } catch(IOException e) {
            return null;
        }
    }

    public static List<PayIn> parseListJson(JsonReader jsonReader) {
        List<PayIn> payIns = new ArrayList<>();
        try {
            jsonReader.beginArray();
            try {
                while (jsonReader.hasNext()) {
                    PayIn payIn = PayIn.parseJson(jsonReader);
                    if (payIn != null) {
                        payIns.add(payIn);
                    }
                }
            } finally {
                jsonReader.endArray();
            }

        } catch(IOException e) {
            // Do nothing
            e.printStackTrace();
        }
        return payIns;

    }

    @Override
    public String toString() {
        return "PayIn{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }
}
