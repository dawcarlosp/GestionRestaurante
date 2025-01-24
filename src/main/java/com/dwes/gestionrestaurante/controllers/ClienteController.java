package com.dwes.gestionrestaurante.controllers;

import com.dwes.gestionrestaurante.entities.Cliente;
import com.dwes.gestionrestaurante.repositories.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    /**
     * Actualizar un cliente existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody @Valid Cliente clienteDetalles) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteDetalles.getNombre());
                    cliente.setEmail(clienteDetalles.getEmail());
                    cliente.setReservas(clienteDetalles.getReservas());
                    Cliente clienteActualizado = clienteRepository.save(cliente);
                    return ResponseEntity.ok(clienteActualizado); // HTTP 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found
    }
    /**
     * Obtener todos los clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes); // HTTP 200 OK
    }
    /**
     * Obtener un cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente)) // HTTP 200 OK
                .orElse(ResponseEntity.notFound().build());   // HTTP 404 Not Found
    }
    /**
     * Crear un nuevo cliente
     */
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody @Valid Cliente cliente) {
        Cliente nuevoCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente); // HTTP 201 Created
    }
    /**
     * Borrar un cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().build(); // HTTP 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // HTTP 404 Not Found
    }
}
