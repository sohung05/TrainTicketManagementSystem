package entity;

public class Tuyen {
    private String maTuyen;
    private String tenTuyen;
    private double doDai;

    public Tuyen(String maTuyen, String tenTuyen, double doDai) {
        this.maTuyen = maTuyen;
        this.tenTuyen = tenTuyen;
        this.doDai = doDai;
    }

    public Tuyen() {
        this("", "", 0);
    }

	public String getMaTuyen() {
		return maTuyen;
	}

	public void setMaTuyen(String maTuyen) {
		this.maTuyen = maTuyen;
	}

	public String getTenTuyen() {
		return tenTuyen;
	}

	public void setTenTuyen(String tenTuyen) {
		this.tenTuyen = tenTuyen;
	}

	public double getDoDai() {
		return doDai;
	}

	public void setDoDai(double doDai) {
		this.doDai = doDai;
	}

   
}

