package de.proskor.fel;

public interface Entity {
	public String getName();
	public int getId();
	public String getGuid();
	public String getDescription();
	public String getAuthor();
	public void setDescription(String description);
	public void setAuthor(String author);
	public String getQualifiedName();
}
