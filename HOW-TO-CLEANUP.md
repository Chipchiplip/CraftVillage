# 🧹 Hướng dẫn Cleanup Dự án CraftVillage

## 📋 Tóm tắt nhanh

Bạn có **2 file** hỗ trợ cleanup dự án:

1. **`cleanup-analysis-report.md`** - Báo cáo phân tích chi tiết 📊
2. **`cleanup-script.sh`** - Script tự động cleanup 🤖

---

## 🚀 Cách sử dụng

### Bước 1: Đọc báo cáo phân tích
```bash
# Xem file báo cáo chi tiết
cat cleanup-analysis-report.md
```

### Bước 2: Chạy script cleanup
```bash
# Xem tóm tắt trước khi cleanup
./cleanup-script.sh summary

# Chạy cleanup (có backup tự động)
./cleanup-script.sh
```

### Bước 3: Kiểm tra kết quả
```bash
# Test build sau cleanup
ant clean compile

# Chạy test suite của bạn
cd web/test/
# Kiểm tra các JSP test pages
```

---

## ✅ Những gì script sẽ làm

### **PHASE 1: Safe Cleanup**
- 🗑️ Xóa `ProductOrderDAO.java` (file template trống)
- 🗑️ Xóa `OrderPaymentControl.java` (file trống)
- 🗑️ Xóa `CartDAO.class` (file compiled)
- 🗑️ Xóa `OrderManagementControl.java` (duplicate controller)

### **PHASE 2: Dependency Check**
- 🔍 Tìm kiếm references đến files đã xóa
- ⚠️ Cảnh báo nếu có dependency chưa update

### **PHASE 3: Build Test**
- 🔨 Test build project sau cleanup
- ✅ Đảm bảo không break code

---

## 🛡️ Tính năng an toàn

- **Auto Backup**: Git commit + file backup tự động
- **User Confirmation**: Hỏi xác nhận trước khi xóa
- **Dependency Check**: Kiểm tra references
- **Build Verification**: Test build sau cleanup

---

## 📊 Kết quả mong đợi

**Trước cleanup:**
- 30+ controllers, 23 DAOs, 35+ services
- ~15,000 lines of code

**Sau cleanup:**
- 28 controllers (-2), 21 DAOs (-2), 35 services
- ~14,500 lines of code (-500 lines rác)

---

## 🚨 Lưu ý quan trọng

1. **Backup được tạo tự động** - không lo mất code
2. **Chạy từ thư mục root** của project (nơi có `build.xml`)
3. **Test kỹ sau cleanup** - chạy test suite của bạn
4. **Chỉ xóa file an toàn** - không touch core functionality

---

## 🆘 Nếu có vấn đề

**Restore từ backup:**
```bash
# Restore từ git tag
git checkout pre-cleanup-YYYYMMDD-HHMMSS

# Hoặc restore từ backup folder
cp -r backup-YYYYMMDD-HHMMSS/src/* src/
```

**Get help:**
```bash
./cleanup-script.sh help
```

---

## ✨ Kết luận

Script này sẽ giúp bạn:
- ✅ Loại bỏ code rác an toàn
- ✅ Giữ nguyên 6 core features
- ✅ Làm sạch project structure
- ✅ Cải thiện maintainability

**Chúc bạn cleanup thành công! 🎉**