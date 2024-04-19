package com.example.laboratorio3.repository;

import com.example.laboratorio3.entity.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica,Integer> {
    @Query(nativeQuery = true,value = "select * from clinica")
    List<Clinica> listarTodasLasClinicas();
}
