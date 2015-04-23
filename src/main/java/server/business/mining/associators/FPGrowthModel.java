package server.business.mining.associators;

import server.business.mining.associators.fpgrowth.FPGrowth;

import java.util.ArrayList;
import java.util.List;

public class FPGrowthModel extends Associator {

    @Override
    public void buildModel(List<ArrayList<String>> miningRecords, int threshold) {
        FPGrowth fpGrowth = new FPGrowth(miningRecords, threshold);
        fpGrowth.getFrequentPattern();
    }
}