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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class frmPhanQuyen3 extends JFrame implements ActionListener {
    private Connection connection;
    private JTable table;
    private JButton changeButton;
    private JButton exitButton;

    
    public frmPhanQuyen3() {
        // Kết nối đến cơ sở dữ liệu
        connection = DBConnection.getConnection();

        // Thiết lập giao diện người dùng
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo bảng và mô hình dữ liệu mặc định
        DefaultTableModel model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Tên đăng nhập");
        model.addColumn("Tên nhân viên");
        model.addColumn("SĐT");
        model.addColumn("Giới tính");
        model.addColumn("Ngày Sinh");
        model.addColumn("Quyền");

        try {
            String query = "SELECT TENDANGNHAP, TENNHANVIEN, SDT, GIOITINH,NGAYSINH, LAQUANLY " +
                    "FROM nhan_vien";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String quyen = resultSet.getInt("LAQUANLY") == 1 ? "Quản lý" : "Nhân viên bán hàng";
                model.addRow(new Object[]{
                        resultSet.getString("TENDANGNHAP"),
                        resultSet.getString("TENNHANVIEN"),
                        resultSet.getString("SDT"),
                        resultSet.getString("GIOITINH"),
                        resultSet.getString("NGAYSINH"),
                        quyen
                });
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tạo nút Thay đổi
        changeButton = new JButton("Thay đổi");
        changeButton.addActionListener(this);
        
        exitButton = new JButton("Thoát");
        exitButton.addActionListener(this);
        exitButton.setBackground(new Color(231, 76, 60)); // Red color for exit button
        exitButton.setForeground(Color.black);

        // Đổi màu nút
        changeButton.setBackground(new Color(52, 152, 219)); // Đổi màu background
        changeButton.setForeground(Color.black); // Đổi màu chữ


        // Đưa bảng và nút vào frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        //panel.add(changeButton, BorderLayout.SOUTH);
        //add(panel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(changeButton);
        buttonPanel.add(exitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        setTitle("Phân Quyền Người Dùng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String tendangnhap = (String) table.getValueAt(selectedRow, 0);
                int quyen = ((String) table.getValueAt(selectedRow, 5)).equals("Quản lý") ? 0 : 1;

                // Hiển thị hộp thoại xác nhận cấp quyền
                int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn cấp quyền cho người dùng này không?");
                if (option == JOptionPane.YES_OPTION) {
                    if (quyen == 1) {
   
                        if (option == JOptionPane.YES_OPTION) {
                            String inputPassword = JOptionPane.showInputDialog(this, "Vui lòng nhập mật khẩu của nhân viên quản lý:");
                            if (inputPassword != null && checkAdminPassword(inputPassword)) {
                                // Thực hiện cập nhật quyền
                                updatePermission(tendangnhap, quyen);
                            } else {
                                JOptionPane.showMessageDialog(this, "Mật khẩu không đúng hoặc đã hủy bỏ.");
                            }
                        }
                    } else {
                        String inputPassword = JOptionPane.showInputDialog(this, "Vui lòng nhập mật khẩu của nhân viên quản lý:");
                        if (inputPassword != null && checkAdminPassword(inputPassword)) {
                            // Thực hiện cấp quyền
                            updatePermission(tendangnhap, quyen);
                        } else {
                            JOptionPane.showMessageDialog(this, "Mật khẩu không đúng hoặc đã hủy bỏ.");
                        }
                    }
                }
            }
        }else if (e.getSource() == exitButton) { // Handle exit button
        int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát không?", "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
           this.dispose();
        }
    }
    }

    // Hàm kiểm tra mật khẩu admin
    private boolean checkAdminPassword(String inputPassword) {
        try {
            String query = "SELECT MATKHAU FROM nhan_vien WHERE TENDANGNHAP=? AND LAQUANLY=1";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "admin"); 
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String adminPassword = resultSet.getString("MATKHAU");
                return inputPassword.equals(adminPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm cập nhật quyền
    private void updatePermission(String tendangnhap, int quyen) {
        try {
            String updateQuery = "UPDATE nhan_vien SET LAQUANLY=? WHERE TENDANGNHAP=?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, quyen);
            statement.setString(2, tendangnhap);
            statement.executeUpdate();

            statement.close();

            // Cập nhật lại bảng
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setValueAt(quyen == 1 ? "Quản lý" : "Nhân viên bán hàng", table.getSelectedRow(), 5);

            JOptionPane.showMessageDialog(this, "Cấp quyền thành công.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new frmPhanQuyen3();
    }
}

