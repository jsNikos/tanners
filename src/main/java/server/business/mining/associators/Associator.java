package server.business.mining.associators;

import server.business.mining.Recommendation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public abstract class Associator {
    protected ArrayList<Recommendation> recommendations;
    protected Map<ArrayList<String>, Integer> frequentPattern;


    public  ArrayList<Recommendation> getRecommendations() { return recommendations; }

    public abstract void buildModel(List<ArrayList<String>> MiningRecords, int threshold);

    public List<Recommendation> getRecommendations(ArrayList<String> premise) {
        premise.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return recommendations.stream().filter(r -> r.getPremise().equals(premise)).collect(toList());
    }

    public double getConfidence(String premise, String consequence) {
//        ArrayList<String> premiseList = new ArrayList<String>();
//        premiseList.add(premise);
//        double premiseSupport = frequentItemSets.get(premiseList);
//        ArrayList<String> bothList = new ArrayList<String>();
//        bothList.add(premise);
//        bothList.add(consequence);
//        bothList.sort(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        double consequenceAndPremiseSupport = frequentItemSets.get(bothList);
//        return consequenceAndPremiseSupport /  premiseSupport;
        return 0.0;
    }

}
