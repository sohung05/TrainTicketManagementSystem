package entity;

public class Toa {
    private String maToa;
    private ChuyenTau chuyenTau;
    private int soToa;
    private LoaiToa loaiToa;

    public Toa(String maToa, ChuyenTau chuyenTau, int soToa, LoaiToa loaiToa) {
        this.maToa = maToa;
        this.chuyenTau = chuyenTau;
        this.soToa = soToa;
        this.loaiToa = loaiToa;
    }

    public Toa() {
        this("", null, 0, null);
    }

	public String getMaToa() {
		return maToa;
	}

	public void setMaToa(String maToa) {
		this.maToa = maToa;
	}

	public ChuyenTau getChuyenTau() {
		return chuyenTau;
	}

	public void setChuyenTau(ChuyenTau chuyenTau) {
		this.chuyenTau = chuyenTau;
	}

	public int getSoToa() {
		return soToa;
	}

	public void setSoToa(int soToa) {
		this.soToa = soToa;
	}

	public LoaiToa getLoaiToa() {
		return loaiToa;
	}

	public void setLoaiToa(LoaiToa loaiToa) {
		this.loaiToa = loaiToa;
	}

   
}
