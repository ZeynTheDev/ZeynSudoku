package com.zeynthedev.zeynsudoku;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

    @FXML private Label lblLeft1, lblLeft2, lblLeft3, lblLeft4, lblLeft5, lblLeft6, lblLeft7, lblLeft8, lblLeft9;
    @FXML private Label lblTimer, lblDifficulty, lblHintCount;
    @FXML private GridPane sudokuGrid;
    @FXML private VBox pauseOverlay;
    @FXML private javafx.scene.layout.Pane marqueePane;
    @FXML private Label lblMarquee;
    private javafx.animation.TranslateTransition marqueeTransition;

    private TextField[][] boardData = new TextField[9][9];
    private Timeline timeline;
    private int selected = -1;
    
    // --- MVC: INSTANS MODEL (Sang Otak) ---
    private SudokuGame game;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game = new SudokuGame(); // Inisialisasi Otak Game
        
        createSudokuBoard();
        
        if(GameState.isContinue && SaveManager.hasSave()) {
            loadGameData();
        } else {
            // --- LOGIKA BARU: LANGSUNG BUAT GAME DARI KURIR ---
            game.startNewGame(GameState.targetDifficulty);
            
            lblDifficulty.setText(game.getDifficulty().toUpperCase());
            updateHintUI();
            renderBoardUI();
            
            lblTimer.setText("00:00");
            startTimer();
//            System.out.println("Game is successfully loaded with difficulty: " + GameState.targetDifficulty);
        }
        
        if (pauseOverlay != null) {
            pauseOverlay.setOnMouseClicked(event -> actPause());
        }
        
        setupMarquee();
        AudioManager.getInstance().setOnTrackChange(() -> setupMarquee());
    }

    @FXML
    private void actMenu() {
        boolean wasRunning = (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING);
        if (wasRunning) timeline.pause();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ZeynSudoku");
        alert.setHeaderText("Return");
        alert.setContentText("Are you sure to return to main menu?");
        
        // --- SUNTIK TEMA KE DALAM DIALOG BAWAAN JAVAFX ---
        // 1. Ambil CSS dari layar utama, masukkan ke dialog
        alert.getDialogPane().getStylesheets().addAll(sudokuGrid.getScene().getStylesheets());
        // 2. Beri nama class khusus agar mudah kita target di file CSS
        alert.getDialogPane().getStyleClass().add("custom-alert");
        // -------------------------------------------------
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                saveCurrentGame();
                try {
                    App.setRoot("secondary");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (wasRunning) {
                timeline.play();
            }
        });
    }

    @FXML
    private void actNewGame() {
        // FUNGSI INI SEKARANG HANYA UNTUK TOMBOL "NEW GAME" DI KANAN LAYAR
        boolean wasRunning = (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING);
        if (wasRunning) timeline.pause();

        String difficulty = showDifficulty();
        
        if (difficulty != null) {
            game.startNewGame(difficulty);
            lblDifficulty.setText(game.getDifficulty().toUpperCase());
            updateHintUI();
            renderBoardUI();
            
            lblTimer.setText("00:00");
            startTimer();
//            System.out.println("Game is restarted with new difficulty!");
        } else {
            // Jika Cancel, cukup lanjutkan timer game yang sedang berjalan
            if (wasRunning) timeline.play();
        }
    }

    @FXML
    private void actPause() {
        if (timeline != null) {
            if (timeline.getStatus() == Timeline.Status.RUNNING) {
                timeline.pause();
                pauseOverlay.setVisible(true);
            } else {
                timeline.play();
                pauseOverlay.setVisible(false);
            }
        }
    }

    @FXML
    private void actReset() {
        boolean wasRunning = (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING);
        if (wasRunning) timeline.pause();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Confirmation");
        alert.setHeaderText("Reset Puzzle?");
        alert.setContentText("All your answers will be erased. Are you sure?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                game.resetBoard(); // Model yang menghapus datanya
                renderBoardUI();
//                System.out.println("Puzzle is reset.");
            }
            if (wasRunning) timeline.play();
        });
    }

    @FXML
    private void actHint() {
        // Tanya Model apakah hint masih bisa digunakan
        if (game.useHint()) {
            AudioManager.getInstance().playSfxPlace();
            updateHintUI();
            renderBoardUI();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ZeynSudoku");
            alert.setHeaderText(null);
            alert.setContentText("Your hint all are already used!");
            alert.show();
        }
    }

    @FXML
    private void actChooseNum(ActionEvent event) {
        Node clickedBtn = (Node) event.getSource();
        selected = Integer.parseInt(clickedBtn.getUserData().toString());
//        System.out.println("Num inputter changed to: " + (selected == 0 ? "Delete" : selected));
        renderBoardUI();
    }
    
    private void createSudokuBoard() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextField box = new TextField();
                boardData[row][column] = box;

                box.setPrefSize(45, 45);
                box.setAlignment(Pos.CENTER);
                box.setFont(Font.font("System", 18));
                box.setEditable(false);
                box.setFocusTraversable(false);

                // Variabel final untuk dipakai di dalam Lambda (Event Handler)
                final int r = row;
                final int c = column;

                box.setOnMouseClicked(event -> {
                    if (game.isClue(r, c) || selected == -1) return;

                    int currentNum = game.getNumAt(r, c);
                    
                    // Logika isi/hapus
                    if (selected == 0 || currentNum == selected) {
                        game.placeNum(r, c, 0); 
                        // Putar suara ketik biasa saat menghapus angka
                        AudioManager.getInstance().playSfxPlace(); 
                    } else {
                        game.placeNum(r, c, selected);
                        
                        // --- CEK ERROR UNTUK MEMILIH SFX YANG TEPAT ---
                        if (game.isError(r, c)) {
                            AudioManager.getInstance().playSfxError(); // Suara salah (bentrok)
                        } else {
                            AudioManager.getInstance().playSfxPlace(); // Suara taruh angka aman
                        }
                    }
                    

                    renderBoardUI();
                    
                    // Auto Switch Logic
                    if (selected >= 1 && selected <= 9 && game.getLeftNum(selected) == 0) {
                        for (int i = 1; i <= 9; i++) {
                            int nextNum = (selected + i - 1) % 9 + 1;
                            if (game.getLeftNum(nextNum) > 0) {
                                selected = nextNum;
//                                System.out.println("Auto-switch to num " + selected);
                                renderBoardUI();
                                break;
                            }
                        }
                    }
                });

                sudokuGrid.add(box, column, row);
            }
        }
    }

    private void renderBoardUI() {
        // --- BLOK 1: UPDATE VISUAL KOTAK (Hanya bertanya ke Model) ---
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextField box = boardData[r][c];
                int num = game.getNumAt(r, c);
                boolean isClue = game.isClue(r, c);
                boolean isError = game.isError(r, c);

                box.setText(num == 0 ? "" : String.valueOf(num));

                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == 8) ? 3 : 1;
                int right = (c == 8) ? 3 : 1;
                String styleLayout = "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + "; -fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 0; ";
                box.setStyle(styleLayout);
                
                box.getStyleClass().removeAll("sudoku-cell", "cell-clue", "cell-user", "cell-error", "cell-selected");
                
                box.getStyleClass().add("sudoku-cell");

                boolean isSelectedNum = (num != 0 && selected != -1 && num == selected);
                
                if (isError) box.getStyleClass().add("cell-error"); 
                else if (isSelectedNum) box.getStyleClass().add("cell-selected");
                
                
                if (isClue) box.getStyleClass().add("cell-clue");
                else box.getStyleClass().add("cell-user");
            }
        }

        // --- BLOK 2: UPDATE INDIKATOR SISA ANGKA ---
        if (lblLeft1 != null) {
            lblLeft1.setText(String.valueOf(game.getLeftNum(1)));
            lblLeft2.setText(String.valueOf(game.getLeftNum(2)));
            lblLeft3.setText(String.valueOf(game.getLeftNum(3)));
            lblLeft4.setText(String.valueOf(game.getLeftNum(4)));
            lblLeft5.setText(String.valueOf(game.getLeftNum(5)));
            lblLeft6.setText(String.valueOf(game.getLeftNum(6)));
            lblLeft7.setText(String.valueOf(game.getLeftNum(7)));
            lblLeft8.setText(String.valueOf(game.getLeftNum(8)));
            lblLeft9.setText(String.valueOf(game.getLeftNum(9)));
        }
        
        // --- BLOK 3: CEK MENANG ---
        if (game.isGameWon()) {
            javafx.application.Platform.runLater(this::winPopUp);
        }
    }
    
    private void winPopUp() {
        if (timeline != null) timeline.stop();
        
        // --- PUTAR SUARA MENANG! ---
        AudioManager.getInstance().playSfxWin();
        // ---------------------------
        
        // --- RECORD STATS BEFORE EXIT ---
        int hintsUsed = game.getHintTotal() - game.getHintLeft();
        StatisticManager.recordWin(
            game.getDifficulty(), 
            game.getSecondsRun(), 
            hintsUsed, 
            game.getErrorMadeCount(), 
            game.getHintLeft()
        );
        // -------------------------------------
        
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ZeynSudoku");
        alert.setHeaderText("🎉 CONGRATULATION! 🎉");
        alert.setContentText("You've solved this puzzle!");
        
        // --- SUNTIK TEMA KE DALAM DIALOG BAWAAN JAVAFX ---
        // 1. Ambil CSS dari layar utama, masukkan ke dialog
        alert.getDialogPane().getStylesheets().addAll(sudokuGrid.getScene().getStylesheets());
        // 2. Beri nama class khusus agar mudah kita target di file CSS
        alert.getDialogPane().getStyleClass().add("custom-alert");
        // -------------------------------------------------
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SaveManager.deleteSave();
                try { App.setRoot("secondary"); } 
                catch (IOException ex) { ex.printStackTrace(); }
            }
        });
    }
    
    private void startTimer() {
        if (timeline != null) timeline.stop();
        
        int min = game.getSecondsRun() / 60;
        int sec = game.getSecondsRun() % 60;
        lblTimer.setText(String.format("%02d:%02d", min, sec));
        
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            game.setSecondsRun(game.getSecondsRun() + 1); // Tambah waktu di Model
            
            int minute = game.getSecondsRun() / 60;
            int second = game.getSecondsRun() % 60;
            lblTimer.setText(String.format("%02d:%02d", minute, second));
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    private void updateHintUI(){
        if (lblHintCount != null) {
            lblHintCount.setText(game.getHintLeft() + "/" + game.getHintTotal());
        }
    }
    
    private void saveCurrentGame() {
        // Ambil datanya langsung dari Model
        SaveManager.saveGame(
            game.getDifficulty(), 
            game.getSecondsRun(), 
            game.getHintLeft(), 
            game.getBoard(), 
            game.getClues(), 
            game.getSolution()
        );
//        System.out.println("Game successfully saved!");
    }
    
    private void loadGameData() {
        Properties props = SaveManager.loadGame();
        if (props == null) return;
        
        game.loadGame(props); // Lempar properties ke Model untuk diurai
        
        lblDifficulty.setText(game.getDifficulty().toUpperCase());
        updateHintUI();
        renderBoardUI();
        startTimer();
//        System.out.println("Game berhasil di-load!");
    }

    private String showDifficulty() {
        final String[] result = {null};
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        // 1. Buang setStyle, ganti dengan styleClass
        layout.getStyleClass().add("dialog-root"); 
        
        Label title = new Label("Choose The Difficulty");
        // 2. Buang setStyle, ganti dengan styleClass
        title.getStyleClass().add("dialog-title"); 
        
        Button btnEasy = new Button("Easy (10 Hint)");
        Button btnMedium = new Button("Medium (6 Hint)");
        Button btnHard = new Button("Hard (3 Hint)");
        Button btnCancel = new Button("Cancel");
        
        btnEasy.setPrefWidth(150); btnMedium.setPrefWidth(150); btnHard.setPrefWidth(150);
        
        btnEasy.setOnAction(e -> { result[0] = "Easy"; dialog.close(); });
        btnMedium.setOnAction(e -> { result[0] = "Medium"; dialog.close(); });
        btnHard.setOnAction(e -> { result[0] = "Hard"; dialog.close(); });
        btnCancel.setOnAction(e -> { dialog.close(); });
        
        layout.getChildren().addAll(title, btnEasy, btnMedium, btnHard, btnCancel);
        
        Scene scene = new Scene(layout);
        
        // --- 3. SUNTIKKAN TEMA KE JENDELA POPUP INI ---
        java.util.Properties config = ConfigManager.loadConfig();
        String theme = config.getProperty("theme", "Light").toLowerCase().replace(" ", "");
        java.net.URL cssUrl = App.class.getResource("css/" + theme + ".css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        // ----------------------------------------------
        
        dialog.setScene(scene);
        dialog.showAndWait();
        
        return result[0];
    }
    
    // --- FUNGSI BGM IN-GAME ---
    private void setupMarquee() {
        if (lblMarquee == null || marqueePane == null) return;
        
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(130, 25);
        marqueePane.setClip(clip);
        
        String trackName = AudioManager.getInstance().getCurrentTrackName();
        lblMarquee.setText("NOW PLAYING: " + trackName + "   ***   ");
        
        if (marqueeTransition != null) {
            marqueeTransition.stop();
        }
        
        // --- TRIK JITU: MENGGUNAKAN TEKS BAYANGAN UNTUK MENGUKUR ---
        javafx.scene.text.Text shadowText = new javafx.scene.text.Text(lblMarquee.getText());
        shadowText.setFont(lblMarquee.getFont()); // Samakan font-nya
        
        // Objek Text tidak akan pernah berbohong soal panjang aslinya!
        double textWidth = shadowText.getLayoutBounds().getWidth(); 
        // -------------------------------------------------------------
        
        // --- LOGIKA KECEPATAN STABIL ---
        double speed = 40.0; // Kecepatan jalan: 40 pixel per detik
        double distance = 130 + textWidth + 20; // Jarak tempuh total
        double seconds = distance / speed; // Waktu tempuh dinamis
        
        marqueeTransition = new javafx.animation.TranslateTransition(Duration.seconds(seconds), lblMarquee);
        marqueeTransition.setFromX(130); 
        marqueeTransition.setToX(-textWidth - 20); // Pastikan teks benar-benar hilang ke kiri
        
        marqueeTransition.setCycleCount(javafx.animation.Animation.INDEFINITE);
        marqueeTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        marqueeTransition.play();
    }

    @FXML
    private void actNextBGM() {
        AudioManager.getInstance().nextTrack();
    }

    @FXML
    private void actPrevBGM() {
        AudioManager.getInstance().prevTrack();
    }
}