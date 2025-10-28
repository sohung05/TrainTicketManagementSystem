package entity;

public class Ga {
    private String maGa;
    private String tenGa;
    private String viTri;

    public Ga(String maGa, String tenGa, String viTri) {
		super();
		this.maGa = maGa;
		this.tenGa = tenGa;
		this.viTri = viTri;
	}

	public Ga() {
        this("","","");
    }

	public String getMaGa() {
		return maGa;
	}

	public void setMaGa(String maGa) {
		this.maGa = maGa;
	}

	public String getTenGa() {
		return tenGa;
	}

	public void setTenGa(String tenGa) {
		this.tenGa = tenGa;
	}

	public String getViTri() {
		return viTri;
	}

	public void setViTri(String viTri) {
		this.viTri = viTri;
	}
}

