package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.services.FilesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilesController {

    private final FilesStorageService filesStorageService;

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = filesStorageService.load(filename);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
