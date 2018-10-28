package com.apap.tugas1.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;


@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("jabatanList", jabatanService.getJabatanList());
		model.addAttribute("instansiList", instansiService.getInstansiList());
		return "home";
	}
	
	@RequestMapping("/pegawai")
	private String viewDetailPegawai(@RequestParam("nip") String nip, Model model) {
		double gaji = 0;
		PegawaiModel pegawai = pegawaiService.findPegawaiByNip(nip);
		
		
		for(JabatanModel jabatan : pegawai.getJabatanList()) {
			double gajiDummy = jabatan.getGajiPokok();
			if(gaji < gajiDummy) {
				gaji = gajiDummy;
			}
		}
		
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		double gajiTotal = gaji + ((tunjangan/100) * gaji);
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gajiPegawai", gajiTotal);
		return "data-pegawai";
		
	}
	
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewTuaMuda(@RequestParam("idInstansi") long id, Model model) {
		InstansiModel instansi = instansiService.getInstansiById(id);
		List<PegawaiModel> listPegawai = instansi.getPegawaiInstansi();
		
		PegawaiModel pegawaiTermuda;
		PegawaiModel pegawaiTertua;
		
		if(listPegawai.size() > 0) {
			pegawaiTermuda = listPegawai.get(0);
			pegawaiTertua = listPegawai.get(0);
			
			for(PegawaiModel pegawai : listPegawai) {
				Date tanggalLahir = pegawai.getTanggalLahir();
				if(tanggalLahir.after(pegawaiTermuda.getTanggalLahir())) {
					pegawaiTermuda = pegawai;
				}
				if(tanggalLahir.before(pegawaiTertua.getTanggalLahir())) {
					pegawaiTertua = pegawai;
				}
			}
			model.addAttribute("pegawaiTermuda", pegawaiTermuda);
			model.addAttribute("pegawaiTertua", pegawaiTertua);
		}
		
		
		return "view-tertua-termuda";
		
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String addPegawai(Model model) {
		PegawaiModel pegawai = new PegawaiModel();
		model.addAttribute("pegawai", new PegawaiModel());
		pegawai.setInstansi(new InstansiModel());
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("instansiList", instansiService.getInstansiList());
		model.addAttribute("listProvinsi", provinsiService.getProvinsiList());
		model.addAttribute("listJabatan", jabatanService.findAllJabatan());
		return "tambah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
		private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
			String NIP = ""; 
			NIP += pegawai.getInstansi().getId();
		
			String[]tanggalLahir = pegawai.getTanggalLahir().toString().split("-");
			String tanggalLahirStr = tanggalLahir[2] + tanggalLahir[1] + tanggalLahir[0].substring(2, 4);
			
			NIP += tanggalLahirStr;
			NIP += pegawai.getTahunMasuk();
		
			int counter = 1;
			for(PegawaiModel instansiPegawai:pegawai.getInstansi().getPegawaiInstansi()) {
				if(instansiPegawai.getTahunMasuk().equals(pegawai.getTahunMasuk()) && instansiPegawai
						.equals(pegawai.getTanggalLahir())) {
					counter += 1;
				}
			}
			
			NIP += "0" + counter;
			pegawai.setNip(NIP);
			pegawaiService.addPegawai(pegawai);
			model.addAttribute("pegawai", pegawai);
			return "berhasil-tambah-pegawai";
		}
	
	
}
