package restui.model;

public class Card {

	private Integer number;

	public Card(Integer number) {
		super();
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Card [number=" + number + "]";
	}

}
