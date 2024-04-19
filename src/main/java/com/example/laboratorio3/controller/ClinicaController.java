package com.example.laboratorio3.controller;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.entity.Oftalmologo;
import com.example.laboratorio3.entity.Paciente;
import com.example.laboratorio3.repository.ClinicaRepository;
import com.example.laboratorio3.repository.OftalmologoRepository;
import com.example.laboratorio3.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/clinica")
public class ClinicaController {
    final ClinicaRepository clinicaRepository;
    final OftalmologoRepository oftalmologoRepository;
    final PacienteRepository pacienteRepository;

    public ClinicaController(ClinicaRepository clinicaRepository, OftalmologoRepository oftalmologoRepository, PacienteRepository pacienteRepository){
        this.clinicaRepository = clinicaRepository;
        this.oftalmologoRepository = oftalmologoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping(value = "")
    public String listarClinicas(Model model){
        List<Clinica> listaClinicas = clinicaRepository.listarTodasLasClinicas();
        model.addAttribute("listaClinicas",listaClinicas);
        return "clinica";
    }

    @GetMapping(value = "/mostrarOftalmologos")
    public String listarOftalmologos(Model model, @RequestParam(value = "id")int id){
        List<Oftalmologo> listaOftalmologos = oftalmologoRepository.listarOftalmologosPorClinica(id);
        Clinica clinicaActual = clinicaRepository.obtenerClinicaPorId(id);
        model.addAttribute("clinicaActual",clinicaActual);
        model.addAttribute("listaOftalmologos",listaOftalmologos);
        return "oftalmologosPorClinica";
    }

    @GetMapping(value = "/mostrarPacientes")
    public String listarPacientes(Model model, @RequestParam(value = "id")int id){
        List<Paciente> listaPacientes = pacienteRepository.listarPacientesPorClinica(id);
        Clinica clinicaActual = clinicaRepository.obtenerClinicaPorId(id);
        model.addAttribute("clinicaActual",clinicaActual);
        model.addAttribute("listaPacientes",listaPacientes);
        return "pacientesPorClinica";
    }
}
