package com.example.laboratorio3.repository;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Integer> {
    @Query(nativeQuery = true,value = "select * from paciente")
    List<Paciente> listarTodosLosPacientes();

    @Query(nativeQuery = true,value = "select * from paciente where clinica_id = ?1")
    List<Paciente> listarPacientesPorClinica(int id_clinica);

    @Query(nativeQuery = true,value = "select * from paciente where oftalmologo_id = ?1")
    List<Paciente> listarPacientesPorOftalmologo(int id_oftalmologo);

    @Query(nativeQuery = true,value = "select * from paciente where oftalmologo_id = ?1 and fecha_cita>now()")
    List<Paciente> listarPacientesPorAtender(int id_oftalmologo);
}
