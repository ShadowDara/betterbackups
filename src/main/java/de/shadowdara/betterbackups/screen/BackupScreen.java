package de.shadowdara.betterbackups.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.ConfirmScreen;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BackupScreen extends Screen {

    // Size of the Backup Folder
    private long totalBackupSize = 0L;

    private BackupListWidget listWidget;

    private final List<File> backups = new ArrayList<>();
    private static final File BACKUP_FOLDER = new File("backups"); // anpassen falls nötig

    // Class Konstruktor
    public BackupScreen() {
        super(Text.translatable("screen.betterbackups.title"));
    }

    @Override
    protected void init() {

        int listWidth = 420; // 👈 breiter machen (Standard ~300)
        int left = (this.width - listWidth) / 2;

        this.listWidget = new BackupListWidget(
                this.client,
                listWidth,
                this.height,
                40,                     // top
                this.height - 80,       // bottom
                24                      // entry height
        );

        // WICHTIG:
        //this.listWidget.setX(left);

        this.loadBackups();

        for (File file : backups) {
            if (file != null) {
                listWidget.addBackup(file);
            }
        }

        this.addSelectableChild(listWidget);

        // Back button
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.back"),
                b -> this.close()
        ).dimensions(this.width / 2 - 100, this.height - 70, 200, 20).build());

        // Open folder button
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("screen.betterbackups.openfolder"),
                b -> net.minecraft.util.Util.getOperatingSystem().open(BACKUP_FOLDER)
        ).dimensions(this.width / 2 - 100, this.height - 45, 200, 20).build());
    }

    private void loadBackups() {
        backups.clear();
        totalBackupSize = 0L;

        if (BACKUP_FOLDER.exists() && BACKUP_FOLDER.isDirectory()) {

            File[] files = BACKUP_FOLDER.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    long size = getFileSize(file);
                    totalBackupSize += size;

                    backups.add(file); // 👈 jetzt File speichern
                }
            } else {
                backups.add(null); // Marker für "leer"
            }

        } else {
            backups.add(null);
        }
    }

    // Get Backup FileSize
    public static long getFileSize(File file) {
        if (file.isFile()) {
            return file.length();
        } else if (file.isDirectory()) {
            long size = 0L;
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    size += getFileSize(child);
                }
            }
            return size;
        }
        return 0L;
    }

    // Convert Bytes into Human readable Count
    public static String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return new DecimalFormat("#.##").format(
                bytes / Math.pow(1024, exp)) + " " + pre + "B";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                15,
                0xFFFFFF
        );

        listWidget.render(context, mouseX, mouseY, delta);

        // Folder size bottom
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                "Folder Size: " + humanReadableByteCount(totalBackupSize),
                this.width / 2,
                this.height - 20,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
