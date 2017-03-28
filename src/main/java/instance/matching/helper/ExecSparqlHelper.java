package instance.matching.helper;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

/**
 * Created by ciferlv on 17-2-25.
 */
public class ExecSparqlHelper {

    public ResultSet execSelect(String queryString, Model model) {

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet results = qexec.execSelect();

        return results;
    }

}
