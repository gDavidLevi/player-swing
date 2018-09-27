/*
 * Created by JFormDesigner on Thu Sep 20 11:25:27 MSK 2018
 */

package ru.davidlevi.swing.player.ui;

import javax.swing.border.*;
import javax.swing.event.*;

import ru.davidlevi.swing.player.interfaces.MainPlayCallbackListener;
import ru.davidlevi.swing.player.interfaces.Playlist;
import ru.davidlevi.swing.player.interfaces.Player;
import ru.davidlevi.swing.player.interfaces.implementation.MainPlaylist;
import ru.davidlevi.swing.player.interfaces.implementation.MainPlayer;
import ru.davidlevi.swing.player.utils.FileUtils;
import ru.davidlevi.swing.player.utils.PlaylistFileFilter;
import ru.davidlevi.swing.player.utils.UISkin;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static ru.davidlevi.swing.player.interfaces.implementation.MainPlaylist.PLS_FILE_DESCRIPTION;
import static ru.davidlevi.swing.player.interfaces.implementation.MainPlaylist.PLS_FILE_EXTENSION;
import static ru.davidlevi.swing.player.interfaces.implementation.MainPlayer.MP3_FILE_DESCRIPTION;
import static ru.davidlevi.swing.player.interfaces.implementation.MainPlayer.MP3_FILE_EXTENSION;

/**
 * UI
 *
 * @author David Levi
 */
public class MainFrame extends JFrame implements MainPlayCallbackListener {

  // Точка входа
  public static void main(String[] args) {
    // Устанавливаем тему по умолчанию
    UISkin.setDefault();
    // Запуск в отдельном потоке
    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = new MainFrame();
      mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      mainFrame.pack();
      mainFrame.setVisible(true);
    });
  }

  private Player player;
  private Playlist playList;
  //
  private FileFilter filterPls = new PlaylistFileFilter(PLS_FILE_EXTENSION, PLS_FILE_DESCRIPTION);
  private FileFilter filterMp3 = new PlaylistFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
  //
  private static final String INPUT_SONG_NAME = "введите имя песни";
  /*
  Поле moveAutomatic
      false - во время проигрывании песни ползунок передвигается
      true - передвигается вручную
   */
  private boolean moveAutomatic = false;
  private int currentVolumeValue;

  @Override
  public void callbackPlayStarted(String name) {
    labelSongName.setText(name);
  }

  @Override
  public void callbackProcessScroll(int position) {
    if (moveAutomatic) {
      sliderProgress.setValue(position);
    }
  }

  @Override
  public void callbackPlayFinished() {
    playList.playlistNext();
  }

  /**
   * Конструктор
   */
  private MainFrame() {
    initComponents();
    initComponentsSettings();
    //
    player = new MainPlayer(this);
    playList = new MainPlaylist(lstPlayList, player);
    player.playerSetVolume(slideVolume.getValue());
    slideVolume.setMaximum(MainPlayer.MAX_VOLUME);
  }

  /**
   * Настройка компонентов после инициализации
   */
  private void initComponentsSettings() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Mp3 player (swing)");
    setMinimumSize(new Dimension(310, 480));
    setResizable(false);
    //
    fileChooser.setAcceptAllFileFilterUsed(false); // не все файлы
    fileChooser.setMultiSelectionEnabled(true); // выбирать несколько файлов
    //
    lstPlayList.setComponentPopupMenu(popupMenuPlaylist); // всплывающее Popup-меню
  }

  private void buttonPlaySongActionPerformed(ActionEvent e) {
    playList.playlistPlayFile();
  }

  private void buttonStopSongActionPerformed(ActionEvent e) {
    player.playerStop();
  }

  private void buttonPauseSongActionPerformed(ActionEvent e) {
    player.playerPause();
  }

  private void slideVolumeStateChanged(ChangeEvent e) {
    player.playerSetVolume(slideVolume.getValue());
    if (slideVolume.getValue() == 0) {
      toggleButtonMute.setSelected(true);
    } else {
      toggleButtonMute.setSelected(false);
    }
  }

  private void buttonPreviousSongActionPerformed(ActionEvent e) {
    playList.playlistPrevious();
  }

  private void buttonNextSongActionPerformed(ActionEvent e) {
    playList.playlistNext();
  }

  private void buttonAddSongActionPerformed(ActionEvent e) {
    FileUtils.addFileFilter(fileChooser, filterMp3);
    // result хранит результат: выбран файл или нет
    int result = fileChooser.showOpenDialog(this);
    // Если нажата клавиша OK или YES
    if (result == JFileChooser.APPROVE_OPTION) {
      playList.playlistOpenFiles(fileChooser.getSelectedFiles());
    }
  }

  private void buttonDeleteSongActionPerformed(ActionEvent e) {
    playList.playlistDelete();
  }

  private void searchSong() {
    String name = txtSearch.getText().trim();
    if (!playList.playlistSearch(name)) {
      JOptionPane.showMessageDialog(this, "Поиск \'" + name + "\' не дал результатов");
      txtSearch.requestFocus();
      txtSearch.selectAll();
    }
  }

  private void buttonSearchActionPerformed(ActionEvent e) {
    searchSong();
  }

  private void textFieldSearchFocusGained(FocusEvent e) {
    if (txtSearch.getText().equals(INPUT_SONG_NAME)) {
      txtSearch.setText(EMPTY_STRING);
    }
  }

  private void textFieldSearchFocusLost(FocusEvent e) {
    if (txtSearch.getText().trim().equals(EMPTY_STRING)) {
      txtSearch.setText(INPUT_SONG_NAME);
    }
  }

  private void menuItemSavePlaylistActionPerformed(ActionEvent e) {
    FileUtils.addFileFilter(fileChooser, filterPls);
    int result = fileChooser.showSaveDialog(this);
    // Если нажата клавиша OK или YES
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      // Если такой файл уже существует
      if (selectedFile.exists()) {
        int resultOvveride = JOptionPane
            .showConfirmDialog(this, "Файл уже существует", "Перезаписать его?",
                JOptionPane.YES_NO_CANCEL_OPTION);
        switch (resultOvveride) {
          case JOptionPane.NO_OPTION:
            // Повторно открыть окно сохранения файла
            menuItemSavePlaylistActionPerformed(e);
            return;
          case JOptionPane.CANCEL_OPTION:
            fileChooser.cancelSelection();
            return;
        }
      }
      fileChooser.approveSelection();
      playList.playlistSaveList(selectedFile);
    }
  }

  private void menuItemOpenPlaylistActionPerformed(ActionEvent e) {
    FileUtils.addFileFilter(fileChooser, filterPls);
    // result хранит результат: выбран файл или нет
    int result = fileChooser.showOpenDialog(this);
    // Если нажата клавиша OK или YES
    if (result == JFileChooser.APPROVE_OPTION) {
      playList.playlistOpenList(fileChooser.getSelectedFile());
    }
  }

  private void menuItemExitActionPerformed(ActionEvent e) {
    System.exit(0);
  }

  private void menuItemSkin1ActionPerformed(ActionEvent e) {
    UISkin.set(this, UIManager.getSystemLookAndFeelClassName());
    fileChooser.updateUI();
  }

  private void menuItemSkin2ActionPerformed(ActionEvent e) {
    UISkin.set(this, new MetalLookAndFeel());
    fileChooser.updateUI();
  }

  private void toggleButtonMuteActionPerformed(ActionEvent e) {
    if (toggleButtonMute.isSelected()) {
      currentVolumeValue = slideVolume.getValue();
      slideVolume.setValue(0);
    } else {
      slideVolume.setValue(currentVolumeValue);
    }
  }

  private void menuItemPopUpAddActionPerformed(ActionEvent e) {
    buttonAddSongActionPerformed(e);
  }

  private void menuItemPopUpClearActionPerformed(ActionEvent e) {
    playList.playlistClear();
  }

  private void menuItemPopUpDeleteActionPerformed(ActionEvent e) {
    buttonDeleteSongActionPerformed(e);
  }

  private void menuItemPopUpOpenPlaylistActionPerformed(ActionEvent e) {
    menuItemOpenPlaylistActionPerformed(e);
  }

  private void txtSearchKeyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_ENTER) {
      searchSong();
    }
  }

  private void sliderProgressMouseReleased(MouseEvent e) {
    if (!sliderProgress.getValueIsAdjusting()) {
      // Позиция для прокрутки
      double posValue = sliderProgress.getValue() * 1.0 / 1000;
      player.playerRewind(posValue);
    }
    moveAutomatic = true;
  }

  private void sliderProgressMousePressed(MouseEvent e) {
    moveAutomatic = false;
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    ResourceBundle bundle = ResourceBundle.getBundle("localization");
    menuBar = new JMenuBar();
    menu1 = new JMenu();
    menuItemOpenPlaylist = new JMenuItem();
    menuItemSavePlaylist = new JMenuItem();
    menuItemExit = new JMenuItem();
    menu2 = new JMenu();
    menu3 = new JMenu();
    menuItemSkin1 = new JMenuItem();
    menuItemSkin2 = new JMenuItem();
    panelSearch = new JPanel();
    txtSearch = new JTextField();
    buttonSearch = new JButton();
    panelListControl = new JPanel();
    buttonAddSong = new JButton();
    buttonDeleteSong = new JButton();
    scrollPane = new JScrollPane();
    lstPlayList = new JList();
    panelProgress = new JPanel();
    labelSongName = new JLabel();
    sliderProgress = new JSlider();
    panelVolume = new JPanel();
    toggleButtonMute = new JToggleButton();
    slideVolume = new JSlider();
    panelPlayerControl = new JPanel();
    buttonPreviousSong = new JButton();
    buttonPlaySong = new JButton();
    buttonPauseSong = new JButton();
    buttonStopSong = new JButton();
    buttonNextSong = new JButton();
    fileChooser = new JFileChooser();
    popupMenuPlaylist = new JPopupMenu();
    menuItemPopUpAdd = new JMenuItem();
    menuItemPopUpClear = new JMenuItem();
    menuItemPopUpDelete = new JMenuItem();
    menuItemPopUpOpenPlaylist = new JMenuItem();

    //======== this ========
    setMaximizedBounds(new Rectangle(8, 8, 8, 8));
    setMinimumSize(new Dimension(310, 480));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getResource("/images/ic_play.png")).getImage());
    Container contentPane = getContentPane();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

    //======== menuBar ========
    {

      //======== menu1 ========
      {
        menu1.setText(bundle.getString("form.menu_file"));
        menu1.setPreferredSize(new Dimension(32, 24));

        //---- menuItemOpenPlaylist ----
        menuItemOpenPlaylist.setText(bundle.getString("form.playlist_open"));
        menuItemOpenPlaylist.setIcon(new ImageIcon(getClass().getResource("/images/ic_open.png")));
        menuItemOpenPlaylist.addActionListener(e -> menuItemOpenPlaylistActionPerformed(e));
        menu1.add(menuItemOpenPlaylist);

        //---- menuItemSavePlaylist ----
        menuItemSavePlaylist.setText(bundle.getString("form.playlist_save"));
        menuItemSavePlaylist.setIcon(new ImageIcon(getClass().getResource("/images/ic_save.png")));
        menuItemSavePlaylist.addActionListener(e -> menuItemSavePlaylistActionPerformed(e));
        menu1.add(menuItemSavePlaylist);
        menu1.addSeparator();

        //---- menuItemExit ----
        menuItemExit.setText(bundle.getString("form.exit"));
        menuItemExit.setIcon(new ImageIcon(getClass().getResource("/images/ic_exit.png")));
        menuItemExit.addActionListener(e -> menuItemExitActionPerformed(e));
        menu1.add(menuItemExit);
      }
      menuBar.add(menu1);

      //======== menu2 ========
      {
        menu2.setText(bundle.getString("form.menu_service"));
        menu2.setPreferredSize(new Dimension(48, 24));

        //======== menu3 ========
        {
          menu3.setText(bundle.getString("form.themes"));
          menu3.setIcon(new ImageIcon(getClass().getResource("/images/ic_settings.png")));
          menu3.setPreferredSize(new Dimension(102, 24));

          //---- menuItemSkin1 ----
          menuItemSkin1.setText(bundle.getString("form.skin1"));
          menuItemSkin1.addActionListener(e -> menuItemSkin1ActionPerformed(e));
          menu3.add(menuItemSkin1);

          //---- menuItemSkin2 ----
          menuItemSkin2.setText(bundle.getString("form.skin2"));
          menuItemSkin2.addActionListener(e -> menuItemSkin2ActionPerformed(e));
          menu3.add(menuItemSkin2);
        }
        menu2.add(menu3);
      }
      menuBar.add(menu2);
    }
    setJMenuBar(menuBar);

    //======== panelSearch ========
    {
      panelSearch.setBorder(new EmptyBorder(8, 8, 8, 8));
      panelSearch.setLayout(new BoxLayout(panelSearch, BoxLayout.X_AXIS));

      //---- txtSearch ----
      txtSearch.setMaximumSize(new Dimension(5008, 24));
      txtSearch.setToolTipText(bundle.getString("form.search_song"));
      txtSearch.setFont(new Font("sansserif", Font.ITALIC, 12));
      txtSearch.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
          textFieldSearchFocusGained(e);
        }

        @Override
        public void focusLost(FocusEvent e) {
          textFieldSearchFocusLost(e);
        }
      });
      txtSearch.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          txtSearchKeyPressed(e);
        }
      });
      panelSearch.add(txtSearch);

      //---- buttonSearch ----
      buttonSearch.setText(bundle.getString("form.find"));
      buttonSearch.setIcon(new ImageIcon(getClass().getResource("/images/ic_search.png")));
      buttonSearch.setMinimumSize(new Dimension(24, 24));
      buttonSearch.addActionListener(e -> buttonSearchActionPerformed(e));
      panelSearch.add(buttonSearch);
    }
    contentPane.add(panelSearch);

    //======== panelListControl ========
    {
      panelListControl.setBorder(new EmptyBorder(8, 8, 8, 8));
      panelListControl.setLayout(new BoxLayout(panelListControl, BoxLayout.X_AXIS));

      //---- buttonAddSong ----
      buttonAddSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_add.png")));
      buttonAddSong.setMinimumSize(new Dimension(24, 24));
      buttonAddSong.setMaximumSize(new Dimension(120, 24));
      buttonAddSong.setToolTipText(bundle.getString("form.add"));
      buttonAddSong.addActionListener(e -> buttonAddSongActionPerformed(e));
      panelListControl.add(buttonAddSong);

      //---- buttonDeleteSong ----
      buttonDeleteSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_remove.png")));
      buttonDeleteSong.setMinimumSize(new Dimension(24, 24));
      buttonDeleteSong.setMaximumSize(new Dimension(120, 24));
      buttonDeleteSong.setToolTipText(bundle.getString("form.delete"));
      buttonDeleteSong.addActionListener(e -> buttonDeleteSongActionPerformed(e));
      panelListControl.add(buttonDeleteSong);
    }
    contentPane.add(panelListControl);

    //======== scrollPane ========
    {
      scrollPane.setViewportView(lstPlayList);
    }
    contentPane.add(scrollPane);

    //======== panelProgress ========
    {
      panelProgress.setBorder(new EmptyBorder(0, 8, 0, 8));
      panelProgress.setLayout(new BoxLayout(panelProgress, BoxLayout.Y_AXIS));

      //---- labelSongName ----
      labelSongName.setText(bundle.getString("form.song_name"));
      labelSongName.setHorizontalAlignment(SwingConstants.CENTER);
      panelProgress.add(labelSongName);

      //---- sliderProgress ----
      sliderProgress.setMaximum(1000);
      sliderProgress.setValue(0);
      sliderProgress.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          sliderProgressMousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
          sliderProgressMouseReleased(e);
        }
      });
      panelProgress.add(sliderProgress);
    }
    contentPane.add(panelProgress);

    //======== panelVolume ========
    {
      panelVolume.setBorder(new EmptyBorder(0, 8, 0, 8));
      panelVolume.setLayout(new BoxLayout(panelVolume, BoxLayout.X_AXIS));

      //---- toggleButtonMute ----
      toggleButtonMute.setIcon(new ImageIcon(getClass().getResource("/images/ic_speaker.png")));
      toggleButtonMute
          .setSelectedIcon(new ImageIcon(getClass().getResource("/images/ic_speaker_mute.png")));
      toggleButtonMute.setMaximumSize(new Dimension(26, 26));
      toggleButtonMute.setMinimumSize(new Dimension(26, 26));
      toggleButtonMute.setPreferredSize(new Dimension(26, 26));
      toggleButtonMute.setForeground(UIManager.getColor("FormattedTextField.foreground"));
      toggleButtonMute.setBackground(UIManager.getColor("Button.background"));
      toggleButtonMute.setBorder(null);
      toggleButtonMute.setBorderPainted(false);
      toggleButtonMute.setToolTipText(bundle.getString("form.mute"));
      toggleButtonMute.addActionListener(e -> toggleButtonMuteActionPerformed(e));
      panelVolume.add(toggleButtonMute);

      //---- slideVolume ----
      slideVolume.setToolTipText(bundle.getString("form.volume"));
      slideVolume.setValue(20);
      slideVolume.setMinorTickSpacing(5);
      slideVolume.addChangeListener(e -> {
        slideVolumeStateChanged(e);
        slideVolumeStateChanged(e);
      });
      panelVolume.add(slideVolume);
    }
    contentPane.add(panelVolume);

    //======== panelPlayerControl ========
    {
      panelPlayerControl.setBorder(new EmptyBorder(8, 8, 8, 8));
      panelPlayerControl.setLayout(new BoxLayout(panelPlayerControl, BoxLayout.X_AXIS));

      //---- buttonPreviousSong ----
      buttonPreviousSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_previous.png")));
      buttonPreviousSong.setMinimumSize(new Dimension(24, 24));
      buttonPreviousSong.setToolTipText(bundle.getString("form.previous"));
      buttonPreviousSong.setMaximumSize(new Dimension(120, 24));
      buttonPreviousSong.addActionListener(e -> buttonPreviousSongActionPerformed(e));
      panelPlayerControl.add(buttonPreviousSong);

      //---- buttonPlaySong ----
      buttonPlaySong.setIcon(new ImageIcon(getClass().getResource("/images/ic_play.png")));
      buttonPlaySong.setMinimumSize(new Dimension(24, 24));
      buttonPlaySong.setMaximumSize(new Dimension(120, 24));
      buttonPlaySong.addActionListener(e -> {
        buttonPlaySongActionPerformed(e);
        buttonPlaySongActionPerformed(e);
      });
      panelPlayerControl.add(buttonPlaySong);

      //---- buttonPauseSong ----
      buttonPauseSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_pause.png")));
      buttonPauseSong.setMinimumSize(new Dimension(24, 24));
      buttonPauseSong.setToolTipText(bundle.getString("form.pause"));
      buttonPauseSong.setMaximumSize(new Dimension(120, 24));
      buttonPauseSong.addActionListener(e -> {
        buttonStopSongActionPerformed(e);
        buttonPauseSongActionPerformed(e);
      });
      panelPlayerControl.add(buttonPauseSong);

      //---- buttonStopSong ----
      buttonStopSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_stop.png")));
      buttonStopSong.setMinimumSize(new Dimension(24, 24));
      buttonStopSong.setToolTipText(bundle.getString("form.stop"));
      buttonStopSong.setMaximumSize(new Dimension(120, 24));
      buttonStopSong.addActionListener(e -> buttonStopSongActionPerformed(e));
      panelPlayerControl.add(buttonStopSong);

      //---- buttonNextSong ----
      buttonNextSong.setIcon(new ImageIcon(getClass().getResource("/images/ic_next.png")));
      buttonNextSong.setMinimumSize(new Dimension(24, 24));
      buttonNextSong.setToolTipText(bundle.getString("form.next"));
      buttonNextSong.setMaximumSize(new Dimension(120, 24));
      buttonNextSong.addActionListener(e -> buttonNextSongActionPerformed(e));
      panelPlayerControl.add(buttonNextSong);
    }
    contentPane.add(panelPlayerControl);
    pack();
    setLocationRelativeTo(getOwner());

    //---- fileChooser ----
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setMultiSelectionEnabled(true);

    //======== popupMenuPlaylist ========
    {

      //---- menuItemPopUpAdd ----
      menuItemPopUpAdd.setText(bundle.getString("form.add"));
      menuItemPopUpAdd.addActionListener(e -> menuItemPopUpAddActionPerformed(e));
      popupMenuPlaylist.add(menuItemPopUpAdd);

      //---- menuItemPopUpClear ----
      menuItemPopUpClear.setText(bundle.getString("form.clear"));
      menuItemPopUpClear.addActionListener(e -> menuItemPopUpClearActionPerformed(e));
      popupMenuPlaylist.add(menuItemPopUpClear);

      //---- menuItemPopUpDelete ----
      menuItemPopUpDelete.setText(bundle.getString("form.delete"));
      menuItemPopUpDelete.addActionListener(e -> menuItemPopUpDeleteActionPerformed(e));
      popupMenuPlaylist.add(menuItemPopUpDelete);

      //---- menuItemPopUpOpenPlaylist ----
      menuItemPopUpOpenPlaylist.setText(bundle.getString("form.playlist_open"));
      menuItemPopUpOpenPlaylist.addActionListener(e -> menuItemPopUpOpenPlaylistActionPerformed(e));
      popupMenuPlaylist.add(menuItemPopUpOpenPlaylist);
    }
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JMenuBar menuBar;
  private JMenu menu1;
  private JMenuItem menuItemOpenPlaylist;
  private JMenuItem menuItemSavePlaylist;
  private JMenuItem menuItemExit;
  private JMenu menu2;
  private JMenu menu3;
  private JMenuItem menuItemSkin1;
  private JMenuItem menuItemSkin2;
  private JPanel panelSearch;
  private JTextField txtSearch;
  private JButton buttonSearch;
  private JPanel panelListControl;
  private JButton buttonAddSong;
  private JButton buttonDeleteSong;
  private JScrollPane scrollPane;
  private JList lstPlayList;
  private JPanel panelProgress;
  private JLabel labelSongName;
  private JSlider sliderProgress;
  private JPanel panelVolume;
  private JToggleButton toggleButtonMute;
  private JSlider slideVolume;
  private JPanel panelPlayerControl;
  private JButton buttonPreviousSong;
  private JButton buttonPlaySong;
  private JButton buttonPauseSong;
  private JButton buttonStopSong;
  private JButton buttonNextSong;
  private JFileChooser fileChooser;
  private JPopupMenu popupMenuPlaylist;
  private JMenuItem menuItemPopUpAdd;
  private JMenuItem menuItemPopUpClear;
  private JMenuItem menuItemPopUpDelete;
  private JMenuItem menuItemPopUpOpenPlaylist;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
