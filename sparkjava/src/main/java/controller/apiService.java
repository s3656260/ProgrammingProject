package controller;

import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

public class apiService {

    private static String _pk = "pk_8e4e901c9ffa4d798bfd7e87afd505d0";
    private static String _sk = "sk_8205a90214374a1bb0e91259405126fd";

    public apiService(){

    }
    /*public String doGet(String foo) throws IOException {
        URL urlForGetRequest = new URL("https://cloud.iexapis.com/stable/stock/aapl/quote?token=sk_8205a90214374a1bb0e91259405126fd");
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            return(response.toString());
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
        return(null);
    }*/
    public String getTop(int top){
        final IEXCloudClient cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1,
                new IEXCloudTokenBuilder()
                        .withPublishableToken(_pk)
                        .withSecretToken(_sk)
                        .build());
        final Quote quote = cloudClient.executeRequest(new QuoteRequestBuilder()
                .withSymbol("AAPL")
                .build());
        return quote.toString();
    }
    public Quote getBySymb(String symbol){
        final IEXCloudClient cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1,
                new IEXCloudTokenBuilder()
                        .withPublishableToken(_pk)
                        .withSecretToken(_sk)
                        .build());
        final Quote quote = cloudClient.executeRequest(new QuoteRequestBuilder()
                .withSymbol(symbol)
                .build());
        return quote;
    }
}
