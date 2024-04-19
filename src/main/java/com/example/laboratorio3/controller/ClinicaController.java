package com.example.laboratorio3.controller;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.repository.ClinicaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/clinica")
public class ClinicaController {
    final ClinicaRepository clinicaRepository;

    public ClinicaController(ClinicaRepository clinicaRepository){
        this.clinicaRepository = clinicaRepository;
    }

    @GetMapping(value = "")
    public String listarClinicas(Model model){
        List<Clinica> listaClinicas = clinicaRepository.listarTodasLasClinicas();
        model.addAttribute("listaClinicas",listaClinicas);
        return "clinica";
    }
}
