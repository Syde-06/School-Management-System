import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Main extends JFrame {
    private static final Color MINT_LIGHT = new Color(236, 250, 229);     // #ecfae5
    private static final Color SAGE_LIGHT = new Color(221, 246, 210);     // #ddf6d2
    private static final Color SAGE_MEDIUM = new Color(202, 232, 189);    // #cae8bd
    private static final Color SAGE_DARK = new Color(176, 219, 156);      // #b0db9c
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color GRAY_LIGHT = new Color(248, 250, 252);
    private static final Color GRAY_MEDIUM = new Color(226, 232, 240);
    private static final Color GRAY_DARK = new Color(100, 116, 139);
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);
    private static final Color TEXT_SECONDARY = new Color(71, 85, 105);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_RED = new Color(239, 68, 68);

    // Service classes
    private StudentService studentService;
    private TeacherService teacherService;
    private StaffService staffService;
    private SubjectService subjectService;

    // UI Components
    private JPanel sidePanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton selectedNavButton;

    // Dashboard components
    private JLabel studentCountLabel, teacherCountLabel, staffCountLabel;

    // Table models and containers
    private DefaultTableModel studentTableModel, teacherTableModel, staffTableModel;
    private JTable studentTable, teacherTable, staffTable;
    private JPanel studentsTableContainer, teachersTableContainer, staffTableContainer;

    // Constants for image processing
    private static final int MAX_IMAGE_WIDTH = 400;
    private static final int MAX_IMAGE_HEIGHT = 400;
    private static final int DISPLAY_WIDTH = 180;
    private static final int DISPLAY_HEIGHT = 200;

    public Main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            DatabaseConfig.initializeDatabase();
            initializeServices();
            initializeUI();
            refreshDashboard();
            refreshAllTables();
        } catch (Exception e) {
            showErrorDialog("Error initializing application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initializeServices() {
        studentService = new StudentService();
        teacherService = new TeacherService();
        staffService = new StaffService();
        subjectService = new SubjectService();
    }

    private void initializeUI() {
        setTitle("CHMSU Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setBackground(GRAY_LIGHT);
        setLayout(new BorderLayout());
        createModernSidebar();
        createMainContent();
        add(sidePanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createModernSidebar() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(SAGE_DARK);
        sidePanel.setPreferredSize(new Dimension(280, getHeight()));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SAGE_MEDIUM));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SAGE_DARK);
        headerPanel.setPreferredSize(new Dimension(280, 80));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel logoLabel = new JLabel("🎓");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        logoLabel.setForeground(WHITE);

        JLabel titleLabel = new JLabel("CHMSU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(WHITE);

        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(SAGE_DARK);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(SAGE_DARK);
        navPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        String[] navItems = {"Dashboard", "Students", "Teachers", "Staff"};
        String[] navIcons = {"📊", "👨‍🎓", "👨‍🏫", "👥"};
        
        for (int i = 0; i < navItems.length; i++) {
            JButton navBtn = createModernNavButton(navItems[i], navIcons[i]);
            navPanel.add(navBtn);
            navPanel.add(Box.createVerticalStrut(5));
            
            if (i == 0) {
                selectedNavButton = navBtn;
                navBtn.setBackground(SAGE_MEDIUM);
            }
        }

        sidePanel.add(headerPanel, BorderLayout.NORTH);
        sidePanel.add(navPanel, BorderLayout.CENTER);
    }

    private JButton createModernNavButton(String text, String icon) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBorder(new EmptyBorder(15, 25, 15, 25));
        btn.setBackground(SAGE_DARK);
        btn.setForeground(WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(WHITE);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(WHITE);
        textLabel.setBorder(new EmptyBorder(0, 15, 0, 0));

        btn.add(iconLabel, BorderLayout.WEST);
        btn.add(textLabel, BorderLayout.CENTER);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != selectedNavButton) {
                    btn.setBackground(new Color(160, 209, 146));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != selectedNavButton) {
                    btn.setBackground(SAGE_DARK);
                }
            }
        });

        btn.addActionListener(e -> {
            if (selectedNavButton != null) {
                selectedNavButton.setBackground(SAGE_DARK);
            }
            selectedNavButton = btn;
            btn.setBackground(SAGE_MEDIUM);

            String cardName = text.toLowerCase();
            if (text.equals("Dashboard")) {
                cardName = "dashboard";
                refreshDashboard();
            }
            cardLayout.show(mainPanel, cardName);
        });

        return btn;
    }

    private void createMainContent() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(GRAY_LIGHT);

        mainPanel.add(createModernDashboardPanel(), "dashboard");
        mainPanel.add(createModernStudentsPanel(), "students");
        mainPanel.add(createModernTeachersPanel(), "teachers");
        mainPanel.add(createModernStaffPanel(), "staff");

        cardLayout.show(mainPanel, "dashboard");
    }

    private JPanel createModernDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GRAY_LIGHT);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GRAY_LIGHT);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Welcome to CHMSU Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(GRAY_LIGHT);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setBackground(GRAY_LIGHT);

        studentCountLabel = new JLabel("0", SwingConstants.CENTER);
        teacherCountLabel = new JLabel("0", SwingConstants.CENTER);
        staffCountLabel = new JLabel("0", SwingConstants.CENTER);

        JPanel studentCard = createModernStatCard("Total Students", studentCountLabel, SAGE_MEDIUM, "👨‍🎓");
        JPanel teacherCard = createModernStatCard("Total Teachers", teacherCountLabel, SAGE_MEDIUM, "👨‍🏫");
        JPanel staffCard = createModernStatCard("Total Staff", staffCountLabel, SAGE_MEDIUM, "👥");

        statsPanel.add(studentCard);
        statsPanel.add(teacherCard);
        statsPanel.add(staffCard);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createModernStatCard(String title, JLabel countLabel, Color bgColor, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1),
            new EmptyBorder(30, 25, 30, 25)
        ));

        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 10)),
            new EmptyBorder(30, 25, 30, 25)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_SECONDARY);

        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        countLabel.setForeground(TEXT_PRIMARY);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createModernStudentsPanel() {
        JPanel panel = createModernTablePanel("Students Management", "👨‍🎓", "Add Student", 
            SAGE_LIGHT, this::showAddStudentDialog);
        
        createStudentTable();
        studentsTableContainer = getTableContainer(panel);
        if (studentsTableContainer != null) {
            JScrollPane scrollPane = new JScrollPane(studentTable);
            styleScrollPane(scrollPane);
            studentsTableContainer.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }

    private JPanel createModernTeachersPanel() {
        JPanel panel = createModernTablePanel("Teachers Management", "👨‍🏫", "Add Teacher", 
            SAGE_MEDIUM, this::showAddTeacherDialog);
        
        createTeacherTable();
        teachersTableContainer = getTableContainer(panel);
        if (teachersTableContainer != null) {
            JScrollPane scrollPane = new JScrollPane(teacherTable);
            styleScrollPane(scrollPane);
            teachersTableContainer.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }

    private JPanel createModernStaffPanel() {
        JPanel panel = createModernTablePanel("Staff Management", "👥", "Add Staff", 
            MINT_LIGHT, this::showAddStaffDialog);
        
        createStaffTable();
        staffTableContainer = getTableContainer(panel);
        if (staffTableContainer != null) {
            JScrollPane scrollPane = new JScrollPane(staffTable);
            styleScrollPane(scrollPane);
            staffTableContainer.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }

    private JPanel getTableContainer(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel subPanel = (JPanel) comp;
                if (subPanel.getBackground().equals(WHITE)) {
                    return subPanel;
                }
            }
        }
        return null;
    }

    private JPanel createModernTablePanel(String title, String icon, String buttonText, 
                                         Color accentColor, Runnable addAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GRAY_LIGHT);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(GRAY_LIGHT);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(GRAY_LIGHT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        JButton addBtn = createModernButton(buttonText);
        addBtn.addActionListener(e -> addAction.run());

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(addBtn, BorderLayout.EAST);

        // Table container
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 10)),
            new EmptyBorder(0, 0, 0, 0)
        ));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tableContainer, BorderLayout.CENTER);

        return panel;
    }

    private JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(WHITE);
        btn.setBackground(SAGE_DARK);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(160, 209, 146));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SAGE_DARK);
            }
        });

        return btn;
    }

    private void createStudentTable() {
        String[] columns = {"ID", "Name", "Course", "Year Level", "Enrolled Subjects"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = createModernTable(studentTableModel);
        setupStudentTableInteractions();
        
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(150); // ID column wider
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name column
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Course column
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Year Level column
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Subjects column
    }

    private void createTeacherTable() {
        String[] columns = {"ID", "Employee #", "Name", "Department", "Specialization", "Assigned Subjects"};
        teacherTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        teacherTable = createModernTable(teacherTableModel);
        setupTeacherTableInteractions();
        
        teacherTable.getColumnModel().getColumn(0).setPreferredWidth(150); // ID column wider
        teacherTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Employee # column
        teacherTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Name column
        teacherTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Department column
        teacherTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Specialization column
        teacherTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Subjects column
    }

    private void createStaffTable() {
        String[] columns = {"ID", "Employee #", "Name", "Position", "Office", "Contact"};
        staffTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        staffTable = createModernTable(staffTableModel);
        
        staffTable.getColumnModel().getColumn(0).setPreferredWidth(150); // ID column wider
        staffTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Employee # column
        staffTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Name column
        staffTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Position column
        staffTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Office column
        staffTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Contact column
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(50);
        table.setBackground(WHITE);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(SAGE_LIGHT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(GRAY_MEDIUM);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(SAGE_MEDIUM);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRAY_MEDIUM));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(WHITE);
                    } else {
                        c.setBackground(new Color(248, 250, 252));
                    }
                }
                
                setBorder(new EmptyBorder(8, 12, 8, 12));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        return table;
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setBackground(WHITE);
    }

    private void setupStudentTableInteractions() {
        // Add right-click context menu
        JPopupMenu popup = createModernPopupMenu();
        JMenuItem viewItem = new JMenuItem("👁️ View Information");
        JMenuItem editItem = new JMenuItem("✏️ Edit Student");
        JMenuItem deleteItem = new JMenuItem("🗑️ Delete Student");
        JMenuItem assignItem = new JMenuItem("📚 Assign Subject");
        
        styleMenuItem(viewItem);
        styleMenuItem(editItem);
        styleMenuItem(deleteItem);
        styleMenuItem(assignItem);
        
        viewItem.addActionListener(e -> showStudentInfoDialog(false));
        editItem.addActionListener(e -> showStudentInfoDialog(true));
        deleteItem.addActionListener(e -> deleteStudent());
        assignItem.addActionListener(e -> showAssignSubjectDialog(true));
        
        popup.add(viewItem);
        popup.add(editItem);
        popup.addSeparator();
        popup.add(deleteItem);
        popup.addSeparator();
        popup.add(assignItem);

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = studentTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        studentTable.setRowSelectionInterval(row, row);
                        popup.show(studentTable, e.getX(), e.getY());
                    }
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = studentTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        studentTable.setRowSelectionInterval(row, row);
                        showStudentInfoDialog(false);
                    }
                }
            }
        });
    }

    private void setupTeacherTableInteractions() {
        JPopupMenu popup = createModernPopupMenu();
        JMenuItem assignItem = new JMenuItem("📚 Assign Subject");
        styleMenuItem(assignItem);
        assignItem.addActionListener(e -> showAssignSubjectDialog(false));
        popup.add(assignItem);

        teacherTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = teacherTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        teacherTable.setRowSelectionInterval(row, row);
                        popup.show(teacherTable, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private JPopupMenu createModernPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(WHITE);
        popup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_MEDIUM, 1),
            new EmptyBorder(5, 0, 5, 0)
        ));
        return popup;
    }

    private void styleMenuItem(JMenuItem item) {
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(WHITE);
        item.setForeground(TEXT_PRIMARY);
        item.setBorder(new EmptyBorder(8, 16, 8, 16));
        
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(SAGE_LIGHT);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(WHITE);
            }
        });
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showStudentInfoDialog(boolean isEditMode) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        Student student = studentService.findById(studentId);
        
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }

        JDialog dialog = new JDialog(this, isEditMode ? "Edit Student" : "Student Information", true);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.setPreferredSize(new Dimension(200, 0));
        picturePanel.setBorder(BorderFactory.createTitledBorder("Profile Picture"));

        JLabel pictureLabel = new JLabel();
        pictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pictureLabel.setVerticalAlignment(SwingConstants.CENTER);
        pictureLabel.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        pictureLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (student.getProfilePicture() != null && !student.getProfilePicture().isEmpty()) {
            try {
                ImageIcon imageIcon = decodeBase64ToImageIcon(student.getProfilePicture(), DISPLAY_WIDTH, DISPLAY_HEIGHT);
                if (imageIcon != null) {
                    pictureLabel.setIcon(imageIcon);
                } else {
                    pictureLabel.setText("Image Error");
                }
            } catch (Exception e) {
                pictureLabel.setText("No Image");
                System.err.println("Error loading student image: " + e.getMessage());
            }
        } else {
            pictureLabel.setText("No Image");
        }

        picturePanel.add(pictureLabel, BorderLayout.CENTER);

        String[] newProfilePicture = {student.getProfilePicture()};
        if (isEditMode) {
            JButton browseBtn = createModernButton("Change Picture");
            browseBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
                if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        String base64Image = encodeImageToBase64(selectedFile);
                        if (base64Image != null) {
                            newProfilePicture[0] = base64Image;
                            ImageIcon imageIcon = decodeBase64ToImageIcon(base64Image, DISPLAY_WIDTH, DISPLAY_HEIGHT);
                            if (imageIcon != null) {
                                pictureLabel.setIcon(imageIcon);
                                pictureLabel.setText("");
                            }
                            System.out.println("Image encoded successfully. Size: " + base64Image.length() + " characters");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Error loading image: " + ex.getMessage(), 
                            "Image Error", 
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });
            picturePanel.add(browseBtn, BorderLayout.SOUTH);
        }

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(student.getId() != null ? student.getId() : "", 25);
        JTextField firstNameField = new JTextField(student.getFirstName() != null ? student.getFirstName() : "", 20);
        JTextField lastNameField = new JTextField(student.getLastName() != null ? student.getLastName() : "", 20);
        JTextField middleNameField = new JTextField(student.getMiddleName() != null ? student.getMiddleName() : "", 20);
        JTextField courseField = new JTextField(student.getCourse() != null ? student.getCourse() : "", 20);
        JSpinner yearLevelSpinner = new JSpinner(new SpinnerNumberModel(
            student.getYearLevel() > 0 ? student.getYearLevel() : 1, 1, 4, 1));
        JTextField emailField = new JTextField(student.getEmail() != null ? student.getEmail() : "", 20);
        JTextField contactField = new JTextField(student.getContactNumber() != null ? student.getContactNumber() : "", 20);
        JTextArea addressArea = new JTextArea(student.getAddress() != null ? student.getAddress() : "", 3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        idField.setEditable(isEditMode);
        firstNameField.setEditable(isEditMode);
        lastNameField.setEditable(isEditMode);
        middleNameField.setEditable(isEditMode);
        courseField.setEditable(isEditMode);
        yearLevelSpinner.setEnabled(isEditMode);
        emailField.setEditable(isEditMode);
        contactField.setEditable(isEditMode);
        addressArea.setEditable(isEditMode);

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Middle Name:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(middleNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Year Level:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(yearLevelSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        infoPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        infoPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        infoPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setPreferredSize(new Dimension(200, 60));
        infoPanel.add(addressScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        infoPanel.add(new JLabel("Enrolled Subjects:"), gbc);
        gbc.gridx = 1;
        StringBuilder subjects = new StringBuilder();
        for (Subject subject : student.getEnrolledSubjects()) {
            if (subjects.length() > 0) subjects.append(", ");
            subjects.append(subject.toString());
        }
        JTextArea subjectsArea = new JTextArea(subjects.toString(), 2, 20);
        subjectsArea.setEditable(false);
        subjectsArea.setLineWrap(true);
        subjectsArea.setWrapStyleWord(true);
        subjectsArea.setBackground(new Color(240, 240, 240));
        JScrollPane subjectsScrollPane = new JScrollPane(subjectsArea);
        subjectsScrollPane.setPreferredSize(new Dimension(200, 60));
        infoPanel.add(subjectsScrollPane, gbc);

        mainPanel.add(picturePanel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (isEditMode) {
            JButton saveBtn = createModernButton("Save Changes");
            JButton cancelBtn = createModernButton("Cancel");

            saveBtn.addActionListener(e -> {
                if (validateStudentForm(idField, firstNameField, lastNameField, courseField)) {
                    try {
                        student.setId(idField.getText().trim());
                        student.setFirstName(firstNameField.getText().trim());
                        student.setLastName(lastNameField.getText().trim());
                        student.setMiddleName(middleNameField.getText().trim());
                        student.setCourse(courseField.getText().trim());
                        student.setYearLevel((Integer) yearLevelSpinner.getValue());
                        student.setEmail(emailField.getText().trim());
                        student.setContactNumber(contactField.getText().trim());
                        student.setAddress(addressArea.getText().trim());
                        student.setProfilePicture(newProfilePicture[0]);

                        if (studentService.updateStudent(student)) {
                            JOptionPane.showMessageDialog(dialog, "Student updated successfully!");
                            refreshStudentTable();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, 
                                "Failed to update student!", 
                                "Update Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Error updating student: " + ex.getMessage(), 
                            "Update Error", 
                            JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            buttonPanel.add(saveBtn);
            buttonPanel.add(cancelBtn);
        } else {
            JButton closeBtn = createModernButton("Close");
            closeBtn.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeBtn);
        }

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first!");
            return;
        }

        String studentId = (String) studentTable.getValueAt(selectedRow, 0);
        String studentName = (String) studentTable.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete student: " + studentName + "?\nID: " + studentId,
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (studentService.deleteStudent(studentId)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    refreshStudentTable();
                    refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete student!", 
                        "Delete Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting student: " + e.getMessage(), 
                    "Delete Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                throw new IOException("Unable to read image file. Please check if the file is a valid image.");
            }
            
            BufferedImage resizedImage = resizeImage(originalImage, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            ImageIO.write(resizedImage, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            
            System.out.println("Original image size: " + originalImage.getWidth() + "x" + originalImage.getHeight());
            System.out.println("Resized image size: " + resizedImage.getWidth() + "x" + resizedImage.getHeight());
            System.out.println("Base64 string length: " + base64String.length() + " characters");
            System.out.println("Estimated storage size: " + (base64String.length() / 1024) + " KB");
            
            if (base64String.length() > 1000000) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "The image is quite large (" + (base64String.length() / 1024) + " KB). This may cause performance issues.\nDo you want to continue?",
                    "Large Image Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (choice != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            
            return base64String;
        } catch (IOException e) {
            System.err.println("Error encoding image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        if (originalWidth > maxWidth || originalHeight > maxHeight) {
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);
            
            newWidth = (int) (originalWidth * ratio);
            newHeight = (int) (originalHeight * ratio);
        }
        
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        return resizedImage;
    }

    private ImageIcon decodeBase64ToImageIcon(String base64String, int width, int height) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(bais);
            bais.close();
            
            if (bufferedImage == null) {
                return null;
            }
            
            Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Error decoding Base64 image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(25);
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField middleNameField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JSpinner yearLevelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        JTextField emailField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        JLabel pictureLabel = new JLabel("No image selected");
        JButton browseBtn = createModernButton("Browse");

        idField.setToolTipText("Enter ID (e.g., GSB09290500, CHMSU-2024-001, STU-ENG-2023-045)");

        String[] profilePicturePath = {null};

        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    profilePicturePath[0] = encodeImageToBase64(selectedFile);
                    if (profilePicturePath[0] != null) {
                        pictureLabel.setText(selectedFile.getName() + " (Ready)");
                    } else {
                        pictureLabel.setText("Error loading image");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error loading image: " + ex.getMessage(), 
                        "Image Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Middle Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(middleNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        formPanel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Year Level:"), gbc);
        gbc.gridx = 1;
        formPanel.add(yearLevelSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setPreferredSize(new Dimension(200, 60));
        formPanel.add(addressScrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Picture:"), gbc);
        gbc.gridx = 1;
        JPanel picturePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        picturePanel.add(pictureLabel);
        picturePanel.add(browseBtn);
        formPanel.add(picturePanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createModernButton("Save");
        JButton cancelBtn = createModernButton("Cancel");

        saveBtn.addActionListener(e -> {
            if (validateStudentForm(idField, firstNameField, lastNameField, courseField)) {
                try {
                    Student student = new Student(
                        idField.getText().trim(), 
                        firstNameField.getText().trim(), 
                        lastNameField.getText().trim()
                    );
                    student.setMiddleName(middleNameField.getText().trim());
                    student.setCourse(courseField.getText().trim());
                    student.setYearLevel((Integer) yearLevelSpinner.getValue());
                    student.setEmail(emailField.getText().trim());
                    student.setContactNumber(contactField.getText().trim());
                    student.setAddress(addressArea.getText().trim());
                    student.setProfilePicture(profilePicturePath[0]);

                    if (studentService.addStudent(student)) {
                        JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                        refreshStudentTable();
                        refreshDashboard();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Failed to add student! Please check if the Student ID already exists.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error adding student: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddTeacherDialog() {
        JDialog dialog = new JDialog(this, "Add New Teacher", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(25);
        JTextField empNumberField = new JTextField(25);
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField departmentField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField contactField = new JTextField(20);
        JTextField specializationField = new JTextField(20);

        idField.setToolTipText("Enter Teacher ID (e.g., TCHR-CS-001, FAC-IT-GSB002)");
        empNumberField.setToolTipText("Enter Employee Number (e.g., EMP-2020-001, GSB-FAC-2019-002)");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Teacher ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Employee Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(empNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        formPanel.add(departmentField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        formPanel.add(specializationField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createModernButton("Save");
        JButton cancelBtn = createModernButton("Cancel");

        saveBtn.addActionListener(e -> {
            if (validateTeacherForm(idField, empNumberField, firstNameField, lastNameField, departmentField)) {
                try {
                    Teacher teacher = new Teacher(
                        idField.getText().trim(), 
                        firstNameField.getText().trim(), 
                        lastNameField.getText().trim()
                    );
                    teacher.setEmployeeNumber(empNumberField.getText().trim());
                    teacher.setDepartment(departmentField.getText().trim());
                    teacher.setEmail(emailField.getText().trim());
                    teacher.setContactNumber(contactField.getText().trim());
                    teacher.setSpecialization(specializationField.getText().trim());

                    if (teacherService.addTeacher(teacher)) {
                        JOptionPane.showMessageDialog(dialog, "Teacher added successfully!");
                        refreshTeacherTable();
                        refreshDashboard();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Failed to add teacher!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error adding teacher: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAddStaffDialog() {
        JDialog dialog = new JDialog(this, "Add New Staff", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(25);
        JTextField empNumberField = new JTextField(25);
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField positionField = new JTextField(20);
        JTextField officeField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField contactField = new JTextField(20);

        idField.setToolTipText("Enter Staff ID (e.g., STAFF-ADM-001, LIB-STAFF-002)");
        empNumberField.setToolTipText("Enter Employee Number (e.g., ADM-2020-001, LIB-2019-002)");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Staff ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Employee Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(empNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Position/Role:"), gbc);
        gbc.gridx = 1;
        formPanel.add(positionField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Office:"), gbc);
        gbc.gridx = 1;
        formPanel.add(officeField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = createModernButton("Save");
        JButton cancelBtn = createModernButton("Cancel");

        saveBtn.addActionListener(e -> {
            if (validateStaffForm(idField, empNumberField, firstNameField, lastNameField, positionField)) {
                try {
                    Staff staff = new Staff(
                        idField.getText().trim(), 
                        firstNameField.getText().trim(), 
                        lastNameField.getText().trim()
                    );
                    staff.setEmployeeNumber(empNumberField.getText().trim());
                    staff.setPosition(positionField.getText().trim());
                    staff.setOffice(officeField.getText().trim());
                    staff.setEmail(emailField.getText().trim());
                    staff.setContactNumber(contactField.getText().trim());

                    if (staffService.addStaff(staff)) {
                        JOptionPane.showMessageDialog(dialog, "Staff added successfully!");
                        refreshStaffTable();
                        refreshDashboard();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Failed to add staff!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error adding staff: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showAssignSubjectDialog(boolean isStudent) {
        JTable currentTable = isStudent ? studentTable : teacherTable;
        int selectedRow = currentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a " + (isStudent ? "student" : "teacher") + " first!");
            return;
        }

        String id = (String) currentTable.getValueAt(selectedRow, 0);
        String name = (String) currentTable.getValueAt(selectedRow, isStudent ? 1 : 2);

        JDialog dialog = new JDialog(this, "Assign Subject to " + name, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Select Subject to Assign:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        ArrayList<Subject> availableSubjects = subjectService.getAllSubjects();
        if (availableSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects available to assign!");
            return;
        }
        
        JList<Subject> subjectList = new JList<>(availableSubjects.toArray(new Subject[0]));
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Subject) {
                    setText(((Subject) value).toString());
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(subjectList);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton assignBtn = createModernButton("Assign");
        JButton cancelBtn = createModernButton("Cancel");

        assignBtn.addActionListener(e -> {
            Subject selectedSubject = subjectList.getSelectedValue();
            if (selectedSubject != null) {
                try {
                    boolean success = false;
                    if (isStudent) {
                        Student student = studentService.findById(id);
                        if (student != null) {
                            success = studentService.enrollInSubject(student, selectedSubject);
                        }
                    } else {
                        Teacher teacher = teacherService.findById(id);
                        if (teacher != null) {
                            success = teacherService.assignSubject(teacher, selectedSubject);
                        }
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Subject assigned successfully!");
                        if (isStudent) {
                            refreshStudentTable();
                        } else {
                            refreshTeacherTable();
                        }
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Failed to assign subject! The subject may already be assigned.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error assigning subject: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a subject!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(assignBtn);
        buttonPanel.add(cancelBtn);

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private boolean validateStudentForm(JTextField idField, JTextField firstNameField,
                                        JTextField lastNameField, JTextField courseField) {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID is required!");
            idField.requestFocus();
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!");
            firstNameField.requestFocus();
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required!");
            lastNameField.requestFocus();
            return false;
        }
        if (courseField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course is required!");
            courseField.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateTeacherForm(JTextField idField, JTextField empNumberField,
                                        JTextField firstNameField, JTextField lastNameField,
                                        JTextField departmentField) {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Teacher ID is required!");
            idField.requestFocus();
            return false;
        }
        if (empNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee number is required!");
            empNumberField.requestFocus();
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!");
            firstNameField.requestFocus();
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required!");
            lastNameField.requestFocus();
            return false;
        }
        if (departmentField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department is required!");
            departmentField.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateStaffForm(JTextField idField, JTextField empNumberField,
                                      JTextField firstNameField, JTextField lastNameField,
                                      JTextField positionField) {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Staff ID is required!");
            idField.requestFocus();
            return false;
        }
        if (empNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee number is required!");
            empNumberField.requestFocus();
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!");
            firstNameField.requestFocus();
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required!");
            lastNameField.requestFocus();
            return false;
        }
        if (positionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Position/Role is required!");
            positionField.requestFocus();
            return false;
        }
        return true;
    }

    private void refreshDashboard() {
        try {
            ArrayList<Student> students = studentService.getAllStudents();
            ArrayList<Teacher> teachers = teacherService.getAllTeachers();
            ArrayList<Staff> staffMembers = staffService.getAllStaff();

            studentCountLabel.setText(String.valueOf(students.size()));
            teacherCountLabel.setText(String.valueOf(teachers.size()));
            staffCountLabel.setText(String.valueOf(staffMembers.size()));
        } catch (Exception e) {
            showErrorDialog("Error refreshing dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshAllTables() {
        try {
            refreshStudentTable();
            refreshTeacherTable();
            refreshStaffTable();
        } catch (Exception e) {
            showErrorDialog("Error refreshing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshStudentTable() {
        try {
            studentTableModel.setRowCount(0);
            ArrayList<Student> students = studentService.getAllStudents();

            for (Student student : students) {
                StringBuilder subjects = new StringBuilder();
                for (Subject subject : student.getEnrolledSubjects()) {
                    if (subjects.length() > 0) subjects.append(", ");
                    subjects.append(subject.getCode());
                }

                Object[] row = {
                    student.getId(),
                    student.getFullName(),
                    student.getCourse(),
                    "Year " + student.getYearLevel(),
                    subjects.toString()
                };
                studentTableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorDialog("Error refreshing student table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshTeacherTable() {
        try {
            teacherTableModel.setRowCount(0);
            ArrayList<Teacher> teachers = teacherService.getAllTeachers();

            for (Teacher teacher : teachers) {
                StringBuilder subjects = new StringBuilder();
                for (Subject subject : teacher.getAssignedSubjects()) {
                    if (subjects.length() > 0) subjects.append(", ");
                    subjects.append(subject.getCode());
                }

                Object[] row = {
                    teacher.getId(),
                    teacher.getEmployeeNumber(),
                    teacher.getFullName(),
                    teacher.getDepartment(),
                    teacher.getSpecialization(),
                    subjects.toString()
                };
                teacherTableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorDialog("Error refreshing teacher table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshStaffTable() {
        try {
            staffTableModel.setRowCount(0);
            ArrayList<Staff> staffMembers = staffService.getAllStaff();

            for (Staff staff : staffMembers) {
                Object[] row = {
                    staff.getId(),
                    staff.getEmployeeNumber(),
                    staff.getFullName(),
                    staff.getPosition(),
                    staff.getOffice(),
                    staff.getContactNumber()
                };
                staffTableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorDialog("Error refreshing staff table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}
