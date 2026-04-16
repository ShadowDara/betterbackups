package de.shadowdara.betterbackups.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BackupScreen extends Screen {

    private final List<String> backups = new ArrayList<>();
    private static final File BACKUP_FOLDER = new File("backups"); // anpassen falls nötig

    public BackupScreen() {
        super(Text.translatable("screen.betterbackups.title"));
    }

    @Override
    protected void init() {
        // Back Button in the Backups Menu
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.back"),
                button -> this.close()
        ).dimensions(this.width / 2 - 100, this.height / 2 + 40,
                200, 20).build());

        // Open the Backup Folder
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("screen.betterbackups.openfolder"),
                button -> {
                    try {
                        if (!BACKUP_FOLDER.exists()) {
                            BACKUP_FOLDER.mkdirs();
                        }

                        net.minecraft.util.Util.getOperatingSystem().open(BACKUP_FOLDER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).dimensions(this.width / 2 - 100, this.height / 2 + 70,
                200, 20).build());

        // Backups einlesen
        this.loadBackups();
    }

    private void loadBackups() {
        backups.clear();


        // Print the Backup List or that no Backup was found
        if (BACKUP_FOLDER.exists() && BACKUP_FOLDER.isDirectory()) {

            File[] files = BACKUP_FOLDER.listFiles();

            if (files != null && files.length > 0) {
                for (File file : files) {
                    long size = getFileSize(file);
                    backups.add(file.getName() + " (" +
                            humanReadableByteCount(size) + ")");
                }
            } else {
                backups.add(Text.translatable(
                        "message.betterbackups.notfound").getString());
            }

        } else {
            backups.add(Text.translatable(
                    "message.betterbackups.notfound").getString());
        }
    }

    // Get Backup FileSize
    private long getFileSize(File file) {
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

    private String humanReadableByteCount(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return new DecimalFormat("#.##").format(
                bytes / Math.pow(1024, exp)) + " " + pre + "B";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, 20, 0xFFFFFF);

        // Backups rendern
        int y = 50;
        for (String backup : backups) {
            context.drawTextWithShadow(this.textRenderer, backup,
                    this.width / 2 - 100, y, 0xAAAAAA);
            y += 12; // Zeilenabstand
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
