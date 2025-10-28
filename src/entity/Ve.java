package entity;

import java.time.LocalDateTime;

public class Ve {
    private String maVe;
    private LoaiVe loaiVe;
    private LichTrinh lichTrinh;
    private String maVach;
    private LocalDateTime thoiGianLenTau;
    private double giaVe;
    private KhachHang khachHang;
    private Toa toa;
    private boolean trangThai;
    private String tenKhachHang;
    private String soCCCD;
    private ChoNgoi choNgoi;
  

    public Ve(String maVe) {
		super();
		this.maVe = maVe;
		this.loaiVe = loaiVe;
		this.maVach = maVach;
		this.thoiGianLenTau = thoiGianLenTau;
		this.giaVe = giaVe;
		this.khachHang = khachHang;
		this.toa = toa;
		this.trangThai = trangThai;
		this.tenKhachHang = tenKhachHang;
		this.soCCCD = soCCCD;
		this.choNgoi = choNgoi;
	}


	public Ve() {
        this("");
    }


	public String getMaVe() {
		return maVe;
	}


	public void setMaVe(String maVe) {
		this.maVe = maVe;
	}


	public LoaiVe getLoaiVe() {
		return loaiVe;
	}


	public void setLoaiVe(LoaiVe loaiVe) {
		this.loaiVe = loaiVe;
	}


	public LichTrinh getLichTrinh() {
		return lichTrinh;
	}


	public void setLichTrinh(LichTrinh lichTrinh) {
		this.lichTrinh = lichTrinh;
	}


	public String getMaVach() {
		return maVach;
	}


	public void setMaVach(String maVach) {
		this.maVach = maVach;
	}


	public LocalDateTime getThoiGianLenTau() {
		return thoiGianLenTau;
	}


	public void setThoiGianLenTau(LocalDateTime thoiGianLenTau) {
		this.thoiGianLenTau = thoiGianLenTau;
	}


	public double getGiaVe() {
		return giaVe;
	}


	public void setGiaVe(double giaVe) {
		this.giaVe = giaVe;
	}


	public KhachHang getKhachHang() {
		return khachHang;
	}


	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}


	public Toa getToa() {
		return toa;
	}


	public void setToa(Toa toa) {
		this.toa = toa;
	}


	public boolean isTrangThai() {
		return trangThai;
	}


	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}


	public String getTenKhachHang() {
		return tenKhachHang;
	}


	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}


	public String getSoCCCD() {
		return soCCCD;
	}


	public void setSoCCCD(String soCCCD) {
		this.soCCCD = soCCCD;
	}


	public ChoNgoi getChoNgoi() {
		return choNgoi;
	}


	public void setChoNgoi(ChoNgoi choNgoi) {
		this.choNgoi = choNgoi;
	}

    
}

