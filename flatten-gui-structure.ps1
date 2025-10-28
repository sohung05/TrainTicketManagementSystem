# Script: Flatten GUI structure - Move all GUI files to src/gui/
# Author: AI Assistant
# Purpose: Revert modular structure to flat structure for easy merge

Write-Host "=== FLATTEN GUI STRUCTURE ===" -ForegroundColor Cyan
Write-Host "Moving files from subfolders to src/gui/ ..." -ForegroundColor Yellow

$srcGui = "src\gui"

# 1. Move all .java and .form files from subfolders to src/gui/
$subfolders = @("banve", "main", "khachhang", "khuyenmai", "nhanvien", "thongke", "trave", "doive")

foreach ($folder in $subfolders) {
    $folderPath = Join-Path $srcGui $folder
    if (Test-Path $folderPath) {
        Write-Host "`nProcessing folder: $folder" -ForegroundColor Green
        
        $files = Get-ChildItem -Path $folderPath -Filter "*.java" -File
        $formFiles = Get-ChildItem -Path $folderPath -Filter "*.form" -File
        
        foreach ($file in ($files + $formFiles)) {
            $destPath = Join-Path $srcGui $file.Name
            
            # Skip if file already exists in destination (menu folder)
            if (Test-Path $destPath) {
                Write-Host "  SKIP: $($file.Name) (already exists)" -ForegroundColor Gray
                continue
            }
            
            # Move file
            Move-Item -Path $file.FullName -Destination $destPath -Force
            Write-Host "  MOVED: $($file.Name)" -ForegroundColor White
        }
    }
}

Write-Host "`n=== STEP 1 COMPLETE: Files moved ===" -ForegroundColor Cyan

# 2. Update package declarations in moved .java files
Write-Host "`nUpdating package declarations..." -ForegroundColor Yellow

$javaFiles = Get-ChildItem -Path $srcGui -Filter "*.java" -File

foreach ($file in $javaFiles) {
    $content = Get-Content $file.FullName -Raw
    
    # Replace package declarations
    $newContent = $content `
        -replace 'package gui\.banve;', 'package gui;' `
        -replace 'package gui\.main;', 'package gui;' `
        -replace 'package gui\.khachhang;', 'package gui;' `
        -replace 'package gui\.khuyenmai;', 'package gui;' `
        -replace 'package gui\.nhanvien;', 'package gui;' `
        -replace 'package gui\.thongke;', 'package gui;' `
        -replace 'package gui\.trave;', 'package gui;' `
        -replace 'package gui\.doive;', 'package gui;'
    
    if ($content -ne $newContent) {
        Set-Content -Path $file.FullName -Value $newContent -NoNewline
        Write-Host "  UPDATED: $($file.Name)" -ForegroundColor White
    }
}

Write-Host "`n=== STEP 2 COMPLETE: Package declarations updated ===" -ForegroundColor Cyan

# 3. Update all import statements in entire src/
Write-Host "`nUpdating import statements in all files..." -ForegroundColor Yellow

$allJavaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse -File

foreach ($file in $allJavaFiles) {
    $content = Get-Content $file.FullName -Raw
    
    # Replace import statements
    $newContent = $content `
        -replace 'import gui\.banve\.', 'import gui.' `
        -replace 'import gui\.main\.', 'import gui.' `
        -replace 'import gui\.khachhang\.', 'import gui.' `
        -replace 'import gui\.khuyenmai\.', 'import gui.' `
        -replace 'import gui\.nhanvien\.', 'import gui.' `
        -replace 'import gui\.thongke\.', 'import gui.' `
        -replace 'import gui\.trave\.', 'import gui.' `
        -replace 'import gui\.doive\.', 'import gui.'
    
    if ($content -ne $newContent) {
        Set-Content -Path $file.FullName -Value $newContent -NoNewline
        Write-Host "  UPDATED: $($file.FullName)" -ForegroundColor White
    }
}

Write-Host "`n=== STEP 3 COMPLETE: Import statements updated ===" -ForegroundColor Cyan

# 4. Remove empty subfolders
Write-Host "`nRemoving empty subfolders..." -ForegroundColor Yellow

foreach ($folder in $subfolders) {
    $folderPath = Join-Path $srcGui $folder
    if (Test-Path $folderPath) {
        $items = Get-ChildItem -Path $folderPath
        if ($items.Count -eq 0) {
            Remove-Item -Path $folderPath -Force
            Write-Host "  REMOVED: $folder/" -ForegroundColor White
        } else {
            Write-Host "  KEPT: $folder/ (not empty)" -ForegroundColor Gray
        }
    }
}

Write-Host "`n=== ALL DONE! ===" -ForegroundColor Green
Write-Host "Structure has been flattened successfully!" -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "1. Compile: javac -encoding UTF-8 -d build\classes -cp 'lib\*;build\classes' src\gui\*.java" -ForegroundColor White
Write-Host "2. Test your application" -ForegroundColor White
Write-Host "3. Commit: git add . && git commit -m 'Flatten GUI structure for merge'" -ForegroundColor White
Write-Host "4. Merge with main" -ForegroundColor White


