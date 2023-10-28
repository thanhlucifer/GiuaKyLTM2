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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class DanhMucNhomMatHang extends JFrame {
    private JTable table;
    private JTextField maLoaiField;
    private JTextField tenLoaiField;
    private JLabel tongSoMatHangLabel;
    public DanhMucNhomMatHang() {
        setTitle("Danh Mục Nhóm Mặt Hàng");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] columnNames = {"Mã Nhóm Mặt Hàng", "Tên Nhóm Mặt Hàng", "Số Mặt Hàng", "Vô Hiệu Hóa"};

        Object[][] data = fetchCategoryData();

        table = new JTable(data, columnNames);
        table.setEnabled(true);
        
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Thêm");
        JButton deleteButton = new JButton("Xóa");
        JButton editButton = new JButton("Sửa");

        maLoaiField = new JTextField(10);
        tenLoaiField = new JTextField(10);

        controlPanel.add(addButton);
        controlPanel.add(deleteButton);
        controlPanel.add(editButton);
        controlPanel.add(new JLabel("Mã Loại Mặt Hàng"));
        controlPanel.add(maLoaiField);
        controlPanel.add(new JLabel("Loại Mặt Hàng"));
        controlPanel.add(tenLoaiField);

        tongSoMatHangLabel = new JLabel("Tổng số mặt hàng: ");
        controlPanel.add(tongSoMatHangLabel);

        JCheckBox voHieuHoaCheckBox = new JCheckBox("Vô Hiệu Hóa");
        controlPanel.add(voHieuHoaCheckBox);


        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                String maLoai = (String) table.getValueAt(selectedRow, 0);
                String tenLoai = (String) table.getValueAt(selectedRow, 1);
                boolean voHieuHoa = (boolean) table.getValueAt(selectedRow, 3); // Lấy trạng thái VOHIEUHOA

                maLoaiField.setText(maLoai);
                tenLoaiField.setText(tenLoai);
                voHieuHoaCheckBox.setSelected(voHieuHoa); // Cập nhật trạng thái checkbox
            }
        }
    }
});


      
   
 addButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String maLoai = maLoaiField.getText();
        String tenLoai = tenLoaiField.getText();
        boolean voHieuHoa = voHieuHoaCheckBox.isSelected(); // Lấy trạng thái checkbox

        if (maLoai.isEmpty() || tenLoai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO loai_mat_hang (MALOAI, TENLOAI, VOHIEUHOA) VALUES (?, ?, ?)");
                pstmt.setString(1, maLoai);
                pstmt.setString(2, tenLoai);
                pstmt.setBoolean(3, voHieuHoa); // Đặt trạng thái VOHIEUHOA
                pstmt.executeUpdate();

                // Sau khi thêm, cập nhật bảng dữ liệu
                Object[][] newData = fetchCategoryData();
                DefaultTableModel model = new DefaultTableModel(newData, columnNames);
                table.setModel(model);
                int tongSoMatHang = getTongSoMatHang();
                tongSoMatHangLabel.setText("Tổng số mặt hàng: " + tongSoMatHang);
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
});

// Tương tự cho các nút xóa và sửa



deleteButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa.");
            return;
        }

        String maLoai = (String) table.getValueAt(selectedRow, 0);

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM loai_mat_hang WHERE MALOAI = ?");
                pstmt.setString(1, maLoai);
                pstmt.executeUpdate();

                // Sau khi xóa, cập nhật bảng dữ liệu
                Object[][] newData = fetchCategoryData();
                DefaultTableModel model = new DefaultTableModel(newData, columnNames);
                table.setModel(model);

                // Xóa dữ liệu trên JTextField
                maLoaiField.setText("");
                tenLoaiField.setText("");

                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
});

editButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để chỉnh sửa.");
            return;
        }

        String maLoai = maLoaiField.getText();
        String tenLoai = tenLoaiField.getText();
        boolean voHieuHoa = voHieuHoaCheckBox.isSelected(); // Lấy trạng thái checkbox

        if (maLoai.isEmpty() || tenLoai.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("UPDATE loai_mat_hang SET TENLOAI = ?, VOHIEUHOA = ? WHERE MALOAI = ?");
                pstmt.setString(1, tenLoai);
                pstmt.setBoolean(2, voHieuHoa); // Đặt trạng thái VOHIEUHOA
                pstmt.setString(3, maLoai);
                pstmt.executeUpdate();

                // Sau khi chỉnh sửa, cập nhật bảng dữ liệu
                Object[][] newData = fetchCategoryData();
                DefaultTableModel model = new DefaultTableModel(newData, columnNames);
                table.setModel(model);

                pstmt.close();

                // Hiển thị thông báo hoàn thành
                JOptionPane.showMessageDialog(null, "Sửa thành công.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
});


     

        JButton exitButton = new JButton("Thoát");
        controlPanel.add(exitButton);

        exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
             dispose(); // Tắt cửa sổ JFrame
        }
    });





        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
void updateTongSoMatHangLabel() {
    int tongSoMatHang = getTongSoMatHang();
    tongSoMatHangLabel.setText("Tổng số mặt hàng: " + tongSoMatHang);
}


    // Thêm phương thức fetchCategoryData ở đây
 private Object[][] fetchCategoryData() {
        Connection conn = DBConnection.getConnection();
        Object[][] data = null;

        if (conn != null) {
            try {
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery("SELECT loai_mat_hang.MALOAI, TENLOAI, COUNT(mat_hang.MAMH), loai_mat_hang.VOHIEUHOA FROM loai_mat_hang LEFT JOIN mat_hang ON loai_mat_hang.MALOAI = mat_hang.MALOAI GROUP BY loai_mat_hang.MALOAI, TENLOAI, loai_mat_hang.VOHIEUHOA");


                // Đếm số dòng trong ResultSet
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();

                data = new Object[rowCount][4]; // Thêm một cột nữa
                int row = 0;
                while (rs.next()) {
                data[row][0] = rs.getString("MALOAI");
                data[row][1] = rs.getString("TENLOAI");
                data[row][2] = rs.getInt(3);
                data[row][3] = rs.getBoolean("VOHIEUHOA"); // Lấy trạng thái VOHIEUHOA
                row++;
}


                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
 
private int getTongSoMatHang() {
    Connection conn = DBConnection.getConnection();
    int tongSoMatHang = 0;

    if (conn != null) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM mat_hang");

            if (rs.next()) {
                tongSoMatHang = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return tongSoMatHang;
}


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            DanhMucNhomMatHang categoryForm = new DanhMucNhomMatHang();
            categoryForm.setVisible(true);
            categoryForm.updateTongSoMatHangLabel(); // Thêm dòng này
        });
    }
}


