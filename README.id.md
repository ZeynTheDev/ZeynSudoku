# Zeyn Sudoku

Read in: [English](README.md)

Zeyn Sudoku adalah permainan teka-teki Sudoku berbasis desktop native yang dibangun menggunakan Java dan JavaFX. Aplikasi ini dirancang dengan antarmuka yang bersih, fungsionalitas yang mulus, dan fitur setara dengan standar game profesional.

## Fitur Utama

- **Tingkat Kesulitan:** Tersedia tiga mode permainan (Easy, Medium, Hard) yang menentukan jumlah teka-teki awal dan kuota bantuan.
- **Sistem Hint Cerdas:** Bantuan otomatis untuk mengisi kotak kosong dengan jawaban yang benar, dengan kuota yang disesuaikan berdasarkan tingkat kesulitan (Easy: 10, Medium: 6, Hard: 3).
- **Deteksi Kesalahan Real-time:** Sistem otomatis menyorot kotak dengan warna merah jika mendeteksi angka yang bentrok di baris, kolom, atau area 3x3.
- **Manajemen Waktu:** Dilengkapi dengan timer yang menghitung waktu penyelesaian. Terdapat fitur Pause yang akan menutupi papan dengan layar buram (anti-cheat) saat diaktifkan.
- **Save & Continue:** Permainan akan disimpan secara otomatis saat pemain kembali ke Menu Utama. Pemain dapat melanjutkan permainan (Continue) tepat di posisi dan waktu terakhir mereka tinggalkan.
- **Auto-Switch Input:** Papan angka akan otomatis berpindah ke angka selanjutnya jika kuota angka yang sedang dipilih sudah habis terpakai di papan.

## Teknologi yang Digunakan

- **Bahasa:** Java 17
- **GUI Framework:** JavaFX 25
- **Build Tool:** Apache Maven
- **Arsitektur:** Pemisahan fungsionalitas UI (FXML), Controller, Game Logic, dan Manajemen File (I/O).

## Persyaratan Sistem

Untuk menjalankan atau melakukan kompilasi proyek ini secara lokal, pastikan sistem kamu memiliki:
- Java Development Kit (JDK) 17 atau lebih baru.
- Apache Maven terinstal dan terkonfigurasi di *environment variables*.

## Cara Menjalankan secara Lokal

1. Clone repositori ini ke komputer lokal kamu:
   ```bash
   git clone [https://github.com/username-kamu/ZeynSudoku.git](https://github.com/username-kamu/ZeynSudoku.git)
   ```
2. Masuk ke direktori proyek:
   ```bash
   cd ZeynSudoku
   ```
3. Jalankan aplikasi menggunakan Maven:
   ```bash
   mvn clean javafx:run
   ```

## Cara Membangun (Build) Executable

Proyek ini telah dikonfigurasi untuk dibungkus menjadi file executable (`.jar`, `.exe`, atau `.dmg`) menggunakan plugin Maven dan JPackage.

Untuk merakit file `.jar` mandiri (Fat JAR):
```bash
mvn clean package
```
File `.jar` akan tersedia di dalam folder `target/`. 

> [!NOTE]
> Repositori ini juga menggunakan GitHub Actions untuk secara otomatis merilis installer Windows `.exe` dan macOS `.dmg` di halaman Releases setiap kali ada tag versi baru.

## Lisensi
Repositori ini dilisensikan di bawah Lisensi MIT