# 🚀 HƯỚNG DẪN LÀM VIỆC NHÓM

## 📦 Cấu trúc Module - Phân chia rõ ràng

```
gui/
├── main/         → Người A (Main app)
├── banve/        → BẠN (Bán vé) ⭐
├── trave/        → BẠN (Trả vé) ⭐
├── doive/        → Người B (Đổi vé)
├── khachhang/    → Người C (Khách hàng)
├── nhanvien/     → Người D (Nhân viên)
├── khuyenmai/    → Người E (Khuyến mãi)
├── thongke/      → Người F (Thống kê)
└── menu/         → SHARED (Không tự ý sửa!)
```

---

## ⭐ MODULE CỦA BẠN

### 1. **gui/banve/** - Bán Vé (4 files)
- `Gui_BanVe.java` (~1555 dòng)
- `Gui_NhapThongTinHanhTrinh.java`
- `Gui_NhapThongTinBanVe.java`
- `Diglog_ThanhToan.java`

### 2. **gui/trave/** - Trả Vé (1 file)
- `Gui_TraVe.java`

---

## 🔥 QUY TẮC VÀNG

### ✅ ĐƯỢC PHÉP:
- Sửa file trong `gui/banve/` và `gui/trave/`
- Commit thường xuyên
- Test trước khi push
- Comment code rõ ràng

### ❌ KHÔNG ĐƯỢC:
- Sửa file của người khác (`gui/doive/`, `gui/khachhang/`, etc.)
- Sửa `gui/menu/*` (shared component)
- Thay đổi `entity/*` hoặc `dao/*` mà không thảo luận
- Push code chưa test

---

## 🔧 WORKFLOW HÀNG NGÀY

### 1️⃣ **Bắt đầu làm việc:**
```bash
# Pull code mới nhất
git pull origin main

# Tạo/chuyển sang branch của bạn
git checkout -b feature/banve-trave

# hoặc nếu đã có branch:
git checkout feature/banve-trave
git pull origin main  # merge code mới
```

### 2️⃣ **Làm việc:**
```bash
# Chỉ sửa file trong gui/banve/ và gui/trave/

# Test thường xuyên:
java -cp "build\classes;lib\*" gui.main.Main
```

### 3️⃣ **Commit:**
```bash
# Xem file đã thay đổi
git status

# Add chỉ file của bạn
git add src/gui/banve/*
git add src/gui/trave/*

# Commit với message rõ ràng
git commit -m "[BANVE] Fix bug chọn ghế"
git commit -m "[TRAVE] Implement hoàn tiền"
```

### 4️⃣ **Push:**
```bash
# Push lên branch của bạn
git push origin feature/banve-trave

# Tạo Pull Request để team review
```

---

## 🐛 KHI GẶP CONFLICT

### Scenario: Người khác đã sửa file shared (dao, entity)

```bash
# 1. Pull code mới
git pull origin main

# 2. Nếu có conflict, resolve thủ công
# Mở file conflict, tìm dòng:
<<<<<<< HEAD
Your code
=======
Their code
>>>>>>> main

# 3. Chọn version đúng hoặc merge cả 2

# 4. Test lại toàn bộ

# 5. Commit
git add .
git commit -m "[MERGE] Resolved conflicts"
```

---

## 📞 COMMUNICATION

### Khi cần thay đổi Shared Code:

**❌ SAI:**
```java
// Sửa trực tiếp dao/Ve_DAO.java mà không báo
public List<Ve> layTatCaVe() { ... }
```

**✅ ĐÚNG:**
```
1. Tạo issue: "Cần thêm method layVeTheoKhachHang() trong Ve_DAO"
2. Tag người phụ trách DAO
3. Đợi review và approve
4. Sau đó mới code
```

---

## 🎯 CHECKLIST TRƯỚC KHI COMMIT

- [ ] Code compile thành công
- [ ] App chạy không lỗi
- [ ] Test các chức năng trong module của bạn
- [ ] Xóa console.log/System.out.println debug
- [ ] Comment code phức tạp
- [ ] Commit message rõ ràng

---

## 📁 FILES CỦA BẠN

### Chỉ sửa những file này:
```
✅ src/gui/banve/Gui_BanVe.java
✅ src/gui/banve/Gui_NhapThongTinHanhTrinh.java
✅ src/gui/banve/Gui_NhapThongTinBanVe.java
✅ src/gui/banve/Diglog_ThanhToan.java
✅ src/gui/banve/*.form
✅ src/gui/trave/Gui_TraVe.java
✅ src/gui/trave/*.form
```

### Cần xin phép:
```
⚠️ src/dao/*.java
⚠️ src/entity/*.java
⚠️ src/gui/menu/**/*
```

---

## 💡 TIPS

### 1. Comment code rõ ràng:
```java
// ✅ TỐT
// Lấy danh sách ghế đã đặt của lịch trình này
// để disable button cho những ghế đã bán
Set<String> gheDaDat = veDAO.layDanhSachGheDaDat(maLichTrinh);

// ❌ TỆ
// get data
Set<String> data = dao.get(id);
```

### 2. Test thoroughly:
- Test happy path (flow bình thường)
- Test edge cases (số lượng = 0, null, etc.)
- Test với database trống
- Test với nhiều data

### 3. Keep it simple:
- Đừng over-engineer
- Code phải readable > clever
- Refactor sau, working code first

---

## 🔥 EMERGENCY

### App không chạy sau khi pull code:

```bash
# 1. Clean build
cd D:\Project_TrainTicketManagementSystem
rmdir /S /Q build\classes
mkdir build\classes

# 2. Copy resources
xcopy /E /I /Y src\icon build\classes\icon
xcopy /E /I /Y src\gui\menu\icon build\classes\gui\menu\icon
copy /Y src\gui\menu\swing\icon\MaterialIcons-Regular.ttf build\classes\gui\menu\swing\icon\

# 3. Rebuild all
javac -encoding UTF-8 -d build\classes -cp "lib\*;." src\entity\*.java
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\connectDB\*.java
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\dao\*.java

# 4. Compile GUI
cd src\gui\menu
Get-ChildItem -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName | Out-File -Encoding UTF8 ..\..\..\menu_sources.txt
cd ..\..\..
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." "@menu_sources.txt"

javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\gui\main\*.java
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\gui\banve\*.java
javac -encoding UTF-8 -d build\classes -cp "lib\*;build\classes;." src\gui\trave\*.java

# 5. Run
java -cp "build\classes;lib\*" gui.main.Main
```

---

## 📞 CONTACTS

**Module Owner:**
- Bán Vé & Trả Vé: **[Your Name]**
- Main: [Teammate A]
- Đổi Vé: [Teammate B]
- Khách Hàng: [Teammate C]
- ...

**Team Lead:** [Lead Name]

**Meeting:** [Time & Place]

---

**🎯 Mục tiêu:** Code clean, không conflict, ship nhanh! 🚀




