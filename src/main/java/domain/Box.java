
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name")
})
public class Box extends DomainEntity {

	// Attributes
	private String	name;
	private Boolean	isSystemBox;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public Boolean getIsSystemBox() {
		return this.isSystemBox;
	}

	public void setIsSystemBox(final Boolean isSystemBox) {
		this.isSystemBox = isSystemBox;
	}


	// Relationships
	private Collection<Message>	messages;
	private Box					parentBox;
	private Collection<Box>		childsBox;


	@Valid
	@EachNotNull
	@ManyToMany
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@ManyToOne(optional = true)
	public Box getParentBox() {
		return this.parentBox;
	}

	public void setParentBox(final Box parentBox) {
		this.parentBox = parentBox;
	}

	@Valid
	@EachNotNull
	@OneToMany(mappedBy = "parentBox")
	public Collection<Box> getChildsBox() {
		return this.childsBox;
	}

	public void setChildsBox(final Collection<Box> childsBox) {
		this.childsBox = childsBox;
	}

}
