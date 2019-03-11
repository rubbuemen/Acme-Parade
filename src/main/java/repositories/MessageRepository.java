
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select distinct m from Actor a join a.boxes b join b.messages m where a.id = ?1")
	Collection<Message> findMessagesByActorId(int actorId);

	@Query("select distinct m from Actor a join a.boxes b join b.messages m where m.sender.id = ?1")
	Collection<Message> findMessagesSentByActorId(int actorId);
}
