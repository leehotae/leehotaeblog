package com.example.demo.Board;




import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.Comment.Comment;
import com.example.demo.User.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Board {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	@NotBlank
	private   String title;
	@NotBlank
	@Lob
	private  String text;
	private String username;
	private String userProfileImage;
	@OneToMany (mappedBy = "board",orphanRemoval = true, fetch=FetchType.LAZY)
	@javax.persistence.OrderBy("id")
	private List<Comment> comments;
	@CreationTimestamp
	private LocalDateTime createDate;
	private Long views;
	public Board(User user, String title, String text)
	{
		this.username=user.getUsername();
		this.userProfileImage=user.getProfileImageUrl();
		this.user=user;
		this.text=text;
		this.title=title;
		this.views=0L;
	}

	
}
