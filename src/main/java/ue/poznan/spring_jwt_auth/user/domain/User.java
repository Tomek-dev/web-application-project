package ue.poznan.spring_jwt_auth.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ue.poznan.spring_jwt_auth.auth.roles.Role;
import ue.poznan.spring_jwt_auth.utils.BaseEntity;
import ue.poznan.spring_jwt_auth.workplace.domain.Workplace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    private String email;

    private String username;

    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Workplace> workplaces = new ArrayList<>();

    public UserDetails toUserDetails() {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return roles.stream()
                        .map((role) -> new SimpleGrantedAuthority(role.getAuthority()))
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return username;
            }
        };
    }
}
