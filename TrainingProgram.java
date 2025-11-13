public class TrainingProgram {
    private int trainingId;
    private String title;
    private String provider;
    private java.sql.Date trainingDate;
    private int duration;      // in hours
    private double cost;

    public TrainingProgram() {}

    public TrainingProgram(int trainingId, String title, String provider,
                           java.sql.Date trainingDate, int duration, double cost) {
        this.trainingId = trainingId;
        this.title = title;
        this.provider = provider;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.cost = cost;
    }

    // Getters
    public int getTrainingId() {
        return trainingId;
    }

    public String getTitle() {
        return title;
    }

    public String getProvider() {
        return provider;
    }

    public java.sql.Date getTrainingDate() {
        return trainingDate;
    }

    public int getDuration() {
        return duration;
    }

    public double getCost() {
        return cost;
    }

    // Setters
    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setTrainingDate(java.sql.Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
