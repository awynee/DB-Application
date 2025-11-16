public class PersonnelCertification {
    private int id;
    private int personnelId;
    private int certId;
    private java.sql.Date issueDate;
    private java.sql.Date expiryDate;

    public PersonnelCertification() {}

    public PersonnelCertification(int id, int personnelId, int certId,
                                  java.sql.Date issueDate, java.sql.Date expiryDate) {
        this.id = id;
        this.personnelId = personnelId;
        this.certId = certId;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public int getPersonnelId() {
        return personnelId;
    }

    public int getCertId() {
        return certId;
    }

    public java.sql.Date getIssueDate() {
        return issueDate;
    }

    public java.sql.Date getExpiryDate() {
        return expiryDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public void setIssueDate(java.sql.Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setExpiryDate(java.sql.Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
