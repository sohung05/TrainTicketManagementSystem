package entity;

public class LoaiTau {
    private String maLoaiTau;
    private String tenLoaiTau;

    public LoaiTau(String maLoaiTau, String tenLoaiTau) {
        this.maLoaiTau = maLoaiTau;
        this.tenLoaiTau = tenLoaiTau;
    }

    public LoaiTau() {
        this("", "");
    }

	public String getMaLoaiTau() {
		return maLoaiTau;
	}

	public void setMaLoaiTau(String maLoaiTau) {
		this.maLoaiTau = maLoaiTau;
	}

	public String getTenLoaiTau() {
		return tenLoaiTau;
	}

	public void setTenLoaiTau(String tenLoaiTau) {
		this.tenLoaiTau = tenLoaiTau;
	}

    
}

