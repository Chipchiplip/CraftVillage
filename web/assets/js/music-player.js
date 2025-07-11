// Bộ điều khiển nhạc có sử dụng localStorage để lưu trạng thái
let musicPlaying = localStorage.getItem('musicPlaying') !== 'false';
let audio = null;

function initMusicPlayer() {
    // Tạo phần tử audio nếu chưa tồn tại
    if (!audio) {
        audio = new Audio('music/background.mp3');
        audio.loop = true;
        audio.volume = 0.5;
        
        // Thử tiếp tục từ vị trí cuối nếu có thể
        const lastPosition = parseFloat(localStorage.getItem('musicPosition') || '0');
        if (lastPosition > 0 && !isNaN(lastPosition)) {
            audio.currentTime = lastPosition;
        }
        
        // Chỉ phát nếu nhạc đang phát trước đó
        if (musicPlaying) {
            const playPromise = audio.play();
            
            if (playPromise !== undefined) {
                playPromise.catch(error => {
                    // Tự động phát bị ngăn chặn, đặt trạng thái nhạc thành tạm dừng
                    musicPlaying = false;
                    localStorage.setItem('musicPlaying', 'false');
                    updateMusicButton();
                });
            }
        }
        
        // Lưu vị trí hiện tại định kỳ
        setInterval(() => {
            if (audio && !audio.paused) {
                localStorage.setItem('musicPosition', audio.currentTime.toString());
            }
        }, 1000);
    }
    
    // Cập nhật trạng thái nút
    updateMusicButton();
}

function toggleMusic() {
    if (!audio) return;
    
    if (musicPlaying) {
        audio.pause();
        musicPlaying = false;
    } else {
        audio.play();
        musicPlaying = true;
    }
    
    localStorage.setItem('musicPlaying', musicPlaying.toString());
    updateMusicButton();
}

function updateMusicButton() {
    const musicBtn = document.getElementById('musicToggleBtn');
    if (musicBtn) {
        if (musicPlaying) {
            musicBtn.innerHTML = '<i class="fas fa-music"></i>'; // Thay đổi icon thành biểu tượng nhạc
            musicBtn.title = "Tắt nhạc";
            musicBtn.classList.add('playing');
            musicBtn.classList.remove('muted');
        } else {
            musicBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
            musicBtn.title = "Bật nhạc";
            musicBtn.classList.remove('playing');
            musicBtn.classList.add('muted');
        }
    }
}

// Khởi tạo trình phát nhạc khi nội dung DOM được tải
document.addEventListener('DOMContentLoaded', initMusicPlayer);

// Xử lý các thay đổi trạng thái hiển thị trang
document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'hidden') {
        // Lưu vị trí khi người dùng rời trang
        if (audio && !audio.paused) {
            localStorage.setItem('musicPosition', audio.currentTime.toString());
        }
    }
});