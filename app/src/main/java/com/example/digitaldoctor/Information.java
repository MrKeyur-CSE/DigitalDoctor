package com.example.digitaldoctor;

public class Information {

    private String ill;
    private String p1;
    private String p2;
    private String p3;
    private String p4;
    private Long prescriptionNo;

    public Information(){
    }

    public Information(String ill, String p1, String p2, String p3, String p4, Long prescriptionNo) {
        this.ill = ill;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.prescriptionNo = prescriptionNo;
    }

    public String getIll() {
        return ill;
    }

    public void setIll(String ill) {
        this.ill = ill;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public Long getPrescriptionNo() {
        return prescriptionNo;
    }

    public void setPrescriptionNo(Long prescriptionNo) {
        this.prescriptionNo = prescriptionNo;
    }

}
