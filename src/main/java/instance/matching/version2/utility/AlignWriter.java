package instance.matching.version2.utility;

import instance.matching.version2.unit.Alignment;
import instance.matching.version2.unit.CounterPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 * Created by ciferlv on 17-3-30.
 */
public class AlignWriter {

    private static Logger logger = LoggerFactory.getLogger(AlignWriter.class);

    public static void printAlign(String resultFilePath, String head, String tail, Alignment align) {

        PrintWriter outStream = null;

        try {
            outStream = new PrintWriter(new FileOutputStream(resultFilePath));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }

        outStream.print(head);

        for (CounterPart cp : align.getCounterPartList()) {

            String out = "\t<map>\n" +
                    "\t\t<Cell>\n" +
                    "\t\t\t<entity1 rdf:resource=\'" + cp.getSubject1() + "\'/>\n" +
                    "\t\t\t<entity2 rdf:resource=\'" + cp.getSubject2() + "\'/>\n" +
                    "\t\t\t<relation>=</relation>\n" +
                    "\t\t\t<measure rdf:datatype=\'http://www.w3.org/2001/XMLSchema#float\'>1.0</measure>\n" +
                    "\t\t</Cell>\n" +
                    "\t</map>\n";
            outStream.print(out);
        }
        outStream.print(tail);
    }

}
