package com.example.demo.Image.Dto;



import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ImageUploadResponseDto {

	private  String imageUrl;
	
	
	public ImageUploadResponseDto(String imageUrl)
	{
		this.imageUrl=imageUrl;
	}
	
	
}
