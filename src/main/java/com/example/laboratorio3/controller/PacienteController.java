package com.example.laboratorio3.controller;

import com.example.laboratorio3.entity.Oftalmologo;
import com.example.laboratorio3.entity.Paciente;
import com.example.laboratorio3.repository.OftalmologoRepository;
import com.example.laboratorio3.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/paciente")
public class PacienteController {
    final PacienteRepository pacienteRepository;
    final OftalmologoRepository oftalmologoRepository;

    public PacienteController(PacienteRepository pacienteRepository, OftalmologoRepository oftalmologoRepository){
        this.pacienteRepository = pacienteRepository;
        this.oftalmologoRepository = oftalmologoRepository;
    }

    @GetMapping(value = "")
    public String listarPacientes(Model model){
        List<Paciente> listaPacientes = pacienteRepository.listarTodosLosPacientes();
        model.addAttribute("listaPacientes",listaPacientes);
        return "paciente";
    }

    @GetMapping(value = "/editarPaciente")
    public String editarPaciente(Model model, @RequestParam(value = "id")int id){
        Paciente paciente = pacienteRepository.obtenerPacientePorId(id);
        model.addAttribute("paciente",paciente);
        return "editarPaciente";
    }

    @PostMapping(value = "/guardar")
    public String guardarPaciente(Paciente paciente){
        pacienteRepository.actualizarPaciente(paciente.getNumeroHabitacion(),paciente.getId());
        return "redirect:/paciente";
    }

    @GetMapping(value = "/derivarPaciente")
    public String derivarPaciente(Model model, @RequestParam(value = "id")int id){
        Paciente paciente = pacienteRepository.obtenerPacientePorId(id);
        List<Oftalmologo> listaOftalmologos = oftalmologoRepository.listarTodosLosOftalmologos();
        model.addAttribute("paciente",paciente);
        model.addAttribute("listaOftalmologos",listaOftalmologos);
        return "derivarPaciente";
    }

    @PostMapping(value = "/derivar")
    public String derivar(@RequestParam(value = "id")int id, @RequestParam(value = "nuevoOftalmologo")int nuevoOftalmologo){
        pacienteRepository.derivarPaciente(nuevoOftalmologo,id);
        return "redirect:/paciente";
    }
}
