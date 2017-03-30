package instance.matching.version1.helper;

import java.io.PrintWriter;

/**
 * Created by ciferlv on 17-3-1.
 */
public class PrintMethodHelper {

    private String head;
    public static String toRDF(String entity1, String entity2, String relation, String measure) {
        String out = "\t<map>\n" +
                "\t\t<Cell>\n" +
                "\t\t\t<entity1 rdf:resource=\'" + entity1 + "\'/>\n" +
                "\t\t\t<entity2 rdf:resource=\'" + entity2 + "\'/>\n" +
                "\t\t\t<relation>" + relation + "</relation>\n" +
                "\t\t\t<measure rdf:datatype=\'http://www.w3.org/2001/XMLSchema#float\'>" + measure + "</measure>\n" +
                "\t\t</Cell>\n" +
                "\t</map>\n";
        return out;
    }

    public static void printHead(PrintWriter outStream,String head) {

        outStream.print(head);
    }

    public static void printTail(PrintWriter outStream) {

        outStream.println("</Alignment>");
        outStream.println("</rdf:RDF>");
    }


}
