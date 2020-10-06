package uk.ac.ebi.ddi.ebe.ws.dao.client.dataset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.Entry;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.SimilarResult;
import uk.ac.ebi.ddi.ebe.ws.dao.model.dataset.TermResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class DatasetWsClientTest {

    @Autowired
    AbstractEbeyeWsConfig ebeyeWsConfig;

    DatasetWsClient datasetWsClient;


    @Before
    public void setUp() throws Exception {
        datasetWsClient = new DatasetWsClient(ebeyeWsConfig);
    }

    @Test
    public void testGetDatasets() throws Exception {
        String[] fields = {"name,description"};
        QueryResult pride = datasetWsClient.getDatasets("", "E-GEOD-21397", fields, null, null, 0 , 20,10);
        assertNotNull(pride.getCount() > 1);
    }

    @Test
    public void testGetDatasetsWithMultiSortFields() throws Exception {
        String[] fields = {"name, id, isprivate"};
        String sort = "isprivate:ascending,tokenised_name:ascending";
        QueryResult biomodels = datasetWsClient.getDatasets("biomodels", "*:*", fields, 0, 10, 20, sort);
        assertNotNull(biomodels.getCount() > 1);
        sort = "id:descending,isprivate:ascending";
        String queryString = "metabolic networks AND curationstatus:\'Manually curated\'";
        biomodels = datasetWsClient.getDatasets("biomodels", queryString, fields,
                0, 10, 20, sort);
        assertNotNull(biomodels.getCount() > 1);
        String idE1 = biomodels.getEntries()[0].getId().substring(5);
        String idE2 = biomodels.getEntries()[1].getId().substring(5);
        // because the results are sorted descending on entry id, so we hope the first entry id is greater than the
        // second one
        assertTrue(Integer.parseInt(idE1) > Integer.parseInt(idE2));
    }

   /* @Test
>>>>>>> omicsdi/master
    public void testGetFrequentlyTerms() throws Exception {

        datasetWsClient = new DatasetWsClient(ebeyeWsConfig);

        String[] exclusion_words = {"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h",
                "i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
                "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with",
                "able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","can","could","dear","did",
                "do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","how","however","i","in","into",
                "its","just","least","let","like","likely","may","me","might","most","must","neither","no","nor","not","of","off",
                "often","on","only","or","other","our","own","rather","should","since","so","some","than","that","the","their","them," +
                "then","there","these","they","this","tis","to","too","us","was","we","were","what","when","where","which","while",
                "who","whom","why","will","with","would","yet","you","your",
                "protein", "proteomics", "proteomic", "proteome", "proteomes", "mass", "proteins", "lc", "ms", "based", "from", "using", "during", "LC-MS", "LC-MS/MS","reveals","as","non","data"};

        TermResult pride = datasetWsClient.getFrequentlyTerms("pride", "description", exclusion_words, 100);
        assertNotNull(pride != null);

    }

    @Test
    public void testGetDatasetsById() throws Exception {
        String[] fields = {"name"};
        String[] ids    = {"9606","9432"};
        Set<String> finalIds = new HashSet<String>(Arrays.asList(ids));
        QueryResult pride = datasetWsClient.getDatasetsById("taxonomy", fields, finalIds);
        assertNotNull(pride.getEntries().length == 1);
    }

    @Test
    public void testGetSimilarProjects() throws Exception {
        String id = "PXD001005";
        String domainName = "pride";
        String[] fields = {"name", "description"};
        SimilarResult results = datasetWsClient.getSimilarProjects(domainName, id, fields);
        assertNotNull(results.getEntries().length > 0);

    }*/
}
