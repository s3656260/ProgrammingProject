package controller;

import model.shareItem;
import pl.zankowski.iextrading4j.api.refdata.v1.ExchangeSymbol;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.refdata.v1.SymbolsRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.ListRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.ListType;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import java.util.ArrayList;
import java.util.List;

public class apiService {

    private static String _pk = "pk_8e4e901c9ffa4d798bfd7e87afd505d0";
    private static String _sk = "sk_8205a90214374a1bb0e91259405126fd";
    //private static String _pk = "Tpk_a61dc94ba18e40ee896db15ff299f9e5";
    //private static String _sk = "Tsk_28a16e80b0d14b5bb0962842b13cf6d8";
    private IEXCloudClient cloudClient = null;
    public apiService(){
        cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1,
                new IEXCloudTokenBuilder()
                        .withPublishableToken(_pk)
                        .withSecretToken(_sk)
                        .build());
    }
    public List<shareItem> getSymbols() {
        final IEXTradingClient iexTradingClient = (IEXTradingClient) IEXTradingClient.create();
        final List<ExchangeSymbol> exchangeSymbolList = iexTradingClient.executeRequest(new SymbolsRequestBuilder()
                .build());
        List<shareItem> res = new ArrayList<shareItem>();
        shareItem temp;
        for (ExchangeSymbol x : exchangeSymbolList){
            String str = x.getSymbol();
            temp = new shareItem(str);
            res.add(temp);
        }
        return res;
        //System.out.println(exchangeSymbolList);
    }
    public List<shareItem> genList(){
        final List<Quote> quoteList = cloudClient.executeRequest(new ListRequestBuilder()
                .withListType(ListType.MOSTACTIVE)
                .build());
        List<shareItem> res = new ArrayList<shareItem>();
        shareItem temp;
        for (Quote x : quoteList){

            temp = new shareItem(x.getSymbol(),x.getCompanyName(),x.getLatestPrice().toString());
            res.add(temp);
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
