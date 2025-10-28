package entity;

public enum DoiTuong {
    SinhVien,
    TreEm,
    NguoiLon,
    NguoiCaoTuoi;

    // Hàm tiện ích để map từ chuỗi DB sang enum
    public static DoiTuong fromString(String str) {
        switch (str.trim()) {
            case "Sinh viên":
            case "SinhVien":
                return SinhVien;
            case "Trẻ em":
            case "TreEm":
                return TreEm;
            case "Người lớn":
            case "NguoiLon":
                return NguoiLon;
            case "Người già":
            case "Người cao tuổi":
            case "NguoiCaoTuoi":
                return NguoiCaoTuoi;
            default:
                throw new IllegalArgumentException("Không map được đối tượng: " + str);
        }
    }
}