package com.comohogar.ClientesBack.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comohogar.ClientesBack.model.Cliente;
import com.comohogar.ClientesBack.service.ClienteService;

@RestController
@RequestMapping(path = "/cliente")
public class ClienteResource {
	
	private final ClienteService clienteService;

	@Autowired
	public ClienteResource(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@PostMapping("/add")
	public ResponseEntity<Cliente> addCliente(@RequestBody Cliente cliente){
		Cliente client = clienteService.addCliente(cliente);
		return new ResponseEntity<>(client, HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Cliente>> getAllCliente(){
		List<Cliente> list = clienteService.findClientes();
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
	
}
