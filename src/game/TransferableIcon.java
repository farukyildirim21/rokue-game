package game;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class TransferableIcon implements Transferable {
    private final ImageIcon icon;

    public static final DataFlavor ICON_FLAVOR = new DataFlavor(ImageIcon.class, "ImageIcon");

    public TransferableIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{ICON_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return ICON_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return icon;
    }
}

