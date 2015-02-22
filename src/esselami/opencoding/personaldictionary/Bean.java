package esselami.opencoding.personaldictionary;

public class Bean {
	public int id;
	public String word;
	public String definition;
	public String status;
	
	public Bean(String word, String definition) {
		this.word = word;
		this.definition = definition;
	}
	
	public Bean(int id, String word, String definition, String status) {
		this.id = id;
		this.word = word;
		this.definition = definition;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Bean [word=" + word + ", definition=" + definition + "]";
	}
}
