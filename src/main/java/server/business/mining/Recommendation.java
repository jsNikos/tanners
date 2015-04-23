package server.business.mining;

import java.util.Collection;

/**
 * Those are the recommendations based on the built model.
 * Recommendation has a premise: i.e item A and B.
 * Recommendation has also a consequense: i.e item C.
 * So the full recommendation is if A + B Then C.
 * The metric value is the primary metric value of the model.
 * (Most common are CONFIDENCE, LIFT-or interest- LEVERAGE)
 *
 */
public class Recommendation {

    private Collection<String> premise;
    private Collection<String> consequense;
    private double confidence;

    public Recommendation(Collection<String> premise,Collection<String> consequense, double confidence) {
        this.premise = premise;
        this.consequense = consequense;
        this.confidence = confidence;
    }

    public Recommendation() {}
    
    public void setPremise(Collection<String> premise) { this.premise = premise; }
    public Collection<String> getPremise() { return premise; }
    
    public void setConsequense(Collection<String> consequense) { this.consequense = consequense; }
    public Collection<String> getConsequense() { return consequense; }
    
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public double getConfidence() { return confidence; }

}
