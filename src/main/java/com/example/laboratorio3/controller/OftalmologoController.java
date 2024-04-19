package com.example.laboratorio3.controller;

import com.example.laboratorio3.entity.Clinica;
import com.example.laboratorio3.entity.Oftalmologo;
import com.example.laboratorio3.entity.Paciente;
import com.example.laboratorio3.repository.OftalmologoRepository;
import com.example.laboratorio3.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/oftalmologo")
public class OftalmologoController {
    final OftalmologoRepository oftalmologoRepository;
    final PacienteRepository pacienteRepository;

    public OftalmologoController(OftalmologoRepository oftalmologoRepository, PacienteRepository pacienteRepository){
        this.oftalmologoRepository = oftalmologoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping(value = "")
    public String listarOftalmologos(Model model){
        List<Oftalmologo> listaOftalmologos = oftalmologoRepository.listarTodosLosOftalmologos();
        model.addAttribute("listaOftalmologos",listaOftalmologos);
        return "oftalmologo";
    }

    @GetMapping(value = "/mostrarPacientes")
    public String listarPacientes(Model model, @RequestParam(value = "id")int id){
        List<Paciente> listaPacientes = pacienteRepository.listarPacientesPorOftalmologo(id);
        Oftalmologo oftalmologoActual = oftalmologoRepository.obtenerOftalmologoPorId(id);
        model.addAttribute("oftalmologoActual",oftalmologoActual);
        model.addAttribute("listaPacientes",listaPacientes);
        return "pacientesPorOftalmologo";
    }

    @GetMapping(value = "/proximasCitas")
    public String listarProximasCitas(Model model, @RequestParam(value = "id")int id){
        List<Paciente> listaPacientes = pacienteRepository.listarPacientesPorAtender(id);
        Oftalmologo oftalmologoActual = oftalmologoRepository.obtenerOftalmologoPorId(id);
        model.addAttribute("oftalmologoActual",oftalmologoActual);
        model.addAttribute("listaPacientes",listaPacientes);
        return "proximasCitas";
    }
}
