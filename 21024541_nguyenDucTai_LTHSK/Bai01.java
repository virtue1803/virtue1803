package tuan2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Bai01 extends JFrame implements ActionListener{
	
	private JButton buttonSearch, buttonAdd, buttonRemoveBlank, buttonRemove, buttonUpdate, buttonSave;
	private JRadioButton rbNam, rbNu;
	private JTextField txtMaNhanVien, txtHo, txtTenNhanVien, txtTuoi, txtTienLuong, txtInputSearch;
	private JLabel lbTitle, lbMaNhanVien, lbHo, lbTenNhanVien, lbTuoi, lbTienLuong, lbPhai, lbInputSearch;
	private JPanel pnNorth, pnCenter, pnSouth, pnSearch, pnFeature;
	private Box b, b1, b2, b3, b4, bSouth;
	private JTable table;
	private JScrollPane pane;
	private DefaultTableModel modelTable;
	private JSplitPane splitPane;
	private JComboBox cbSex;
	private QuanLyNhanVien quanLy;
	private DatabaseNhanVien database;
	
	public Bai01() {
		database = new DatabaseNhanVien();
		quanLy = new QuanLyNhanVien();
		
		this.setTitle("Quản lý thông tin nhân viên");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.execute();
		
		try {
			themDuLieuVaoCot();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void execute() {
		this.setLayout(new BorderLayout());
		this.add(pnNorth = new JPanel(), BorderLayout.NORTH);
		this.add(pnCenter = new JPanel(), BorderLayout.CENTER);
		this.add(bSouth = new Box(BoxLayout.X_AXIS), BorderLayout.SOUTH);
		
		pnNorth.add(lbTitle = new JLabel("THÔNG TIN NHÂN VIÊN"));
		lbTitle.setFont(new Font("Arial", Font.BOLD, 20));
		lbTitle.setForeground(Color.BLUE);
		
		pnCenter.setLayout(new BorderLayout(0, 10));
		pnCenter.add(b = new Box(BoxLayout.Y_AXIS), BorderLayout.NORTH);
		b.add(b1 = new Box(BoxLayout.X_AXIS));
		
		b1.add(lbMaNhanVien = new JLabel("Mã nhân viên:"));
		lbMaNhanVien.setPreferredSize(new Dimension(80, 25));
		b1.add(txtMaNhanVien = new JTextField());
		b.add(b2 = new Box(BoxLayout.X_AXIS));
		b2.add(lbHo = new JLabel("Họ:"));
		lbHo.setPreferredSize(new Dimension(80, 25));
		b2.add(txtHo = new JTextField());
		b2.add(lbTenNhanVien = new JLabel("Tên nhân viên:"));
		lbTenNhanVien.setPreferredSize(new Dimension(85, 25));
		b2.add(txtTenNhanVien = new JTextField());
		b.add(b3 = new Box(BoxLayout.X_AXIS));
		b3.add(lbTuoi = new JLabel("Tuổi:"));
		lbTuoi.setPreferredSize(new Dimension(80, 25));
		b3.add(txtTuoi = new JTextField());
		b3.add(lbPhai = new JLabel("Phái:"));
		b3.add(rbNam = new JRadioButton("Nam"));
		b3.add(rbNu = new JRadioButton("Nữ"));
		b.add(b4 = new Box(BoxLayout.X_AXIS));
		b4.add(lbTienLuong = new JLabel("Tiền lương:"));
		lbTienLuong.setPreferredSize(new Dimension(80, 25));
		b4.add(txtTienLuong = new JTextField());
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbNam);
		group.add(rbNu);
		
		String[] cols = {"Mã NV", "Họ", "Tên", "Phái", "Tuổi", "Tiền lương"};
		pnCenter.add(pane = new JScrollPane(table = new JTable(modelTable = new DefaultTableModel(cols, 0))), BorderLayout.CENTER);
		
		bSouth.add(splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT), BoxLayout.X_AXIS);
		
		splitPane.setResizeWeight(0.5);
		splitPane.add(pnSearch = new JPanel());
		splitPane.add(pnFeature = new JPanel());
		splitPane.setPreferredSize(new Dimension(1000, 40));
		
		pnFeature.setLayout(new FlowLayout());
		pnFeature.add(buttonAdd = new JButton("Thêm"));
		pnFeature.add(buttonRemoveBlank = new JButton("Xoá trắng"));
		pnFeature.add(buttonRemove = new JButton("Xoá"));
		pnFeature.add(buttonUpdate = new JButton("Sửa"));
		pnFeature.add(buttonSave = new JButton("Lưu"));
		pnFeature.setBorder(BorderFactory.createLoweredBevelBorder());
		
		pnSearch.setLayout(new FlowLayout());
		pnSearch.add(lbInputSearch = new JLabel("Nhập mã cần tìm:"));
		pnSearch.add(txtInputSearch = new JTextField());
		txtInputSearch.setPreferredSize(new Dimension(150, 25));
		pnSearch.add(buttonSearch = new JButton("Tìm"));
		pnSearch.setBorder(BorderFactory.createLoweredBevelBorder());
		
		cbSex = new JComboBox();
		cbSex.addItem("Nam");
		cbSex.addItem("Nữ");
		
		DefaultCellEditor ce = new DefaultCellEditor(cbSex);
		table.getColumnModel().getColumn(3).setCellEditor(ce);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		buttonAdd.addActionListener(this);
		buttonRemoveBlank.addActionListener(this);
		buttonRemove.addActionListener(this);
		buttonSave.addActionListener(this);
		buttonUpdate.addActionListener(this);
		buttonSearch.addActionListener(this);
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				txtMaNhanVien.setText(modelTable.getValueAt(table.getSelectedRow(), 0).toString());
				txtHo.setText(modelTable.getValueAt(table.getSelectedRow(), 1).toString());
				txtTenNhanVien.setText(modelTable.getValueAt(table.getSelectedRow(), 2).toString());
				txtTuoi.setText(modelTable.getValueAt(table.getSelectedRow(), 4).toString());
				txtTienLuong.setText(modelTable.getValueAt(table.getSelectedRow(), 5).toString());
				if(modelTable.getValueAt(table.getSelectedRow(), 3).toString().equalsIgnoreCase("Nam")) {
					rbNam.setSelected(true);
					rbNu.setSelected(false);
				} else {
					rbNam.setSelected(false);
					rbNu.setSelected(true);
				}
			}
		});
	}
	
	private void themDuLieuVaoCot() {
		quanLy = (QuanLyNhanVien) database.readFile("nhanvien.dat");
		if(quanLy != null) {
			for(NhanVien target : quanLy.layDanhSachNV()) {
				String[] getData = {target.getMaNhanVien(), target.getHo(), target.getTenNhanVien(), 
						target.getPhai(), target.getTuoi() + "", target.getTienLuong() + ""};
				modelTable.addRow(getData);
			}
		} 
	}
	
	public static void main(String[] args) {
		new Bai01().setVisible(true);
	}

	private boolean coChuaKiTu(String text) {
		return text.matches("[^A-Za-z]+");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(buttonSearch)) {
			if(txtInputSearch.getText().equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(null, "Bạn chưa nhập mã số cần tìm. Vui lòng nhập!");
				return;
			} else {
				for(int i = 0; i < modelTable.getRowCount(); ++i) {
					if(modelTable.getValueAt(i, 0).toString().equals(txtInputSearch.getText())) {
						table.setRowSelectionInterval(i, i);
						return;
					}
				}				
			}

		} else if(e.getSource().equals(buttonAdd)) {
			if(e.getActionCommand().equals("Thêm")) {
				xoaTrang();
				txtMaNhanVien.requestFocus();
				buttonAdd.setText("Huỷ");
				buttonRemove.setEnabled(false);
			} else {
				buttonAdd.setText("Thêm");
				buttonRemove.setEnabled(true);
			}
		} else if(e.getSource().equals(buttonRemoveBlank)) {
			xoaTrang();
			this.txtMaNhanVien.requestFocus();
		} else if(e.getSource().equals(buttonRemove)) {
			if(table.getSelectedRow() == -1) {
				JOptionPane pane = new JOptionPane(null);
				pane.showMessageDialog(null, "Bạn chưa chọn dòng để xoá");
			} else {
				if(JOptionPane.showConfirmDialog(this, "Bạn có chắc xoá dòng này không?", "Cảnh bảo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					QuanLyNhanVien modify = new QuanLyNhanVien();
					modify.xoaNhanVien(new NhanVien(this.modelTable.getValueAt(table.getSelectedRow(), 0).toString(),
												  this.modelTable.getValueAt(table.getSelectedColumn(), 1).toString(),
												  this.modelTable.getValueAt(table.getSelectedColumn(), 2).toString(),
												  this.modelTable.getValueAt(table.getSelectedColumn(), 3).toString(),
												  Integer.valueOf(this.modelTable.getValueAt(table.getSelectedColumn(), 4).toString()),
												  Double.valueOf(this.modelTable.getValueAt(table.getSelectedColumn(), 5).toString())));
					modelTable.removeRow(table.getSelectedRow());
					xoaTrang();
				}
			}
		} else if(e.getActionCommand().equalsIgnoreCase("Lưu")) {
			String getMaNv, getHo, getTen, getPhai;
			int getTuoi;
			double getTienLuong;
			
			if(txtMaNhanVien.getText().equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(null, "Mã nhân viên không được để trống");
				return;
			} else {
				getMaNv = txtMaNhanVien.getText();
			}
			
			if(!this.coChuaKiTu(txtHo.getText()) && !txtHo.getText().equalsIgnoreCase("")) {
				getHo = txtHo.getText();
			} else {
				JOptionPane.showMessageDialog(null, "Họ không được chứa số hoặc bỏ trống");
				return;
			}
			
			if(!this.coChuaKiTu(txtTenNhanVien.getText()) && !txtTenNhanVien.getText().equalsIgnoreCase("")) {
				getTen = txtTenNhanVien.getText();
			} else {
				JOptionPane.showMessageDialog(null, "Tên nhân viên không được chứa số hoặc bỏ trống");
				return;
			}
			
			if(!this.coChuaKiTu(txtTuoi.getText()) || !this.coChuaKiTu(txtTienLuong.getText())) {
				JOptionPane.showMessageDialog(null, "Tuổi và tiền lương phải chứa số và không bỏ trống");
				return;
			} else {
				getTuoi = Integer.valueOf(txtTuoi.getText());
				getTienLuong = Double.valueOf(txtTienLuong.getText());
			}
			
			if(this.rbNam.isSelected() == false && this.rbNu.isSelected() == false) {
				JOptionPane.showMessageDialog(null, "Bạn chưa chọn phái!");
				return;
			} else {
				getPhai = rbNam.isSelected() ? "Nam" : "Nữ";
			}
			
			if(quanLy.themNhanVien(new NhanVien(getMaNv, getHo, getTen, getPhai, getTuoi, getTienLuong))) {
				String[] newRows = {getMaNv, getHo, getTen, getPhai, getTuoi + "", getTienLuong + ""};
				modelTable.addRow(newRows);
				database.saveFile("nhanvien.dat", quanLy);
				JOptionPane.showMessageDialog(null, "Thêm thành công");
			} else {
				JOptionPane.showMessageDialog(null, "Thêm thất bại vì trùng mã ID");
			}
			xoaTrang();
		} else {
			
		}
	}
	
	/**
	 * Phương thức xoá trắng
	 */
	private void xoaTrang() {
		this.txtMaNhanVien.setText("");
		this.txtHo.setText("");
		this.txtTenNhanVien.setText("");
		this.txtTuoi.setText("");
		this.rbNam.setSelected(false);
		this.rbNu.setSelected(false);
		this.txtTienLuong.setText("");
	}
}

/**
 * Class database nhân viên
 * @author Administrator
 *
 */
class DatabaseNhanVien {
	
	public void saveFile(String fileName, Object o) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "IO Error!");
			return;
		}
	}
	
	public Object readFile(String fileName) {
		Object o = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			o = ois.readObject();
			ois.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "IO Error!");
		}
		return o;
	}
}

/**
 * Class quản lí nhân viên
 * @author Administrator
 *
 */
class QuanLyNhanVien implements Serializable{
	private ArrayList<NhanVien> list;
	
	/**
	 * Khởi tạo nhân viên
	 */
	public QuanLyNhanVien() {
		list = new ArrayList<NhanVien>();
	}
	
	private NhanVien timNhanVien(String id) {
		for(NhanVien target : list) {
			if(target.getMaNhanVien().equalsIgnoreCase(id)) {
				return target;
			}
		}
		return null;
	}
	
	/**
	 * Thêm nhân viên vào danh sách
	 * @param nv
	 * @return
	 */
	public boolean themNhanVien(NhanVien nv) {
		if(timNhanVien(nv.getMaNhanVien()) == null) return false;
		list.add(nv);
		return true;
	}
	
	/**
	 * Xoá nhân viên trong danh sách
	 * @param nv
	 * @return
	 */
	public boolean xoaNhanVien(NhanVien nv) {
		NhanVien target = timNhanVien(nv.getMaNhanVien());
		if(target == null) {
			return false;
		}
		list.remove(target);
		return true;
	}
	
	public ArrayList<NhanVien> layDanhSachNV(){
		return this.list;
	}
}

/**
 * Class nhân viên
 * @author Administrator
 *
 */
class NhanVien implements Serializable{
	private String maNhanVien, ho, tenNhanVien, phai;
	private int tuoi;
	private double tienLuong;
	/**
	 * @param maNhanVien
	 * @param ho
	 * @param tenNhanVien
	 * @param phai
	 * @param tuoi
	 * @param tienLuong
	 */
	public NhanVien(String maNhanVien, String ho, String tenNhanVien, String phai, int tuoi, double tienLuong) {
		this.setMaNhanVien(maNhanVien);
		this.setHo(ho);
		this.setTenNhanVien(tenNhanVien);
		this.setPhai(phai);
		this.setTuoi(tuoi);
		this.setTienLuong(tienLuong);
	}
	/**
	 * @return the maNhanVien
	 */
	public String getMaNhanVien() {
		return maNhanVien;
	}
	/**
	 * @param maNhanVien the maNhanVien to set
	 */
	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	/**
	 * @return the ho
	 */
	public String getHo() {
		return ho;
	}
	/**
	 * @param ho the ho to set
	 */
	public void setHo(String ho) {
		this.ho = ho;
	}
	/**
	 * @return the tenNhanVien
	 */
	public String getTenNhanVien() {
		return tenNhanVien;
	}
	/**
	 * @param tenNhanVien the tenNhanVien to set
	 */
	public void setTenNhanVien(String tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
	}
	/**
	 * @return the phai
	 */
	public String getPhai() {
		return phai;
	}
	/**
	 * @param phai the phai to set
	 */
	public void setPhai(String phai) {
		this.phai = phai;
	}
	/**
	 * @return the tuoi
	 */
	public int getTuoi() {
		return tuoi;
	}
	/**
	 * @param tuoi the tuoi to set
	 */
	public void setTuoi(int tuoi) {
		this.tuoi = tuoi;
	}
	/**
	 * @return the tienLuong
	 */
	public double getTienLuong() {
		return tienLuong;
	}
	/**
	 * @param tienLuong the tienLuong to set
	 */
	public void setTienLuong(double tienLuong) {
		this.tienLuong = tienLuong;
	}
	
	
}