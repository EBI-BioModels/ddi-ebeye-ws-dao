package uk.ac.ebi.ddi.ebe.ws.dao.client.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.ddi.ebe.ws.dao.client.EbeyeClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.SimilarResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.TermResult;
import uk.ac.ebi.ddi.ebe.ws.dao.utils.Constans;
import uk.ac.ebi.ddi.ebe.ws.dao.utils.DDIUtils;

import java.net.URI;
import java.util.Set;


/**
 * @author Yasset Perez-Riverol ypriverol
 */
public class DatasetWsClient extends EbeyeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetWsClient.class);

    public DatasetWsClient(AbstractEbeyeWsConfig config) {
        super(config);
    }

    /**
     * Returns the Datasets for a domain with an specific Query
     *
     * @param domainName Domain to retrieve the information
     * @param query      Web-service query
     * @param fields     fields to be query
     * @param start      number of the first entry to retrieve
     * @param size       Number of entries to be retrieve maximum 100.
     * @param facetCount Face count the number of facets by entry.
     * @param sortfield field to sort
     * @param order order type
     * @return A list of entries and the facets included
     */

    public QueryResult getDatasets(String domainName, String query, String[] fields, String sortfield,
                                   String order, int start, int size, int facetCount) {

        String finalFields = DDIUtils.getConcatenatedField(fields);

       UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
               .scheme(config.getProtocol())
               .host(config.getHostName())
               .path("/ebisearch/ws/rest")
               .path("/" + domainName)
               .queryParam("query", query)
               .queryParam("fields", finalFields)
               .queryParam("start", start)
               .queryParam("size", size)
               .queryParam("facetcount", facetCount)
               .queryParam("format", "JSON");

       if ((sortfield != null && sortfield.length() > 0) && (order == null || order.length() == 0)) {
          order = Constans.ASCENDING;
       }

       if (!(sortfield == null || sortfield.length() == 0)) {
          builder
                  .queryParam("sortfield", sortfield)
                  .queryParam("order", order);
       }

       URI uri = builder.build().encode().toUri();
       return restTemplate.getForObject(uri, QueryResult.class);
    }

    /**
     * Returns the Datasets for a domain with a specific query with the optional parameters.
     * This method overrides @getDatasets one but implements another way to sort search
     * results as described in EBI Search documentation.
     *
     * @param domainName Domain to retrieve the information
     * @param query      Web-service query
     * @param fields     the set of the fields to be queried
     * @param start      number of the first entry to retrieve
     * @param size       Number of entries to be retrieve maximum 100.
     * @param facetCount Face count the number of facets by entry.
     * @param sortField  Sortable fields and how to sort data.
     *                   Pattern example: "name:ascending,description:descending"
     * @return           A {@link java.util.List} of entries and the facets included
     */
    public QueryResult getDatasets(String domainName, String query, String[] fields,
                                   int start, int size, int facetCount, String sortField) {
         String finalFields = DDIUtils.getConcatenatedField(fields);

         UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
            .scheme(config.getProtocol())
            .host(config.getHostName())
            .path("/ebisearch/ws/rest")
            .path("/" + domainName)
            .queryParam("query", query)
            .queryParam("fields", finalFields)
            .queryParam("start", start)
            .queryParam("size", size)
            .queryParam("facetcount", facetCount)
            .queryParam("format", "JSON");

         if (sortField != null && sortField.length() > 0) {
            builder.queryParam("sort", sortField);
         }

         URI uri = builder.build().encode().toUri();
         String searchURI = uri.toString();
         LOGGER.debug("Search URI: {}", searchURI);
         return this.restTemplate.getForObject(uri, QueryResult.class);
    }

    /**
     * This query retrieve the specific entries using a set of identifiers from an specific domain
     *
     * @param domainName domain
     * @param fields     fields to be retrieved from the domain for each specific id
     * @param ids        the set of ids to be retrieved.
     * @return QueryResult
     */
    public QueryResult getDatasetsById(String domainName, String[] fields, Set<String> ids) {

        String finalFields = DDIUtils.getConcatenatedField(fields);
        String finalIds = String.join(",", ids);
        String database = Constans.Database.retriveSorlName(domainName);

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/ebisearch/ws/rest")
                .path("/" + database)
                .path("/entry")
                .path("/" + finalIds)
                .queryParam("fields", finalFields)
                .queryParam("format", "JSON");
        URI uri = builder.build().encode().toUri();
        return restTemplate.getForObject(uri, QueryResult.class);
    }

    /**
     * This function returns the most frequently terms for an specific field in the database or repository
     *
     * @param domainName     The domain name that will be used
     * @param field          The specific field for the most frequently terms.
     * @param exclusionTerms List of terms to be excluded
     * @param size           number of terms to be retrieved
     * @return TermResult
     */
    public TermResult getFrequentlyTerms(String domainName, String field, String[] exclusionTerms, int size) {
        String exclusionWord = String.join(",", exclusionTerms);

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/ebisearch/ws/rest")
                .path("/" + domainName)
                .path("/topterms")
                .path("/" + field)
                .queryParam("excludesets", "omics_stopwords")
                .queryParam("size", size)
                .queryParam("excludes", exclusionWord)
                .queryParam("format", "JSON");
        URI uri = builder.build().encode().toUri();
        return restTemplate.getForObject(uri, TermResult.class);
    }

    public SimilarResult getSimilarProjects(String domainName, String id, String[] fields) {
        String finalFields = DDIUtils.getConcatenatedField(fields);

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHostName())
                .path("/ebisearch/ws/rest")
                .path("/" + domainName)
                .path("/entry")
                .path("/" + id)
                .path("/morelikethis/omics")
                .queryParam("mltfields", finalFields)
                .queryParam("excludesets", "omics_stopwords")
                .queryParam("entryattrs", "score")
                .queryParam("format", "JSON");
        URI uri = builder.build().encode().toUri();
        return this.restTemplate.getForObject(uri, SimilarResult.class);
    }
}
