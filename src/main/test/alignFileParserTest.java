import instance.matching.version2.unit.Alignment;

import static instance.matching.version2.fileParser.AlignFileParser.parseAlignFile;

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
