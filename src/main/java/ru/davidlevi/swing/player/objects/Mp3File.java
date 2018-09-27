package ru.davidlevi.swing.player.objects;

import ru.davidlevi.swing.player.utils.FileUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Mp3-файл
 *
 * @author David Levi
 */
public class Mp3File implements Serializable {
    private String name;
    private String path;
    private int duration;

    public Mp3File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mp3File))
            return false;
        Mp3File mp3File = (Mp3File) obj;
        return path.equals(mp3File.getPath());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.path);
        return hash;
    }

    /**
     * Для корректного отображения объекта Mp3File при добавлении в плейлист
     */
    @Override
    public String toString() {
        return FileUtils.getFileNameWithoutExtension(name);
    }
}
