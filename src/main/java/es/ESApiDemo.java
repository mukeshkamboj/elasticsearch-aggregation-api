package es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

public class ESApiDemo {

    public static String INDEX = "erc-fido";
    public static String INDEX_TYPE = "invoice";

    static Client client = null;

    protected void finalize() {
        client.close();
        System.out.println("client.close();");
    }

    public static void main(String[] args) {

        /*
         * System.out.println("GetAPIDemo : "); System.out.println(GetApiDemo.getAPI().getSourceAsString());
         * 
         * System.out.println("GetAPIDemo : ");
         * 
         * System.out.println(SearchAPIDemo.getSearchResponse().toString());
         * 
         * 
         * BoolQueryBuilder boolQuery = QueryBuilders.boolQuery(); RangeQueryBuilder rangeBuilder =
         * QueryBuilders.rangeQuery("tradeInDate");
         * 
         * 
         * 
         * boolQuery.must(rangeBuilder.from(1420050600000L)); boolQuery.must(rangeBuilder.to(1427221800000L));
         * 
         * System.out.println(ClientFactory.getClient().prepareSearch(ESApiDemo.INDEX).setTypes(ESApiDemo.INDEX_TYPE)
         * .addFacet(FacetBuilders.termsFacet("f").field("status")).setQuery(boolQuery).setSize(10).execute()
         * .actionGet());
         */

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeBuilder = QueryBuilders.rangeQuery("tradeInDate");

/*        boolQuery.must(rangeBuilder.from(1420050600000L));
        boolQuery.must(rangeBuilder.to(1427221800000L));*/

     //  boolQuery.must(QueryBuilders.matchQuery("companyId", 1L));

        // Term Aggregation

        SearchResponse sr = ClientFactory
                .getClient()
                .prepareSearch()
                .setQuery(boolQuery)
                .addAggregation(
                        AggregationBuilders.terms("deviceStatus").field("status").order(Terms.Order.count(false)))
                .execute().actionGet();
        System.out.println(sr);

        /*
         * Terms status= sr.getAggregations().get("deviceStatus"); status.getBuckets();
         */

        // Range Aggregation

        sr = ClientFactory
                .getClient()
                .prepareSearch()
                .setQuery(boolQuery)
                .addAggregation(
                        AggregationBuilders.range("totalTradeInValue").field("totalTradeInValue")
                                .addRange(10.00, 70.00)).execute().actionGet();
        System.out.println(sr);

        ;

        // dateHistogram Aggregation

        sr = ClientFactory
                .getClient()
                .prepareSearch()
                .setQuery(boolQuery)
                .addAggregation(
                        AggregationBuilders.dateHistogram("dateHistogram").field("tradeInDate")
                                .interval(DateHistogram.Interval.DAY)).execute().actionGet();
        System.out.println(sr);

        // stats Aggregation

        sr = ClientFactory.getClient().prepareSearch().setQuery(boolQuery)
                .addAggregation(AggregationBuilders.stats("stats").field("totalTradeInValue")).execute().actionGet();

        System.out.println(sr);

    }

}
