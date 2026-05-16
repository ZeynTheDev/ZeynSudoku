# Changelog - ZeynSudoku

> [!NOTE]
> Latest Changelog: [v2.0](#changelog---zeynsudoku-v20)

This changelog is added on v2.0 and will be used as the application update documentation from its creation.

*Log perubahan ini ditambahkan pada v2.0 dan akan digunakan sebagai dokumentasi pembaruan aplikasi ini sejak pembuatan dokumen ini.*

## Changelog - ZeynSudoku v2.0
### [Added] - Fitur Baru
1. **Dynamic Theming Engine**: Implementing CSS-based theming system that separates UI logic with visual style.<br/>
***Mesin Pengatur Tema Dinamis**: Mengimplementasikan sistem tema berbasis CSS yang memisahkan logika UI dengan gaya visual.*

2. **6 New Built-in Themes**: Add Light, Mono Dark, Classic Dark, Vampire, Cappuccino, and Nature theme choices that can be applied real-time via Settings.<br/>
***6 Tema Bawaan Baru**: Menambahkan pilihan tema Light, Mono Dark, Classic Dark, Vampire, Cappuccino, dan Nature yang bisa diganti secara real-time melalui Settings.*

3. **Interactive Credits Page**: Add new Credits page with split-layout and Horizontal Scrolling feature to access the BGM composer list. Featured links are also added for users that want to access materials used in this application's development.<br/>
***Halaman Kredit Interaktif**: Menambahkan halaman Credits baru dengan layout terbelah (split-layout) dan fitur Horizontal Scrolling untuk daftar komposer BGM. Tautan pendukung juga ditambahkan bagi pengguna yang ingin mengakses material yang digunakan dalam pengembangan aplikasi ini.*

4. **OS-Level Hyperlink Integration**: Hyperlink on Credits page are now integrated via `java.awt.Desktop` so they can trigger the default OS browser to open the link.<br/>
***Integrasi Hyperlink pada Level OS**:Tautan di halaman Credits sekarang terintegrasi dengan `java.awt.Desktop` sehingga langsung membuka browser default sistem operasi untuk membuka alamat tautan.*

5. **CI/CD Pipeline:** Add release automation using GitHub Actions to build Fat JAR and native installers (`.exe` for Windows, `.dmg` for macOS) using JPackage with the official vendor name "Zeyn The Dev".<br/>
***Alur CI/CD**: Menambahkan otomatisasi rilis menggunakan GitHub Actions untuk mem- build Fat JAR dan installer native (`.exe` untuk Windows, `.dmg` untuk macOS) menggunakan JPackage dengan vendor name resmi "Zeyn The Dev".*

### [Changed/Improved] - Peningkatan UI/UX
1. **Complete UI Overhaul**: Cleaned up all inline-style codes (hardcoded) in FXML files and replaced them with centralized `styleClass`.<br/>
***Perombakan UI Menyeluruh**:Membersihkan seluruh kode inline-style (hardcode) di file FXML dan menggantinya dengan `styleClass` yang terpusat.*

2. **Custom Alert Dialogs**: Default JavaFX dialog windows (such as Reset confirmation, Main Menu, and Win message) are now injected with CSS so their shapes and colors blend with the active theme.<br/>
***Dialog Peringatan Kustom**: Jendela dialog bawaan JavaFX (seperti konfirmasi Reset, Main Menu, dan pesan Menang) kini telah diinjeksi dengan CSS agar bentuk dan warnanya menyatu dengan tema yang sedang aktif.*

3. **Hall of Records Redesign**: Changed the color scheme and layout structure on the Records page so the text no longer camouflages with the background during Dark Mode.<br/>
***Desain Ulang Hall of Records**: Mengubah skema warna dan struktur layout pada halaman Records agar teks tidak lagi berkamuflase dengan background saat mode gelap (Dark Mode).*

4. **Custom Scrollbars**: Replaced the rigid default JavaFX scrollbar design with a modern, transparent, and minimalist version across all themes.<br/>
***Scrollbar Kustom**: Mengganti desain scrollbar bawaan JavaFX yang kaku menjadi versi modern, transparan, dan minimalis di seluruh tema.*

5. **TextFlow Wrapping**: Implemented `TextFlow` on the UI to ensure long text and links can wrap neatly without breaching the container box limits.<br/>
***Pembungkusan TextFlow**: Mengimplementasikan `TextFlow` pada UI untuk memastikan teks dan tautan yang panjang dapat turun baris (wrap) dengan rapi tanpa menembus batas kotak kontainer.*

### [Fixed] - Perbaikan Bug
1. **Empty Board Glitch**: Fixed a navigation flow bug where pressing "Cancel" on the difficulty selection dialog from the Main Menu would trap the player on an empty board screen. (Selection logic moved to `SecondaryController`).<br/>
***Glitch Papan Kosong**: Memperbaiki bug alur navigasi di mana menekan "Cancel" pada dialog pemilihan tingkat kesulitan dari Main Menu akan membuat pemain terjebak di layar papan yang kosong. (Logika pemilihan dipindah ke `SecondaryController`).*

2. **Marquee Text Calculation Bug**: Fixed the BGM player where the song title text was cut off and the animation repeated too quickly. Resolved using the Shadow Text technique to measure the absolute pixel width of the text.<br/>
***Bug Kalkulasi Teks Berjalan (Marquee)**: Memperbaiki pemutar BGM di mana teks judul lagu terpotong dan animasi mengulang terlalu cepat. Diselesaikan dengan teknik Shadow Text untuk mengukur lebar piksel teks secara absolut.*

3. **Cell Highlight Override**: Fixed a CSS logic bug where clue numbers turned faded gray and lost their status when highlighted/selected by the player.<br/>
***Timpaan Sorotan Kotak**: Memperbaiki bug CSS logika di mana angka petunjuk (clue) berubah menjadi warna abu-abu pudar dan kehilangan statusnya saat disorot/dipilih oleh pemain.*

4. **First Load Theme Bug**: Fixed an issue where the theme was not immediately applied when the application was first launched (First Load).<br/>
***Bug Tema Muatan Pertama**: Memperbaiki isu di mana tema tidak langsung teraplikasikan saat aplikasi pertama kali dijalankan (First Load).*