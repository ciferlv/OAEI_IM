package instance.matching.version2.utility;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static instance.matching.version2.MatchingEntry.init;
import static instance.matching.version2.MatchingEntry.matching;
import static instance.matching.version2.utility.ParamDef.PROP_PAIR_NUM_NEED_THRESHOLD;
import static instance.matching.version2.utility.ParamDef.confFileIndex;

/**
 * Created by ciferlv on 17-5-8.
 */

public class AdjustParam {

    public static void main(String[] args) throws IOException, OWLOntologyCreationException {

        for (int i = 1; i <= 5; i++) {

            confFileIndex = 4;
            PROP_PAIR_NUM_NEED_THRESHOLD = i;
            init();
            matching();
        }
    }

}
