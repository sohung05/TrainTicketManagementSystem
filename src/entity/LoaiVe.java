package entity;

public class LoaiVe {
	private String maLoaiVe;
	private String tenLoaiVe;
	private float mucGiamGia;
	
	public LoaiVe(String maLoaiVe, String tenLoaiVe, float mucGiamGia) {
		super();
		this.maLoaiVe = maLoaiVe;
		this.tenLoaiVe = tenLoaiVe;
		this.mucGiamGia = mucGiamGia;
	}

	public LoaiVe() {
		this("","", 0);
	}

	public String getMaLoaiVe() {
		return maLoaiVe;
	}

	public void setMaLoaiVe(String maLoaiVe) {
		this.maLoaiVe = maLoaiVe;
	}

	public String getTenLoaiVe() {
		return tenLoaiVe;
	}

	public void setTenLoaiVe(String tenLoaiVe) {
		this.tenLoaiVe = tenLoaiVe;
	}

	public float getMucGiamGia() {
		return mucGiamGia;
	}

	public void setMucGiamGia(float mucGiamGia) {
		this.mucGiamGia = mucGiamGia;
	}
	
	
}
