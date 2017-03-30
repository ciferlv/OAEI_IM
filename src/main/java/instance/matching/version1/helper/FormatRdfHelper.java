package instance.matching.version1.helper;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import static instance.matching.version1.helper.PrintMethodHelper.printHead;
import static instance.matching.version1.helper.PrintMethodHelper.printTail;
import static instance.matching.version1.helper.PrintMethodHelper.toRDF;

/**
 * Created by XinzeLv on 2017/3/9.
 */
public class FormatRdfHelper {

    public static void main(String[] args) throws DocumentException, FileNotFoundException {

        Logger logger = LoggerFactory.getLogger(FormatRdfHelper.class);

        String oldFilePath = "src/main/resources/dataSet/sabine_linguistic/refalign.rdf";
        String newFilePath = "src/main/resources/dataSet/sabine_linguistic/new_refalign.rdf";

        PrintWriter outStream = new PrintWriter(new FileOutputStream(newFilePath));
        SAXReader reader = new SAXReader();
        Document document = reader.read(oldFilePath);
        Element root = document.getRootElement();

        Iterator<Element> alignmentIterator = root.elements("CounterPart").iterator();
        Element alignmentElement = alignmentIterator.next();

        Iterator<Element> mapIterator = alignmentElement.elements("map").iterator();

        String head = "<?xml version='1.0' encoding='utf-8' standalone='no'?>\n"
                + "<rdf:RDF xmlns='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'\n"
                + "\t\t xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' \n"
                + "\t\t xmlns:xsd='http://www.w3.org/2001/XMLSchema#'\n"
                + "\t\t xmlns:align='http://knowledgeweb.semanticweb.org/heterogeneity/alignment#'>\n"
                + "<CounterPart>\n"
                + "\t<xml>yes</xml>\n"
                + "\t<level>0</level>\n"
                + "\t<type>**</type>\n"
                + "\t<onto1>\n"
                + "\t\t<Ontology rdf:about=\"http://big.csr.unibo.it/sabine-eng.owl\" >\n"
//                + "\t\t\t<location>null</location>\n"
//                + "\t\t</Ontology>\n"
                + "\t</onto1>\n"
                + "\t<onto1>\n"
                + "\t\t<Ontology rdf:about=\"http://big.csr.unibo.it/sabine-ita.owl\">\n"
//                + "\t\t\t<location>null</location>\n"
//                + "\t\t</Ontology>\n"
                + "\t</onto1>\n";

        printHead(outStream,head);

        while (mapIterator.hasNext()) {

            Element mapElement = mapIterator.next();

            Element cellElement = mapElement.element("Cell");

            Element entity1Element = cellElement.element("entity1");
            Element entity2Element = cellElement.element("entity2");

            String relation = cellElement.element("relation").getText();
            String measure = cellElement.element("measure").getText();

            outStream.print(toRDF(entity1Element.attribute(0).getValue(),entity2Element.attribute(0).getValue(),relation,measure));
        }

        printTail(outStream);

        outStream.close();
    }
}
