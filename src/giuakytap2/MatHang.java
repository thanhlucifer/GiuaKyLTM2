/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuakytap2;

/**
 *
 * @author thanh
 */
public class MatHang {
    private String maMH;
    private String tenMH;
    private double giaBan;
    private String donViTinh;
    private String moTa;
    private LoaiMatHang loaiMatHang;
    private boolean voHieuHoa; // Thêm trường voHieuHoa

    public MatHang(String maMH, String tenMH, double giaBan, String donViTinh, String moTa, LoaiMatHang loaiMatHang, boolean voHieuHoa) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.giaBan = giaBan;
        this.donViTinh = donViTinh;
        this.moTa = moTa;
        this.loaiMatHang = loaiMatHang;
        this.voHieuHoa = voHieuHoa;
    }


    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LoaiMatHang getLoaiMatHang() {
        return loaiMatHang;
    }

    public void setLoaiMatHang(LoaiMatHang loaiMatHang) {
        this.loaiMatHang = loaiMatHang;
    }

    public boolean isVoHieuHoa() {
        return voHieuHoa;
    }

    public void setVoHieuHoa(boolean voHieuHoa) {
        this.voHieuHoa = voHieuHoa;
    }
}

