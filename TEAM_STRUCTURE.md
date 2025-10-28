# 📋 PHÂN CHIA TRÁCH NHIỆM DỰ ÁN

## 🏗️ Cấu trúc Module

```
src/gui/
├── main/           ← Main application (Dashboard, Login)
├── banve/          ← BÁN VÉ ⭐ (YOUR RESPONSIBILITY)
├── trave/          ← TRẢ VÉ ⭐ (YOUR RESPONSIBILITY)
├── doive/          ← Đổi vé
├── khachhang/      ← Quản lý khách hàng
├── nhanvien/       ← Quản lý nhân viên
├── khuyenmai/      ← Khuyến mãi
├── thongke/        ← Thống kê
└── menu/           ← Shared menu system
```

---

## ⭐ MODULE CỦA BẠN

### 1️⃣ **gui/banve/** - Module Bán Vé

**Files cần maintain:**
```
banve/
├── Gui_BanVe.java                    (1555 dòng - Main UI)
├── Gui_BanVe.form                    (NetBeans form)
├── Gui_NhapThongTinHanhTrinh.java    (Form nhập hành trình)
├── Gui_NhapThongTinBanVe.java        (Form nhập thông tin khách)
├── Gui_NhapThongTinBanVe.form
├── Diglog_ThanhToan.java             (Dialog thanh toán)
└── Diglog_ThanhToan.form
```

**Package:** `gui.banve.*`

**Chức năng:**
- ✅ Tìm kiếm chuyến tàu
- ✅ Hiển thị danh sách chuyến
- ✅ Chọn toa tàu
- ✅ Sơ đồ ghế (2 loại: ngồi mềm và giường nằm)
- ✅ Giỏ vé
- ✅ Nhập thông tin khách hàng
- ✅ Thanh toán

**Dependencies:**
- `dao/`: ChoNgoi_DAO, LichTrinh_DAO, Toa_DAO, Ve_DAO, KhachHang_DAO
- `entity/`: ChoNgoi, LichTrinh, Toa, Ve, KhachHang

---

### 2️⃣ **gui/trave/** - Module Trả Vé

**Files cần maintain:**
```
trave/
├── Gui_TraVe.java
└── Gui_TraVe.form
```

**Package:** `gui.trave.*`

**Chức năng:**
- ⚠️ Cần implement logic trả vé
- Tìm kiếm vé đã bán
- Hoàn tiền
- Cập nhật trạng thái vé

**Dependencies:**
- `dao/`: Ve_DAO, HoaDon_DAO
- `entity/`: Ve, HoaDon

---

## 🤝 LÀM VIỆC NHÓM - BEST PRACTICES

### ✅ **Quy tắc làm việc:**

1. **Chỉ sửa file trong module của mình**
   - Bạn: `gui/banve/*` và `gui/trave/*`
   - Người khác: Các module khác

2. **KHÔNG sửa Shared Components**
   - `gui/menu/*` - Chỉ sửa khi có thỏa thuận team
   - `entity/*` - Cần thảo luận trước
   - `dao/*` - Cần review code

3. **Commit messages rõ ràng:**
   ```
   ✅ [BANVE] Fix bug chọn ghế trùng
   ✅ [TRAVE] Implement logic hoàn tiền
   ❌ Fix bug
   ❌ Update code
   ```

4. **Branch strategy:**
   ```bash
   main/master          ← Production code
   ├── feature/banve    ← Your branch for bán vé
   ├── feature/trave    ← Your branch for trả vé
   ├── feature/doive    ← Teammate's branch
   └── feature/khachhang← Teammate's branch
   ```

---

## 📁 FILE STRUCTURE - CHI TIẾT

### Bạn sở hữu:
```
✅ src/gui/banve/*.java
✅ src/gui/banve/*.form
✅ src/gui/trave/*.java
✅ src/gui/trave/*.form
```

### Shared (cần thỏa thuận):
```
⚠️ src/dao/*           - Data access layer
⚠️ src/entity/*        - Database entities
⚠️ src/gui/menu/*      - Menu system
```

### Người khác sở hữu:
```
❌ src/gui/main/*
❌ src/gui/doive/*
❌ src/gui/khachhang/*
❌ src/gui/nhanvien/*
❌ src/gui/khuyenmai/*
❌ src/gui/thongke/*
```

---

## 🔧 COMPILE & TEST

### Compile chỉ module của bạn:
```bash
# Bán vé
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\gui\banve\*.java

# Trả vé
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\gui\trave\*.java
```

### Run app để test:
```bash
java -cp "build\classes;lib\*" gui.main.Main
```

---

## 📝 CODE STYLE

### Naming Convention:
```java
// Class names
Gui_BanVe.java          ← GUI classes
ChoNgoi_DAO.java        ← DAO classes

// Variables
private LichTrinh lichTrinhDangChon;     ← camelCase
private DefaultTableModel modelGioVe;    ← camelCase

// Methods
public void hienThiDanhSachToa() { }     ← camelCase, động từ đầu
public List<Ve> timKiemVe() { }          ← động từ + object
```

---

## 🐛 DEBUG

### Log để track issues:
```java
System.out.println("[BANVE] Selected: " + maLichTrinh);
System.err.println("[BANVE] ERROR: " + e.getMessage());
```

### Test checklist:
- [ ] Tìm kiếm chuyến tàu
- [ ] Chọn ghế (cả 2 loại toa)
- [ ] Thêm/xóa vé trong giỏ
- [ ] Tính tổng tiền đúng
- [ ] Thanh toán thành công
- [ ] Trả vé và hoàn tiền

---

## 📞 COMMUNICATION

### Khi cần thay đổi Shared Code:
1. **Tạo issue/ticket** mô tả thay đổi
2. **Thảo luận với team** trước khi code
3. **Code review** bắt buộc
4. **Test kỹ** trước khi merge

### Khi gặp conflict:
1. **Pull code mới nhất** từ main
2. **Merge vào branch của bạn**
3. **Test lại toàn bộ**
4. **Resolve conflicts** thủ công nếu cần

---

## 🎯 PRIORITIES

### Module Bán Vé (HIGH):
- ✅ Core functionality hoạt động
- ⚠️ Cần optimize performance (load ghế nhanh hơn)
- ⚠️ Cần thêm validation input

### Module Trả Vé (MEDIUM):
- ❌ Chưa có logic đầy đủ
- 🔨 Cần implement:
  - Tìm vé theo mã
  - Kiểm tra điều kiện trả vé
  - Tính phí hủy
  - Cập nhật database

---

## 📚 DOCUMENTATION

### Cần document:
- [ ] API của các method trong Gui_BanVe
- [ ] Flow diagram cho quy trình bán vé
- [ ] Business rules cho trả vé
- [ ] Test cases

---

## ✅ DONE / 🔨 TODO

### Bán Vé:
- ✅ UI hoàn chỉnh
- ✅ Chức năng chọn ghế
- ✅ Giỏ vé
- ✅ Tính tiền
- ⚠️ Cần refactor Gui_BanVe.java (1555 dòng quá dài)
- ⚠️ Cần thêm helper classes

### Trả Vé:
- ✅ UI cơ bản
- 🔨 Cần implement logic hoàn toàn
- 🔨 Cần tích hợp database
- 🔨 Cần test cases

---

**📅 Last Updated:** 2025-10-26  
**👤 Maintainer:** [Your Name] - Bán Vé & Trả Vé modules




