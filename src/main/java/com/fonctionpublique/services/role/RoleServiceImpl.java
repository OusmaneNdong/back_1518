package com.fonctionpublique.services.role;

import com.fonctionpublique.entities.Profile;
import com.fonctionpublique.entities.Role;
import com.fonctionpublique.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl {

    private final RoleRepository roleRepository;
    @PostConstruct
    public void roleExisting() {

        int num = 2;

        roleRepository.findAllById(num).orElseGet(() -> {
            Role userRole = new Role();
            userRole.setRoleName("user");
            userRole.setDescriptionRole("user role par default");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setRoleName("admin");
            adminRole.setDescriptionRole("admininstrateur");
            roleRepository.save(adminRole);
            return null;
        });
    }
}
