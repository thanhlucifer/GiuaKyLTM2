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
import java.sql.*;
import java.util.*;

public class MatHangDAO {
   public static List<MatHang> getAllMatHang() {
    List<MatHang> danhSachMatHang = new ArrayList<>();
    Connection conn = DBConnection.getConnection();

    try {
        String query = "SELECT MAMH, TENMH, GIABAN, DVT, MOTA, LOAI_MAT_HANG.TENLOAI, MAT_HANG.VOHIEUHOA " +
                       "FROM MAT_HANG " +
                       "JOIN LOAI_MAT_HANG ON MAT_HANG.MALOAI = LOAI_MAT_HANG.MALOAI";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String maMH = rs.getString("MAMH");
            String tenMH = rs.getString("TENMH");
            double giaBan = rs.getDouble("GIABAN");
            String donViTinh = rs.getString("DVT");
            String moTa = rs.getString("MOTA");
            String tenLoai = rs.getString("TENLOAI");
            boolean voHieuHoa = rs.getBoolean("VOHIEUHOA");

            LoaiMatHang loaiMatHang = new LoaiMatHang(null, tenLoai);
            MatHang matHang = new MatHang(maMH, tenMH, giaBan, donViTinh, moTa, loaiMatHang, voHieuHoa);
            danhSachMatHang.add(matHang);
        }
    } catch (SQLException e) {
        System.out.println("Lỗi truy vấn: " + e.getMessage());
    }

    return danhSachMatHang;
}

    public static List<String> getAllLoaiMatHang() {
    List<String> danhSachLoaiMatHang = new ArrayList<>();
    Connection conn = DBConnection.getConnection();

    try {
        String query = "SELECT TENLOAI FROM LOAI_MAT_HANG";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String tenLoai = rs.getString("TENLOAI");
            danhSachLoaiMatHang.add(tenLoai);
        }
    } catch (SQLException e) {
        System.out.println("Lỗi truy vấn: " + e.getMessage());
    }

    return danhSachLoaiMatHang;
}
    public static void addMatHang(String nhomMatHang, String maMatHang, String tenMatHang, double giaBan, String donViTinh, String moTa, boolean voHieuHoa) {
    Connection conn = DBConnection.getConnection();

    try {
        String query = "INSERT INTO MAT_HANG (MALOAI, MAMH, TENMH, GIABAN, DVT, MOTA, VOHIEUHOA) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(query);
        
        // Lấy mã loại mặt hàng tương ứng với tên nhóm mặt hàng
        String maLoai = getMaLoaiByTenLoai(nhomMatHang);

        // Đặt các tham số vào câu lệnh SQL
        statement.setString(1, maLoai);
        statement.setString(2, maMatHang);
        statement.setString(3, tenMatHang);
        statement.setDouble(4, giaBan);
        statement.setString(5, donViTinh);
        statement.setString(6, moTa);
        statement.setBoolean(7, voHieuHoa);

        // Thực thi câu lệnh
        statement.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Lỗi truy vấn: " + e.getMessage());
    }
}


private static String getMaLoaiByTenLoai(String tenLoai) {
    Connection conn = DBConnection.getConnection();
    String maLoai = null;

    try {
        String query = "SELECT MALOAI FROM LOAI_MAT_HANG WHERE TENLOAI = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, tenLoai);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            maLoai = rs.getString("MALOAI");
        }
    } catch (SQLException e) {
        System.out.println("Lỗi truy vấn: " + e.getMessage());
    }

    return maLoai;
}

    
}
