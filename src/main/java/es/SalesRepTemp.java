package es;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;

public class SalesRepTemp {

    public static void main(String[] args) throws Exception {

        SearchResponse searchResponse = ClientFactory
                .getClient()
                .prepareSearch()
                .addAggregation(
                        AggregationBuilders.terms("apiSalesRepUser").field("apiSalesRep") 
                                .order(Terms.Order.count(false))).execute().actionGet();

        Map<String, ApiSalesUserReportDTO> apiSalesUserReportDTOs = new LinkedHashMap<>();

        // System.out.println(searchResponse);
        Terms apiSalesRepUser = searchResponse.getAggregations().get("apiSalesRepUser");

        ApiSalesUserReportDTO tempApiSalesUserReportDTO = null;

        List<Terms.Bucket> buckets= apiSalesRepUser.getBuckets();
        Filters discrepancyFilter = null;
        for (Terms.Bucket entry : apiSalesRepUser.getBuckets()) {

            tempApiSalesUserReportDTO = new ApiSalesUserReportDTO();
            tempApiSalesUserReportDTO.setTotalTrades(entry.getDocCount());
            tempApiSalesUserReportDTO.setApiSalesRep(entry.getKey());
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.matchQuery("apiSalesRep", entry.getKey()));
            searchResponse = ClientFactory
                    .getClient()
                    .prepareSearch()
                    .setQuery(boolQuery)
                    .addAggregation(AggregationBuilders.sum("discrepancyTotal").field("discrepancyTotal"))
                    .addAggregation(AggregationBuilders.missing("totalCancelation").field("status"))
                    .addAggregation(AggregationBuilders.sum("totalTiv").field("tradeInValue"))
                    .addAggregation(
                            AggregationBuilders
                                    .filters("agg")
                                    .filter(FilterBuilders.existsFilter("discrepancyReason"))
                                    .subAggregation(AggregationBuilders.count("totalTrades").field("itemId"))
                                    .subAggregation(
                                            AggregationBuilders.count("discrepanciesCount").field(
                                                    "itemDiscrepancyReasons.adjustmentReason"))).execute().actionGet();

            tempApiSalesUserReportDTO.setTotalDiscrepancyValue(new BigDecimal(((Sum) searchResponse.getAggregations()
                    .get("discrepancyTotal")).getValue()));
            tempApiSalesUserReportDTO.setTotalTradeInValue(new BigDecimal(((Sum) searchResponse.getAggregations().get(
                    "totalTiv")).getValue()));
            tempApiSalesUserReportDTO.setTotalCancellations(((Missing) searchResponse.getAggregations().get(
                    "totalCancelation")).getDocCount());

            discrepancyFilter = searchResponse.getAggregations().get("agg");
            for (Filters.Bucket tempEntry : discrepancyFilter.getBuckets()) {
                tempApiSalesUserReportDTO.setDevicesAdjusted(((ValueCount) tempEntry.getAggregations().get(
                        "totalTrades")).getValue());
                tempApiSalesUserReportDTO.setTotalDiscrepancies(((ValueCount) tempEntry.getAggregations().get(
                        "discrepanciesCount")).getValue());
            }
            
            searchResponse = ClientFactory
            .getClient()
            .prepareSearch()
            .setQuery(boolQuery).setSize(1).execute().actionGet();
            
            tempApiSalesUserReportDTO.setTradeInCompanyParentName((String) searchResponse.getHits().getAt(0)
                    .getSource().get("parentCompanyName"));
            tempApiSalesUserReportDTO.setTradeInCompanyName((String) searchResponse.getHits().getAt(0).getSource()
                    .get("companyName"));
            tempApiSalesUserReportDTO.setTradeInUserName((String) searchResponse.getHits().getAt(0).getSource()
                    .get("userName"));

            apiSalesUserReportDTOs.put(entry.getKey(), tempApiSalesUserReportDTO);
        }

        System.out.println(apiSalesUserReportDTOs);
    }

}
