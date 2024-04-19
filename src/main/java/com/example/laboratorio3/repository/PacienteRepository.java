package com.example.laboratorio3.repository;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.entity.Oftalmologo;
import com.example.laboratorio3.entity.Paciente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(nativeQuery = true,value = "select * from paciente where id=?1")
    Paciente obtenerPacientePorId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update paciente set numero_habitacion=?1 where id=?2")
    void actualizarPaciente(int numero_habitacion, int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update paciente set oftalmologo_id=?1 where id=?2")
    void derivarPaciente(int nuevoOftalmologo, int id);
}
