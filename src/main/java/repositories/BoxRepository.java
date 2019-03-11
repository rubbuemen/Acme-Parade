
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Actor a join a.boxes b where b.name LIKE 'Out box' and a.id = ?1")
	Box findOutBoxByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b where b.name LIKE 'Spam box' and a.id = ?1")
	Box findSpamBoxByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b where b.name LIKE 'In box' and a.id = ?1")
	Box findInBoxByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b where b.name LIKE 'Trash box' and a.id = ?1")
	Box findTrashBoxByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b where b.name LIKE 'Notification box' and a.id = ?1")
	Box findNotificationBoxByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b where b.parentBox is null and a.id = ?1")
	Collection<Box> findRootBoxesByActorId(int actorId);

	@Query("select b from Actor a join a.boxes b join b.messages m where m.id = ?1 and a.id = ?2")
	Box findBoxFromMessageIdActorLogged(int messageId, int actorId);

	@Query("select b from Actor a join a.boxes b where a.id = ?1")
	Collection<Box> findBoxesActorLogged(int actorId);

	@Query("select b from Box b join b.messages m where m.id = ?1")
	Collection<Box> findBoxesByMessageId(int messageId);
}
