package instance.matching.pr.utility;

import instance.matching.pr.unit.Alignment;
import instance.matching.pr.unit.CounterPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 * Created by ciferlv on 17-3-30.
 */
public class PrintAlignment {

    private Logger logger = LoggerFactory.getLogger(PrintAlignment.class);

    private String head, tail;
    private String resultFilePath;
    private PrintWriter outStream = null;
    private Alignment alignment = null;

    public PrintAlignment(String resultFilePath, Alignment align) {

        this.resultFilePath = resultFilePath;
        this.alignment = align;
        try {
            outStream = new PrintWriter(new FileOutputStream(resultFilePath));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    public void print() {

        outStream.print(head);

        for (CounterPart cp : alignment.getCounterPartList()) {

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


    public void setHead(String head) {
        this.head = head;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
}
