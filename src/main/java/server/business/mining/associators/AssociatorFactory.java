package server.business.mining.associators;

public class AssociatorFactory {

	/**
	 * Creates an algorithm-instance based on the given type.
	 * @param type
	 * @return
	 */
    public Associator create(AssociatorType type) {
        switch (type) {
            case FPGROWTH:
                return new FPGrowthModel();
            default:
                break;
        }
        return null;
    }
}
