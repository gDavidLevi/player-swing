package ru.davidlevi.swing.player.interfaces.implementation;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;
import javazoom.jlgui.basicplayer.*;
import ru.davidlevi.swing.player.interfaces.MainPlayCallbackListener;
import ru.davidlevi.swing.player.interfaces.Player;
import ru.davidlevi.swing.player.ui.MainFrame;
import ru.davidlevi.swing.player.utils.FileUtils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Реализация плеера  для проигрывания mp3 файлов
 *
 * @author David Levi
 */
public class MainPlayer implements Player {
    public static final String MP3_FILE_EXTENSION = "mp3";
    public static final String MP3_FILE_DESCRIPTION = "Файлы mp3";
    public static int MAX_VOLUME = 100;
    /*
     Библиотека com.googlecode.soundlibs:basicplayer:3.0.0.0
     */
    private BasicPlayer basicPlayer = new BasicPlayer();
    // Song
    private long songDuration; // длительность, сек.
    private int songLenghtBytes; // размер песни, байт
    private String songCurrentFileName; // текущая песня, имя файла
    private long songPassedSeconds; // прошло с начала проигрывания, сек.
    private double volumeCurrent; // текущая громкость

    /**
     * Конструктор
     *
     * @param listener объект, который будет получать callback'и их это класса
     */
    public MainPlayer(@NotNull final MainPlayCallbackListener listener) {
        /*
         Слушаем события плеера из библиотеки basicplayer.jar и отправляем callback'и через
         интерфейс MainPlayCallbackListener в класс MainFrame
         */
        basicPlayer.addBasicPlayerListener(new BasicPlayerListener() {
            @Override
            public void progress(int bytesRead, long microseconds, byte[] pcmData, Map properties) {
                float progress = -1.0f;
                if ((bytesRead > 0) && ((songDuration > 0)))
                    progress = bytesRead * 1.0f / songLenghtBytes * 1.0f;
                // Сколько секунд прошло
                songPassedSeconds = (long) (songDuration * progress);
                if (songDuration != 0) {
                    int length = Math.round(songPassedSeconds * 1000f / songDuration);
                    listener.callbackProcessScroll(length); // callback
                }
            }

            @Override
            public void opened(Object o, Map map) {
                // Вариант 1 определения mp3-тегов
                //System.out.println(map);
            /*
                {mp3.copyright=false,
                date=,
                mp3.framesize.bytes=1040,
                mp3.vbr=false,
                mp3.framerate.fps=38.28125,
                vbr=false,
                mp3.id3tag.track=0,
                bitrate=320000,
                title=Кудесник,
                mp3.channels=2,
                audio.channels=2,
                audio.framerate.fps=38.28125,
                duration=186227000,
                basicplayer.sourcedataline=com.sun.media.sound.DirectAudioDevice$DirectSDL@10b71a3a,
                mp3.bitrate.nominal.bps=320000,
                mp3.padding=false,
                mp3.version.mpeg=1,
                mp3.crc=false,
                mp3.original=false,
                mp3.frequency.hz=44100,
                mp3.length.bytes=7444058,
                audio.length.frames=7129,
                author=Зануда feat. Кажэ,
                album=,
                mp3.id3tag.v2=java.io.ByteArrayInputStream@69a783f5,
                mp3.vbr.scale=0,
                mp3.length.frames=7129,
                mp3.version.encoding=MPEG1L3,
                audio.length.bytes=7444058,
                audio.type=MP3,
                mp3.version.layer=3,
                mp3.id3tag.v2.version=4,
                audio.samplerate.hz=44100.0,
                mp3.header.pos=1123,
                comment=,
                mp3.mode=0}
             */
                // Вариант 2 определения mp3-тегов
//                AudioFileFormat audioFileFormat = null;
//                try {
//                    audioFileFormat = AudioSystem.getAudioFileFormat(new File(o.toString()));
//                } catch (UnsupportedAudioFileException | IOException e) {
//                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
//                }
                songDuration = (long) Math.round((((Long) map.get("duration"))) / 1000000f);
                songLenghtBytes = Math.round(((Integer) map.get("mp3.length.bytes")));
                // Если есть mp3 тег для имени, то берем его, если нет, то вытаскиваем название из имени файла
                String songName;
                if (map.get("title") != null)
                    songName = map.get("title").toString();
                else
                    songName = FileUtils.getFileNameWithoutExtension(new File(o.toString()).getName());
                // Если длинное название, то укоротить его
                if (songName.length() > 30)
                    songName = songName.substring(0, 30) + "...";
                listener.callbackPlayStarted(songName); // callback
            }

            @Override
            public void stateUpdated(BasicPlayerEvent event) {
                int state = event.getCode();
                if (state == BasicPlayerEvent.EOM)
                    listener.callbackPlayFinished(); // callback
            }

            @Override
            public void setController(BasicController basicController) {
            }
        });
    }


    @Override
    public void playerPlay(String fileName) {
        try {
            // Если включают ту же самую песню, которая была на паузе
            if (songCurrentFileName != null
                    && songCurrentFileName.equals(fileName)
                    && basicPlayer.getStatus() == BasicPlayer.PAUSED) {
                basicPlayer.resume();
                return;
            }
            File mp3File = new File(fileName);
            songCurrentFileName = fileName;
            basicPlayer.open(mp3File);
            basicPlayer.play();
            basicPlayer.setGain(volumeCurrent);
        } catch (BasicPlayerException e) {
            Logger.getLogger(MainPlayer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void playerStop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException e) {
            Logger.getLogger(MainPlayer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void playerPause() {
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException e) {
            Logger.getLogger(MainPlayer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Метод регулирует звук при проигрывании песни
     *
     * @param controlValue double
     */
    @Override
    public void playerSetVolume(double controlValue) {
        try {
            volumeCurrent = controlValue / MAX_VOLUME;
            basicPlayer.setGain(volumeCurrent);
        } catch (BasicPlayerException e) {
            Logger.getLogger(MainPlayer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Метод отвечает за перемотку песни
     *
     * @param rewindPosition double
     */
    @Override
    public void playerRewind(double rewindPosition) {
        try {
            long skipBytes = Math.round(songLenghtBytes * rewindPosition);
            basicPlayer.seek(skipBytes);
            basicPlayer.setGain(volumeCurrent);
        } catch (BasicPlayerException e) {
            Logger.getLogger(MainPlayer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
