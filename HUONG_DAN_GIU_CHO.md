# HƯỚNG DẪN TÍNH NĂNG GIỮ CHỖ 5 PHÚT

## 📌 TỔNG QUAN

Khi khách hàng nhấn **"Treo đơn"**, các ghế đã chọn sẽ được **GIỮ CHỖ trong 5 PHÚT** (màu VÀNG) và không cho người khác chọn.

---

## 🎯 LUỒNG HOẠT ĐỘNG

### 1️⃣ **Chọn ghế và Treo đơn**
```
User chọn ghế trong Gui_BanVe
    ↓
Nhấn "Mua vé" → Gui_NhapThongTinBanVe
    ↓
Nhập thông tin người đặt vé
    ↓
Nhấn "Tiếp tục thanh toán" → Diglog_ThanhToan
    ↓
Nhấn "Treo đơn"
    ↓
✅ Ghế được lưu vào QuanLyGheGiuCho (5 phút)
✅ Đơn được lưu vào QuanLyDonTreo
✅ Quay lại Gui_BanVe → Ghế hiện MÀU VÀNG
```

### 2️⃣ **Xử lý đơn treo**
```
Nhấn "Xử lý đơn tạm" → Dialog_TreoDon
    ↓
Chọn đơn cần xử lý → Nhấn "Xử lí"
    ↓
Mở Diglog_ThanhToan với dữ liệu đơn treo
    ↓
Thanh toán thành công?
    ├─ CÓ: ✅ Xóa ghế giữ chỗ + Xóa đơn treo
    └─ KHÔNG: Ghế vẫn giữ chỗ (nếu chưa hết 5 phút)
```

### 3️⃣ **Hủy đơn treo**
```
Dialog_TreoDon → Chọn đơn → Nhấn "Hủy Đơn"
    ↓
✅ Xóa ghế giữ chỗ
✅ Xóa đơn treo
✅ Ghế trở về trạng thái TRỐNG (màu trắng)
```

### 4️⃣ **Hết hạn 5 phút**
```
Timer tự động chạy sau 5 phút
    ↓
✅ Xóa ghế khỏi danh sách giữ chỗ
✅ Ghế tự động trở về trạng thái TRỐNG (màu trắng)
```

---

## 🎨 MÀU SẮC GHẾ

| Trạng thái | Màu | Mô tả |
|------------|-----|-------|
| **Ghế trống** | ⬜ Trắng | Cho phép chọn |
| **Ghế đang chọn** | 🟦 Xanh dương | Đã chọn nhưng chưa treo đơn |
| **Ghế đang giữ chỗ** | 🟨 **VÀNG** | Đã treo đơn, không cho chọn (5 phút) |
| **Ghế đã bán** | 🟥 Đỏ | Đã thanh toán, không cho chọn |

---

## 📁 CẤU TRÚC FILE MỚI

### 1. **Entity**
- `src/entity/GheGiuCho.java`
  - Lưu thông tin ghế giữ chỗ: `maChoNgoi`, `maDonTreo`, `thoiGianGiuCho`
  - Method: `conTrongThoiGianGiuCho()`, `getSoGiayConLai()`

### 2. **Quản lý**
- `src/gui/banve/QuanLyGheGiuCho.java`
  - Static class quản lý danh sách ghế giữ chỗ
  - Method chính:
    - `themGheGiuCho(maChoNgoi, maDonTreo)` - Thêm ghế vào danh sách
    - `kiemTraGheDangGiuCho(maChoNgoi)` - Kiểm tra ghế có đang giữ không
    - `xoaTatCaGheCuaDonTreo(maDonTreo)` - Xóa tất cả ghế của đơn treo
    - `xoaGheHetHan()` - Tự động xóa ghế hết hạn

### 3. **Sửa đổi GUI**
- `src/gui/banve/Gui_BanVe.java`
  - Thêm kiểm tra ghế giữ chỗ trong `taoGocGhe()` và `taoBtnGheCompact()`
  - Hiển thị ghế màu VÀNG nếu đang giữ chỗ

- `src/gui/banve/Gui_NhapThongTinBanVe.java`
  - Thêm `getPreviousGuiBanVe()` để truy cập `Gui_BanVe`

- `src/gui/banve/Diglog_ThanhToan.java`
  - Khi nhấn "Treo đơn": Lưu tất cả ghế vào `QuanLyGheGiuCho`

- `src/gui/banve/Dialog_TreoDon.java`
  - Khi thanh toán thành công: Xóa ghế giữ chỗ
  - Khi hủy đơn: Xóa ghế giữ chỗ

---

## ⏰ TIMER TỰ ĐỘNG

```java
// Trong QuanLyGheGiuCho.java
timer.schedule(new TimerTask() {
    @Override
    public void run() {
        xoaGheGiuCho(maChoNgoi);
        System.out.println("Đã hết hạn giữ chỗ: " + maChoNgoi);
    }
}, 5 * 60 * 1000); // 5 phút
```

---

## 🧪 KIỂM TRA

### Test Case 1: Giữ chỗ thành công
1. Chọn ghế Toa 1 - Ghế 1, 2, 3
2. Nhấn "Mua vé" → Nhập thông tin → "Treo đơn"
3. ✅ Kiểm tra: Ghế 1, 2, 3 màu VÀNG
4. ✅ Kiểm tra: Không thể chọn lại ghế 1, 2, 3

### Test Case 2: Thanh toán đơn treo
1. Nhấn "Xử lý đơn tạm" → Chọn đơn → "Xử lí"
2. Nhập tiền khách đưa → "Thanh toán"
3. ✅ Kiểm tra: Ghế chuyển từ VÀNG → ĐỎ (đã bán)
4. ✅ Kiểm tra: Đơn treo bị xóa khỏi Dialog_TreoDon

### Test Case 3: Hủy đơn treo
1. Nhấn "Xử lý đơn tạm" → Chọn đơn → "Hủy Đơn"
2. ✅ Kiểm tra: Ghế chuyển từ VÀNG → TRẮNG (trống)
3. ✅ Kiểm tra: Có thể chọn lại ghế

### Test Case 4: Hết hạn 5 phút
1. Treo đơn → Đợi 5 phút
2. ✅ Kiểm tra: Ghế tự động chuyển từ VÀNG → TRẮNG
3. ✅ Kiểm tra: Có thể chọn lại ghế

---

## 🎓 LƯU Ý

1. **Thời gian giữ chỗ**: Mặc định 5 phút, có thể thay đổi trong `GheGiuCho.java`:
   ```java
   private static final int PHUT_GIU_CHO = 5;
   ```

2. **Tooltip**: Khi hover vào ghế màu VÀNG sẽ hiện: "Ghế đang được giữ chỗ (5 phút)"

3. **Performance**: Timer chạy trong daemon thread nên không ảnh hưởng đến UI

4. **Đồng bộ**: Khi reload `Gui_BanVe`, ghế giữ chỗ vẫn được kiểm tra và hiển thị đúng

---

## ✅ HOÀN THÀNH!

Tính năng giữ chỗ 5 phút đã được implement đầy đủ:
- ✅ Lưu ghế khi treo đơn
- ✅ Hiển thị màu VÀNG
- ✅ Không cho chọn ghế đang giữ
- ✅ Tự động hết hạn sau 5 phút
- ✅ Xóa ghế khi thanh toán thành công
- ✅ Xóa ghế khi hủy đơn

**🚂 Chúc bạn code vui vẻ! 🎉**


