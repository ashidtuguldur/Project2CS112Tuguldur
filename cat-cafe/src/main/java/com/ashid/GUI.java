package com.ashid;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import javax.swing.*;

public class GUI extends JFrame {

  private static final Color DARK_BROWN = new Color(0x48, 0x2E, 0x1D);
  private static final Color MED_BROWN = new Color(0x89, 0x5D, 0x2B);
  private static final Color TAN = new Color(0xA3, 0x96, 0x6A);
  private static final Color CREAM = new Color(0xF0, 0xDA, 0xAE);
  private static final Color RUSSET = new Color(0x90, 0x55, 0x3C);

  private Font font;
  private Font headerFont;
  private Font subHeaderFont;

  private JTextField tfName;
  private JTextField tfWeight;
  private JComboBox<Cat.Pattern> pattern;
  private JComboBox<Month> bornMonth;
  private JComboBox<Integer> bornDay;
  private JComboBox<Integer> bornYear;
  private JComboBox<Month> cameMonth;
  private JComboBox<Integer> cameDay;
  private JComboBox<Integer> cameYear;
  private JButton addBtn;

  private JTextField tfSearch;
  private JComboBox<String> searchPattern;
  private JTextField tfWeightMin;
  private JTextField tfWeightMax;
  private JComboBox<String> adoptedFilter;
  private JTextField tfDateSearch;
  private JButton searchBtn;
  private JButton showAllBtn;

  private DefaultListModel<Cat> listModel;
  private JList<Cat> catJList;

  private JTextArea detailArea;
  private JLabel photoLabel;
  private JButton choosePhotoBtn;
  private JTextArea notesArea;
  private JButton adoptBtn;
  private JButton deleteBtn;
  private JButton saveNotesBtn;
  private Cat selectedCat;

  private CatLinkedList cats;

  public GUI(List<Cat> ls) {
    super("Cat Cafe Data Base");
    setSize(1150, 700);
    setLocation(100, 100);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().setBackground(DARK_BROWN);
    cats = new CatLinkedList();
    for (Cat c : ls) cats.add(c);
    cats.sort();

    font = new Font("Times", Font.PLAIN, 16);
    headerFont = new Font("Times", Font.BOLD, 18);
    subHeaderFont = new Font("Times", Font.ITALIC, 15);

    JPanel sidebar = buildSidebar();
    sidebar.setPreferredSize(new Dimension(330, 0));
    getContentPane().add(sidebar, BorderLayout.WEST);
    getContentPane().add(buildMainPanel(), BorderLayout.CENTER);

    refreshDisplay(cats);
    setVisible(true);
    getRootPane().setDefaultButton(addBtn);

    addWindowListener(
      new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      }
    );
  }

  private JPanel buildSidebar() {
    JPanel sidebar = new JPanel(new BorderLayout());
    sidebar.setBackground(DARK_BROWN);

    // Tab button row — sits above the content box, no border overlap
    JPanel tabRow = new JPanel(new GridLayout(1, 2, 3, 0));
    tabRow.setBackground(DARK_BROWN);
    tabRow.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

    JButton searchTabBtn = makeTabButton("Search");
    JButton addTabBtn = makeTabButton("Add Cat");
    tabRow.add(searchTabBtn);
    tabRow.add(addTabBtn);
    sidebar.add(tabRow, BorderLayout.NORTH);

    CardLayout cards = new CardLayout();
    JPanel cardPanel = new JPanel(cards);
    cardPanel.setBackground(CREAM);
    cardPanel.setBorder(
      BorderFactory.createMatteBorder(0, 6, 6, 6, DARK_BROWN)
    );

    JPanel searchPanel = buildSearchPanel();
    JPanel addPanel = buildAddPanel();
    cardPanel.add(searchPanel, "Search");
    cardPanel.add(addPanel, "Add Cat");
    sidebar.add(cardPanel, BorderLayout.CENTER);

    styleTabActive(searchTabBtn);
    styleTabInactive(addTabBtn);

    searchTabBtn.addActionListener(e -> {
      cards.show(cardPanel, "Search");
      styleTabActive(searchTabBtn);
      styleTabInactive(addTabBtn);
    });
    addTabBtn.addActionListener(e -> {
      cards.show(cardPanel, "Add Cat");
      styleTabActive(addTabBtn);
      styleTabInactive(searchTabBtn);
    });

    return sidebar;
  }

  private JButton makeTabButton(String text) {
    JButton btn = new JButton(text);
    btn.setFont(font);
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setOpaque(true);
    return btn;
  }

  private void styleTabActive(JButton btn) {
    btn.setBackground(CREAM);
    btn.setForeground(DARK_BROWN);
  }

  private void styleTabInactive(JButton btn) {
    btn.setBackground(TAN);
    btn.setForeground(DARK_BROWN);
  }

  private JPanel buildSearchPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(CREAM);
    panel.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

    tfSearch = new JTextField();
    tfSearch.setFont(font);
    panel.add(labeledRow("Name:", tfSearch));
    panel.add(Box.createVerticalStrut(6));

    String[] patternOptions = new String[Cat.Pattern.values().length + 1];
    patternOptions[0] = "Any";
    for (int i = 0; i < Cat.Pattern.values().length; i++) patternOptions[i +
    1] = Cat.Pattern.values()[i].toString();
    searchPattern = new JComboBox<>(patternOptions);
    searchPattern.setFont(font);
    panel.add(labeledRow("Pattern:", searchPattern));
    panel.add(Box.createVerticalStrut(6));

    tfWeightMin = new JTextField();
    tfWeightMin.setFont(font);
    panel.add(labeledRow("Min Wt (oz):", tfWeightMin));
    panel.add(Box.createVerticalStrut(6));

    tfWeightMax = new JTextField();
    tfWeightMax.setFont(font);
    panel.add(labeledRow("Max Wt (oz):", tfWeightMax));
    panel.add(Box.createVerticalStrut(6));

    adoptedFilter = new JComboBox<>(
      new String[] { "Any", "Available", "Adopted" }
    );
    adoptedFilter.setFont(font);
    panel.add(labeledRow("Adopted:", adoptedFilter));
    panel.add(Box.createVerticalStrut(6));

    tfDateSearch = new JTextField();
    tfDateSearch.setFont(font);
    panel.add(labeledRow("Date:", tfDateSearch));
    panel.add(Box.createVerticalStrut(12));

    JPanel searchButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    searchButtons.setOpaque(false);
    searchButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
    searchBtn = makeActionButton("Search");
    showAllBtn = makeActionButton("Show All");
    searchBtn.addActionListener(e -> performSearch());
    showAllBtn.addActionListener(e -> refreshDisplay(cats));
    searchButtons.add(searchBtn);
    searchButtons.add(showAllBtn);
    panel.add(searchButtons);

    panel.add(Box.createVerticalGlue());
    return panel;
  }

  private JPanel buildAddPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(CREAM);
    panel.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

    tfName = new JTextField();
    tfName.setFont(font);
    panel.add(labeledRow("Name:", tfName));
    panel.add(Box.createVerticalStrut(6));

    tfWeight = new JTextField();
    tfWeight.setFont(font);
    panel.add(labeledRow("Weight (oz):", tfWeight));
    panel.add(Box.createVerticalStrut(6));

    pattern = new JComboBox<>(Cat.Pattern.values());
    pattern.setFont(font);
    panel.add(labeledRow("Pattern:", pattern));
    panel.add(Box.createVerticalStrut(10));

    panel.add(subHeader("── Date Born ──"));
    panel.add(Box.createVerticalStrut(4));

    bornMonth = new JComboBox<>(Month.values());
    bornMonth.setFont(font);
    panel.add(labeledRow("Month:", bornMonth));
    panel.add(Box.createVerticalStrut(4));

    bornDay = new JComboBox<>();
    bornDay.setFont(font);
    panel.add(labeledRow("Day:", bornDay));
    panel.add(Box.createVerticalStrut(4));

    Integer[] years = new Integer[51];
    for (int i = 0; i < 51; i++) years[i] = 2000 + i;
    bornYear = new JComboBox<>(years);
    bornYear.setFont(font);
    panel.add(labeledRow("Year:", bornYear));
    panel.add(Box.createVerticalStrut(10));

    updateBornDays();
    bornMonth.addItemListener(e -> updateBornDays());
    bornYear.addItemListener(e -> updateBornDays());

    panel.add(subHeader("── Date Came ──"));
    panel.add(Box.createVerticalStrut(4));

    cameMonth = new JComboBox<>(Month.values());
    cameMonth.setFont(font);
    panel.add(labeledRow("Month:", cameMonth));
    panel.add(Box.createVerticalStrut(4));

    cameDay = new JComboBox<>();
    cameDay.setFont(font);
    panel.add(labeledRow("Day:", cameDay));
    panel.add(Box.createVerticalStrut(4));

    cameYear = new JComboBox<>(years);
    cameYear.setFont(font);
    panel.add(labeledRow("Year:", cameYear));
    panel.add(Box.createVerticalStrut(12));

    updateCameDays();
    cameMonth.addItemListener(e -> updateCameDays());
    cameYear.addItemListener(e -> updateCameDays());

    addBtn = makeActionButton("Add Cat");
    addBtn.setFont(headerFont);
    addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
    addBtn.addActionListener(new AddCatHandler());
    panel.add(addBtn);

    panel.add(Box.createVerticalGlue());
    return panel;
  }

  private JButton makeActionButton(String text) {
    JButton btn = new JButton(text);
    btn.setFont(font);
    btn.setBackground(MED_BROWN);
    btn.setForeground(CREAM);
    btn.setFocusPainted(false);
    btn.setOpaque(true);
    btn.setBorderPainted(false);
    return btn;
  }

  private JPanel buildMainPanel() {
    JPanel listPanel = new JPanel(new BorderLayout());
    listPanel.setBackground(CREAM);
    listPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));

    JLabel listTitle = new JLabel("Cat List");
    listTitle.setFont(headerFont);
    listTitle.setForeground(DARK_BROWN);
    listTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
    listPanel.add(listTitle, BorderLayout.NORTH);

    listModel = new DefaultListModel<>();
    catJList = new JList<>(listModel);
    catJList.setFont(font);
    catJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    catJList.setBackground(new Color(0xF8, 0xF0, 0xDC));
    catJList.setForeground(DARK_BROWN);
    catJList.setCellRenderer(new CatCellRenderer());
    catJList.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) showCatDetail(catJList.getSelectedValue());
    });
    JScrollPane listScroll = new JScrollPane(catJList);
    listScroll.getViewport().setBackground(new Color(0xF8, 0xF0, 0xDC));
    listPanel.add(listScroll, BorderLayout.CENTER);

    JPanel detailPanel = new JPanel(new BorderLayout());
    detailPanel.setBackground(CREAM);
    detailPanel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 8));

    JLabel detailTitle = new JLabel("Cat Details");
    detailTitle.setFont(headerFont);
    detailTitle.setForeground(DARK_BROWN);
    detailTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
    detailPanel.add(detailTitle, BorderLayout.NORTH);

    JPanel centerContent = new JPanel();
    centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
    centerContent.setBackground(CREAM);

    photoLabel = new JLabel("(no photo)", SwingConstants.CENTER);
    photoLabel.setFont(font);
    photoLabel.setForeground(TAN);
    photoLabel.setOpaque(true);
    photoLabel.setBackground(new Color(0xE8, 0xD8, 0xB0));
    photoLabel.setBorder(BorderFactory.createLineBorder(TAN));
    photoLabel.setPreferredSize(new Dimension(300, 200));
    photoLabel.setMaximumSize(new Dimension(300, 200));
    photoLabel.setMinimumSize(new Dimension(300, 200));
    JPanel photoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    photoWrapper.setOpaque(false);
    photoWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
    photoWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
    photoWrapper.add(photoLabel);
    centerContent.add(photoWrapper);
    centerContent.add(Box.createVerticalStrut(6));

    choosePhotoBtn = makeActionButton("Choose Photo");
    choosePhotoBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
    choosePhotoBtn.setEnabled(false);
    choosePhotoBtn.addActionListener(e -> choosePhoto());
    centerContent.add(choosePhotoBtn);
    centerContent.add(Box.createVerticalStrut(8));

    detailArea = new JTextArea();
    detailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    detailArea.setEditable(false);
    detailArea.setBackground(new Color(0xF8, 0xF0, 0xDC));
    detailArea.setForeground(DARK_BROWN);
    detailArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    detailArea.setText("(select a cat from the list)");
    JScrollPane infoScroll = new JScrollPane(detailArea);
    infoScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    infoScroll.setPreferredSize(new Dimension(300, 155));
    infoScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 155));
    centerContent.add(infoScroll);
    centerContent.add(Box.createVerticalStrut(8));

    JLabel notesLabel = new JLabel("Notes:");
    notesLabel.setFont(font);
    notesLabel.setForeground(DARK_BROWN);
    notesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    centerContent.add(notesLabel);
    centerContent.add(Box.createVerticalStrut(4));

    notesArea = new JTextArea(4, 0);
    notesArea.setFont(font);
    notesArea.setLineWrap(true);
    notesArea.setWrapStyleWord(true);
    notesArea.setEnabled(false);
    notesArea.setBackground(new Color(0xF8, 0xF0, 0xDC));
    notesArea.setForeground(DARK_BROWN);
    notesArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    JScrollPane notesScroll = new JScrollPane(notesArea);
    notesScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
    notesScroll.setPreferredSize(new Dimension(300, 100));
    notesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    centerContent.add(notesScroll);

    detailPanel.add(centerContent, BorderLayout.CENTER);

    adoptBtn = makeActionButton("Adopt");
    adoptBtn.setFont(headerFont);
    adoptBtn.setEnabled(false);
    adoptBtn.addActionListener(e -> adoptSelectedCat());

    deleteBtn = makeActionButton("Delete");
    deleteBtn.setFont(headerFont);
    deleteBtn.setBackground(RUSSET);
    deleteBtn.setEnabled(false);
    deleteBtn.addActionListener(e -> deleteSelectedCat());

    saveNotesBtn = makeActionButton("Save Notes");
    saveNotesBtn.setFont(headerFont);
    saveNotesBtn.setEnabled(false);
    saveNotesBtn.addActionListener(e -> saveNotes());

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    btnPanel.setBackground(CREAM);
    btnPanel.add(adoptBtn);
    btnPanel.add(deleteBtn);
    btnPanel.add(saveNotesBtn);
    detailPanel.add(btnPanel, BorderLayout.SOUTH);

    JSplitPane split = new JSplitPane(
      JSplitPane.HORIZONTAL_SPLIT,
      listPanel,
      detailPanel
    );
    split.setDividerLocation(220);
    split.setResizeWeight(0.3);
    split.setBackground(TAN);

    JPanel main = new JPanel(new BorderLayout());
    main.setBackground(CREAM);
    main.add(split, BorderLayout.CENTER);
    return main;
  }

  private void showCatDetail(Cat cat) {
    selectedCat = cat;
    if (cat == null) {
      clearDetail();
      return;
    }
    detailArea.setText(cat.toString());
    detailArea.setCaretPosition(0);
    loadPhoto(cat);
    notesArea.setText(cat.notes != null ? cat.notes : "");
    notesArea.setEnabled(true);
    choosePhotoBtn.setEnabled(true);
    saveNotesBtn.setEnabled(true);
    if (cat.adopted != null) {
      adoptBtn.setText("Already Adopted");
      adoptBtn.setEnabled(false);
    } else {
      adoptBtn.setText("Adopt");
      adoptBtn.setEnabled(true);
    }
    deleteBtn.setEnabled(true);
  }

  private void clearDetail() {
    selectedCat = null;
    detailArea.setText("(select a cat from the list)");
    photoLabel.setIcon(null);
    photoLabel.setText("(no photo)");
    notesArea.setText("");
    notesArea.setEnabled(false);
    choosePhotoBtn.setEnabled(false);
    saveNotesBtn.setEnabled(false);
    adoptBtn.setEnabled(false);
    adoptBtn.setText("Adopt");
    deleteBtn.setEnabled(false);
  }

  private void adoptSelectedCat() {
    if (selectedCat == null || selectedCat.adopted != null) return;
    LocalDate today = LocalDate.now();
    Month m = Month.values()[today.getMonthValue() - 1];
    selectedCat.adopted = new Date(today.getDayOfMonth(), today.getYear(), m);
    CatData.updateAdopted(selectedCat);
    showCatDetail(selectedCat);
    catJList.repaint();
  }

  private void deleteSelectedCat() {
    if (selectedCat == null) return;
    int confirm = JOptionPane.showConfirmDialog(
      this,
      "Delete " + selectedCat.name + "?",
      "Confirm Delete",
      JOptionPane.YES_NO_OPTION
    );
    if (confirm != JOptionPane.YES_OPTION) return;
    cats.delete(selectedCat);
    CatData.delete(selectedCat);
    refreshDisplay(cats);
  }

  private void loadPhoto(Cat cat) {
    if (cat.photoPath != null && !cat.photoPath.isEmpty()) {
      try {
        String fullPath = CatData.resolvePhotoPath(cat.photoPath);
        ImageIcon raw = new ImageIcon(fullPath);
        Image scaled = raw.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        photoLabel.setIcon(new ImageIcon(scaled));
        photoLabel.setText(null);
      } catch (Exception ex) {
        photoLabel.setIcon(null);
        photoLabel.setText("(photo not found)");
      }
    } else {
      photoLabel.setIcon(null);
      photoLabel.setText("(no photo)");
    }
  }

  private void choosePhoto() {
    if (selectedCat == null) return;
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
      "Image files", "jpg", "jpeg", "png", "gif", "bmp"));
    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      try {
        String relative = CatData.copyPhoto(
          fc.getSelectedFile().getAbsolutePath(), selectedCat.name);
        selectedCat.photoPath = relative;
        CatData.updatePhoto(selectedCat);
        loadPhoto(selectedCat);
      } catch (java.io.IOException ex) {
        JOptionPane.showMessageDialog(this,
          "Could not copy photo: " + ex.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void saveNotes() {
    if (selectedCat == null) return;
    selectedCat.notes = notesArea.getText();
    CatData.updateNotes(selectedCat);
  }

  private void refreshDisplay(CatLinkedList list) {
    listModel.clear();
    clearDetail();
    for (Cat cat : list) {
      listModel.addElement(cat);
    }
  }

  private void performSearch() {
    String nameQuery = tfSearch.getText().trim();
    String patStr = (String) searchPattern.getSelectedItem();
    String adoptedStr = (String) adoptedFilter.getSelectedItem();
    Cat.Pattern pat = "Any".equals(patStr) ? null : Cat.Pattern.valueOf(patStr);

    Double minW = null,
      maxW = null;
    try {
      if (!tfWeightMin.getText().trim().isEmpty()) minW = Double.parseDouble(
        tfWeightMin.getText().trim()
      );
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(
        this,
        "Min weight must be a number.",
        "Input Error",
        JOptionPane.ERROR_MESSAGE
      );
      return;
    }
    try {
      if (!tfWeightMax.getText().trim().isEmpty()) maxW = Double.parseDouble(
        tfWeightMax.getText().trim()
      );
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(
        this,
        "Max weight must be a number.",
        "Input Error",
        JOptionPane.ERROR_MESSAGE
      );
      return;
    }

    String dateQuery = tfDateSearch.getText().trim();
    CatLinkedList results = cats.search(
      nameQuery.isEmpty() ? null : nameQuery,
      pat,
      minW,
      maxW,
      dateQuery.isEmpty() ? null : dateQuery
    );

    if ("Available".equals(adoptedStr)) {
      CatLinkedList filtered = new CatLinkedList();
      for (Cat cat : results) {
        if (cat.adopted == null) filtered.add(cat);
      }
      results = filtered;
    } else if ("Adopted".equals(adoptedStr)) {
      CatLinkedList filtered = new CatLinkedList();
      for (Cat cat : results) {
        if (cat.adopted != null) filtered.add(cat);
      }
      results = filtered;
    }

    refreshDisplay(results);
  }

  private void updateBornDays() {
    updateDaysFor(bornMonth, bornYear, bornDay);
  }

  private void updateCameDays() {
    updateDaysFor(cameMonth, cameYear, cameDay);
  }

  private void updateDaysFor(
    JComboBox<Month> monthBox,
    JComboBox<Integer> yearBox,
    JComboBox<Integer> dayBox
  ) {
    Month selectedMonth = (Month) monthBox.getSelectedItem();
    int selectedYear = (Integer) yearBox.getSelectedItem();
    int maxDays = selectedMonth.getMaxDays(selectedYear);
    Integer currentDay = (Integer) dayBox.getSelectedItem();
    dayBox.removeAllItems();
    for (int i = 1; i <= maxDays; i++) dayBox.addItem(i);
    if (currentDay != null && currentDay <= maxDays) dayBox.setSelectedItem(
      currentDay
    );
  }

  private JLabel subHeader(String text) {
    JLabel lbl = new JLabel(text);
    lbl.setFont(subHeaderFont);
    lbl.setForeground(RUSSET);
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    return lbl;
  }

  private JPanel labeledRow(String labelText, JComponent comp) {
    JPanel row = new JPanel(new BorderLayout(6, 0));
    row.setOpaque(false);
    row.setAlignmentX(Component.LEFT_ALIGNMENT);
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
    JLabel lbl = new JLabel(labelText);
    lbl.setFont(font);
    lbl.setForeground(DARK_BROWN);
    lbl.setPreferredSize(new Dimension(95, 28));
    row.add(lbl, BorderLayout.WEST);
    row.add(comp, BorderLayout.CENTER);
    return row;
  }

  private static class CatCellRenderer extends DefaultListCellRenderer {

    private static final Color DARK_BROWN = new Color(0x48, 0x2E, 0x1D);
    private static final Color MED_BROWN = new Color(0x89, 0x5D, 0x2B);
    private static final Color CREAM = new Color(0xF0, 0xDA, 0xAE);

    @Override
    public Component getListCellRendererComponent(
      JList<?> list,
      Object value,
      int index,
      boolean isSelected,
      boolean cellHasFocus
    ) {
      super.getListCellRendererComponent(
        list,
        value,
        index,
        isSelected,
        cellHasFocus
      );
      if (value instanceof Cat) {
        Cat cat = (Cat) value;
        String adoptedMark = cat.adopted != null ? " ✓" : "";
        setText(
          (index + 1) + ". " + cat.name + " (" + cat.pattern + ")" + adoptedMark
        );
      }
      if (isSelected) {
        setBackground(MED_BROWN);
        setForeground(CREAM);
      } else {
        setBackground(new Color(0xF8, 0xF0, 0xDC));
        setForeground(DARK_BROWN);
      }
      return this;
    }
  }

  public class AddCatHandler implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String catName = tfName.getText().trim();
      String catWeightStr = tfWeight.getText().trim();
      if (catName.isEmpty() || catWeightStr.isEmpty()) {
        JOptionPane.showMessageDialog(
          GUI.this,
          "Please enter a name and weight.",
          "Missing Fields",
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }
      double catWeightVal;
      try {
        catWeightVal = Double.parseDouble(catWeightStr);
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
          GUI.this,
          "Weight must be a number.",
          "Invalid Weight",
          JOptionPane.ERROR_MESSAGE
        );
        return;
      }

      Date bornDate = new Date(
        (Integer) bornDay.getSelectedItem(),
        (Integer) bornYear.getSelectedItem(),
        (Month) bornMonth.getSelectedItem()
      );
      Date cameDate = new Date(
        (Integer) cameDay.getSelectedItem(),
        (Integer) cameYear.getSelectedItem(),
        (Month) cameMonth.getSelectedItem()
      );
      Cat newCat = new Cat(
        bornDate,
        cameDate,
        (Cat.Pattern) pattern.getSelectedItem(),
        catWeightVal,
        catName
      );
      cats.enter(newCat);
      cats.sort();
      CatData.save(newCat);
      tfName.setText("");
      tfWeight.setText("");
      refreshDisplay(cats);
    }
  }

  public static void main(String[] args) {
    new GUI(CatData.load());
  }
}
