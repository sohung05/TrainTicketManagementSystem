package entity;

public class ChuyenTau {
    private String soHieuTau;
    private double tocDo;
    private LoaiTau loaiTau;
    private Integer namSanXuat;

    public ChuyenTau(String soHieuTau, double tocDo, LoaiTau loaiTau, Integer namSanXuat) {
		super();
		this.soHieuTau = soHieuTau;
		this.tocDo = tocDo;
		this.loaiTau = loaiTau;
		this.namSanXuat = namSanXuat;
	}


	public ChuyenTau() {
        this("", 0, null, null);
    }


	public String getSoHieuTau() {
		return soHieuTau;
	}


	public void setSoHieuTau(String soHieuTau) {
		this.soHieuTau = soHieuTau;
	}


	public double getTocDo() {
		return tocDo;
	}


	public void setTocDo(double tocDo) {
		this.tocDo = tocDo;
	}


	public LoaiTau getLoaiTau() {
		return loaiTau;
	}


	public void setLoaiTau(LoaiTau loaiTau) {
		this.loaiTau = loaiTau;
	}


	public Integer getNamSanXuat() {
		return namSanXuat;
	}


	public void setNamSanXuat(Integer namSanXuat) {
		this.namSanXuat = namSanXuat;
	}

}

