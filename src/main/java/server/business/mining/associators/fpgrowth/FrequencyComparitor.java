package server.business.mining.associators.fpgrowth;

import java.util.Comparator;

/**
 *
 * @author Kamran
 */

class FrequencyComparitorinHeaderTable implements Comparator<FPtree> {

    public FrequencyComparitorinHeaderTable() {
    }

    public int compare(FPtree o1, FPtree o2) {
        if(o1.count>o2.count){
            return 1;
        }
        else if(o1.count < o2.count)
            return -1;
        else
            return 0;
    }

}
