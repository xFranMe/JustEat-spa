package es.unex.pi.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {

	private long idr;
	private long idu;
	private String review;
	private int grade;
	
	public long getIdr() {
		return idr;
	}
	
	public void setIdr(long idr) {
		this.idr = idr;
	}
	
	public long getIdu() {
		return idu;
	}
	
	public void setIdu(long idu) {
		this.idu = idu;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public void setGrade(int grade) {
		this.grade = grade;
	}
}
