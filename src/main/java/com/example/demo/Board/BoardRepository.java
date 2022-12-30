package com.example.demo.Board;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board,Long>{

	

	Page<Board> findAll(Pageable pageable);
	
	

	
	
	@Query(value="SELECT * FROM board WHERE title LIKE %?1%",nativeQuery = true)
	Page<Board> findByTitle(String title,Pageable pageable);
}
