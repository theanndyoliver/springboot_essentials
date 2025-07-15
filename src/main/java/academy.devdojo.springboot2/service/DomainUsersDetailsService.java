package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.DomainUsers;
import academy.devdojo.springboot2.repository.DomainUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DomainUsersDetailsService implements UserDetailsService {

    private final DomainUsersRepository domainUsersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(domainUsersRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("DomainUsers name not found!"));
    }
}
