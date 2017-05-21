package instance.matching.utility;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;

import static instance.matching.MatchingEntry.init;
import static instance.matching.MatchingEntry.matching;
import static instance.matching.utility.ParamDef.prop_pair_num_need_threshold;
import static instance.matching.utility.ParamDef.confFileIndex;

/**
 * Created by ciferlv on 17-5-8.
 */

public class AdjustAlgorithm {

    public static void adjustAlignPropNum() throws IOException, OWLOntologyCreationException {

        for (int i = 1; i <= 5; i++) {

            confFileIndex = 5;
            prop_pair_num_need_threshold = i;
            init();
            matching();
        }
    }

    public static void testStability() throws IOException, OWLOntologyCreationException {

        for(int i = 0; i < 10; i ++ )
        {
            confFileIndex = 5;
            init();
            matching();
        }
    }

    public static void main(String[] args) throws IOException, OWLOntologyCreationException {

        testStability();
    }
}
