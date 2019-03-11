
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
	"nameEnglish", "nameSpanish"
}))
public class PositionBrotherhood extends DomainEntity {

	// Attributes
	private String	nameEnglish;
	private String	nameSpanish;


	// Getters and Setters
	@NotBlank
	@Column(unique = true)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNameEnglish() {
		return this.nameEnglish;
	}

	public void setNameEnglish(final String nameEnglish) {
		this.nameEnglish = nameEnglish;
	}

	@NotBlank
	@Column(unique = true)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNameSpanish() {
		return this.nameSpanish;
	}

	public void setNameSpanish(final String nameSpanish) {
		this.nameSpanish = nameSpanish;
	}
}
