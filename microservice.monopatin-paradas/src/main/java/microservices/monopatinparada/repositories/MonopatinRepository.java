package microservices.monopatinparada.repositories;

import microservices.monopatinparada.models.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    @Query("SELECT m FROM Monopatin m WHERE m.id = :id")
    Monopatin findMonopatinById(@Param("id") Long id);
}
