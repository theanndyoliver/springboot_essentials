package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.DomainUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  DomainUsersRepository extends JpaRepository<DomainUsers,Long> {

    DomainUsers findByUsername(String username);
}
