package com.comohogar.ClientesBack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comohogar.ClientesBack.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	@Query("SELECT c FROM Cliente c WHERE c.name = ?1")
	List<Cliente> findClienteByName(String name);
	
	@Query("SELECT c FROM Cliente c WHERE c.grupo= ?1")
	List<Cliente> findClienteByNameGrupo(String grupo);

}
