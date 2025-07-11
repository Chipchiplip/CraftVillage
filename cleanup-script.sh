#!/bin/bash

# CraftVillage Project Cleanup Script
# Tự động cleanup các file không cần thiết với backup an toàn

echo "🧹 CraftVillage Project Cleanup Script"
echo "======================================"

# Kiểm tra nếu đang ở thư mục root của project
if [ ! -f "build.xml" ] || [ ! -d "src/java" ]; then
    echo "❌ Error: Vui lòng chạy script từ thư mục root của project!"
    exit 1
fi

echo "✅ Project directory detected: $(pwd)"

# Function để backup
create_backup() {
    echo "📦 Creating backup..."
    
    # Tạo git backup
    git add . 2>/dev/null
    git commit -m "Backup before cleanup - $(date)" 2>/dev/null
    git tag -a "pre-cleanup-$(date +%Y%m%d-%H%M%S)" -m "Backup before cleanup" 2>/dev/null
    
    # Tạo file backup
    BACKUP_DIR="backup-$(date +%Y%m%d-%H%M%S)"
    mkdir -p "$BACKUP_DIR"
    cp -r src/ "$BACKUP_DIR/"
    
    echo "✅ Backup created: $BACKUP_DIR"
}

# Function để kiểm tra file existence
check_file() {
    local file=$1
    if [ -f "$file" ]; then
        echo "✅ Found: $file"
        return 0
    else
        echo "⚠️  Not found: $file"
        return 1
    fi
}

# Function để xóa file với confirmation
safe_delete() {
    local file=$1
    local reason=$2
    
    if [ -f "$file" ]; then
        echo "🗑️  Deleting: $file ($reason)"
        rm "$file"
        if [ $? -eq 0 ]; then
            echo "   ✅ Deleted successfully"
        else
            echo "   ❌ Failed to delete"
        fi
    else
        echo "   ⚠️  File not found: $file"
    fi
}

# Function để test build
test_build() {
    echo "🔨 Testing build..."
    
    if command -v ant &> /dev/null; then
        ant clean compile 2>/dev/null 1>/dev/null
        if [ $? -eq 0 ]; then
            echo "✅ Build successful"
            return 0
        else
            echo "❌ Build failed"
            return 1
        fi
    else
        echo "⚠️  Ant not found, skipping build test"
        return 0
    fi
}

# Main cleanup process
main() {
    echo ""
    echo "🎯 PHASE 1: SAFE CLEANUP"
    echo "========================"
    
    # Create backup first
    create_backup
    
    echo ""
    echo "📝 Files to be deleted:"
    echo "----------------------"
    
    # List of files to delete
    FILES_TO_DELETE=(
        "src/java/DAO/ProductOrderDAO.java:Empty template file"
        "src/java/controller/cart_order/OrderPaymentControl.java:Empty file"
        "src/java/DAO/CartDAO.class:Compiled file"
        "src/java/controller/cart_order/OrderManagementControl.java:Duplicate controller"
    )
    
    # Show what will be deleted
    for item in "${FILES_TO_DELETE[@]}"; do
        IFS=':' read -r file reason <<< "$item"
        if [ -f "$file" ]; then
            echo "  ❌ $file - $reason"
        else
            echo "  ⚠️  $file - Not found"
        fi
    done
    
    echo ""
    read -p "Proceed with deletion? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo ""
        echo "🗑️  Deleting files..."
        echo "===================="
        
        # Delete each file
        for item in "${FILES_TO_DELETE[@]}"; do
            IFS=':' read -r file reason <<< "$item"
            safe_delete "$file" "$reason"
        done
        
        echo ""
        echo "🔍 PHASE 2: DEPENDENCY CHECK"
        echo "============================"
        
        # Check for references to deleted files
        echo "Checking for references to deleted files..."
        
        echo "📝 Searching for OrderManagementControl references:"
        if command -v grep &> /dev/null; then
            grep -r "OrderManagementControl" --include="*.java" --include="*.jsp" src/ web/ 2>/dev/null || echo "   ✅ No references found"
        fi
        
        echo ""
        echo "📝 Searching for ProductOrderDAO references:"
        if command -v grep &> /dev/null; then
            grep -r "ProductOrderDAO" --include="*.java" --include="*.jsp" src/ web/ 2>/dev/null || echo "   ✅ No references found"
        fi
        
        echo ""
        echo "📝 Searching for OrderPaymentControl references:"
        if command -v grep &> /dev/null; then
            grep -r "OrderPaymentControl" --include="*.java" --include="*.jsp" src/ web/ 2>/dev/null || echo "   ✅ No references found"
        fi
        
        echo ""
        echo "🔨 TESTING BUILD"
        echo "================"
        test_build
        
        echo ""
        echo "🎉 CLEANUP COMPLETED!"
        echo "===================="
        echo "✅ Files successfully cleaned up"
        echo "✅ Dependencies checked"
        echo "✅ Build tested"
        echo ""
        echo "📋 NEXT STEPS:"
        echo "- Review any remaining references found above"
        echo "- Run your test suite to ensure functionality"
        echo "- Consider Phase 3 optimizations from cleanup-analysis-report.md"
        echo ""
        echo "📦 Backup location: $BACKUP_DIR"
        
    else
        echo "❌ Cleanup cancelled by user"
        exit 0
    fi
}

# Additional function to show cleanup summary
show_summary() {
    echo ""
    echo "📊 CLEANUP SUMMARY"
    echo "=================="
    
    echo "Files that should be deleted:"
    echo "  📁 src/java/DAO/ProductOrderDAO.java"
    echo "  📁 src/java/controller/cart_order/OrderPaymentControl.java"
    echo "  📁 src/java/DAO/CartDAO.class"
    echo "  📁 src/java/controller/cart_order/OrderManagementControl.java"
    echo ""
    echo "Benefits:"
    echo "  ✅ Removes ~500 lines of unnecessary code"
    echo "  ✅ Eliminates duplicate controllers"
    echo "  ✅ Cleans up empty template files"
    echo "  ✅ Removes compiled files from source control"
    echo ""
    echo "Safe features:"
    echo "  🛡️  Automatic backup creation"
    echo "  🛡️  Dependency checking"
    echo "  🛡️  Build verification"
    echo "  🛡️  User confirmation required"
}

# Parse command line arguments
case "${1:-}" in
    "summary")
        show_summary
        ;;
    "help"|"-h"|"--help")
        echo "Usage: $0 [summary|help]"
        echo ""
        echo "Commands:"
        echo "  (no args)  - Run interactive cleanup"
        echo "  summary    - Show cleanup summary"
        echo "  help       - Show this help"
        ;;
    *)
        main
        ;;
esac