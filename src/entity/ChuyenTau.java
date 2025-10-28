package entity;

import java.time.LocalDateTime;

public class ChuyenTau {
    private String soHieuTau;
    private double tocDo;
    private LoaiTau loaiTau;
    private LocalDateTime namSanXuat;

    public ChuyenTau(String soHieuTau, double tocDo, LoaiTau loaiTau, LocalDateTime namSanXuat) {
		super();
		this.soHieuTau = soHieuTau;
		this.tocDo = tocDo;
		this.loaiTau = loaiTau;
		this.namSanXuat = namSanXuat;
	}


	public ChuyenTau() {
        this("", 0, null, LocalDateTime.now());
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


	public LocalDateTime getNamSanXuat() {
		return namSanXuat;
	}


	public void setNamSanXuat(LocalDateTime namSanXuat) {
		this.namSanXuat = namSanXuat;
	}

}

