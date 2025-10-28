@echo off
chcp 65001 >nul
echo ========================================
echo   REFACTOR PROJECT STRUCTURE
echo ========================================
echo.

cd /d "%~dp0src\gui"

REM === Tạo các thư mục mới ===
echo [1/4] Tạo cấu trúc thư mục mới...
mkdir main 2>nul
mkdir doive 2>nul
mkdir trave 2>nul
mkdir khachhang 2>nul
mkdir nhanvien 2>nul
mkdir khuyenmai 2>nul
mkdir thongke 2>nul
mkdir component 2>nul

echo [2/4] Di chuyển file GUI...

REM === Di chuyển file ===
REM Main
copy /Y Gui_Dasboard.java main\Gui_Dashboard.java
copy /Y Gui_Login.java main\

REM Bán vé (đã có trong banve/)
copy /Y Gui_BanVe.java banve\
copy /Y Gui_NhapThongTinHanhTrinh.java banve\
copy /Y Gui_NhapThongTinBanVe.java banve\

REM Đổi vé
copy /Y Gui_DoiVe.java doive\

REM Trả vé
copy /Y Gui_TraVe.java trave\

REM Khách hàng
copy /Y Gui_KhachHang.java khachhang\

REM Nhân viên
copy /Y Gui_NhanVien.java nhanvien\
copy /Y Gui_TaiKhoan.java nhanvien\

REM Khuyến mãi
copy /Y Gui_KhuyenMaiHoaDon.java khuyenmai\
copy /Y Gui_KhuyenMaiDoiTuong.java khuyenmai\

REM Thống kê
copy /Y Gui_ThongKeDoanhThu.java thongke\
copy /Y Gui_ThongKeLuotVe.java thongke\

echo [3/4] Di chuyển menu/ sang component/...
xcopy /E /I /Y menu component\menu

echo.
echo ========================================
echo ✅ ĐÃ COPY XONG!
echo ========================================
echo.
echo BƯỚC TIẾP THEO (QUAN TRỌNG):
echo 1. Mở IntelliJ IDEA
echo 2. Right-click từng file trong thư mục mới
echo 3. Chọn "Refactor" ^> "Move File"
echo 4. IDE sẽ tự động update TOÀN BỘ import paths!
echo.
echo HOẶC:
echo 1. Chạy script: update_packages.bat
echo 2. Rebuild project
echo.
pause





