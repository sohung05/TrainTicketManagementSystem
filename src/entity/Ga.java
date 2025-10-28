package entity;

public class Ga {
    private String maGa;
    private String tenGa;
    private String viTri;
    private String gaDen;
    private String gaDi;

    

    public Ga(String maGa, String tenGa, String viTri, String gaDen, String gaDi) {
		super();
		this.maGa = maGa;
		this.tenGa = tenGa;
		this.viTri = viTri;
		this.gaDen = gaDen;
		this.gaDi = gaDi;
	}

	public Ga() {
        this("","","","","");
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

	public String getGaDen() {
		return gaDen;
	}

	public void setGaDen(String gaDen) {
		this.gaDen = gaDen;
	}

	public String getGaDi() {
		return gaDi;
	}

	public void setGaDi(String gaDi) {
		this.gaDi = gaDi;
	}

   
}

