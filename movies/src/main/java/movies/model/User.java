package movies.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Map;

import static movies.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(name = "ClientUser", schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userName"})
public class User {

	static final String INVALID_USERNAME = "A valid username must be provided";

	@Id
	private long id;
	@Column(unique = true)
	private String userName;

	public User(long id, String userName) {
		this.id = id;
		this.userName = new NotBlankString(userName,
				INVALID_USERNAME).value();
	}


	public String userName() {
		return userName;
	}

	public boolean hasUsername(String aUserName) {
		return this.userName.equals(aUserName);
	}

	Long id() {
		return id;
	}
}
