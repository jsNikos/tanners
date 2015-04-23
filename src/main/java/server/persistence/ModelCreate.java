package server.persistence;

import java.util.Date;

/**
 * Represents a model-instance created based on mining-data.
 *
 */
public class ModelCreate extends TaskEntity {	
	private Long numberMiningData; // number of records the model is based on
	private Long dataFrom; // the restriction of records based on created-prop
	private Long dataTo;

	public ModelCreate(Date created) {
		super(created);
	}
	
	public Long getNumberMiningData() {
		return numberMiningData;
	}

	public void setNumberMiningData(Long numberMiningData) {
		this.numberMiningData = numberMiningData;
	}

	public Long getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(Long dataFrom) {
		this.dataFrom = dataFrom;
	}

	public Long getDataTo() {
		return dataTo;
	}

	public void setDataTo(Long dataTo) {
		this.dataTo = dataTo;
	}

}
