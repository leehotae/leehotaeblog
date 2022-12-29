package com.example.demo.Image;

import java.io.IOException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.example.demo.CommonDto.ResponseDto;
import com.example.demo.Image.Dto.ImageUploadResponseDto;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/image")
@RestController
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;

	
	@PostMapping("")
	public  ResponseEntity<?> saveImage(MultipartHttpServletRequest request) throws IllegalStateException, IOException
	{
		MultipartFile imageFile=request.getFile("image");
		String imageUrl=imageService.이미지저장(imageFile);
		return new ResponseEntity<>(new ResponseDto<>("이미지 저장완료",new ImageUploadResponseDto(imageUrl)),
				HttpStatus.OK);
	}
	
}
