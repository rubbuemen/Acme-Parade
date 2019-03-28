
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query("select h from History h where h.brotherhood.id = ?1")
	History findHistoryByBrotherhoodId(int brotherhoodId);

	@Query("select h from History h where ?1 member of h.periodRecords")
	History findHistoryByPeriodRecordId(int periodRecord);

	@Query("select h from History h where ?1 member of h.legalRecords")
	History findHistoryByLegalRecordId(int legalRecordId);

	@Query("select h from History h where ?1 member of h.linkRecords")
	History findHistoryByLinkRecordId(int linkRecordId);

	@Query("select h from History h where ?1 member of h.miscellaneousRecords")
	History findHistoryByMiscellaneousRecordId(int miscellaneousRecordId);

	@Query("select distinct h from History h join h.linkRecords lr where lr.brotherhood.id = ?1")
	Collection<History> findHistoriesByLinkRecordBrotherhoodId(int brotherhoodId);

}
