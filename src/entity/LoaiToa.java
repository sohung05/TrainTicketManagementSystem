package entity;

public class LoaiToa {
    private String maLoaiToa;
    private String tenLoaiToa;
    
    public LoaiToa(String maLoaiToa, String tenLoaiToa) {
		super();
		this.maLoaiToa = maLoaiToa;
		this.tenLoaiToa = tenLoaiToa;
	}

	public LoaiToa() {
        this("", "");
    }

	public String getMaLoaiToa() {
		return maLoaiToa;
	}

	public void setMaLoaiToa(String maLoaiToa) {
		this.maLoaiToa = maLoaiToa;
	}

	public String getTenLoaiToa() {
		return tenLoaiToa;
	}

	public void setTenLoaiToa(String tenLoaiToa) {
		this.tenLoaiToa = tenLoaiToa;
	}

    
}

