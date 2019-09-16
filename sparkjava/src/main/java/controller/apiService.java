package controller;

import com.google.gson.Gson;
import model.shareItem;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.zankowski.iextrading4j.api.marketdata.LastTrade;
import pl.zankowski.iextrading4j.api.refdata.v1.ExchangeSymbol;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.marketdata.LastTradeRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.refdata.v1.SymbolsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.ListRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.ListType;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class apiService {

    private static String _pk = "pk_8e4e901c9ffa4d798bfd7e87afd505d0";
    private static String _sk = "sk_8205a90214374a1bb0e91259405126fd";
    //private static String _pk = "Tpk_a61dc94ba18e40ee896db15ff299f9e5";
    //private static String _sk = "Tsk_28a16e80b0d14b5bb0962842b13cf6d8";
    private IEXCloudClient cloudClient = null;
    private IEXTradingClient iexTradingClient = null;
    public apiService(){
        cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1,
                new IEXCloudTokenBuilder()
                        .withPublishableToken(_pk)
                        .withSecretToken(_sk)
                        .build());
        iexTradingClient = (IEXTradingClient) IEXTradingClient.create();
    }
    /*public List<shareItem> genList(){
        final List<Quote> quoteList = cloudClient.executeRequest(new ListRequestBuilder()
                .withListType(ListType.IEXPERCENT)
                .build());
        List<shareItem> res = new ArrayList<shareItem>();

        shareItem temp;
        for (Quote x : quoteList){

            temp = new shareItem(x.getSymbol(),x.getCompanyName(),x.getLatestPrice().toString());
            res.add(temp);
        }
        return res;
    }*/

    public List<shareItem> genList() throws IOException {
        URL url = new URL("https://api.iextrading.com/1.0/tops/last");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream response = con.getInputStream();
        String responseBody = null;
        try (Scanner scanner = new Scanner(response)) {
            responseBody = scanner.useDelimiter("\\A").next();
            System.out.println(responseBody);
        }
        JSONArray jsonArray = new JSONArray(responseBody);
        List<shareItem> res = new ArrayList<shareItem>();
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject x = jsonArray.getJSONObject(i);
            String sym = x.getString("symbol");
            String price = x.getBigDecimal("price").toString();
            res.add(new shareItem(sym,price));
        }
        return res;
    }

    public Quote getBySymb(String symbol){
        final Quote quote = cloudClient.executeRequest(new QuoteRequestBuilder()
                .withSymbol(symbol)
                .build());
        return quote;
    }
}
