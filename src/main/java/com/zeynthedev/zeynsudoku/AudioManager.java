/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.zeynthedev.zeynsudoku;

import java.net.URL;
import java.util.Properties;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Zeyn
 */
public class AudioManager {
    private static AudioManager instance;
    
    private MediaPlayer bgmPlayer;
    private AudioClip sfxPlaceNum;
    private AudioClip sfxWin;
    private AudioClip sfxMatch; //used if player can fill an entire area (3x3), a row, or column
    private AudioClip sfxError;
    
    //variable for playlist
    private int currentPack = -1;
    private int currentTrackIndex = 0;
    
    //variable for marquee
    private Runnable onTrackChange;

    public void setOnTrackChange(Runnable callback) {
        this.onTrackChange = callback;
    }
    
    //playlist-database
    private final String[][] playlists = {
        //pack 0: itch io bgm
        {"bgm/basic/track_1.mp3", "bgm/basic/track_2.mp3", "bgm/basic/track_6.mp3", "bgm/basic/track_7.mp3", "bgm/basic/track_10.mp3"},
        // Pack 1: Classic
        {"bgm/classic/air_on_g_string.mp3", "bgm/classic/clair_de_lune.mp3", "bgm/classic/gymnopédie_no.1.mp3", "bgm/classic/nocturne_op.9_no.2", "bgm/classic/rêverie"},
        // Pack 2: Touhou
        {"bgm/touhou/beloved_tomboyish_girl_(slowed).mp3", "bgm/touhou/if_the_sky_clears_(cafe_de_touhou).mp3", "bgm/touhou/lullaby_of_deserted_hell_(orchestra_arrange).mp3", "bgm/touhou/the_gensokyo_the_gods_loved_(re-extended).mp3", "bgm/touhou/tiny_little_adiantum_(instrumental).mp3"},
        // Pack 3: Custom / TBA
        {"bgm/custom_1.mp3"}
    };
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    private AudioManager() { loadSFX(); }
    
    private void loadSFX() {
        try {
            URL placeUrl = getClass().getResource("sfx/place.mp3");
            if (placeUrl != null) sfxPlaceNum = new AudioClip(placeUrl.toString());
            
            URL winUrl = getClass().getResource("sfx/win.mp3");
            if (winUrl != null) sfxWin = new AudioClip(winUrl.toString());
            
            URL matchUrl = getClass().getResource("sfx.match.mp3");
            if (matchUrl != null) sfxMatch = new AudioClip(matchUrl.toString());

            URL errorUrl = getClass().getResource("sfx/error.mp3");
            if (errorUrl != null) sfxError = new AudioClip(errorUrl.toString());
        } catch (Exception e) {
//            System.out.println("SFX not found!");
        }
    }
    
    public void playBGM() {
        Properties config = ConfigManager.loadConfig();
        int pack = Integer.parseInt(config.getProperty("bgmPack", "0"));
        
        // 1. Cek apakah pack lagunya berubah dari sebelumnya
        if (pack != currentPack) { 
            currentPack = pack;
            currentTrackIndex = 0;
            playTrack(); // Mulai lagu dari awal karena pack baru
        } 
        // 2. Cek apakah lagu belum diputar sama sekali (saat baru buka aplikasi)
        else if (bgmPlayer == null) {
            playTrack();
        }
        
        // 3. Jika pack sama dan lagu sudah jalan, kita abaikan pemanggilan playTrack() 
        // agar lagu mengalun tanpa terputus. Tapi kita tetap pastikan volumenya sinkron 
        // dengan file .dat (berjaga-jaga jika user mengubah volume lalu menekan Apply/Cancel).
        if (bgmPlayer != null) {
            double volume = Double.parseDouble(config.getProperty("bgmVolume", "50")) / 100.0;
            bgmPlayer.setVolume(volume);
        }
    }
    
    private void playTrack() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
        }
        
        if (currentPack < 0 || currentPack >= playlists.length) return;
        if (currentTrackIndex < 0 || currentTrackIndex >= playlists[currentPack].length) return;
        
        String path = playlists[currentPack][currentTrackIndex];
        
        try {
            URL fileUrl = getClass().getResource(path);
            if (fileUrl == null) {
//                System.out.println("File BGM belum dimasukkan: " + path);
                return;
            }

            Media media = new Media(fileUrl.toString());
            bgmPlayer = new MediaPlayer(media);
            
            double volume = Double.parseDouble(ConfigManager.loadConfig().getProperty("bgmVolume", "50")) / 100.0;
            bgmPlayer.setVolume(volume);

            // LOGIKA AUTO-NEXT: Saat lagu habis, langsung panggil lagu berikutnya
            bgmPlayer.setOnEndOfMedia(() -> {
                nextTrack();
            });

            bgmPlayer.play();
//            System.out.println("Memutar BGM: " + path);
            
            if (onTrackChange != null) {
                javafx.application.Platform.runLater(onTrackChange);
            }
        } catch (Exception e) {
//            System.out.println("Gagal memutar BGM: " + e.getMessage());
        }
    }
    
    public void nextTrack() {
        if (currentPack == -1) return;
        currentTrackIndex++;
        
        // If reach end track of the pack, return to the first (Playlist Loop)
        if (currentTrackIndex >= playlists[currentPack].length) {
            currentTrackIndex = 0; 
        }
        playTrack();
    }
    
    public void prevTrack() {
        if (currentPack == -1) return;
        currentTrackIndex--;
        
        // If in first track, move to the last track of the pack
        if (currentTrackIndex < 0) {
            currentTrackIndex = playlists[currentPack].length - 1; 
        }
        playTrack();
    }
    
    public String getCurrentTrackName() {
        if (currentPack >= 0 && currentPack < playlists.length) {
            String fullPath = playlists[currentPack][currentTrackIndex];
            //making audio bgm_touhou.mp3 as BGM Touhou to be shown on marquee
            String fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
            return fileName.replace(".mp3", "").replace("_", " ").toUpperCase();
        }
        return "NO TRACK PLAYING";
    }
    
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public void setBgmVolume(double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume / 100.0);
        }
    }
    
    public void playSfxPlace() {
        if (sfxPlaceNum != null) {
            double vol = Double.parseDouble(ConfigManager.loadConfig().getProperty("sfxVolume", "70")) / 100.0;
            sfxPlaceNum.play(vol);
        }
    }
    
    public void playSfxWin() {
        if (sfxWin != null) {
            double vol = Double.parseDouble(ConfigManager.loadConfig().getProperty("sfxVolume", "70")) / 100.0;
            sfxWin.play(vol);
        }
    }
    
    public void playSfxMatch() {
        if (sfxMatch != null) {
            double vol = Double.parseDouble(ConfigManager.loadConfig().getProperty("sfxVolume", "70"))/ 100.0;
            sfxMatch.play(vol);
        }
    }

    public void playSfxError() {
        if (sfxError != null) {
            double vol = Double.parseDouble(ConfigManager.loadConfig().getProperty("sfxVolume", "70")) / 100.0;
            sfxError.play(vol);
        }
    }
}
