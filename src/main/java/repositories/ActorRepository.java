
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findActorByUserAccountId(int userAccountId);

	@Query("select a from Actor a where a.name like 'System'")
	Actor getSystemActor();

	@Query("select a from SocialProfile sp join sp.actor a where sp.id = ?1")
	Actor findActorBySocialProfileId(int socialProfileId);

	@Query("select a from Actor a where a.userAccount.statusAccount = 1 and (a.isSpammer = 1 or a.polarityScore < -0.5)")
	Collection<Actor> findActorsToBan();

	@Query("select a from Actor a where a.userAccount.statusAccount = 0")
	Collection<Actor> findActorsBanned();

}
