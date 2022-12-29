package com.example.demo.Image;

import java.io.File;
import java.io.IOException;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {


	@Value("${file.path}")
	private String uploadFolder;
	
	public String 이미지저장(MultipartFile imageFile) throws IllegalStateException, IOException
	{
		

UUID uuid=UUID.randomUUID();

String imageFileName=uuid.toString();

if (imageFile.getOriginalFilename()!=null)
{
imageFileName+="_"+imageFile.getOriginalFilename();
		
}
File file=new File(uploadFolder+imageFileName);

imageFile.transferTo(file);
		
		
		return imageFileName;
		
	}
}
