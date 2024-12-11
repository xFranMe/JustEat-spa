package es.unex.pi.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrderDishes {

	private long ido;
	private long iddi;
	private int amount;
	
	public long getIdo() {
		return ido;
	}
	
	public void setIdo(long ido) {
		this.ido = ido;
	}
	
	public long getIddi() {
		return iddi;
	}
	
	public void setIddi(long iddi) {
		this.iddi = iddi;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
