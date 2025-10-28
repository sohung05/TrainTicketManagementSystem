package entity;

public class ChoNgoi {
    private String maChoNgoi;
    private String tenChoNgoi;
    private Toa toa;
    private String moTa;
    private int viTri;
    private double gia;

    

    public ChoNgoi(String maChoNgoi, String tenChoNgoi, Toa toa, String moTa, int viTri, double gia) {
		super();
		this.maChoNgoi = maChoNgoi;
		this.tenChoNgoi = tenChoNgoi;
		this.toa = toa;
		this.moTa = moTa;
		this.viTri = viTri;
		this.gia = gia;
	}
    
	public ChoNgoi() {
        this("", "", null, "",0 ,0);
    }

	public String getMaChoNgoi() {
		return maChoNgoi;
	}

	public void setMaChoNgoi(String maChoNgoi) {
		this.maChoNgoi = maChoNgoi;
	}

	public String getTenChoNgoi() {
		return tenChoNgoi;
	}

	public void setTenChoNgoi(String tenChoNgoi) {
		this.tenChoNgoi = tenChoNgoi;
	}

	public Toa getToa() {
		return toa;
	}

	public void setToa(Toa toa) {
		this.toa = toa;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public int getViTri() {
		return viTri;
	}

	public void setViTri(int viTri) {
		this.viTri = viTri;
	}

	public double getGia() {
		return gia;
	}

	public void setGia(double gia) {
		this.gia = gia;
	}

    
}

