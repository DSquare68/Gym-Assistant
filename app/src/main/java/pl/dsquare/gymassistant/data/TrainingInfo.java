package pl.dsquare.gymassistant.data;

import java.sql.Date;

public class TrainingInfo {
    private int _ID;
    private String trainingName;
    private Date trainingDate;

    public TrainingInfo(int _ID, String trainingName, Date trainingDate) {
        this._ID = _ID;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }
    public void setTrainingDate(int trainingDate) {
        this.trainingDate.setTime(trainingDate);
    }
}
