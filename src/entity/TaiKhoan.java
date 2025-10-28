/*
 * @ (#) TaiKhoan.java          1.0        10/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package entity;

/**
 * @description: Entity Tài Khoản
 * @author: Truong Tran Hung
 * @date: 10/26/2025
 * @version: 1.0
 */
public class TaiKhoan {
    private String userName;
    private String passWord;
    private NhanVien nhanVien;

    public TaiKhoan() {}

    public TaiKhoan(String userName, String passWord, NhanVien nhanVien) {
        this.userName = userName;
        this.passWord = passWord;
        this.nhanVien = nhanVien;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "userName='" + userName + '\'' +
                ", nhanVien=" + nhanVien +
                '}';
    }
}



