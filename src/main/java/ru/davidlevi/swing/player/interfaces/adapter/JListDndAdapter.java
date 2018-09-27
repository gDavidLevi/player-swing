package ru.davidlevi.swing.player.interfaces.adapter;

import ru.davidlevi.swing.player.objects.Mp3File;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * https://docs.oracle.com/javase/tutorial/uiswing/dnd/
 */
public class JListDndAdapter extends DropTargetAdapter {

  private JList playlist;

  public JListDndAdapter(JList playlist) {
    this.playlist = playlist;
  }

  @Override
  public void drop(DropTargetDropEvent event) {
    Transferable transferable = event.getTransferable();
    if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      event.acceptDrop(event.getDropAction());
      try {
        List<File> fileList = (List<File>) transferable
            .getTransferData(DataFlavor.javaFileListFlavor);
        if (fileList.size() > 0) {
          for (File file : fileList) {
            Mp3File mp3File = new Mp3File(file.getName(), file.getPath());
            ((DefaultListModel) playlist.getModel()).addElement(mp3File);
          }
          event.dropComplete(true);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      event.rejectDrop();
    }
  }
}
