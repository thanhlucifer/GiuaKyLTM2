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


/**
 *
 * @author thanh
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComboBox;


public class DanhMucMatHang extends JFrame {
    private JPanel panel;
    private JButton addButton, editButton, deleteButton, saveButton, skipButton;
    private JTextField  codeField, itemField, priceField, unitField, descriptionField;
    private JTable table;
    private JComboBox<String> groupField;
   
    public DanhMucMatHang() {
        // Khởi tạo JFrame
        setTitle("Danh Mục Mặt Hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo JPanel
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 3, 10, 10));

        // Tạo các thành phần
        addButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        skipButton = new JButton("Bỏ qua");



        groupField = new JComboBox<>();
        updateComboBox(); // Cập nhật danh sách loại mặt hàng

        codeField = new JTextField();
        itemField = new JTextField();
        priceField = new JTextField();
        unitField = new JTextField();
        descriptionField = new JTextField();

        // Thêm các thành phần vào JPanel
        panel.add(new JLabel("Nhóm mặt hàng:"));
        panel.add(groupField);

        panel.add(new JLabel("Mã mặt hàng:"));
        panel.add(codeField);

        panel.add(new JLabel("Mặt hàng:"));
        panel.add(itemField);

        panel.add(new JLabel("Giá bán:"));
        panel.add(priceField);

        panel.add(new JLabel("Đơn vị tính:"));
        panel.add(unitField);

        panel.add(new JLabel("Mô tả:"));
        panel.add(descriptionField);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(skipButton);
        
        JCheckBox disableCheckbox = new JCheckBox("Vô hiệu hóa");
        panel.add(disableCheckbox);

        // Thêm nút thoát
        JButton exitButton = new JButton("Thoát");
        panel.add(exitButton);
        // Thêm ActionListener cho nút thoát
        exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dispose(); // Tắt cửa sổ JFrame
            }
        });
    

        // Tạo JTable và JScrollPane
        String[] columnNames = {"Nhóm mặt hàng", "Mã mặt hàng", "Mặt hàng", "Giá bán", "Đơn vị tính", "Mô tả", "Vô hiệu hóa"};

        Object[][] data = {}; // Dữ liệu bảng

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            int selectedRow = table.getSelectedRow();

            // Lấy dữ liệu từ bảng
            String nhomMatHang = (String) table.getValueAt(selectedRow, 0);
            String maMatHang = (String) table.getValueAt(selectedRow, 1);
            String tenMatHang = (String) table.getValueAt(selectedRow, 2);
            double giaBan = (double) table.getValueAt(selectedRow, 3);
            String donViTinh = (String) table.getValueAt(selectedRow, 4);
            String moTa = (String) table.getValueAt(selectedRow, 5);
            boolean voHieuHoa = (boolean) table.getValueAt(selectedRow, 6); // Lấy trạng thái checkbox
            // Hiển thị dữ liệu trên các TextField
            codeField.setText(maMatHang);
            itemField.setText(tenMatHang);
            priceField.setText(String.valueOf(giaBan));
            unitField.setText(donViTinh);
            descriptionField.setText(moTa);

            // Cập nhật giá trị cho JComboBox
            groupField.setSelectedItem(nhomMatHang);
            
            // Cập nhật trạng thái checkbox
            disableCheckbox.setSelected(voHieuHoa);
        }
    }
});
    

       editButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để sửa.");
            return;
        }

        int selectedRow = table.getSelectedRow();
        String maMatHang = (String) table.getValueAt(selectedRow, 1);
        String nhomMatHang = (String) groupField.getSelectedItem();
        String tenMatHang = itemField.getText();
        double giaBan = Double.parseDouble(priceField.getText());
        String donViTinh = unitField.getText();
        String moTa = descriptionField.getText();
        boolean voHieuHoa = disableCheckbox.isSelected(); // Lấy trạng thái checkbox

        updateMatHang(maMatHang, nhomMatHang, tenMatHang, giaBan, donViTinh, moTa, voHieuHoa);

        table.setValueAt(nhomMatHang, selectedRow, 0);
        table.setValueAt(tenMatHang, selectedRow, 2);
        table.setValueAt(giaBan, selectedRow, 3);
        table.setValueAt(donViTinh, selectedRow, 4);
        table.setValueAt(moTa, selectedRow, 5);
        table.setValueAt(voHieuHoa, selectedRow, 6);

        groupField.setSelectedIndex(0);
        codeField.setText("");
        itemField.setText("");
        priceField.setText("");
        unitField.setText("");
        descriptionField.setText("");
        disableCheckbox.setSelected(false);

        JOptionPane.showMessageDialog(null, "Đã cập nhật thông tin.");
    }
});




       deleteButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Kiểm tra xem đã chọn hàng trong bảng chưa
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để xóa.");
            return;
        }

        // Lấy hàng được chọn
        int selectedRow = table.getSelectedRow();

        // Lấy mã mặt hàng của hàng được chọn
        String maMatHang = (String) table.getValueAt(selectedRow, 1);

        // Thực hiện truy vấn xóa dữ liệu từ cơ sở dữ liệu
        deleteMatHang(maMatHang);

        // Xóa dòng trong bảng
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(selectedRow);

        JOptionPane.showMessageDialog(null, "Đã xóa thông tin.");
    }
});

    
       
       addButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lấy thông tin từ các JTextField và JComboBox
        String nhomMatHang = (String) groupField.getSelectedItem(); // Lấy giá trị từ JComboBox
        String maMatHang = codeField.getText();
        String tenMatHang = itemField.getText();
        double giaBan = Double.parseDouble(priceField.getText());
        String donViTinh = unitField.getText();
        String moTa = descriptionField.getText();
        boolean voHieuHoa = disableCheckbox.isSelected(); // Lấy trạng thái checkbox

        // Kiểm tra các trường dữ liệu nếu có trường nào rỗng thì thông báo lỗi
        if (nhomMatHang.isEmpty() || maMatHang.isEmpty() || tenMatHang.isEmpty() || donViTinh.isEmpty() || moTa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Thêm hàng mới vào bảng
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{nhomMatHang, maMatHang, tenMatHang, giaBan, donViTinh, moTa, voHieuHoa});

        // Xóa dữ liệu trong các JTextField và JComboBox
        groupField.setSelectedIndex(0);
        codeField.setText("");
        itemField.setText("");
        priceField.setText("");
        unitField.setText("");
        descriptionField.setText("");
        disableCheckbox.setSelected(false);

        // Thêm dữ liệu vào cơ sở dữ liệu
        try {
            MatHangDAO.addMatHang(nhomMatHang, maMatHang, tenMatHang, giaBan, donViTinh, moTa, voHieuHoa);
            JOptionPane.showMessageDialog(null, "Đã thêm mặt hàng.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm mặt hàng vào cơ sở dữ liệu.");
        }
    }
});

       skipButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Gọi phương thức clearFields()
        clearFields();
    }
});

        // Đặt layout cho JFrame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // Hiển thị JFrame
        setVisible(true);
        
         updateTable(); // Gọi phương thức để cập nhật bảng
        
    }
    
    
    private void updateTable() {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    List<MatHang> danhSachMatHang = MatHangDAO.getAllMatHang();

   for (MatHang matHang : danhSachMatHang) {
    model.addRow(new Object[] {
            matHang.getLoaiMatHang().getTenLoai(),
            matHang.getMaMH(),
            matHang.getTenMH(),
            matHang.getGiaBan(),
            matHang.getDonViTinh(),
            matHang.getMoTa(),
            matHang.isVoHieuHoa() // Thêm cột VOHIEUHOA vào bảng
    });
    // Cập nhật trạng thái checkbox
    int rowIndex = model.getRowCount() - 1;
    table.setValueAt(matHang.isVoHieuHoa(), rowIndex, 6);
}

}


    
    private void updateComboBox() {
    List<String> danhSachLoaiMatHang = MatHangDAO.getAllLoaiMatHang(); // Lấy danh sách loại mặt hàng
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(danhSachLoaiMatHang.toArray(new String[0]));
    groupField.setModel(model);
}

    // Phương thức xóa mặt hàng từ cơ sở dữ liệu
private void deleteMatHang(String maMatHang) {
    Connection conn = DBConnection.getConnection();

    try {
        String query = "DELETE FROM MAT_HANG WHERE MAMH = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, maMatHang);
        statement.executeUpdate();
    } catch (SQLException ex) {
        System.out.println("Lỗi truy vấn: " + ex.getMessage());
    }
}
private void updateMatHang(String maMatHang, String nhomMatHang, String tenMatHang, double giaBan, String donViTinh, String moTa, boolean voHieuHoa) {
    Connection conn = DBConnection.getConnection();

    try {
        String query = "UPDATE MAT_HANG SET MALOAI = (SELECT MALOAI FROM LOAI_MAT_HANG WHERE TENLOAI = ?), TENMH = ?, GIABAN = ?, DVT = ?, MOTA = ?, VOHIEUHOA = ? WHERE MAMH = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, nhomMatHang);
        statement.setString(2, tenMatHang);
        statement.setDouble(3, giaBan);
        statement.setString(4, donViTinh);
        statement.setString(5, moTa);
        statement.setBoolean(6, voHieuHoa);
        statement.setString(7, maMatHang);
        statement.executeUpdate();
    } catch (SQLException ex) {
        System.out.println("Lỗi truy vấn: " + ex.getMessage());
    }
}


private void clearFields() {
    groupField.setSelectedIndex(0); // Đặt lại giá trị của JComboBox về mặc định
    codeField.setText(""); // Xóa nội dung của JTextField codeField
    itemField.setText(""); // Xóa nội dung của JTextField itemField
    priceField.setText(""); // Xóa nội dung của JTextField priceField
    unitField.setText(""); // Xóa nội dung của JTextField unitField
    descriptionField.setText(""); // Xóa nội dung của JTextField descriptionField
}



    public static void main(String[] args) {
        new DanhMucMatHang();
    }
}

