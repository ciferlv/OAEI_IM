package instance.matching.version1.helper;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static instance.matching.version1.helper.LinkSubjectStringHelper.linkSubjectString;

/**
 * Created by ciferlv on 17-3-6.
 */
public class ParseStandardFileHelper {

    Logger logger = LoggerFactory.getLogger(ParseStandardFileHelper.class);

    private String filePath;

    public ParseStandardFileHelper(String filePath) {

        this.filePath = filePath;
    }

    public Map<String, String> parseXML() throws DocumentException {

        Map<String, String> standardLink = new HashMap<String, String>();

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePath);
        Element root = document.getRootElement();

        Iterator<Element> alignmentIterator = root.elements("Alignment").iterator();
        Element alignmentElement = alignmentIterator.next();

        Iterator<Element> mapIterator = alignmentElement.elements("map").iterator();

        while (mapIterator.hasNext()) {

            Element mapElement = mapIterator.next();

            Element cellElement = mapElement.element("Cell");

            Element entity1Element = cellElement.element("entity1");
            Element entity2Element = cellElement.element("entity2");


            standardLink.put(
                    linkSubjectString(
                            entity1Element.attribute(0).getValue(),
                            entity2Element.attribute(0).getValue()
                    ), "0");
        }

        return standardLink;
    }

}
