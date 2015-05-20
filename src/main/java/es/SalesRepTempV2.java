package es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class SalesRepTempV2 {

    public static void main(String[] args) throws Exception {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();

        boolQuery
                .must(QueryBuilders.nestedQuery("promotionsApplied", nestedboolQuery));

        

        
/*        List<String> listApplied = new ArrayList<String>(){{add("TRADE_POST_INSPECTION");add("TRADE_IN_VALUE");}};
        List<String> listType = new ArrayList<String>(){{add("BONUS_SINGLE_ITEM");add("BONUS_SINGLE_USE");}};*/
        
        

        /*nestedboolQuery.must(QueryBuilders.matchQuery(
                "promotionsApplied.APPLIED_TO", "TRADE_POST_INSPECTION,TRADE_IN_VALUE"));
                
        nestedboolQuery.must(QueryBuilders.matchQuery("promotionsApplied.TYPE", "BONUS_SINGLE_USE"));*/
        
        
/*        nestedboolQuery.must(QueryBuilders.matchQuery(
        "promotionsApplied.APPLIED_TO", listApplied));
        
        nestedboolQuery.must(QueryBuilders.matchQuery("promotionsApplied.TYPE", listType));*/
        
        nestedboolQuery.must(QueryBuilders.matchQuery(
                "promotionsApplied.APPLIED_TO","TRADE_POST_INSPECTION"));
                
        nestedboolQuery.should(QueryBuilders.matchQuery("promotionsApplied.TYPE", "BONUS_SINGLE_ITEM"));
        
        nestedboolQuery.should(QueryBuilders.matchQuery("promotionsApplied.TYPE", "BONUS_SINGLE_USE"));
        
        
        
        SearchResponse searchResponse = ClientFactory.getClient().prepareSearch("erc-test").
        //SearchResponse searchResponse = ClientFactory.getClient().prepareSearch("erc-test_not_analyzed").        
        setQuery(boolQuery).

        get();

        System.out.println(searchResponse);
    }

}
