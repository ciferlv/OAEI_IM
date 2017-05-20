import instance.matching.unit.Alignment;

import static instance.matching.fileParser.AlignFileParser.parseAlignFile;

/**
 * Created by ciferlv on 17-4-17.
 */
public class alignFileParserTest {

    public static void main(String[] args) {

        Alignment align = new Alignment();
        String filePath = "src/main/resources/dataSet/UOBM_small/refalign.rdf";
        parseAlignFile(filePath,align);

    }

}
