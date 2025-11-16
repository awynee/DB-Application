public class TrainingRecord {
    private int recordId;
    private int personnelId;
    private int trainingId;
    private String status;            // Enrolled, In-Progress, Completed, Failed
    private java.sql.Date completionDate;

    public TrainingRecord() {}

    public TrainingRecord(int recordId, int personnelId, int trainingId,
                          String status, java.sql.Date completionDate) {
        this.recordId = recordId;
        this.personnelId = personnelId;
        this.trainingId = trainingId;
        this.status = status;
        this.completionDate = completionDate;
    }

    public int getRecordId() {
        return recordId;
    }

    public int getPersonnelId() {
        return personnelId;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public String getStatus() {
        return status;
    }

    public java.sql.Date getCompletionDate() {
        return completionDate;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCompletionDate(java.sql.Date completionDate) {
        this.completionDate = completionDate;
    }
}
