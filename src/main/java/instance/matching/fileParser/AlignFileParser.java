package instance.matching.fileParser;

import instance.matching.unit.Alignment;
import instance.matching.unit.CounterPart;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Created by xinzelv on 17-3-27.
 */
public class AlignFileParser {

    private static Logger logger = LoggerFactory.getLogger(AlignFileParser.class);

    public static void parseAlignFile(String filePath, Alignment aligns) {

        SAXReader reader = new SAXReader();
        Document document = null;

        try {

            document = reader.read(filePath);
        } catch (DocumentException e) {

            logger.info("Can't find alignment file!");
            logger.error(e.getMessage());
        }

        Element root = document.getRootElement();

        Iterator<Element> alignmentIterator = root.elements("Alignment").iterator();
        Element alignmentElement = alignmentIterator.next();

        Iterator<Element> mapIterator = alignmentElement.elements("map").iterator();

        while (mapIterator.hasNext()) {

            Element mapElement = mapIterator.next();

            Element cellElement = mapElement.element("Cell");

            Element entity1Element = cellElement.element("entity1");
            Element entity2Element = cellElement.element("entity2");

            String uri1 = entity1Element.attribute(0).getValue().toLowerCase();
            String uri2 = entity2Element.attribute(0).getValue().toLowerCase();

            aligns.addCounterPart(new CounterPart(uri1, uri2));
        }
    }
}
