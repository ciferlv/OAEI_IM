package instance.matching.pr.fileParser;

import instance.matching.pr.unit.Alignment;
import instance.matching.pr.unit.CounterPart;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xinzelv on 17-3-27.
 */
public class AlignFileParser {

    private String filePath;

    Alignment aligns = new Alignment();

    public AlignFileParser(String filePath) throws DocumentException {

        this.filePath = filePath;
        parseAlignFile();
    }

    public void parseAlignFile() throws DocumentException {

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

            String uri1 = entity1Element.attribute(0).getValue();
            String uri2 = entity2Element.attribute(0).getValue();


            aligns.addCounterPart(new CounterPart(uri1,uri2));

        }
    }

    public Alignment getAligns() {
        return aligns;
    }
}
