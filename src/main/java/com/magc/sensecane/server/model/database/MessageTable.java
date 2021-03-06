package com.magc.sensecane.server.model.database;

import com.magc.sensecane.framework.model.database.TableEntity;
import com.magc.sensecane.framework.model.database.annotation.Autogenerated;
import com.magc.sensecane.framework.model.database.annotation.Column;
import com.magc.sensecane.framework.model.database.annotation.PrimaryKey;
import com.magc.sensecane.framework.model.database.annotation.Table;

@Table("message")
public class MessageTable extends TableEntity<Integer> {

	@PrimaryKey @Column("id") @Autogenerated private final Integer id;
	@Column("user_from") private final Integer userFrom;
	@Column("user_to") private final Integer userTo;
	@Column("timestamp") private final Long timestamp;
	@Column("message") private final String message;
	
	public MessageTable() {
		this(null, null, null, null, null);
	}
	
	public MessageTable(Integer id, Integer from, Integer to, Long timestamp, String message) {
		super();
		this.id = id;
		this.userFrom = from;
		this.userTo = to;
		this.timestamp = timestamp;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public Integer getUserFrom() {
		return userFrom;
	}

	public Integer getUserTo() {
		return userTo;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}
}
