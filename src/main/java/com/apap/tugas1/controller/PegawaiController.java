package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.PegawaiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@RequestMapping("/")
	private String home() {
		return "home";
	}
	
	@RequestMapping("/pegawai")
	private String viewDetailPegawai(@RequestParam("nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		System.out.println(pegawai.getNama());
		System.out.println(pegawai.getNip());
		System.out.println(pegawai.getId());
		model.addAttribute("pegawai", pegawai);
		
		
		
		
		return "data-pegawai";
		
	}
	
}
