package com.aidanduff.weatherdataapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.aidanduff.weatherdataapi.message.ResponseMessage;
import com.aidanduff.weatherdataapi.service.FilesStorageService;
import com.aidanduff.weatherdataapi.util.CSVReader;

@Controller
@CrossOrigin("http://localhost:9090")
public class FilesController {
	@Autowired
	FilesStorageService storageService;
	
	@Autowired
	CSVReader csvReader;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file);
			Resource savedFile = storageService.load(file.getOriginalFilename());
			csvReader.readData(savedFile.getFile());

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			e.printStackTrace();
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
}
