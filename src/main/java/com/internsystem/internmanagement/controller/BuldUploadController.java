package com.internsystem.internmanagement.controller;

import com.internsystem.internmanagement.service.BulkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bulk-upload")
@CrossOrigin(origins = "*")
public class BuldUploadController {

    @Autowired
    private BulkUploadService bulkUploadService;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        bulkUploadService.processFile(file);
        return ResponseEntity.ok("Bulk upload successful");
    }
}