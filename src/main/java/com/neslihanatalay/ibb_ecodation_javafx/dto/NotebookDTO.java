package com.neslihanatalay.ibb_ecodation_javafx.dto;

import lombok.*;
import com.neslihanatalay.ibb_ecodation_javafx.utils.ECategory;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotebookDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private ECategory category; // Örn: "Kişisel", "İş", "Okul"
    private boolean pinned;  // Sabitlenmiş not mu?
    private Integer userId;

    public NotebookDTO(Integer id, String title, String content, LocalDate createdDate, LocalDate updatedDate, ECategory category, Boolean pinned, Integer userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
	this.updatedDate = updatedDate;
        this.category = category;
	this.pinned = pinned;
	this.userId = userId;
    }
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	
	public LocalDate getUpdateDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public ECategory getCategory() {
		return category;
	}
	
	public void setCategory(ECategory category) {
		this.category = category;
	}
	
	public Boolean getPinned() {
		return pinned;
	}
	
	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
