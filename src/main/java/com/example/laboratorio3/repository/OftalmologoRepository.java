package com.example.laboratorio3.repository;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.entity.Oftalmologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OftalmologoRepository extends JpaRepository<Oftalmologo,Integer> {
    @Query(nativeQuery = true,value = "select * from oftalmologo")
    List<Oftalmologo> listarTodosLosOftalmologos();

    @Query(nativeQuery = true,value = "select * from oftalmologo where clinica_id=?1")
    List<Oftalmologo> listarOftalmologosPorClinica(int id_clinica);

    @Query(nativeQuery = true,value = "select * from oftalmologo where id=?1")
    Oftalmologo obtenerOftalmologoPorId(int id);
}
