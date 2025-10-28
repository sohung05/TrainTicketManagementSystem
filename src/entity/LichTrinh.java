package entity;

import java.time.LocalDateTime;

public class LichTrinh {
    private String maLichTrinh;
    private ChuyenTau chuyenTau;
    private Tuyen tuyen;
    private Ga gaDi;
    private Ga gaDen;
    private LocalDateTime gioKhoiHanh;
    private LocalDateTime gioDen;
    private boolean trangThai;

    public LichTrinh(String maLichTrinh, ChuyenTau chuyenTau, Ga gaDi, Ga gaDen, LocalDateTime gioKhoiHanh,
			LocalDateTime gioDen, boolean trangThai) {
		super();
		this.maLichTrinh = maLichTrinh;
		this.chuyenTau = chuyenTau;
		this.gaDi = gaDi;
		this.gaDen = gaDen;
		this.gioKhoiHanh = gioKhoiHanh;
		this.gioDen = gioDen;
		this.trangThai = trangThai;
	}

	public LichTrinh() {
        this("",null, null, null, LocalDateTime.now(), LocalDateTime.now(), true);
    }

    public LichTrinh(String ma, Ga gaDi, Ga gaDen, LocalDateTime gioKhoiHanh, LocalDateTime gioDen) {
    }

    public String getMaLichTrinh() {
		return maLichTrinh;
	}

	public void setMaLichTrinh(String maLichTrinh) {
		this.maLichTrinh = maLichTrinh;
	}

	public ChuyenTau getChuyenTau() {
		return chuyenTau;
	}

	public void setChuyenTau(ChuyenTau chuyenTau) {
		this.chuyenTau = chuyenTau;
	}

	public Tuyen getTuyen() {
		return tuyen;
	}

	public void setTuyen(Tuyen tuyen) {
		this.tuyen = tuyen;
	}

	public Ga getGaDi() {
		return gaDi;
	}

	public void setGaDi(Ga gaDi) {
		this.gaDi = gaDi;
	}

	public Ga getGaDen() {
		return gaDen;
	}

	public void setGaDen(Ga gaDen) {
		this.gaDen = gaDen;
	}

	public LocalDateTime getGioKhoiHanh() {
		return gioKhoiHanh;
	}

	public void setGioKhoiHanh(LocalDateTime gioKhoiHanh) {
		this.gioKhoiHanh = gioKhoiHanh;
	}

	public LocalDateTime getGioDen() {
		return gioDen;
	}

	public void setGioDen(LocalDateTime gioDen) {
		this.gioDen = gioDen;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

    
}

