package com.example.Effdog_Cygnius_API;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
	
    // You can add custom queries if needed, e.g., find by name or email.

}
